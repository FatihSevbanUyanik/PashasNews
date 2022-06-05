package com.example.pashanews.di

import com.example.pashanews.domain.repository.CurrencyRepository
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
    fun provideGetTopHeadlinesUseCase(newsRepository: NewsRepository) = GetTopHeadlinesFromApiUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideGetNewsUseCase(newsRepository: NewsRepository) = GetNewsFromApiUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideInsertArticleToDbUseCase(newsRepository: NewsRepository) = InsertArticleToDbUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideDeleteArticleFromDbUseCase(newsRepository: NewsRepository) = DeleteArticleFromDBUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideGetArticlesFromDbUseCase(newsRepository: NewsRepository) = GetFavoriteArticlesFromDbUseCase(newsRepository)

    @Singleton
    @Provides
    fun provideGetCurrenciesUseCase(currencyRepository: CurrencyRepository) = GetCurrenciesUseCase(currencyRepository)

    @Singleton
    @Provides
    fun provideGetCryptoUseCase(currencyRepository: CurrencyRepository) = GetCryptoUseCase(currencyRepository)

}