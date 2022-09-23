package com.jean.postapp.presentation.post.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.domain.usecase.post.GetFavoritePostUseCase
import com.jean.postapp.domain.usecase.post.RemoveFavoritePostUseCase
import com.jean.postapp.presentation.post.favorite.state.FavoritePostUiState
import com.jean.postapp.util.Constant.EMPTY_STRING
import com.jean.postapp.util.Constant.GENERIC_ERROR
import com.jean.postapp.util.Constant.REMOVE_FAVORITE_SUCCESSFULLY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritePostViewModel @Inject constructor(
    private val getFavoritePostUseCase: GetFavoritePostUseCase,
    private val removeFavoritePostUseCase: RemoveFavoritePostUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(FavoritePostUiState())
    val uiState: StateFlow<FavoritePostUiState> = _uiState.asStateFlow()

    fun getFavoritePosts() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            getFavoritePostUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                items = if (result.data.isNullOrEmpty()) listOf()
                                        else result.data
                            )
                        }
                    }
                    is Result.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = if (result.data.isNullOrEmpty()) GENERIC_ERROR
                                               else EMPTY_STRING
                            )
                        }
                    }
                }
            }
        }
    }

    fun removeFavorite(post: Post) {
        viewModelScope.launch {
            removeFavoritePostUseCase(post.copy(isFavorite = false))
            _uiState.update {
                it.copy(
                    informativeMessage = REMOVE_FAVORITE_SUCCESSFULLY
                )
            }
        }
    }
}