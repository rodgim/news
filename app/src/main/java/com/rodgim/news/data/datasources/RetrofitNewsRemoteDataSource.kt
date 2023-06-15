package com.rodgim.news.data.datasources

import com.rodgim.news.data.api.NewsApi
import com.rodgim.news.data.toDomain
import com.rodgim.news.domain.models.Article

class RetrofitNewsRemoteDataSource(
    private val newsApi: NewsApi
) : NewsRemoteDataSource {
    override suspend fun getBreakingNews(
        countryCode: String,
        page: Int
    ): Result<List<Article>> {
        return try {
            val response = newsApi.getBreakingNews(countryCode, page)
            if (response.isSuccessful) {
                Result.success(response.body()?.articles?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}