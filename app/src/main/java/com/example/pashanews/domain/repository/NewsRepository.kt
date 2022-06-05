package com.example.pashanews.domain.repository

import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.util.DataState
import com.example.pashanews.data.api.model.news.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun insertArticleToDb(article: Article)
    suspend fun deleteArticleFromDb(article: ArticleDB)
    suspend fun getFavouriteNewsFromDb(): Flow<DataState<List<ArticleDB>>>
    suspend fun getNewsFromApi(query: String): Flow<DataState<List<Article>>>
    suspend fun getTopHeadlinesFromApi(page: Int, categoryOrSource: String): Flow<DataState<List<Article>>>
    suspend fun doesArticleExistInDB(url: String): Flow<DataState<Boolean>>
}