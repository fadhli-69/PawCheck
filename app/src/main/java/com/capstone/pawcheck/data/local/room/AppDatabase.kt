package com.capstone.pawcheck.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstone.pawcheck.data.local.entity.ArticleEntity
import com.capstone.pawcheck.data.local.entity.UserEntity

@Database(
    entities = [
        ArticleEntity::class,
        UserEntity::class  // Add this line
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun articleDao(): ArticleDao
}