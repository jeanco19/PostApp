package com.jean.postapp.presentation.post.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.jean.postapp.GENERIC_ERROR
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.REMOVE_FAVORITE_SUCCESSFULLY
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.usecase.post.GetFavoritePostUseCase
import com.jean.postapp.domain.usecase.post.RemoveFavoritePostUseCase
import com.jean.postapp.presentation.post.favorite.state.FavoritePostUiState
import com.jean.postapp.providePost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class FavoritePostViewModelTest {

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getFavoritePostUseCase: GetFavoritePostUseCase
    private lateinit var removeFavoritePostUseCase: RemoveFavoritePostUseCase
    private lateinit var sut: FavoritePostViewModel

    @Before
    fun setup() {
        getFavoritePostUseCase = Mockito.mock(GetFavoritePostUseCase::class.java)
        removeFavoritePostUseCase = Mockito.mock(RemoveFavoritePostUseCase::class.java)
        sut = FavoritePostViewModel(
            getFavoritePostUseCase,
            removeFavoritePostUseCase
        )
    }

    @Test
    fun check_getFavoritePost_whenIsLoading() = runTest {
        sut.getFavoritePosts()

        sut.uiState.test {
            assertEquals(FavoritePostUiState(isLoading = true), awaitItem())
            cancel()
        }
    }

    @Test
    fun check_getFavoritePost_whenReturnDataFromUseCase() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)
        `when`(getFavoritePostUseCase.invoke()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel)))
        )

        sut.getFavoritePosts()

        verify(getFavoritePostUseCase).invoke()
        sut.uiState.test {
            assertEquals(
                FavoritePostUiState(isLoading = false, items = listOf(expectedModel)),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun check_getFavoritePost_whenReturnFailureFromUseCase() = runTest {
        `when`(getFavoritePostUseCase.invoke()).thenReturn(
            flowOf(Result.Failure())
        )

        sut.getFavoritePosts()

        verify(getFavoritePostUseCase).invoke()
        sut.uiState.test {
            assertEquals(
                FavoritePostUiState(isLoading = false, errorMessage = GENERIC_ERROR),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun check_removeFavoritePost_fromUseCase() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)
        `when`(getFavoritePostUseCase.invoke()).thenReturn(
            flowOf(Result.Success(listOf()))
        )

        sut.getFavoritePosts()
        sut.removeFavorite(expectedModel)

        verify(removeFavoritePostUseCase).invoke(expectedModel)
        sut.uiState.test {
            assertEquals(
                FavoritePostUiState(informativeMessage = REMOVE_FAVORITE_SUCCESSFULLY), awaitItem()
            )
            cancel()
        }
    }
}