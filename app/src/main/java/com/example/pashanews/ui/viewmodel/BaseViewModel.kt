package com.example.pashanews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    protected val _toast: MutableLiveData<String> = MutableLiveData("")
    val toast: LiveData<String>
        get() = _toast

}