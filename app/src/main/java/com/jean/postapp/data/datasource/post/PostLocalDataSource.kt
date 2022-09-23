package com.jean.postapp.data.datasource.post

import com.jean.postapp.data.database.entity.PostEntity
import com.jean.postapp.data.util.Result
import kotlinx.coroutines.flow.Flow

interface PostLocalDataSource {

    suspend fun insertPost(postEntity: PostEntity)

    suspend fun insertSeenPost(postEntity: PostEntity)

    suspend fun insertFavoritePost(postEntity: PostEntity)

    suspend fun getPost(postId: Int): Result<PostEntity>

    suspend fun getAllPost(): Result<List<PostEntity>>

    fun getFavoritePosts(): Flow<Result<List<PostEntity>>>

    suspend fun removePost(postEntity: PostEntity)

    suspend fun removeFavoritePost(postEntity: PostEntity)

    suspend fun removeAllPost()
}