package com.example.pashanews.data.repository

import com.example.pashanews.data.api.model.currency.Currency
import com.example.pashanews.data.api.service.CurrencyService
import com.example.pashanews.domain.repository.CurrencyRepository
import com.example.pashanews.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val currencyService: CurrencyService): CurrencyRepository {
    override suspend fun getCurrenciesFromApi(): Flow<DataState<List<Currency>>> = flow {
        emit(DataState.Loading())

        try {
            val response = currencyService.getCurrencies()

            if (response.isSuccessful) {
                val apiResponse = response.body() ?: throw HttpException(response)
                emit(DataState.Success(apiResponse.data))
            } else {
                emit(DataState.Error("Response not successful"))
            }

        } catch (e: HttpException) {
            emit(DataState.Error(e.message()))
        } catch (e: IOException) {
            emit(DataState.Error("Connection Error"))
        }
    }

    override suspend fun getCryptoCurrenciesFromApi(): Flow<DataState<List<Currency>>> = flow {
        emit(DataState.Loading())

        try {
            val response = currencyService.getCryptoCurrencies()

            if (response.isSuccessful) {
                val apiResponse = response.body() ?: throw HttpException(response)
                emit(DataState.Success(apiResponse.data))
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