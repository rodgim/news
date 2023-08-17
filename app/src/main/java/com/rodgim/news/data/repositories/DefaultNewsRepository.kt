package com.rodgim.news.data.repositories

import com.rodgim.news.data.datasources.NewsLocalDataSource
import com.rodgim.news.data.datasources.NewsRemoteDataSource
import com.rodgim.news.domain.models.Article
import com.rodgim.news.domain.repositories.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultNewsRepository @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {
    override suspend fun getBreakingNews(countryCode: String, page: Int) = newsRemoteDataSource.getBreakingNews(countryCode, page)

    override suspend fun searchNews(searchQuery: String, page: Int) = newsRemoteDataSource.searchNews(searchQuery, page)

    override fun getSavedArticles(): Flow<List<Article>> = newsLocalDataSource.getSavedArticles()

    override suspend fun saveArticle(article: Article) = newsLocalDataSource.saveArticle(article)

    override suspend fun deleteArticle(article: Article) = newsLocalDataSource.deleteArticle(article)
}
