package com.rodgim.news.domain.repositories

import com.rodgim.news.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getBreakingNews(countryCode: String, page: Int): Result<List<Article>>

    suspend fun searchNews(searchQuery: String, page: Int): Result<List<Article>>

    fun getSavedArticles(): Flow<List<Article>>

    suspend fun saveArticle(article: Article)

    suspend fun deleteArticle(article: Article)

}
