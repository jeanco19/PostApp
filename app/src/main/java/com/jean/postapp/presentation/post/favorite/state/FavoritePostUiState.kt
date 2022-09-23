package com.jean.postapp.presentation.post.favorite.state

import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.util.Constant

data class FavoritePostUiState(
    val items: List<Post> = listOf(),
    val isLoading: Boolean = false,
    val informativeMessage: String = Constant.EMPTY_STRING,
    val errorMessage: String = Constant.EMPTY_STRING
)