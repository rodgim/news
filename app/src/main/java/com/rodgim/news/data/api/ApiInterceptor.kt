package com.rodgim.news.data.api

import com.rodgim.news.BuildConfig
import com.rodgim.news.data.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(Constants.API_KEY, BuildConfig.API_KEY)
            .build()

        return chain.proceed(request)
    }
}
