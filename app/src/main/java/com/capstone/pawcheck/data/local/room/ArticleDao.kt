package com.capstone.pawcheck.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.pawcheck.data.local.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<ArticleEntity>>

    @Query("DELETE FROM articles")
    suspend fun clearArticles()
}
