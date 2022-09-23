package com.jean.postapp.data.network.retrofit.response.post

class PostApiResponse : ArrayList<PostApiModel>()

data class PostApiModel(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)