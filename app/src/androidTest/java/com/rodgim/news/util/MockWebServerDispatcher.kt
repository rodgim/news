package com.rodgim.news.util

import com.rodgim.news.BuildConfig
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebServerDispatcher {

    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/v2/top-headlines?country=us&page=1&apiKey=${BuildConfig.API_KEY}" -> {
                    MockResponse().setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("top_headlines_response.json"))
                }
                else -> MockResponse().setResponseCode(400)
            }
        }
    }

    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
                .setBody(Constants.ERROR_MESSAGE)
        }
    }
}