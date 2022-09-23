package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.data.util.Result
import javax.inject.Inject

class GetAllPostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : GetAllPostUseCase {

    override suspend operator fun invoke(isRefreshing: Boolean): Result<List<Post>> {
        val allPosts = postRepository.getAllPost(isRefreshing)
        return if (allPosts is Result.Success) {
            Result.Success(postRepository.getAllPost(isRefreshing).data?.
            sortedByDescending { post ->
                post.isFavorite
            } ?: listOf())
        } else {
            Result.Failure()
        }
    }
}