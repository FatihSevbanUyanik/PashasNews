package com.example.pashanews.domain.usecase

import com.example.pashanews.domain.repository.NewsRepository
import com.example.pashanews.util.DataState
import com.example.pashasnews.model.Article
import com.example.pashasnews.model.NewsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesFromApiUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute(page: Int): Flow<DataState<MutableList<Article>>> =
        newsRepository.getTopHeadlinesFromApi(page)
}