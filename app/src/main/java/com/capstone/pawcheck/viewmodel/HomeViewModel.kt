package com.capstone.pawcheck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.pawcheck.data.local.entity.ArticleEntity
import com.capstone.pawcheck.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    val articles: LiveData<List<ArticleEntity>> = repository.localArticles

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchArticles() {
        viewModelScope.launch {
            try {
                repository.fetchArticlesFromApi()
            } catch (e: IOException) {
                _error.postValue(e.message ?: "No Internet Connection")
            } catch (e: Exception) {
                _error.postValue("An unknown error occurred")
            }
        }
    }
}