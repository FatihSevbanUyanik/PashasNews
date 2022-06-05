package com.example.pashanews.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pashanews.data.db.model.ArticleDB

@Dao
interface ArticleDao {
    @Insert
    suspend fun insertArticle(article: ArticleDB)

    @Delete
    suspend fun deleteArticle(article: ArticleDB)

    @Query("SELECT * FROM ArticleDB ORDER BY createdAt DESC")
    suspend fun getArticles(): List<ArticleDB>

    @Query("SELECT COUNT(*) FROM ArticleDB where url=:articleURL")
    suspend fun doesArticleExist(articleURL: String): Int
}