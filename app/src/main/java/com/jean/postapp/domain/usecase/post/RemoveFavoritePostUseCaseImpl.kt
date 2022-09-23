package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.domain.model.post.Post
import javax.inject.Inject

class RemoveFavoritePostUseCaseImpl @Inject constructor(
    private val repository: PostRepository
) : RemoveFavoritePostUseCase {

    override suspend operator fun invoke(post: Post) {
        repository.removeFavoritePost(post)
    }
}