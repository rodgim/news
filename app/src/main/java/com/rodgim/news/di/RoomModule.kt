package com.rodgim.news.di

import android.content.Context
import androidx.room.Room
import com.rodgim.news.data.db.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context) =
        Room
            .databaseBuilder(
                context,
                NewsDatabase::class.java,
                "news_db.db"
            )
            .build()

    @Singleton
    @Provides
    fun provideArticleDao(newsDatabase: NewsDatabase) = newsDatabase.getArticleDao()
}
