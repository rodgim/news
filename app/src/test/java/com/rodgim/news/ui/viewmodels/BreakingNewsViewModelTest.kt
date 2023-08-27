package com.rodgim.news.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.rodgim.news.MainDispatcherRule
import com.rodgim.news.data.repositories.FakeNewsRepository
import com.rodgim.news.data.repositories.fakeArticle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException


@OptIn(ExperimentalCoroutinesApi::class)
class BreakingNewsViewModelTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: BreakingNewsViewModel
    private lateinit var fakeNewsRepository: FakeNewsRepository

    @Before
    fun setup() {
        fakeNewsRepository = FakeNewsRepository()
        viewModel = BreakingNewsViewModel(fakeNewsRepository)
    }

    @Test
    fun `The first state that shows is the loading`() = runTest {
        val expectedResult = BreakingNewsUiModel.Loading

        val state = viewModel.state.value

        assertThat(state).isEqualTo(expectedResult)
    }

    @Test
    fun `Return list of articles`() = runTest {
        val expectedResult = BreakingNewsUiModel.Load(listOf(fakeArticle))

        viewModel.getBreakingNews()
        advanceUntilIdle()

        val state = viewModel.state.value

        assertThat(state).isEqualTo(expectedResult)
    }

    @Test
    fun `When last visible change, return the next list of articles`() = runTest {
        val expectedResult = BreakingNewsUiModel.Load(listOf(fakeArticle, fakeArticle.copy(id = 2)))

        viewModel.getBreakingNews()
        advanceUntilIdle()
        viewModel.lastVisible.value = 2
        advanceUntilIdle()

        val state = viewModel.state.value

        assertThat(state).isEqualTo(expectedResult)
    }

    @Test
    fun `When the app doesn't have an internet connection, return an unknownHostException`() = runTest {
        val unknownHostException = UnknownHostException("UnknownHostException")
        val expectedResult = BreakingNewsUiModel.Error(unknownHostException.message ?: "")

        fakeNewsRepository.hasInternetConnectivity(false)
        viewModel.getBreakingNews()
        advanceUntilIdle()

        val state = viewModel.state.value

        assertThat(state).isEqualTo(expectedResult)
    }
}