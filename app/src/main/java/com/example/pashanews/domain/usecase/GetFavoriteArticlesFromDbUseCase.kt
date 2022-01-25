package com.example.pashanews.domain.usecase

import com.example.pashanews.domain.repository.NewsRepository
import javax.inject.Inject

class GetFavoriteArticlesFromDbUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute() = newsRepository.getFavouriteNewsFromDb()
}