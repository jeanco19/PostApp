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
class InsertFavoritePostUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var sut: InsertFavoritePostUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        sut = InsertFavoritePostUseCaseImpl(postRepository)
    }

    @Test
    fun check_insertFavoritePost_toRepository() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        sut.invoke(expectedModel)
        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel)))
        )

        val post = postRepository.getFavoritePosts().first()
        assertThat(post.data).isNotNull()
        assertThat(post.data).isNotEmpty()
        assertThat(post).isInstanceOf(Result.Success::class.java)
        assertThat(post.data?.first()).isInstanceOf(Post::class.java)
        assertThat(post.data).contains(expectedModel)
        assertThat(post.data?.first()?.isSeen).isTrue()
        assertThat(post.data?.first()?.isFavorite).isTrue()
    }

    @Test
    fun check_insertFavoritePost_whenFailTheInsert() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        sut.invoke(expectedModel)
        `when`(postRepository.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf()))
        )

        val post = postRepository.getFavoritePosts().first()
        assertThat(post.data).isNotNull()
        assertThat(post.data).isEmpty()
        assertThat(post).isInstanceOf(Result.Success::class.java)
        assertThat(post.data).doesNotContain(expectedModel)
    }
}