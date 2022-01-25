package com.example.pashanews.domain.usecase

import com.example.pashanews.domain.repository.NewsRepository
import com.example.pashanews.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DoesArticleExistInDBUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute(url: String): Flow<DataState<Boolean>> = newsRepository.doesArticleExistInDB(url)
}