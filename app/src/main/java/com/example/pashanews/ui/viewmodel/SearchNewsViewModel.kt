package com.example.pashanews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pashanews.domain.usecase.*
import com.example.pashanews.util.DataState
import com.example.pashanews.data.api.model.news.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(private val getNewsUseCase: GetNewsFromApiUseCase): BaseViewModel() {

    private var _newsQuery: MutableLiveData<String> = MutableLiveData("")

    private val _areNewsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val areNewsLoading: LiveData<Boolean>
        get() = _areNewsLoading

    private val _news: MutableLiveData<List<Article>> = MutableLiveData()
    val news: LiveData<List<Article>>
        get() = _news

    fun updateNewsQuery(query: String) {
        _newsQuery.value = query
        getSearchNews()
    }

    private fun getSearchNews() = viewModelScope.launch {
        val query = _newsQuery.value
        if (query.isNullOrEmpty()) return@launch

        getNewsUseCase.execute(query)
            .onEach {
                when (it) {
                    is DataState.Success -> {
                         _news.value = it.data?.filter { article ->
                             !article.description.isNullOrEmpty() && !article.title.isNullOrEmpty()
                         }
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

}