package com.jean.postapp.data.network.retrofit.response.author

data class AuthorApiModel(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String
)