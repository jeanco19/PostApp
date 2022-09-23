package com.jean.postapp.presentation.post.detail.state

import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.util.Constant

data class PostDetailUiState(
    val post: Post? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = Constant.EMPTY_STRING,
    val informativeMessage: String = Constant.EMPTY_STRING
)