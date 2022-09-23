package com.jean.postapp.data.repository.user

import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.user.Author

interface UserRepository {

    suspend fun getAuthorPost(postId: Int): Result<Author>
}