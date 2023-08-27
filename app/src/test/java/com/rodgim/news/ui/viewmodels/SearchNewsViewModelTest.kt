package com.rodgim.news.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.rodgim.news.MainDispatcherRule
import com.rodgim.news.data.repositories.FakeNewsRepository
import com.rodgim.news.data.repositories.fakeArticle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchNewsViewModelTest {
    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: SearchNewsViewModel
    private lateinit var fakeNewsRepository: FakeNewsRepository

    @Before
    fun setup() {
        fakeNewsRepository = FakeNewsRepository()
        viewModel = SearchNewsViewModel(fakeNewsRepository)
    }

    @Test
    fun `By default return an empty list of articles`() = runTest {
        val expectedResult = SearchNewsUiModel.Load(emptyList())

        val result = viewModel.state.value

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `Filter the list of articles by query, return the loading state`() = runTest {
        val expectedResult = SearchNewsUiModel.Loading
        val testResults = mutableListOf<SearchNewsUiModel>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(testResults)
        }

        viewModel.search.value = SearchRequestData("Hurricane")
        advanceUntilIdle()

        assertThat(testResults.drop(1).first()).isEqualTo(expectedResult)
    }

    @Test
    fun `Filter the list of articles by query, return a list if the query matches an article`() = runTest {
        val expectedResult = SearchNewsUiModel.Load(listOf(fakeArticle))

        viewModel.search.value = SearchRequestData("Hurricane")
        advanceUntilIdle()

        val result = viewModel.state.value

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `Filter the list of articles by query, return an empty list if the query doesn't match an article`() = runTest {
        val expectedResult = SearchNewsUiModel.Load(emptyList())

        viewModel.search.value = SearchRequestData("Android")
        advanceUntilIdle()

        val result = viewModel.state.value

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `When the last visible variable changes, return the next list of articles`() = runTest {
        val expectedResult = SearchNewsUiModel.Load(listOf(fakeArticle, fakeArticle.copy(id = 2)))

        viewModel.search.value = SearchRequestData("Hurricane")
        advanceUntilIdle()
        viewModel.search.value = SearchRequestData("Hurricane", page = 2)
        advanceUntilIdle()

        val state = viewModel.state.value

        assertThat(state).isEqualTo(expectedResult)
    }

    @Test
    fun `When the app doesn't have an internet connection return an unknownHostException`() = runTest {
        val unknownHostException = UnknownHostException("UnknownHostException")
        val expectedResult = SearchNewsUiModel.Error(unknownHostException.message ?: "")

        fakeNewsRepository.hasInternetConnectivity(false)
        viewModel.search.value = SearchRequestData("Hurricane")
        advanceUntilIdle()

        val state = viewModel.state.value

        assertThat(state).isEqualTo(expectedResult)
    }
}