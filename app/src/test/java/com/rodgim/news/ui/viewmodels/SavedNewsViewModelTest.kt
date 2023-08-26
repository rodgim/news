package com.rodgim.news.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.rodgim.news.MainDispatcherRule
import com.rodgim.news.data.repositories.FakeNewsRepository
import com.rodgim.news.data.repositories.fakeArticle
import com.rodgim.news.domain.models.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SavedNewsViewModelTest {
    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: SavedNewsViewModel
    private lateinit var fakeNewsRepository: FakeNewsRepository

    @Before
    fun setup() {
        fakeNewsRepository = FakeNewsRepository()
        viewModel = SavedNewsViewModel(fakeNewsRepository)
    }

    @Test
    fun `when an article is saved, verify that the article is in the local data source`() = runTest {
        val expectedResult = listOf(fakeArticle)

        viewModel.saveArticle(fakeArticle)
        advanceUntilIdle()

        val result = viewModel.state.first()

        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `when an article is deleted, verify that the article isn't in the local data source`() = runTest {
        val expectedResult = emptyList<Article>()

        viewModel.saveArticle(fakeArticle)
        advanceUntilIdle()
        viewModel.deleteArticle(fakeArticle)
        advanceUntilIdle()

        val result = fakeNewsRepository.getSavedArticles().first()

        Truth.assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `when the local data source is empty, return an empty list`() = runTest {
        val expectedResult = emptyList<Article>()

        val result = fakeNewsRepository.getSavedArticles().first()

        Truth.assertThat(result).isEqualTo(expectedResult)
    }
}