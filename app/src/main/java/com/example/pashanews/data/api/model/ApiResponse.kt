package com.example.pashasnews.model

import com.example.pashanews.data.api.model.news.Article

data class ApiResponse<T>(
    val status: Int,
    val data: T
)