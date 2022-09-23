package com.jean.postapp.data.datasource.post

import com.jean.postapp.data.database.PostDao
import com.jean.postapp.data.database.entity.PostEntity
import com.jean.postapp.data.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostLocalDataSourceImpl @Inject constructor(
    private val postDao: PostDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostLocalDataSource {

    override suspend fun insertPost(postEntity: PostEntity) = withContext(ioDispatcher) {
        postDao.insertPost(postEntity)
    }

    override suspend fun insertSeenPost(postEntity: PostEntity) = withContext(ioDispatcher) {
        postDao.updatePost(postEntity)
    }

    override suspend fun insertFavoritePost(postEntity: PostEntity) = withContext(ioDispatcher) {
        postDao.updatePost(postEntity)
    }

    override suspend fun getPost(postId: Int): Result<PostEntity> = withContext(ioDispatcher) {
        try {
            val localData = postDao.getPost(postId)
            Result.Success(localData)
        } catch (e: Exception) {
            Result.Failure()
        }
    }

    override suspend fun getAllPost(): Result<List<PostEntity>> = withContext(ioDispatcher) {
        try {
            val localData = postDao.getAllPost()
            Result.Success(localData)
        } catch (e: Exception) {
            Result.Failure()
        }
    }

    override fun getFavoritePosts(): Flow<Result<List<PostEntity>>> {
        return flow<Result<List<PostEntity>>> {
            postDao.getFavoritePosts().collect { postEntities ->
                emit(Result.Success(postEntities))
            }
        }.catch {
            emit(Result.Failure())
        }.flowOn(ioDispatcher)
    }

    override suspend fun removePost(postEntity: PostEntity) = withContext(ioDispatcher) {
        postDao.removePost(postEntity)
    }

    override suspend fun removeFavoritePost(postEntity: PostEntity) = withContext(ioDispatcher) {
        postDao.updatePost(postEntity)
    }

    override suspend fun removeAllPost() = withContext(ioDispatcher) {
        postDao.removeAllPost()
    }
}