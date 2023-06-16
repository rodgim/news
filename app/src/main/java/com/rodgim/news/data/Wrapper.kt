package com.rodgim.news.data

import com.rodgim.news.domain.models.Article
import com.rodgim.news.domain.models.Source
import com.rodgim.news.data.api.models.Article as ArticleApi
import com.rodgim.news.data.api.models.Source as SourceApi
import com.rodgim.news.data.db.models.Article as ArticleDb
import com.rodgim.news.data.db.models.Source as SourceDb

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

fun ArticleDb.toDomain() = Article(
    id = id,
    author = author ?: "",
    content = content ?: "",
    description = description ?: "",
    publishedAt = publishedAt,
    source = source.toDomain(),
    title = title,
    url = url,
    urlToImage = urlToImage ?: ""
)

fun SourceDb.toDomain() = Source(
    id = id,
    name = name
)

fun Article.toEntity() = ArticleDb(
    id = id ?: -1,
    author = author ?: "",
    content = content ?: "",
    description = description ?: "",
    publishedAt = publishedAt,
    source = source.toEntity(),
    title = title,
    url = url,
    urlToImage = urlToImage ?: ""
)

fun Source.toEntity() = SourceDb(
    id = id ?: "",
    name = name
)
