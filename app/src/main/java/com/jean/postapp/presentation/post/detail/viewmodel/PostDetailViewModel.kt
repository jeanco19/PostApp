package com.jean.postapp.presentation.post.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.domain.usecase.post.InsertFavoritePostUseCase
import com.jean.postapp.domain.usecase.post.GetPostDetailUseCase
import com.jean.postapp.domain.usecase.post.RemoveFavoritePostUseCase
import com.jean.postapp.presentation.post.detail.state.PostDetailUiState
import com.jean.postapp.util.Constant.ADD_FAVORITE_SUCCESSFULLY
import com.jean.postapp.util.Constant.GET_DETAIL_ERROR
import com.jean.postapp.util.Constant.REMOVE_FAVORITE_SUCCESSFULLY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val insertFavoritePostUseCase: InsertFavoritePostUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val removeFavoritePostUseCase: RemoveFavoritePostUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    fun getPostDetail(postId: Int) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            when (val result = getPostDetailUseCase(postId)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            post = result.data
                        )
                    }
                }
                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            post = null,
                            errorMessage = GET_DETAIL_ERROR
                        )
                    }
                }
            }
        }
    }

    fun insertFavorite(post: Post) {
        viewModelScope.launch {
            insertFavoritePostUseCase(post.copy(isFavorite = true))
            _uiState.update {
                it.copy(
                    post = post.copy(isFavorite = true),
                    informativeMessage = ADD_FAVORITE_SUCCESSFULLY
                )
            }
        }
    }

    fun removeFavorite(post: Post) {
        viewModelScope.launch {
            removeFavoritePostUseCase(post.copy(isFavorite = false))
            _uiState.update {
                it.copy(
                    post = post.copy(isFavorite = false),
                    informativeMessage = REMOVE_FAVORITE_SUCCESSFULLY
                )
            }
        }
    }
}