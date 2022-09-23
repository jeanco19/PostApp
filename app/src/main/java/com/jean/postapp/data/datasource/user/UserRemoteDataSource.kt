package com.jean.postapp.data.datasource.user

import com.jean.postapp.data.network.retrofit.response.author.AuthorApiModel
import com.jean.postapp.data.util.Result

interface UserRemoteDataSource {

    suspend fun getAuthor(postId: Int): Result<AuthorApiModel>
}