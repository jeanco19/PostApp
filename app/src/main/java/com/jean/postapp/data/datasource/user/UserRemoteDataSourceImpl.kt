package com.jean.postapp.data.datasource.user

import com.jean.postapp.data.network.retrofit.RetrofitService
import com.jean.postapp.data.network.retrofit.response.author.AuthorApiModel
import com.jean.postapp.data.util.Result
import com.jean.postapp.data.util.bodyOrException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val retrofitService: RetrofitService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRemoteDataSource {

    override suspend fun getAuthor(postId: Int): Result<AuthorApiModel> = withContext(ioDispatcher) {
        try {
            val response = retrofitService.getAuthorPost(postId).bodyOrException()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Failure()
        }
    }
}