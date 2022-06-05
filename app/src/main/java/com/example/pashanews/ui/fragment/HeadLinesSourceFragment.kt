package com.example.pashanews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.pashanews.R
import com.example.pashanews.data.api.model.news.Article

class HeadLinesSourceFragment : HeadLinesFragment() {

    override fun initUI() {
        super.initUI()

        context?.let { context ->
            ArrayAdapter.createFromResource(
                context,
                R.array.news_sources_titles,
                R.layout.spinner_headline_categories_header
            ).also {
                it.setDropDownViewResource(R.layout.spinner_headline_categories_item)
                viewBinding.spinner.adapter = it
                viewBinding.spinner.onItemSelectedListener = this
            }
        }

        categories = resources.getStringArray(R.array.news_sources).toList()
    }

    override fun onViewArticle(article: Article) {
        val bundle = Bundle().apply { putSerializable("article", article) }
        findNavController().navigate(R.id.action_headLinesSourceFragment_to_articleFragment, bundle)
    }

}