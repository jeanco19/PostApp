package com.jean.postapp.presentation.post.all.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.usecase.post.*
import com.jean.postapp.presentation.post.all.state.AllPostUiState
import com.jean.postapp.util.Constant.EMPTY_STRING
import com.jean.postapp.util.Constant.GENERIC_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPostViewModel @Inject constructor(
    private val insertSeenPostUseCase: InsertSeenPostUseCase,
    private val getAllPostUseCase: GetAllPostUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val removeAllPostUseCase: RemoveAllPostUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(AllPostUiState())
    val uiState: StateFlow<AllPostUiState> = _uiState.asStateFlow()

    fun getAllPost(isRefreshing: Boolean) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            when (val result = getAllPostUseCase(isRefreshing)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            items = if (result.data.isNullOrEmpty()) listOf() else result.data,
                            postSize = if (result.data.isNullOrEmpty()) 0 else result.data.size
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

    fun insertSeen(post: Post) {
        viewModelScope.launch {
            insertSeenPostUseCase(post.copy(isSeen = true))
        }
    }

    fun removePost(post: Post) {
        viewModelScope.launch {
            removePostUseCase(post)
            when (val result = getAllPostUseCase(isRefreshing = false)) {
                is Result.Success -> {
                   _uiState.update {
                       it.copy(
                           postSize = if (result.data.isNullOrEmpty()) 0 else result.data.size
                       )
                   }
                }
                is Result.Failure -> { }
            }
        }
    }

    fun removeAllPost() {
        viewModelScope.launch {
            removeAllPostUseCase()
            _uiState.update {
                it.copy(
                    items = listOf(),
                    postSize = 0
                )
            }
        }
    }
}