package com.capstone.pawcheck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.pawcheck.data.repository.ArticleRepository
import com.capstone.pawcheck.data.remote.response.ResponseArticle
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _articles = MutableLiveData<ResponseArticle>()
    val articles: LiveData<ResponseArticle> get() = _articles

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchArticles() {
        viewModelScope.launch {
            try {
                val response = repository.getArticles()
                _articles.postValue(response)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}