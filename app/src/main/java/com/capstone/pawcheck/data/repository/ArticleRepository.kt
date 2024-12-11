package com.capstone.pawcheck.data.repository

import com.capstone.pawcheck.data.remote.connect.ApiService

class ArticleRepository(private val apiService: ApiService) {
    suspend fun getArticles() = apiService.getArticle()
}