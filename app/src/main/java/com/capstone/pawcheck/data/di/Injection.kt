package com.capstone.pawcheck.data.di

import com.capstone.pawcheck.data.repository.ArticleRepository
import com.capstone.pawcheck.data.remote.connect.ApiConfig

object Injection {
    fun provideRepository(): ArticleRepository {
        val apiService = ApiConfig.getApiService()
        return ArticleRepository(apiService)
    }
}