package com.example.pashanews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pashanews.R
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.databinding.FragmentFavouriteNewsBinding
import com.example.pashanews.ui.adapter.NewsDBAdapter
import com.example.pashanews.data.api.model.news.Article
import com.example.pashanews.data.api.model.news.Source
import com.example.pashanews.ui.viewmodel.FavoriteNewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteNewsFragment : Fragment(R.layout.fragment_favourite_news), NewsDBAdapter.Listener {

    private val viewModel: FavoriteNewsViewModel by viewModels()
    private lateinit var favoriteNewsAdapter: NewsDBAdapter
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
            areFavoriteNewsLoading.observe(viewLifecycleOwner) {
                val count = viewModel.favoriteArticles.value?.size ?: 0
                isLoading(it, count)
            }

            toast.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            };

            favoriteArticles.observe(viewLifecycleOwner) {
                favoriteNewsAdapter.differ.submitList(it)
                viewModel.areFavoriteNewsLoading.value?.let { loading -> isLoading(loading, it.size) }
            }
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
        favoriteNewsAdapter = NewsDBAdapter(this)
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
            Source(article.url, ""),
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