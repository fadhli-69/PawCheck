package com.capstone.pawcheck.data.repository

import androidx.lifecycle.LiveData
import com.capstone.pawcheck.data.local.entity.ArticleEntity
import com.capstone.pawcheck.data.local.room.ArticleDao
import com.capstone.pawcheck.data.remote.connect.ApiService
import java.io.IOException
import java.net.UnknownHostException

class ArticleRepository(
    private val apiService: ApiService,
    private val articleDao: ArticleDao
) {
    val localArticles: LiveData<List<ArticleEntity>> = articleDao.getAllArticles()

    suspend fun fetchArticlesFromApi() {
        try {
            val response = apiService.getArticle()
            val articles = response.listStory?.map { story ->
                ArticleEntity(
                    id = story?.id ?: "",
                    name = story?.name ?: "",
                    description = story?.description ?: "",
                    photoUrl = story?.photoUrl ?: "",
                    createdAt = story?.createdAt ?: ""
                )
            } ?: emptyList()

            articleDao.clearArticles()
            articleDao.insertArticles(articles)
        } catch (e: UnknownHostException) {
            throw IOException("No Internet Connection")
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("An error occurred while fetching articles")
        }
    }
}