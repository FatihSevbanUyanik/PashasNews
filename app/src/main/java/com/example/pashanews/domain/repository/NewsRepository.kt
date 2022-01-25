package com.example.pashanews.domain.repository

import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.util.DataState
import com.example.pashasnews.model.Article
import com.example.pashasnews.model.NewsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {
    suspend fun insertArticleToDb(article: Article)
    suspend fun deleteArticleFromDb(article: ArticleDB)
    suspend fun getFavouriteNewsFromDb(): Flow<DataState<List<ArticleDB>>>
    suspend fun getNewsFromApi(query: String): Flow<DataState<List<Article>>>
    suspend fun getTopHeadlinesFromApi(page: Int): Flow<DataState<MutableList<Article>>>
    suspend fun doesArticleExistInDB(url: String): Flow<DataState<Boolean>>
}