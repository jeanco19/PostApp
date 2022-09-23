package com.jean.postapp.presentation.post.all.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.jean.postapp.GENERIC_ERROR
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.usecase.post.GetAllPostUseCase
import com.jean.postapp.domain.usecase.post.InsertSeenPostUseCase
import com.jean.postapp.domain.usecase.post.RemoveAllPostUseCase
import com.jean.postapp.domain.usecase.post.RemovePostUseCase
import com.jean.postapp.presentation.post.all.state.AllPostUiState
import com.jean.postapp.providePost
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
class AllPostViewModelTest {

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var insertSeenPostUseCase: InsertSeenPostUseCase
    private lateinit var getAllPostUseCase: GetAllPostUseCase
    private lateinit var removePostUseCase: RemovePostUseCase
    private lateinit var removeAllPostUseCase: RemoveAllPostUseCase
    private lateinit var sut: AllPostViewModel

    @Before
    fun setup() {
        insertSeenPostUseCase = Mockito.mock(InsertSeenPostUseCase::class.java)
        getAllPostUseCase = Mockito.mock(GetAllPostUseCase::class.java)
        removePostUseCase = Mockito.mock(RemovePostUseCase::class.java)
        removeAllPostUseCase = Mockito.mock(RemoveAllPostUseCase::class.java)
        sut = AllPostViewModel(
            insertSeenPostUseCase,
            getAllPostUseCase,
            removePostUseCase,
            removeAllPostUseCase
        )
    }

    @Test
    fun check_getAllPosts_whenIsLoading() = runTest {
        sut.getAllPost(isRefreshing = false)

        sut.uiState.test {
            assertEquals(AllPostUiState(isLoading = true), awaitItem())
            cancel()
        }
    }

    @Test
    fun check_getAllPosts_whenReturnDataFromUseCase() = runTest {
        val expectedModel = providePost(isSeen = false, isFavorite = false)
        `when`(getAllPostUseCase.invoke(isRefreshing = false)).thenReturn(
            Result.Success(listOf(expectedModel))
        )

        sut.getAllPost(isRefreshing = false)

        verify(getAllPostUseCase).invoke(isRefreshing = false)
        sut.uiState.test {
            assertEquals(
                AllPostUiState(
                    isLoading = false,
                    items = listOf(expectedModel),
                    postSize = 1
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun check_getAllPosts_whenReturnFailureFromUseCase() = runTest {
        `when`(getAllPostUseCase.invoke(isRefreshing = false)).thenReturn(
            Result.Failure()
        )

        sut.getAllPost(isRefreshing = false)

        verify(getAllPostUseCase).invoke(isRefreshing = false)
        sut.uiState.test {
            assertEquals(
                AllPostUiState(
                    isLoading = false,
                    errorMessage = GENERIC_ERROR
                ),
                awaitItem()
            )
            cancel()
        }
    }

    @Test
    fun check_insertSeen_toUseCase() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)
        `when`(getAllPostUseCase.invoke(isRefreshing = false)).thenReturn(
            Result.Success(listOf(expectedModel))
        )

        sut.insertSeen(expectedModel)
        sut.getAllPost(isRefreshing = false)

        verify(insertSeenPostUseCase).invoke(expectedModel)
        sut.uiState.test {
            assertEquals(AllPostUiState(items = listOf(expectedModel), postSize = 1), awaitItem())
            cancel()
        }
    }

    @Test
    fun check_removePost_fromUseCase() = runTest {
        `when`(getAllPostUseCase.invoke(isRefreshing = false)).thenReturn(
            Result.Success(listOf())
        )

        sut.removePost(providePost(isSeen = false, isFavorite = false))
        sut.getAllPost(isRefreshing = false)

        verify(removePostUseCase).invoke(providePost(isSeen = false, isFavorite = false))
        sut.uiState.test {
            assertEquals(AllPostUiState(), awaitItem())
            cancel()
        }
    }

    @Test
    fun check_removePost_whenFailTheRemove() = runTest {
        val expectedModel = providePost(isSeen = false, isFavorite = false)
        `when`(getAllPostUseCase.invoke(isRefreshing = false)).thenReturn(
            Result.Success(listOf(expectedModel))
        )

        sut.removePost(expectedModel)
        sut.getAllPost(isRefreshing = false)

        verify(removePostUseCase).invoke(expectedModel)
        sut.uiState.test {
            assertEquals(AllPostUiState(items = listOf(expectedModel), postSize = 1), awaitItem())
            cancel()
        }
    }

    @Test
    fun check_removeAllPosts_fromUseCase() = runTest {
        `when`(getAllPostUseCase.invoke(isRefreshing = false)).thenReturn(
            Result.Success(listOf())
        )

        sut.getAllPost(isRefreshing = false)
        sut.removeAllPost()

        verify(removeAllPostUseCase).invoke()
        sut.uiState.test {
            assertEquals(AllPostUiState(), awaitItem())
            cancel()
        }
    }
}