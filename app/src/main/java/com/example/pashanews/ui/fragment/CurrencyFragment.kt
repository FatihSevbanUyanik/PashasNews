package com.example.pashanews.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pashanews.R
import com.example.pashanews.databinding.FragmentCurrenciesBinding
import com.example.pashanews.ui.adapter.CurrencyAdapter
import com.example.pashanews.ui.viewmodel.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.lang.Runnable

@AndroidEntryPoint
class CurrencyFragment: Fragment(R.layout.fragment_currencies) {

    private val viewModel: CurrencyViewModel by viewModels()
    private lateinit var viewBinding: FragmentCurrenciesBinding
    private lateinit var adapterCurrencies: CurrencyAdapter
    private lateinit var repeatingJob: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentCurrenciesBinding.bind(view)
        initUI()
        subscribeObservers()
    }

    private fun subscribeObservers() {
         viewModel.cryptos.observe(viewLifecycleOwner) {
             adapterCurrencies.differ.submitList(it)
         }

        viewModel.toast.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) return@observe
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        viewModel.areCryptosLoading.observe(viewLifecycleOwner) {
            val currentPhotoCount = adapterCurrencies.differ.currentList.size
            viewBinding.apply {
                if (it && currentPhotoCount == 0) {
                    progressBar.visibility = View.VISIBLE
                    rcvCurrency.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    rcvCurrency.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initUI() {
        adapterCurrencies = CurrencyAdapter()
        viewBinding.rcvCurrency.apply {
            adapter = adapterCurrencies
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun startRepeatingJob(): Job = CoroutineScope(Dispatchers.Default).launch {
        while (NonCancellable.isActive) {
            viewModel.getCryptos()
            delay(30000L)
        }
    }

    override fun onStart() {
        super.onStart()
        repeatingJob = startRepeatingJob()
    }

    override fun onStop() {
        super.onStop()
        repeatingJob.cancel()
    }
}