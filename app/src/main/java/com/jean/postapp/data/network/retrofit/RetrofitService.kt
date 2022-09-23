package com.jean.postapp.data.network.retrofit

import com.jean.postapp.data.network.retrofit.response.author.AuthorApiModel
import com.jean.postapp.data.network.retrofit.response.post.CommentApiResponse
import com.jean.postapp.data.network.retrofit.response.post.PostApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    @GET("posts")
    suspend fun getAllPosts(): Response<PostApiResponse>

    @GET("posts/{post_id}/comments")
    suspend fun getPostComments(@Path("post_id") postId: Int): Response<CommentApiResponse>

    @GET("users/{post_id}")
    suspend fun getAuthorPost(@Path("post_id") postId: Int): Response<AuthorApiModel>
}