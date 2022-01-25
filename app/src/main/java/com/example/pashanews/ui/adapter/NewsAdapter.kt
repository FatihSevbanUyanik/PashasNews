package com.example.pashanews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.pashanews.databinding.FragmentNewsBinding
import com.example.pashanews.databinding.ListItemNewsBinding
import com.example.pashanews.util.ImgUtil
import com.example.pashasnews.model.Article

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

    inner class NewsViewHolder(val binding: ListItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                tvHeader.text = article.title
                tvDescription.text = article.description
                tvDate.text = article.publishedAt.split("T")[0]
                ImgUtil.loadImage(imgNews, article.urlToImage)

                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onViewArticle(article)
                    }
                }
            }
        }
    }

}