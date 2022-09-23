package com.jean.postapp.domain.usecase.post

import com.google.common.truth.Truth.assertThat
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Post
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
class GetFavoritePostUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var sut: GetFavoritePostUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        sut = GetFavoritePostUseCaseImpl(postRepository)
    }

    @Test
    fun check_getFavoritesPost_whenReturnDataFromRepository() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel)))
        )

        val posts = sut.invoke().first()
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data?.first()).isInstanceOf(Post::class.java)
        assertThat(posts.data?.first()?.id).isEqualTo(expectedModel.id)
        assertThat(posts.data?.first()?.title).isEqualTo(expectedModel.title)
        assertThat(posts.data?.first()?.body).isEqualTo(expectedModel.body)
        assertThat(posts.data?.first()?.isSeen).isTrue()
        assertThat(posts.data?.first()?.isFavorite).isTrue()
    }

    @Test
    fun check_getFavoritesPost_whenReturnEmptyDataFromRepository() = runTest {
        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf()))
        )

        val posts = sut.invoke().first()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }

    @Test
    fun check_getFavoritesPost_whenReturnFailureFromRepository() = runTest {
        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Failure())
        )

        val posts = sut.invoke().first()
        assertThat(posts).isInstanceOf(Result.Failure::class.java)
        assertThat(posts.data).isNull()
    }
}