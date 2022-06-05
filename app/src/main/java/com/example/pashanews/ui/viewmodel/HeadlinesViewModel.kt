package com.example.pashanews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pashanews.data.api.model.news.Article
import com.example.pashanews.domain.usecase.GetTopHeadlinesFromApiUseCase
import com.example.pashanews.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesFromApiUseCase
): BaseViewModel() {

    companion object {
        private const val MAX_PAGE_SIZE = 3
    }

    private val _topHeadlinesPage: MutableLiveData<Int> = MutableLiveData(1)

    private val _topHeadLinesCategory: MutableLiveData<String> = MutableLiveData("")
    val topHeadLinesCategory: LiveData<String>
        get() = _topHeadLinesCategory

    private var _shouldLoadMoreHeadLines: MutableLiveData<Boolean> = MutableLiveData(true)
    val shouldLoadMoreHeadLines: LiveData<Boolean>
        get() = _shouldLoadMoreHeadLines

    private val _areTopHeadLinesLoading: MutableLiveData<Boolean> = MutableLiveData()
    val areTopHeadLinesLoading: LiveData<Boolean>
        get() = _areTopHeadLinesLoading

    private val _topHeadLines: MutableLiveData<MutableList<Article>> = MutableLiveData()
    val topHeadLines: LiveData<MutableList<Article>>
        get() = _topHeadLines

    fun updateCategory(category: String) {
        _topHeadLinesCategory.value = category
    }

    fun refreshHeadlines() = viewModelScope.launch {
        _topHeadlinesPage.value = 1
        _shouldLoadMoreHeadLines.value = true
        _topHeadLines.value = mutableListOf()
        getTopHeadLines()
    }

    fun getTopHeadLines() = viewModelScope.launch {
        val currentPage = _topHeadlinesPage.value ?: return@launch
        val shouldLoadMore = _shouldLoadMoreHeadLines.value ?: return@launch

        if (currentPage > MAX_PAGE_SIZE || !shouldLoadMore) {
            _shouldLoadMoreHeadLines.value = false
            return@launch
        }

        getTopHeadlinesUseCase.execute(currentPage, topHeadLinesCategory.value ?: return@launch)
            .onEach {
                when (it) {
                    is DataState.Success -> {
                        val newList = mutableListOf<Article>()
                        val newArticles = it.data ?: mutableListOf()

                        val currArticles = _topHeadLines.value ?: mutableListOf()
                        newList.addAll(currArticles)

                        newList.addAll(
                            newArticles.filter { article ->
                                !article.description.isNullOrEmpty() && !article.title.isNullOrEmpty()
                            }
                        )

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

}