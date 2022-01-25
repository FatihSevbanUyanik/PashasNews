package com.example.pashanews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pashanews.R
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.databinding.FragmentNewsBinding
import com.example.pashanews.ui.activity.MainActivity
import com.example.pashanews.ui.adapter.NewsAdapter
import com.example.pashanews.ui.viewmodel.NewsViewModel
import com.example.pashanews.util.DataState
import com.example.pashasnews.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.Listener {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapterNews: NewsAdapter
    private lateinit var viewBinding: FragmentNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentNewsBinding.bind(view)
        initUI()
        subscribeObservers()
        subscribeListeners()
    }

    private fun initUI() {
        adapterNews = NewsAdapter(this)
        viewBinding.rcvNews.apply {
            adapter = adapterNews
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun subscribeObservers() {
        viewModel.areNewsLoading.observe(viewLifecycleOwner, {
            val count = viewModel.news.value?.size ?: 0
            isLoading(it, count)
        })

        viewModel.news.observe(viewLifecycleOwner, {
            adapterNews.differ.submitList(it)
            viewModel.areNewsLoading.value?.let { loading -> isLoading(loading, it.size) }
        })

        viewModel.toast.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) return@observe
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        });
    }

    private fun isLoading(areNewsLoading: Boolean, articleCount: Int) {
        viewBinding.apply {
            progressBar.visibility = if (areNewsLoading) View.VISIBLE else View.GONE
            rcvNews.visibility = if (areNewsLoading) View.GONE else View.VISIBLE
            viewSearch.visibility = if (!areNewsLoading && articleCount == 0)
                View.VISIBLE else View.GONE
        }
    }

    private fun subscribeListeners() {
        var job: Job? = null
        viewBinding.etSearchNews.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(300)
                it?.let {
                    if(it.toString().isNotEmpty()) {
                        viewModel.updateNewsQuery(it.toString())
                    }
                }
            }
        }
    }

    override fun onViewArticle(article: Article) {
        val bundle = Bundle().apply { putSerializable("article", article) }
        findNavController().navigate(R.id.action_newsFragment_to_articleFragment, bundle)
    }
}