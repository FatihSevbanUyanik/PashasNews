package com.example.pashanews.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pashanews.R
import com.example.pashanews.databinding.FragmentHeadLinesBinding
import com.example.pashanews.ui.adapter.NewsAdapter
import com.example.pashanews.util.Constants
import com.example.pashanews.ui.viewmodel.HeadlinesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class HeadLinesFragment : Fragment(R.layout.fragment_head_lines),
    NewsAdapter.Listener,
    AdapterView.OnItemSelectedListener {

    private lateinit var newsAdapter: NewsAdapter
    protected lateinit var categories: List<String>
    protected val viewModel: HeadlinesViewModel by viewModels()
    protected lateinit var viewBinding: FragmentHeadLinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentHeadLinesBinding.bind(view)
        initUI()
        subscribeListeners()
        subscribeObservers()
    }

    open fun initUI() {
        newsAdapter = NewsAdapter(this)
        viewBinding.rcvHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun subscribeListeners() {
        viewBinding.rcvHeadlines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isLoading = viewModel.areTopHeadLinesLoading.value ?: true
                val shouldLoadMore = viewModel.shouldLoadMoreHeadLines.value ?: true
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisiblePosition: Int = layoutManager.findFirstVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (firstVisiblePosition + Constants.PER_PAGE >= totalItemCount && !isLoading && shouldLoadMore) {
                    viewModel.getTopHeadLines()
                }
            }
        })
    }

    private fun subscribeObservers() {
        viewModel.areTopHeadLinesLoading.observe(viewLifecycleOwner) {
            val currentPhotoCount = newsAdapter.differ.currentList.size
            viewBinding.apply {
                if (it && currentPhotoCount == 0) {
                    progressBar.visibility = View.VISIBLE
                    rcvHeadlines.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    rcvHeadlines.visibility = View.VISIBLE
                }
            }
        }

        viewModel.toast.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) return@observe
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        };

        viewModel.topHeadLines.observe(viewLifecycleOwner) {
            newsAdapter.differ.submitList(it)
        }

        viewModel.topHeadLinesCategory.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) return@observe
            viewModel.refreshHeadlines()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val selectedCategory = categories[position]
        viewModel.updateCategory(selectedCategory)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}