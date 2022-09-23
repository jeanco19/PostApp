package com.jean.postapp.domain.usecase.post

import com.jean.postapp.domain.model.post.Post

interface RemoveFavoritePostUseCase {

    suspend operator fun invoke(post: Post)
}