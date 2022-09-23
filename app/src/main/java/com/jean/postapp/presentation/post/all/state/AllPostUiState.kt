package com.jean.postapp.presentation.post.all.state

import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.util.Constant

data class AllPostUiState(
    val items: List<Post> = listOf(),
    val isLoading: Boolean = false,
    val postSize: Int = 0,
    val errorMessage: String = Constant.EMPTY_STRING
)