package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.repository.post.PostRepository
import javax.inject.Inject

class RemoveAllPostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : RemoveAllPostUseCase {

    override suspend operator fun invoke() {
        postRepository.removeAllPost()
    }
}