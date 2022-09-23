package com.jean.postapp.data.datasource.post

import com.jean.postapp.data.network.retrofit.response.post.CommentApiModel
import com.jean.postapp.data.network.retrofit.response.post.PostApiModel
import com.jean.postapp.data.util.Result

interface PostRemoteDataSource {

    suspend fun getAllPosts(): Result<List<PostApiModel>>

    suspend fun getComments(postId: Int): Result<List<CommentApiModel>>
}