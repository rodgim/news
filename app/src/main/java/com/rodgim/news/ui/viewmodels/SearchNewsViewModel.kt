package com.rodgim.news.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodgim.news.data.repositories.NewsRepository
import com.rodgim.news.domain.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchNewsUiModel>(SearchNewsUiModel.Load(emptyList()))
    val state = _state.asStateFlow()

    suspend fun search(query: String) {
        viewModelScope.launch {
            val result = newsRepository.searchNews(query, 1)
            result.fold(
                onSuccess = {
                    _state.value = SearchNewsUiModel.Load(it)
                },
                onFailure = {
                    it.message?.let { message ->
                        _state.value = SearchNewsUiModel.Error(message)
                    }
                }
            )
        }
    }

}

sealed class SearchNewsUiModel {
    data class Load(val articles: List<Article>): SearchNewsUiModel()
    data class Error(val message: String): SearchNewsUiModel()
    object Loading: SearchNewsUiModel()
}
