package com.example.pashanews.domain.repository

import com.example.pashanews.data.api.model.currency.Currency
import com.example.pashanews.util.DataState
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getCurrenciesFromApi(): Flow<DataState<List<Currency>>>
    suspend fun getCryptoCurrenciesFromApi(): Flow<DataState<List<Currency>>>
}