package com.example.pashanews.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pashanews.databinding.ListItemNewsBinding
import com.example.pashanews.util.ImgUtil
import com.example.pashanews.data.api.model.news.Article

class NewsAdapter(private val listener: Listener): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder(ListItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) = holder.bind(differ.currentList[position])

    override fun getItemCount(): Int = differ.currentList.size

    interface Listener { fun onViewArticle(article: Article); }

    inner class NewsViewHolder(private val binding: ListItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                tvHeader.text = article.title
                tvDescription.text = article.description
                ImgUtil.loadImage(imgNews, article.urlToImage, true)

                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onViewArticle(article)
                    }
                }
            }
        }
    }

}