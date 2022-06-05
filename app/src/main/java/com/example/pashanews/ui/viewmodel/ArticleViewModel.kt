package com.example.pashanews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pashanews.data.api.model.news.Article
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.domain.usecase.DeleteArticleFromDBUseCase
import com.example.pashanews.domain.usecase.DoesArticleExistInDBUseCase
import com.example.pashanews.domain.usecase.InsertArticleToDbUseCase
import com.example.pashanews.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val deleteArticleFromDBUseCase: DeleteArticleFromDBUseCase,
    private val insertArticleToDbUseCase: InsertArticleToDbUseCase,
    private val doesArticleExistInDBUseCase: DoesArticleExistInDBUseCase
): ViewModel() {

    private val _toast: MutableLiveData<String> = MutableLiveData("")
    val toast: LiveData<String>
        get() = _toast

    private val _isArticleFavorite: MutableLiveData<Boolean> = MutableLiveData(false)
    val isArticleFavorite: LiveData<Boolean>
        get() = _isArticleFavorite

    fun isArticleFavorite(url: String) = viewModelScope.launch {
        doesArticleExistInDBUseCase.execute(url).onEach {
            if (it is DataState.Success) _isArticleFavorite.value = it.data
        }.launchIn(viewModelScope)
    }

    fun deleteArticleFromDB(articleDB: ArticleDB) = viewModelScope.launch {
        deleteArticleFromDBUseCase.execute(articleDB)
        _isArticleFavorite.value = false
        _toast.value = "Article Deleted"
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        insertArticleToDbUseCase.execute(article)
        _isArticleFavorite.value = true
        _toast.value = "Article Saved"
    }

}