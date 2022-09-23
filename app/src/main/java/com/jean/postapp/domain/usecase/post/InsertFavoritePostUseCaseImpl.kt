package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.domain.model.post.Post
import javax.inject.Inject

class InsertFavoritePostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : InsertFavoritePostUseCase {

    override suspend operator fun invoke(post: Post) {
        postRepository.insertFavoritePost(post)
    }
}