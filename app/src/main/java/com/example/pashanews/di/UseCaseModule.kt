package com.example.pashanews.di

import com.example.pashanews.domain.repository.NewsRepository
import com.example.pashanews.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideGetTopHeadlinesUseCase(newsRepository: NewsRepository): GetTopHeadlinesFromApiUseCase {
        return GetTopHeadlinesFromApiUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetNewsUseCase(newsRepository: NewsRepository): GetNewsFromApiUseCase {
        return GetNewsFromApiUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideInsertArticleToDbUseCase(newsRepository: NewsRepository): InsertArticleToDbUseCase {
        return InsertArticleToDbUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteArticleFromDbUseCase(newsRepository: NewsRepository): DeleteArticleFromDBUseCase {
        return DeleteArticleFromDBUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetArticlesFromDbUseCase(newsRepository: NewsRepository): GetFavoriteArticlesFromDbUseCase {
        return GetFavoriteArticlesFromDbUseCase(newsRepository)
    }

}