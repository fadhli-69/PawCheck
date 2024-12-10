package com.capstone.pawcheck.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstone.pawcheck.data.local.entity.UserProfile

@Database(entities = [UserProfile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
}
