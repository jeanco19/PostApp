package com.jean.postapp.domain.usecase.post

import com.google.common.truth.Truth.assertThat
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.util.Result
import com.jean.postapp.providePost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class RemoveFavoritePostUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var sut: RemoveFavoritePostUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        sut = RemoveFavoritePostUseCaseImpl(postRepository)
    }

    @Test
    fun check_removeFavoritePost_fromRepository() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel)))
        )

        sut.invoke(expectedModel)
        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf()))
        )

        val posts = postRepository.getFavoritePosts().first()
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun check_removeFavoritePost_whenFailTheRemove() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel)))
        )
        sut.invoke(expectedModel)

        val posts = postRepository.getFavoritePosts().first()
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
    }
}