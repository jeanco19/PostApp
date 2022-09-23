package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.domain.model.post.Post
import javax.inject.Inject

class RemovePostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : RemovePostUseCase {

    override suspend operator fun invoke(post: Post) {
        postRepository.removePost(post)
    }
}