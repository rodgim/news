package com.rodgim.news.di

import android.content.Context
import androidx.room.Room
import com.rodgim.news.data.api.ApiInterceptor
import com.rodgim.news.data.api.NewsApi
import com.rodgim.news.data.datasources.NewsLocalDataSource
import com.rodgim.news.data.datasources.NewsRemoteDataSource
import com.rodgim.news.data.datasources.RetrofitNewsRemoteDataSource
import com.rodgim.news.data.datasources.RoomNewsLocalDataSource
import com.rodgim.news.data.db.ArticleDao
import com.rodgim.news.data.db.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun provideArticleDao(newsDatabase: NewsDatabase) = newsDatabase.getArticleDao()

    @Singleton
    @Provides
    fun provideNewsLocalDataSource(articleDao: ArticleDao): NewsLocalDataSource = RoomNewsLocalDataSource(articleDao)

    @Singleton
    @Provides
    fun provideBaseUrl() = "http://127.0.0.1:8080/"

    @Singleton
    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
        apiInterceptor: ApiInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiInterceptor)

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideApiInterceptor() = ApiInterceptor()

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Singleton
    @Provides
    fun providesNewsRemoteDataSource(newsApi: NewsApi): NewsRemoteDataSource = RetrofitNewsRemoteDataSource(newsApi)
}