package com.rodgim.news.data.datasources

import com.rodgim.news.domain.models.Article

interface NewsRemoteDataSource {
    suspend fun getBreakingNews(countryCode: String, page: Int): Result<List<Article>>

    suspend fun searchNews(searchQuery: String, page: Int): Result<List<Article>>
}