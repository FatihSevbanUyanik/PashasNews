package com.example.pashanews.data.api.service

import com.example.pashanews.data.api.model.currency.Currency
import com.example.pashasnews.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyService {

    @GET("currency")
    suspend fun getCurrencies(): Response<ApiResponse<List<Currency>>>

    @GET("currency/crypto")
    suspend fun getCryptoCurrencies(): Response<ApiResponse<List<Currency>>>

}