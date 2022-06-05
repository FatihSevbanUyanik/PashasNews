package com.example.pashanews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.domain.usecase.DeleteArticleFromDBUseCase
import com.example.pashanews.domain.usecase.GetFavoriteArticlesFromDbUseCase
import com.example.pashanews.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteNewsViewModel @Inject constructor(
    private val getFavoriteArticlesFromDbUseCase: GetFavoriteArticlesFromDbUseCase,
    private val deleteArticleFromDBUseCase: DeleteArticleFromDBUseCase
): ViewModel() {

    private val _toast: MutableLiveData<String> = MutableLiveData("")
    val toast: LiveData<String>
        get() = _toast

    private val _areFavoriteNewsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val areFavoriteNewsLoading: LiveData<Boolean>
        get() = _areFavoriteNewsLoading

    private val _favoriteArticles: MutableLiveData<List<ArticleDB>> = MutableLiveData()
    val favoriteArticles: LiveData<List<ArticleDB>>
        get() = _favoriteArticles

    fun getFavoriteArticles() = viewModelScope.launch {
        getFavoriteArticlesFromDbUseCase.execute()
            .onEach {
                when(it) {
                    is DataState.Success -> {
                        _favoriteArticles.value = it.data
                        _areFavoriteNewsLoading.value = false
                    }
                    is DataState.Error -> {
                        _areFavoriteNewsLoading.value = false
                        _toast.value = it.message
                    }
                    is DataState.Loading -> _areFavoriteNewsLoading.value = true
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteArticle(position: Int) = viewModelScope.launch {
        val articles = _favoriteArticles.value ?: listOf()
        val article = articles[position]
        deleteArticleFromDBUseCase.execute(article)
        val newList = mutableListOf<ArticleDB>()
        newList.addAll(articles)
        newList.removeAt(position)
        _favoriteArticles.value = newList
        _toast.value = "Article Deleted"
    }

}