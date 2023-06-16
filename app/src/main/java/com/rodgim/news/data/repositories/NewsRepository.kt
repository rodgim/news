package com.rodgim.news.data.repositories

import com.rodgim.news.data.datasources.NewsLocalDataSource
import com.rodgim.news.data.datasources.NewsRemoteDataSource
import com.rodgim.news.domain.models.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) {
    suspend fun getBreakingNews(countryCode: String, page: Int) = newsRemoteDataSource.getBreakingNews(countryCode, page)

    suspend fun searchNews(searchQuery: String, page: Int) = newsRemoteDataSource.searchNews(searchQuery, page)

    fun getSavedArticles(): Flow<List<Article>> = newsLocalDataSource.getSavedArticles()

    suspend fun saveArticle(article: Article) = newsLocalDataSource.saveArticle(article)

    suspend fun deleteArticle(article: Article) = newsLocalDataSource.deleteArticle(article)
}
