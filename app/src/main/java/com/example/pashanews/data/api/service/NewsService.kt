package com.example.pashanews.data.api.service

import com.example.pashanews.data.api.model.news.Article
import com.example.pashasnews.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("news/top-headlines")
    suspend fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("categorySource") categorySource: String
    ): Response<ApiResponse<List<Article>>>

    @GET("news/everything")
    suspend fun getNews(
        @Query("query") query: String,
    ): Response<ApiResponse<List<Article>>>

}