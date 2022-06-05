package com.example.pashanews.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pashanews.data.db.dao.ArticleDao
import com.example.pashanews.data.db.model.ArticleDB

@Database(entities = [ArticleDB::class], version = 3, exportSchema = false)
abstract class FavoriteArticlesDatabase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}