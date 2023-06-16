package com.rodgim.news.data.datasources

import com.rodgim.news.data.db.ArticleDao
import com.rodgim.news.data.toDomain
import com.rodgim.news.data.toEntity
import com.rodgim.news.domain.models.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomNewsLocalDataSource(
    private val articleDao: ArticleDao
) : NewsLocalDataSource {
    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDao.getSavedArticles().map { articles -> articles.map { it.toDomain() } }
    }

    override suspend fun saveArticle(article: Article) {
        articleDao.save(article.toEntity())
    }

    override suspend fun deleteArticle(article: Article) {
        articleDao.delete(article.toEntity())
    }
}
