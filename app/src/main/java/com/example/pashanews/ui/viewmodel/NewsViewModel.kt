package com.example.pashanews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.domain.usecase.*
import com.example.pashanews.util.DataState
import com.example.pashasnews.model.Article
import com.example.pashasnews.model.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsFromApiUseCase,
    private val insertArticleToDbUseCase: InsertArticleToDbUseCase,
    private val getTopHeadlinesUseCase: GetTopHeadlinesFromApiUseCase,
    private val deleteArticleFromDBUseCase: DeleteArticleFromDBUseCase,
    private val doesArticleExistInDBUseCase: DoesArticleExistInDBUseCase,
    private val getFavoriteArticlesFromDbUseCase: GetFavoriteArticlesFromDbUseCase): ViewModel() {

    private val MAX_PAGE_SIZE = 5

    private val _areFavoriteNewsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val areFavoriteNewsLoading: LiveData<Boolean>
        get() = _areFavoriteNewsLoading

    private val _favoriteArticles: MutableLiveData<List<ArticleDB>> = MutableLiveData()
    val favoriteArticles: LiveData<List<ArticleDB>>
        get() = _favoriteArticles

    private var _newsQuery: MutableLiveData<String> = MutableLiveData("")

    private val _areNewsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val areNewsLoading: LiveData<Boolean>
        get() = _areNewsLoading

    private val _news: MutableLiveData<List<Article>> = MutableLiveData()
    val news: LiveData<List<Article>>
        get() = _news

    private val _topHeadlinesPage: MutableLiveData<Int> = MutableLiveData(1)

    private var _shouldLoadMoreHeadLines: MutableLiveData<Boolean> = MutableLiveData(true)
    val shouldLoadMorePhotos: LiveData<Boolean>
        get() = _shouldLoadMoreHeadLines

    private val _areTopHeadLinesLoading: MutableLiveData<Boolean> = MutableLiveData()
    val areTopHeadLinesLoading: LiveData<Boolean>
        get() = _areTopHeadLinesLoading

    private val _topHeadLines: MutableLiveData<MutableList<Article>> = MutableLiveData()
    val topHeadLines: LiveData<MutableList<Article>>
        get() = _topHeadLines

    private val _toast: MutableLiveData<String> = MutableLiveData("")
    val toast: LiveData<String>
        get() = _toast

    fun getTopHeadLines() = viewModelScope.launch {
        val currentPage = _topHeadlinesPage.value ?: 1
        val shouldLoadMore = _shouldLoadMoreHeadLines.value ?: true

        if (currentPage > MAX_PAGE_SIZE || !shouldLoadMore) {
            _shouldLoadMoreHeadLines.value = false
            return@launch
        }

        getTopHeadlinesUseCase.execute(currentPage)
            .onEach {
                when (it) {
                    is DataState.Success -> {
                        val newArticles = it.data ?: mutableListOf()
                        val newList = mutableListOf<Article>()
                        val currArticles = _topHeadLines.value ?: mutableListOf()
                        newList.addAll(currArticles)
                        newList.addAll(newArticles)
                        _topHeadLines.value = newList
                        _topHeadlinesPage.value = currentPage + 1
                        _areTopHeadLinesLoading.value = false

                        if (currArticles.size == newList.size) {
                            _shouldLoadMoreHeadLines.value = false
                        }
                    }
                    is DataState.Error -> {
                        _areTopHeadLinesLoading.value = false
                        _toast.value = it.message
                    }
                    is DataState.Loading -> _areTopHeadLinesLoading.value = true
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateNewsQuery(query: String) {
        _newsQuery.value = query
        getNews()
    }

    private fun getNews() = viewModelScope.launch {
        val query = _newsQuery.value ?: ""
        if (query.isEmpty()) return@launch

        getNewsUseCase.execute(query)
            .onEach {
                when (it) {
                    is DataState.Success -> {
                        _news.value = it.data
                        _areNewsLoading.value = false
                    }
                    is DataState.Error -> {
                        _areNewsLoading.value = false
                        _toast.value = it.message
                    }
                    is DataState.Loading -> _areNewsLoading.value = true
                }
            }
            .launchIn(viewModelScope)
    }

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

    private val _isArticleFavorite: MutableLiveData<Boolean> = MutableLiveData(false)
    val isArticleFavorite: LiveData<Boolean>
        get() = _isArticleFavorite

    fun isArticleFavorite(url: String) = viewModelScope.launch {
        doesArticleExistInDBUseCase.execute(url).onEach {
            if (it is DataState.Success) _isArticleFavorite.value = it.data
        }.launchIn(viewModelScope)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        insertArticleToDbUseCase.execute(article)
        _isArticleFavorite.value = true
        _toast.value = "Article Saved"
    }

    fun deleteArticleFromDB(articleDB: ArticleDB) = viewModelScope.launch {
        deleteArticleFromDBUseCase.execute(articleDB)
        _isArticleFavorite.value = false
        _toast.value = "Article Deleted"
    }

    fun deleteArticle(position: Int) = viewModelScope.launch {
        val articles = _favoriteArticles.value ?: listOf()
        val article = articles[position]
        deleteArticleFromDB(article)
        val newList = mutableListOf<ArticleDB>()
        newList.addAll(articles)
        newList.removeAt(position)
        _favoriteArticles.value = newList
    }
}