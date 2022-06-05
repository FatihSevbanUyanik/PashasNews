package com.example.pashanews.data.repository

import com.example.pashanews.data.api.service.NewsService
import com.example.pashanews.data.db.dao.ArticleDao
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.domain.repository.NewsRepository
import com.example.pashanews.util.DataState
import com.example.pashanews.data.api.model.news.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao
) : NewsRepository {

    override suspend fun insertArticleToDb(article: Article) = articleDao.insertArticle(ArticleDB(article))
    override suspend fun deleteArticleFromDb(article: ArticleDB) = articleDao.deleteArticle(article)

    override suspend fun doesArticleExistInDB(url: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading())
        val count = articleDao.doesArticleExist(url)
        emit(DataState.Success(count > 0))
    }

    override suspend fun getFavouriteNewsFromDb(): Flow<DataState<List<ArticleDB>>> = flow {
        emit(DataState.Loading())
        val articles = articleDao.getArticles().toMutableList()
        emit(DataState.Success(articles))
    }

    override suspend fun getNewsFromApi(query: String): Flow<DataState<List<Article>>> = flow {
        emit(DataState.Loading())

        try {
            val response = newsService.getNews(query)

            if (response.isSuccessful) {
                val news = response.body() ?: throw HttpException(response)
                emit(DataState.Success(news.data))
            } else {
                emit(DataState.Error("Response not successful"))
            }

        } catch (e: HttpException) {
            emit(DataState.Error(e.message()))
        } catch (e: IOException) {
            emit(DataState.Error("Connection Error"))
        }
    }

    override suspend fun getTopHeadlinesFromApi(page: Int, categoryOrSource: String): Flow<DataState<List<Article>>> =
        flow {
            emit(DataState.Loading())

            try {
                val response = newsService.getTopHeadlines(page, categoryOrSource)

                if (response.isSuccessful) {
                    val news = response.body() ?: throw HttpException(response)
                    emit(DataState.Success(news.data))
                } else {
                    emit(DataState.Error("Response not successful"))
                }

            } catch (e: HttpException) {
                emit(DataState.Error(e.message()))
            } catch (e: IOException) {
                emit(DataState.Error("Connection Error"))
            }
        }

}