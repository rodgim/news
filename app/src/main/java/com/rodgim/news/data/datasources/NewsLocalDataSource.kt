package com.rodgim.news.data.datasources

import com.rodgim.news.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {
    fun getSavedArticles(): Flow<List<Article>>

    suspend fun saveArticle(article: Article)

    suspend fun deleteArticle(article: Article)
}
