package com.jean.postapp.data.network.retrofit.response.post

class CommentApiResponse : ArrayList<CommentApiModel>()

data class CommentApiModel(
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String
)