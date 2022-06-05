package com.example.pashanews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pashanews.data.db.model.ArticleDB
import com.example.pashanews.databinding.ListItemFavoriteNewsBinding
import com.example.pashanews.util.ImgUtil

class NewsDBAdapter(private val listener: Listener) :
    RecyclerView.Adapter<NewsDBAdapter.FavouriteNewsViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ArticleDB>() {
        override fun areItemsTheSame(oldItem: ArticleDB, newItem: ArticleDB): Boolean = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: ArticleDB, newItem: ArticleDB): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteNewsViewHolder =
        FavouriteNewsViewHolder(ListItemFavoriteNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FavouriteNewsViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    override fun getItemCount(): Int = differ.currentList.size

    interface Listener {
        fun onViewArticle(article: ArticleDB);
        fun onDeleteArticle(position: Int)
    }

    inner class FavouriteNewsViewHolder(private val binding: ListItemFavoriteNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleDB) {
            binding.apply {
                tvHeader.text = article.title
                tvDescription.text = article.description
                tvDate.text = article.publishedAt!!.split("T")[0]
                ImgUtil.loadImage(imgNews, article.urlToImage, true)

                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onViewArticle(article)
                    }
                }

                imgDelete.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onDeleteArticle(adapterPosition)
                    }
                }

            }
        }
    }
}