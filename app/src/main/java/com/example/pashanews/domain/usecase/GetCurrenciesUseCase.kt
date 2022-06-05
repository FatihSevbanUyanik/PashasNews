package com.example.pashanews.domain.usecase

import com.example.pashanews.data.api.model.currency.Currency
import com.example.pashanews.domain.repository.CurrencyRepository
import com.example.pashanews.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {
    suspend fun execute(): Flow<DataState<List<Currency>>> = currencyRepository.getCurrenciesFromApi()
}