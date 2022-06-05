package com.example.pashanews.domain.usecase

import com.example.pashanews.domain.repository.NewsRepository
import com.example.pashanews.util.DataState
import com.example.pashanews.data.api.model.news.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesFromApiUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute(page: Int, categoryOrSource: String): Flow<DataState<List<Article>>> =
        newsRepository.getTopHeadlinesFromApi(page, categoryOrSource)
}