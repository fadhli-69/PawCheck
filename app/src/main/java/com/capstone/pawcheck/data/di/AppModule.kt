package com.capstone.pawcheck.data.di

import android.app.Application
import androidx.room.Room
import com.capstone.pawcheck.data.local.room.AppDatabase
import com.capstone.pawcheck.data.local.room.ArticleDao
import com.capstone.pawcheck.data.local.room.UserProfileDao
import com.capstone.pawcheck.data.remote.connect.ApiConfig
import com.capstone.pawcheck.data.remote.connect.ApiService
import com.capstone.pawcheck.data.repository.ArticleRepository
import com.capstone.pawcheck.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(db: AppDatabase): UserProfileDao {
        return db.userProfileDao()
    }

    @Provides
    @Singleton
    fun provideArticleDao(db: AppDatabase): ArticleDao {
        return db.articleDao()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userProfileDao: UserProfileDao): UserRepository {
        return UserRepository(userProfileDao)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(apiService: ApiService, articleDao: ArticleDao): ArticleRepository {
        return ArticleRepository(apiService, articleDao)
    }
}
