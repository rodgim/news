package com.rodgim.news.data.api

import com.rodgim.news.data.api.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>
}
