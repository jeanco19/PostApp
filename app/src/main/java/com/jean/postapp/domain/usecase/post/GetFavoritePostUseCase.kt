package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post
import kotlinx.coroutines.flow.Flow

interface GetFavoritePostUseCase {

    operator fun invoke(): Flow<Result<List<Post>>>
}