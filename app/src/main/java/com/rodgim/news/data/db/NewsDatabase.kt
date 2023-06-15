package com.rodgim.news.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodgim.news.data.db.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}