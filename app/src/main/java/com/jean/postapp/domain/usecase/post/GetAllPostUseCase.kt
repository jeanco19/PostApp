package com.jean.postapp.domain.usecase.post

import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.data.util.Result

interface GetAllPostUseCase {

    suspend operator fun invoke(isRefreshing: Boolean): Result<List<Post>>
}