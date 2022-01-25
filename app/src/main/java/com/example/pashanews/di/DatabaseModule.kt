package com.example.pashanews.di

import android.content.Context
import androidx.room.Room
import com.example.pashanews.data.db.FavoriteArticlesDatabase
import com.example.pashanews.data.db.dao.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideFavouriteArticlesDB(@ApplicationContext appContext: Context): FavoriteArticlesDatabase {
        return Room.databaseBuilder(
            appContext,
            FavoriteArticlesDatabase::class.java,
            "favorite-article-db"
        ).
        fallbackToDestructiveMigration().
        build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(db: FavoriteArticlesDatabase): ArticleDao {
        return db.articleDao();
    }

}