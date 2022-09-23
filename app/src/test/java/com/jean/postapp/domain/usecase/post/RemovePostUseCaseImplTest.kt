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
class RemovePostUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var sut: RemovePostUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        sut = RemovePostUseCaseImpl(postRepository)
    }

    @Test
    fun check_removePost_fromRepository() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        `when`(postRepository.getPost(postId = 1)).thenReturn(
            Result.Success(expectedModel)
        )

        sut.invoke(expectedModel)
        `when`(postRepository.getPost(postId = 1)).thenReturn(
            Result.Failure()
        )

        val posts = postRepository.getPost(postId = 1)
        assertThat(posts.data).isNull()
        assertThat(posts).isInstanceOf(Result.Failure::class.java)
    }

    @Test
    fun check_removePost_whenFailTheRemove() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        `when`(postRepository.getPost(postId = 1)).thenReturn(
            Result.Success(expectedModel)
        )
        sut.invoke(expectedModel)

        val posts = postRepository.getPost(postId = 1)
        assertThat(posts.data).isNotNull()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
    }
}