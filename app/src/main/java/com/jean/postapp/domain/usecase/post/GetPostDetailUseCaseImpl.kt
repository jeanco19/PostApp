package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.repository.user.UserRepository
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post
import javax.inject.Inject

class GetPostDetailUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : GetPostDetailUseCase {

    override suspend operator fun invoke(postId: Int): Result<Post>  {
        return when(val post = postRepository.getPost(postId)) {
            is Result.Success -> {
                post.data?.let {
                    val comments = postRepository.getCommentPost(postId)
                    val author = userRepository.getAuthorPost(postId)
                    it.comments = comments.data ?: listOf()
                    it.author = author.data
                    Result.Success(it)
                } ?: kotlin.run {
                    Result.Failure()
                }
            }
            else -> Result.Failure()
        }
    }
}