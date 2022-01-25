package com.example.pashanews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pashanews.R
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.databinding.FragmentHeadLinesBinding
import com.example.pashanews.ui.activity.MainActivity
import com.example.pashanews.ui.adapter.HeadlinesAdapter
import com.example.pashanews.ui.viewmodel.NewsViewModel
import com.example.pashanews.util.Constants
import com.example.pashanews.util.DataState
import com.example.pashasnews.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
class HeadLinesFragment : Fragment(R.layout.fragment_head_lines), HeadlinesAdapter.Listener {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var headlinesAdapter: HeadlinesAdapter
    private lateinit var viewBinding: FragmentHeadLinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentHeadLinesBinding.bind(view)
        initUI()
        subscribeListeners()
        subscribeObservers()
        viewModel.getTopHeadLines()
    }

    private fun initUI() {
        headlinesAdapter = HeadlinesAdapter(this)
        viewBinding.rcvHeadlines.apply {
            adapter = headlinesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun subscribeListeners() {
        viewBinding.rcvHeadlines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isLoading = viewModel.areTopHeadLinesLoading.value ?: true
                val shouldLoadMore = viewModel.shouldLoadMorePhotos.value ?: true
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
        viewModel.areTopHeadLinesLoading.observe(viewLifecycleOwner, {
            val currentPhotoCount = headlinesAdapter.differ.currentList.size
            viewBinding.apply {
                if (it && currentPhotoCount == 0) {
                    progressBar.visibility = View.VISIBLE
                    rcvHeadlines.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    rcvHeadlines.visibility = View.VISIBLE
                }
            }
        })

        viewModel.toast.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) return@observe
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        });

        viewModel.topHeadLines.observe(viewLifecycleOwner, {
            headlinesAdapter.differ.submitList(it)
        })
    }

    override fun onViewArticle(article: Article) {
        val bundle = Bundle().apply { putSerializable("article", article) }
        findNavController().navigate(R.id.action_topHeadlinesFragment_to_articleFragment, bundle)
    }
}