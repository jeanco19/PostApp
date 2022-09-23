package com.jean.postapp.presentation.post.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.jean.postapp.*
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.usecase.post.GetPostDetailUseCase
import com.jean.postapp.domain.usecase.post.InsertFavoritePostUseCase
import com.jean.postapp.domain.usecase.post.RemoveFavoritePostUseCase
import com.jean.postapp.presentation.post.detail.state.PostDetailUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostDetailViewModelTest {

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var insertFavoritePostUseCase: InsertFavoritePostUseCase
    private lateinit var getPostDetailUseCase: GetPostDetailUseCase
    private lateinit var removeFavoritePostUseCase: RemoveFavoritePostUseCase
    private lateinit var sut: PostDetailViewModel

    @Before
    fun setup() {
        insertFavoritePostUseCase = Mockito.mock(InsertFavoritePostUseCase::class.java)
        getPostDetailUseCase = Mockito.mock(GetPostDetailUseCase::class.java)
        removeFavoritePostUseCase = Mockito.mock(RemoveFavoritePostUseCase::class.java)
        sut = PostDetailViewModel(
            insertFavoritePostUseCase,
            getPostDetailUseCase,
            removeFavoritePostUseCase
        )
    }

    @Test
    fun check_getPostDetail_whenIsLoading() = runTest {
        sut.getPostDetail(postId = 1)

        sut.uiState.test {
            assertEquals(PostDetailUiState(isLoading = true), awaitItem())
            cancel()
        }
    }

    @Test
    fun check_getPostDetail_whenReturnDataFromUseCase() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)
        `when`(getPostDetailUseCase.invoke(postId = 1)).thenReturn(
            Result.Success(expectedModel)
        )

        sut.getPostDetail(postId = 1)

        verify(getPostDetailUseCase).invoke(postId = 1)
        sut.uiState.test {
            assertEquals(PostDetailUiState(isLoading = false, post = expectedModel), awaitItem())
            cancel()
        }
    }

    @Test
    fun check_getPostDetail_whenFailFromUseCase() = runTest {
        `when`(getPostDetailUseCase.invoke(postId = 1)).thenReturn(
            Result.Failure()
        )

        sut.getPostDetail(postId = 1)

        verify(getPostDetailUseCase).invoke(postId = 1)
        sut.uiState.test {
            assertEquals(
                PostDetailUiState(isLoading = false, errorMessage = GET_DETAIL_ERROR),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun check_insertFavoritePost_toUseCase() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)
        `when`(getPostDetailUseCase.invoke(postId = 1)).thenReturn(
            Result.Success(expectedModel)
        )

        sut.insertFavorite(expectedModel)
        sut.getPostDetail(postId = 1)

        verify(insertFavoritePostUseCase).invoke(expectedModel)
        sut.uiState.test {
            assertEquals(
                PostDetailUiState(
                    post = expectedModel,
                    informativeMessage = ADD_FAVORITE_SUCCESSFULLY
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun check_removeFavoritePost_fromUseCase() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)
        `when`(getPostDetailUseCase.invoke(postId = 1)).thenReturn(
            Result.Success(expectedModel)
        )

        sut.removeFavorite(expectedModel)
        sut.getPostDetail(postId = 1)

        verify(removeFavoritePostUseCase).invoke(expectedModel)
        sut.uiState.test {
            assertEquals(
                PostDetailUiState(
                    post = expectedModel,
                    informativeMessage = REMOVE_FAVORITE_SUCCESSFULLY
                ),
                awaitItem()
            )
            cancel()
        }
    }
}