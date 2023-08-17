package com.rodgim.news.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodgim.news.domain.models.Article
import com.rodgim.news.domain.repositories.NewsRepository
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

    val search = MutableStateFlow(SearchRequestData(queryText = "", page = 1))

    init {
        viewModelScope.launch {
            search.collect {
                if (it.queryText.isNotEmpty()) {
                    search(it)
                }
            }
        }
    }

    private suspend fun search(searchRequestData: SearchRequestData) {
        viewModelScope.launch {
            val result = newsRepository.searchNews(searchRequestData.queryText, searchRequestData.page)
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

data class SearchRequestData(
    val queryText: String = "",
    val page: Int = 1
)

sealed class SearchNewsUiModel {
    data class Load(val articles: List<Article>): SearchNewsUiModel()
    data class Error(val message: String): SearchNewsUiModel()
    object Loading: SearchNewsUiModel()
}
