package com.jean.postapp.domain.usecase.post

import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritePostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : GetFavoritePostUseCase {

    override operator fun invoke(): Flow<Result<List<Post>>> {
        return postRepository.getFavoritePosts()
    }
}