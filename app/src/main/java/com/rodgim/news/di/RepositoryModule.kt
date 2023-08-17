package com.rodgim.news.di

import com.rodgim.news.data.datasources.NewsLocalDataSource
import com.rodgim.news.data.datasources.NewsRemoteDataSource
import com.rodgim.news.data.repositories.DefaultNewsRepository
import com.rodgim.news.domain.repositories.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        newsRemoteDataSource: NewsRemoteDataSource,
        newsLocalDataSource: NewsLocalDataSource
    ): NewsRepository = DefaultNewsRepository(newsRemoteDataSource, newsLocalDataSource)
}
