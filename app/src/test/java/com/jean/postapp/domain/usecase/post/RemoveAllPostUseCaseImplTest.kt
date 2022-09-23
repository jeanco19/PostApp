package com.jean.postapp.domain.usecase.post

import com.google.common.truth.Truth.assertThat
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.util.Result
import com.jean.postapp.providePost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoveAllPostUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var sut: RemoveAllPostUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        sut = RemoveAllPostUseCaseImpl(postRepository)
    }

    @Test
    fun check_removeAllPosts_fromRepository() = runTest {
        sut.invoke()
        `when`(postRepository.getAllPost(isRefreshing = false)).thenReturn(
            Result.Success(listOf())
        )

        val posts = postRepository.getAllPost(isRefreshing = false)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun check_removeAllPosts_whenFailTheRemove() = runTest {
        sut.invoke()
        `when`(postRepository.getAllPost(isRefreshing = false)).thenReturn(
            Result.Success(listOf(providePost(isSeen = false, isFavorite = false)))
        )

        val posts = postRepository.getAllPost(isRefreshing = false)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
    }
}