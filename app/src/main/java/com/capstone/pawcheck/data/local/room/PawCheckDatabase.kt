package com.capstone.pawcheck.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.pawcheck.data.local.entity.DrugsEntity
import com.capstone.pawcheck.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, DrugsEntity::class], version = 1, exportSchema = false)
abstract class PawCheckDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun drugsDao(): DrugsDao

    companion object {
        @Volatile
        private var instance: PawCheckDatabase? = null

        fun getInstance(context: Context): PawCheckDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PawCheckDatabase::class.java, "PawCheckDatabase.db"
                ).build().also { instance = it }
            }
    }
}