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
class BreakingNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<BreakingNewsUiModel>(BreakingNewsUiModel.Loading)
    val state = _state.asStateFlow()

    val lastVisible = MutableStateFlow(1)

    init {
        viewModelScope.launch {
            lastVisible.collect {
                notifyLastVisible(it)
            }
        }
    }

    private suspend fun notifyLastVisible(lastVisible: Int) {
        val result = newsRepository.getBreakingNews("us", lastVisible)
        result.fold(
            onSuccess = {
                _state.value = BreakingNewsUiModel.Load(it)
            },
            onFailure = {
                it.message?.let { message ->
                    _state.value = BreakingNewsUiModel.Error(message)
                }
            }
        )
    }

}

sealed class BreakingNewsUiModel {
    data class Load(val articles: List<Article>): BreakingNewsUiModel()
    data class Error(val message: String): BreakingNewsUiModel()
    object Loading: BreakingNewsUiModel()
}
