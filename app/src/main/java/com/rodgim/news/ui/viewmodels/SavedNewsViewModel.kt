package com.rodgim.news.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodgim.news.domain.models.Article
import com.rodgim.news.domain.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    val state = newsRepository.getSavedArticles()

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.saveArticle(article)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
    }
}
