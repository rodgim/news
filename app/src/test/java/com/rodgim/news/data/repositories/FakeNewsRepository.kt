package com.rodgim.news.data.repositories

import com.rodgim.news.domain.models.Article
import com.rodgim.news.domain.models.Source
import com.rodgim.news.domain.repositories.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class FakeNewsRepository : NewsRepository {

    private val savedNews = mutableListOf<Article>()
    private var hasInternetConnectivity = true

    override suspend fun getBreakingNews(countryCode: String, page: Int): Result<List<Article>> {
        if (!hasInternetConnectivity) {
            return Result.failure(UnknownHostException("UnknownHostException"))
        }
        return if (page == 1) {
            Result.success(listOf(fakeArticle))
        } else {
            Result.success(listOf(fakeArticle.copy(id = 2)))
        }
    }

    override suspend fun searchNews(searchQuery: String, page: Int): Result<List<Article>> {
        if (!hasInternetConnectivity) {
            return Result.failure(UnknownHostException("UnknownHostException"))
        }
        return if (page == 1) {
            Result.success(listOf(fakeArticle).filter { it.content.lowercase().contains(searchQuery.lowercase()) })
        } else {
            Result.success(listOf(fakeArticle.copy(id = 2)).filter { it.content.lowercase().contains(searchQuery.lowercase()) })
        }
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return flow { emit(savedNews) }
    }

    override suspend fun saveArticle(article: Article) {
        savedNews.add(article)
    }

    override suspend fun deleteArticle(article: Article) {
        savedNews.remove(article)
    }

    fun hasInternetConnectivity(hasConnectivity: Boolean) {
        this.hasInternetConnectivity = hasConnectivity
    }
}

val fakeArticle = Article(
    id = 1,
    author = "John Doe",
    content = "Hurricane is rapidly intensifying, could bring significant flooding to Southern California",
    description = "Hurricane is rapidly intensifying, could bring significant flooding to Southern California",
    publishedAt = "2023-08-17T16:39:00Z",
    source = Source(id = "fake", "Fake"),
    title = "Hurricane is rapidly intensifying, could bring significant flooding to Southern California",
    url = "https://www.test.com",
    urlToImage = "https://www.image.com"
)
