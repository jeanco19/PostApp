package com.jean.postapp.data.repository.post

import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Comment
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun insertFavoritePost(post: Post)

    suspend fun insertSeenPost(post: Post)

    suspend fun getPost(postId: Int): Result<Post>

    suspend fun getCommentPost(postId: Int): Result<List<Comment>>

    suspend fun getAllPost(isRefreshing: Boolean): Result<List<Post>>

    fun getFavoritePosts(): Flow<Result<List<Post>>>

    suspend fun removeFavoritePost(post: Post)

    suspend fun removePost(post: Post)

    suspend fun removeAllPost()
}