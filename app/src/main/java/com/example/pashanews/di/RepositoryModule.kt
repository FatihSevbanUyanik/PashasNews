package com.example.pashanews.di

import com.example.pashanews.data.api.service.NewsService
import com.example.pashanews.data.db.dao.ArticleDao
import com.example.pashanews.data.repository.NewsRepositoryImpl
import com.example.pashanews.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepo(newsService: NewsService, articleDao: ArticleDao): NewsRepository {
        return NewsRepositoryImpl(newsService, articleDao)
    }

}