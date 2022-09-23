package com.jean.postapp.data.repository.user

import com.jean.postapp.data.datasource.user.UserRemoteDataSource
import com.jean.postapp.data.mapper.toAuthor
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.user.Author
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun getAuthorPost(postId: Int): Result<Author> = withContext(ioDispatcher) {
        try {
            val response = userRemoteDataSource.getAuthor(postId)
            response.data?.let {
                Result.Success(it.toAuthor())
            } ?: kotlin.run {
                Result.Failure()
            }
        } catch (e: Exception) {
            Result.Failure()
        }
    }
}