package com.capstone.pawcheck.data.remote.connect

import com.capstone.pawcheck.data.remote.response.ResponseArticle
import retrofit2.http.GET

interface ApiService {
    @GET("api/articles")
    suspend fun getArticle() : ResponseArticle
}