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


@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModelTest {
    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: ArticleViewModel
    private lateinit var fakeNewsRepository: FakeNewsRepository

    @Before
    fun setup() {
        fakeNewsRepository = FakeNewsRepository()
        viewModel = ArticleViewModel(fakeNewsRepository)
    }

    @Test
    fun `when an article is saved, verify that the article is in the local data source`() = runTest {
        val expectedResult = fakeArticle

        viewModel.save(fakeArticle)
        advanceUntilIdle()

        val result = fakeNewsRepository.getSavedNews().first()

        assertThat(result).isEqualTo(expectedResult)
    }
}