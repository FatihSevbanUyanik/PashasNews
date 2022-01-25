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
import com.example.pashanews.databinding.FragmentFavouriteNewsBinding
import com.example.pashanews.databinding.FragmentHeadLinesBinding
import com.example.pashanews.ui.adapter.FavoriteNewsAdapter
import com.example.pashanews.ui.adapter.HeadlinesAdapter
import com.example.pashanews.ui.viewmodel.NewsViewModel
import com.example.pashanews.util.DataState
import com.example.pashasnews.model.Article
import com.example.pashasnews.model.NewsSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteNewsFragment : Fragment(R.layout.fragment_favourite_news), FavoriteNewsAdapter.Listener {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var favoriteNewsAdapter: FavoriteNewsAdapter
    private lateinit var viewBinding: FragmentFavouriteNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentFavouriteNewsBinding.bind(view)
        initUI()
        subscribeObservers()
        viewModel.getFavoriteArticles()
    }

    private fun subscribeObservers() {
        viewModel.apply {
            areFavoriteNewsLoading.observe(viewLifecycleOwner, {
                val count = viewModel.favoriteArticles.value?.size ?: 0
                isLoading(it, count)
            })

            toast.observe(viewLifecycleOwner, {
                if (it.isNullOrEmpty()) return@observe
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            });

            favoriteArticles.observe(viewLifecycleOwner, {
                favoriteNewsAdapter.differ.submitList(it)
                viewModel.areFavoriteNewsLoading.value?.let { loading -> isLoading(loading, it.size) }
            })
        }
    }

    private fun isLoading(areFavoriteNewsLoading: Boolean, articleCount: Int) {
        viewBinding.apply {
            progressBar.visibility = if (areFavoriteNewsLoading) View.VISIBLE else View.GONE
            rcvFavouriteNews.visibility = if (areFavoriteNewsLoading) View.GONE else View.VISIBLE
            viewNoFavoriteArticles.visibility = if (!areFavoriteNewsLoading && articleCount == 0)
                View.VISIBLE else View.GONE
        }
    }

    private fun initUI() {
        favoriteNewsAdapter = FavoriteNewsAdapter(this)
        viewBinding.rcvFavouriteNews.apply {
            adapter = favoriteNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onViewArticle(article: ArticleDB) {
        val articleConverted = Article(
            article.author,
            "",
            article.description,
            article.publishedAt,
            NewsSource(article.url, ""),
            article.title,
            article.url,
            article.urlToImage
        )

        val bundle = Bundle().apply { putSerializable("article", articleConverted) }
        findNavController().navigate(R.id.action_favouriteNewsFragment_to_articleFragment, bundle)
    }

    override fun onDeleteArticle(position: Int) {
        viewModel.deleteArticle(position)
    }

}