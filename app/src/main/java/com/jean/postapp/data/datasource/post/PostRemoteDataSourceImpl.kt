package com.jean.postapp.data.datasource.post

import com.jean.postapp.data.network.retrofit.RetrofitService
import com.jean.postapp.data.network.retrofit.response.post.CommentApiModel
import com.jean.postapp.data.network.retrofit.response.post.PostApiModel
import com.jean.postapp.data.util.Result
import com.jean.postapp.data.util.bodyOrException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(
    private val retrofitService: RetrofitService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostRemoteDataSource {

    override suspend fun getAllPosts(): Result<List<PostApiModel>> = withContext(ioDispatcher) {
        try {
            val response = retrofitService.getAllPosts().bodyOrException()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Failure()
        }
    }

    override suspend fun getComments(postId: Int): Result<List<CommentApiModel>> =
        withContext(ioDispatcher) {
        try {
            val response = retrofitService.getPostComments(postId).bodyOrException()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Failure()
        }
    }
}