package com.example.pashanews.data.api.service

import com.example.pashanews.util.Constants
import com.example.pashasnews.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("country") country: String = "us",
        @Query("category") category: String = "business",
        @Query("apiKey") apiKey: String = Constants.NEWS_API_KEY
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun getNews(
        @Query("q") category: String = "us",
        @Query("apiKey") apiKey: String = Constants.NEWS_API_KEY
    ): Response<NewsResponse>

}