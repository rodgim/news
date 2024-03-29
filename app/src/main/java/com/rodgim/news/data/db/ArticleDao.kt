package com.rodgim.news.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rodgim.news.data.db.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getSavedArticles(): Flow<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}
