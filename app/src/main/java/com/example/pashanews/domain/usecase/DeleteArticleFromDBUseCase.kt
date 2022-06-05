package com.example.pashanews.domain.usecase

import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteArticleFromDBUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute(article: ArticleDB) = newsRepository.deleteArticleFromDb(article)
}