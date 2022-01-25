package com.example.pashanews.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pashasnews.model.Article
import java.io.Serializable

@Entity
data class ArticleDB(
    @PrimaryKey
    val url: String,
    val author: String,
    val description: String,
    val publishedAt: String,
    val name: String,
    val title: String,
    val urlToImage: String
) {
    constructor(article: Article) : this(
        article.url,
        article.author,
        article.description,
        article.publishedAt,
        article.source.name,
        article.title,
        article.urlToImage
    )
}