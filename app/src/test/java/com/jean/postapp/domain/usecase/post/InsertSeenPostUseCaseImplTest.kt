package com.jean.postapp.domain.usecase.post

import com.google.common.truth.Truth.assertThat
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post
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
class InsertSeenPostUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var sut: InsertSeenPostUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        sut = InsertSeenPostUseCaseImpl(postRepository)
    }

    @Test
    fun check_insertSeenPost_toRepository() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)

        sut.invoke(expectedModel)
        `when`(postRepository.getAllPost(isRefreshing = false)).thenReturn(
            Result.Success(listOf(expectedModel))
        )

        val post = postRepository.getAllPost(isRefreshing = false)
        assertThat(post.data).isNotNull()
        assertThat(post.data).isNotEmpty()
        assertThat(post).isInstanceOf(Result.Success::class.java)
        assertThat(post.data?.first()).isInstanceOf(Post::class.java)
        assertThat(post.data).contains(expectedModel)
        assertThat(post.data?.first()?.isSeen).isTrue()
    }

    @Test
    fun check_insertSeenPost_whenFailTheInsert() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)

        sut.invoke(expectedModel)
        `when`(postRepository.getAllPost(isRefreshing = false)).thenReturn(
            Result.Success(listOf())
        )

        val post = postRepository.getAllPost(isRefreshing = false)
        assertThat(post.data).isNotNull()
        assertThat(post.data).isEmpty()
        assertThat(post).isInstanceOf(Result.Success::class.java)
        assertThat(post.data).doesNotContain(expectedModel)
    }
}