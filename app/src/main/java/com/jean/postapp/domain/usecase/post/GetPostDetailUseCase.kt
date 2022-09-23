package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post

interface GetPostDetailUseCase {

    suspend operator fun invoke(postId: Int): Result<Post>
}