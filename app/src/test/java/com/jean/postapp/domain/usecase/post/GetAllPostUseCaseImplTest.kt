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
class GetAllPostUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var sut: GetAllPostUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        sut = GetAllPostUseCaseImpl(postRepository)
    }

    @Test
    fun check_getAllPosts_whenReturnDataFromRepository() = runTest {
        val expectedSeenModel = providePost(isSeen = true, isFavorite = false)
        val expectedFavoriteModel = providePost(isSeen = true, isFavorite = true)

        `when`(postRepository.getAllPost(isRefreshing = false)).thenReturn(
            Result.Success(listOf(
                expectedSeenModel,
                expectedFavoriteModel
            ))
        )

        val posts = sut.invoke(isRefreshing = false)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data?.first()).isInstanceOf(Post::class.java)
        assertThat(posts.data?.first()?.id).isEqualTo(expectedFavoriteModel.id)
        assertThat(posts.data?.first()?.title).isEqualTo(expectedFavoriteModel.title)
        assertThat(posts.data?.first()?.body).isEqualTo(expectedFavoriteModel.body)
        assertThat(posts.data?.first()?.body).isEqualTo(expectedFavoriteModel.body)
        assertThat(posts.data?.first()?.isFavorite).isTrue()
        assertThat(posts.data?.last()?.isFavorite).isFalse()
    }

    @Test
    fun check_getAllPosts_whenReturnEmptyDataFromRepository() = runTest {
        `when`(postRepository.getAllPost(isRefreshing = false)).thenReturn(
            Result.Success(listOf())
        )

        val posts = sut.invoke(isRefreshing = false)
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }

    @Test
    fun check_getAllPosts_whenReturnFailureFromRepository() = runTest {
        `when`(postRepository.getAllPost(isRefreshing = false)).thenReturn(
            Result.Failure()
        )

        val posts = sut.invoke(isRefreshing = false)
        assertThat(posts).isInstanceOf(Result.Failure::class.java)
        assertThat(posts.data).isNull()
    }
}