package com.jean.postapp.data.repository.post

import com.jean.postapp.data.datasource.post.PostLocalDataSource
import com.jean.postapp.data.datasource.post.PostRemoteDataSource
import com.jean.postapp.data.mapper.toComment
import com.jean.postapp.data.mapper.toPost
import com.jean.postapp.data.mapper.toPostEntity
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Comment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val postLocalDataSource: PostLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostRepository {

    override suspend fun insertFavoritePost(post: Post) = withContext(ioDispatcher) {
        postLocalDataSource.insertFavoritePost(post.toPostEntity())
    }

    override suspend fun insertSeenPost(post: Post) = withContext(ioDispatcher) {
        postLocalDataSource.insertSeenPost(post.toPostEntity())
    }

    override suspend fun getPost(postId: Int): Result<Post> = withContext(ioDispatcher) {
        try {
            val response = postLocalDataSource.getPost(postId)
            response.data?.let {
                Result.Success(it.toPost())
            } ?: kotlin.run {
                Result.Failure()
            }
        } catch (e: Exception) {
            Result.Failure()
        }
    }

    override suspend fun getCommentPost(postId: Int): Result<List<Comment>> =
        withContext(ioDispatcher) {
            try {
                val response = postRemoteDataSource.getComments(postId)
                response.data?.let {
                    Result.Success(it.map { commentApiModel ->
                        commentApiModel.toComment()
                    })
                } ?: kotlin.run {
                    Result.Failure()
                }
            } catch (e: Exception) {
                Result.Failure()
            }
        }

    override suspend fun getAllPost(isRefreshing: Boolean): Result<List<Post>> {
        return try {
            val localData = postLocalDataSource.getAllPost()
            if (isRefreshing) {
                val postRemote = postRemoteDataSource.getAllPosts().data?.map { it.toPost() }
                val postLocal = localData.data?.map { it.toPost() }

                postRemote?.forEach { remotePost ->
                    var isPostExist = false
                    postLocal?.forEach { localPost ->
                        if (remotePost.id == localPost.id) isPostExist = true
                    }
                    if (!isPostExist) {
                        remotePost.isSeen = false
                        postLocalDataSource.insertPost(remotePost.toPostEntity())
                    }
                }
            } else {
                if (localData is Result.Success) {
                    val postLocal = localData.data?.map { it.toPost() }

                    if (postLocal.isNullOrEmpty()) {
                        val postRemote = postRemoteDataSource.getAllPosts().data?.map { it.toPost() }
                        postRemote?.forEach { post ->
                            post.isSeen = false
                            postLocalDataSource.insertPost(post.toPostEntity())
                        }
                    }
                }
            }
            Result.Success(postLocalDataSource.getAllPost().data?.map { postEntity ->
                postEntity.toPost()
            } ?: listOf())
        } catch (e: Exception) {
            Result.Failure()
        }
    }

    override fun getFavoritePosts(): Flow<Result<List<Post>>> {
        return flow<Result<List<Post>>> {
            postLocalDataSource.getFavoritePosts().collect { postEntities ->
                emit(Result.Success(postEntities.data?.map { postEntity ->
                    postEntity.toPost()
                } ?: listOf()))
            }
        }.catch {
            emit(Result.Failure())
        }.flowOn(ioDispatcher)
    }

    override suspend fun removeFavoritePost(post: Post) = withContext(ioDispatcher) {
        postLocalDataSource.removeFavoritePost(post.toPostEntity())
    }

    override suspend fun removePost(post: Post) = withContext(ioDispatcher) {
        postLocalDataSource.removePost(post.toPostEntity())
    }

    override suspend fun removeAllPost() = withContext(ioDispatcher) {
        postLocalDataSource.removeAllPost()
    }
}