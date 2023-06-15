package com.rodgim.news.data

import com.rodgim.news.domain.models.Article
import com.rodgim.news.domain.models.Source
import com.rodgim.news.data.api.models.Article as ArticleApi
import com.rodgim.news.data.api.models.Source as SourceApi

fun ArticleApi.toDomain() = Article(
    author = author ?: "",
    content = content ?: "",
    description = description ?: "",
    publishedAt = publishedAt,
    source = source.toDomain(),
    title = title,
    url = url,
    urlToImage = urlToImage ?: ""
)

fun SourceApi.toDomain() = Source(
    id = id,
    name = name
)
