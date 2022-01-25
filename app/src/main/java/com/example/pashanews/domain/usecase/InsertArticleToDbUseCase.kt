package com.example.pashanews.domain.usecase

import com.example.pashanews.domain.repository.NewsRepository
import com.example.pashasnews.model.Article
import javax.inject.Inject

class InsertArticleToDbUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun execute(article: Article) = newsRepository.insertArticleToDb(article)
}