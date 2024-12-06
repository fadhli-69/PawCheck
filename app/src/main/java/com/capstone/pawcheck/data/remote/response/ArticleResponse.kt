package com.capstone.pawcheck.data.remote.response

data class ArticleResponse(
     val id : String,
     val title : String,
     val image : String,
     val content : String,
     val author : String,
     val createdAt: String,
)