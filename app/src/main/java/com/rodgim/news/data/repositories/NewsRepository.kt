package com.rodgim.news.data.repositories

import com.rodgim.news.data.datasources.NewsRemoteDataSource
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource
) {
    suspend fun getBreakingNews(countryCode: String, page: Int) = newsRemoteDataSource.getBreakingNews(countryCode, page)

    suspend fun searchNews(searchQuery: String, page: Int) = newsRemoteDataSource.searchNews(searchQuery, page)
}