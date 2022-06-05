package com.example.pashanews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.pashanews.R
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.databinding.FragmentArticleBinding
import com.example.pashanews.data.api.model.news.Article
import com.example.pashanews.ui.activity.MainActivity
import com.example.pashanews.ui.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var article: Article
    private lateinit var viewBinding: FragmentArticleBinding
    private val viewModel: ArticleViewModel by viewModels()
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentArticleBinding.bind(view)
        article = args.article
        initUI()
        subscribeObservers()
        subscribeListeners()
        viewModel.isArticleFavorite(article.url)
    }

    private fun initUI() {
        viewBinding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
    }

    private fun subscribeListeners() {
        viewBinding.fabSaveNews.setOnClickListener {
            val isArticleFavorite = viewModel.isArticleFavorite.value
            isArticleFavorite?.let { isFavorite ->
                if (isFavorite) {
                    viewModel.deleteArticleFromDB(ArticleDB(article))
                } else {
                    viewModel.saveArticle(article)
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.toast.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) return@observe
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        };

        viewModel.isArticleFavorite.observe(viewLifecycleOwner) {
            viewBinding.fabSaveNews.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (it) R.drawable.ic_baseline_close_24 else R.drawable.ic_baseline_star_24
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigationView()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).showBottomNavigationView()
    }

}