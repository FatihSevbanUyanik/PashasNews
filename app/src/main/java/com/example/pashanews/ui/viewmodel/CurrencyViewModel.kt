package com.example.pashanews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pashanews.data.api.model.currency.Currency
import com.example.pashanews.domain.usecase.GetCryptoUseCase
import com.example.pashanews.domain.usecase.GetCurrenciesUseCase
import com.example.pashanews.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getCryptoUseCase: GetCryptoUseCase
): BaseViewModel() {

    private val _areCryptosLoading: MutableLiveData<Boolean> = MutableLiveData(false);
    val areCryptosLoading: LiveData<Boolean>
        get() = _areCryptosLoading

    private val _cryptos: MutableLiveData<List<Currency>> = MutableLiveData(listOf())
    val cryptos: LiveData<List<Currency>>
        get() = _cryptos

    fun getCryptos() = viewModelScope.launch {
        getCryptoUseCase.execute()
            .onEach {
                when(it) {
                    is DataState.Success -> {
                        _areCryptosLoading.value = false
                        _cryptos.value = it.data ?: mutableListOf()
                    }
                    is DataState.Error -> _toast.value = it.message
                    is DataState.Loading -> _areCryptosLoading.value = true
                }
            }
            .launchIn(viewModelScope)
    }

}