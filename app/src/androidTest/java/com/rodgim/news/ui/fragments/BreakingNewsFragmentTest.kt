package com.rodgim.news.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.rodgim.news.R
import com.rodgim.news.di.RetrofitModule
import com.rodgim.news.di.RoomModule
import com.rodgim.news.ui.adapters.NewsAdapter
import com.rodgim.news.util.Constants
import com.rodgim.news.util.MockWebServerDispatcher
import com.rodgim.news.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@LargeTest
@UninstallModules(
    RetrofitModule::class,
    RoomModule::class
)
@HiltAndroidTest
class BreakingNewsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockWebServer = MockWebServer()

    @Inject
    lateinit var okHttp: OkHttpClient

    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Before
    fun setup() {
        hiltRule.inject()
        okHttp3IdlingResource = OkHttp3IdlingResource.create("OkHttp", okHttp)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
        mockWebServer.shutdown()
    }

    @Test
    fun screenIsReady() {
        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()

        launchFragmentInHiltContainer<BreakingNewsFragment> {}

        onView(withId(R.id.rvBreakingNews)).check { view, noViewException ->
            if (noViewException != null) {
                throw noViewException
            }
            val recyclerView = view as RecyclerView
            assertThat(recyclerView.adapter?.itemCount).isEqualTo(20)
        }
    }

    @Test
    fun showErrorWhenNewsLoadFailed() {
        mockWebServer.dispatcher = MockWebServerDispatcher().ErrorDispatcher()

        launchFragmentInHiltContainer<BreakingNewsFragment> {}

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(Constants.ERROR_MESSAGE)))

    }

    @Test
    fun clickNewsItem_navigateToArticleFragment() {
        mockWebServer.dispatcher = MockWebServerDispatcher().RequestDispatcher()
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        launchFragmentInHiltContainer<BreakingNewsFragment> {
            navController.setGraph(R.navigation.news_nav_graph)

            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.rvBreakingNews)).perform(
            RecyclerViewActions.actionOnItemAtPosition<NewsAdapter.ViewHolder>(
                0,
                click()
            )
        )

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.articleFragment)
    }
}