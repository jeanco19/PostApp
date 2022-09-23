package com.jean.postapp.domain.usecase.post

import com.google.common.truth.Truth.assertThat
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.repository.user.UserRepository
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Comment
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.domain.model.user.Author
import com.jean.postapp.provideAuthor
import com.jean.postapp.provideComment
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
class GetPostDetailUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRepository: PostRepository
    private lateinit var userRepository: UserRepository
    private lateinit var sut: GetPostDetailUseCase

    @Before
    fun setup() {
        postRepository = Mockito.mock(PostRepository::class.java)
        userRepository = Mockito.mock(UserRepository::class.java)
        sut = GetPostDetailUseCaseImpl(
            postRepository,
            userRepository
        )
    }

    @Test
    fun check_getPostDetail_whenReturnDataFromRepositories() = runTest {
        val expectedPostModel = providePost(isSeen = true, isFavorite = false)
        val expectedCommentModel = provideComment()
        val expectedAuthorModel = provideAuthor()

        `when`(postRepository.getPost(postId = 1)).thenReturn(
            Result.Success(expectedPostModel)
        )

        `when`(postRepository.getCommentPost(postId = 1)).thenReturn(
            Result.Success(listOf(expectedCommentModel))
        )

        `when`(userRepository.getAuthorPost(postId = 1)).thenReturn(
            Result.Success(expectedAuthorModel)
        )

        val posts = sut.invoke(postId = 1)
        assertThat(posts.data).isNotNull()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isInstanceOf(Post::class.java)
        assertThat(posts.data?.id).isEqualTo(expectedPostModel.id)
        assertThat(posts.data?.title).isEqualTo(expectedPostModel.title)
        assertThat(posts.data?.body).isEqualTo(expectedPostModel.body)
        assertThat(posts.data?.author).isNotNull()
        assertThat(posts.data?.author).isInstanceOf(Author::class.java)
        assertThat(posts.data?.author?.id).isEqualTo(expectedAuthorModel.id)
        assertThat(posts.data?.author?.name).isEqualTo(expectedAuthorModel.name)
        assertThat(posts.data?.author?.email).isEqualTo(expectedAuthorModel.email)
        assertThat(posts.data?.author?.phone).isEqualTo(expectedAuthorModel.phone)
        assertThat(posts.data?.author?.website).isEqualTo(expectedAuthorModel.website)
        assertThat(posts.data?.comments).isNotNull()
        assertThat(posts.data?.comments).isNotEmpty()
        assertThat(posts.data?.comments?.first()).isInstanceOf(Comment::class.java)
        assertThat(posts.data?.comments?.first()?.id).isEqualTo(expectedCommentModel.id)
        assertThat(posts.data?.comments?.first()?.name).isEqualTo(expectedCommentModel.name)
        assertThat(posts.data?.comments?.first()?.body).isEqualTo(expectedCommentModel.body)
        assertThat(posts.data?.isSeen).isEqualTo(expectedPostModel.isSeen)
        assertThat(posts.data?.isFavorite).isEqualTo(expectedPostModel.isFavorite)
    }

    @Test
    fun check_getPostDetail_whenReturnEmptyCommentFromRepository() = runTest {
       val expectedPostModel = providePost(isSeen = true, isFavorite = false)
        val expectedAuthorModel = provideAuthor()

        `when`(postRepository.getPost(postId = 1)).thenReturn(
            Result.Success(expectedPostModel)
        )

        `when`(postRepository.getCommentPost(postId = 1)).thenReturn(
            Result.Success(listOf())
        )

        `when`(userRepository.getAuthorPost(postId = 1)).thenReturn(
            Result.Success(provideAuthor())
        )

        val posts = sut.invoke(postId = 1)
        assertThat(posts).isNotNull()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isInstanceOf(Post::class.java)
        assertThat(posts.data?.id).isEqualTo(expectedPostModel.id)
        assertThat(posts.data?.title).isEqualTo(expectedPostModel.title)
        assertThat(posts.data?.body).isEqualTo(expectedPostModel.body)
        assertThat(posts.data?.author).isNotNull()
        assertThat(posts.data?.author).isInstanceOf(Author::class.java)
        assertThat(posts.data?.author?.id).isEqualTo(expectedAuthorModel.id)
        assertThat(posts.data?.author?.name).isEqualTo(expectedAuthorModel.name)
        assertThat(posts.data?.author?.email).isEqualTo(expectedAuthorModel.email)
        assertThat(posts.data?.author?.phone).isEqualTo(expectedAuthorModel.phone)
        assertThat(posts.data?.author?.website).isEqualTo(expectedAuthorModel.website)
        assertThat(posts.data?.comments).isNotNull()
        assertThat(posts.data?.comments).isEmpty()
        assertThat(posts.data?.isSeen).isEqualTo(expectedPostModel.isSeen)
        assertThat(posts.data?.isFavorite).isEqualTo(expectedPostModel.isFavorite)
    }

    @Test
    fun check_getPostDetail_whenReturnEmptyAuthorFromRepository() = runTest {
        val expectedPostModel = providePost(isSeen = true, isFavorite = false)
        val expectedCommentModel = provideComment()

        `when`(postRepository.getPost(postId = 1)).thenReturn(
            Result.Success(expectedPostModel)
        )

        `when`(postRepository.getCommentPost(postId = 1)).thenReturn(
            Result.Success(listOf(provideComment()))
        )

        `when`(userRepository.getAuthorPost(postId = 1)).thenReturn(
            Result.Failure()
        )

        val posts = sut.invoke(postId = 1)
        assertThat(posts).isNotNull()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isInstanceOf(Post::class.java)
        assertThat(posts.data?.id).isEqualTo(expectedPostModel.id)
        assertThat(posts.data?.title).isEqualTo(expectedPostModel.title)
        assertThat(posts.data?.body).isEqualTo(expectedPostModel.body)
        assertThat(posts.data?.author).isNull()
        assertThat(posts.data?.comments).isNotNull()
        assertThat(posts.data?.comments).isNotEmpty()
        assertThat(posts.data?.comments?.first()).isInstanceOf(Comment::class.java)
        assertThat(posts.data?.comments?.first()?.id).isEqualTo(expectedCommentModel.id)
        assertThat(posts.data?.comments?.first()?.name).isEqualTo(expectedCommentModel.name)
        assertThat(posts.data?.comments?.first()?.body).isEqualTo(expectedCommentModel.body)
        assertThat(posts.data?.isSeen).isEqualTo(expectedPostModel.isSeen)
        assertThat(posts.data?.isFavorite).isEqualTo(expectedPostModel.isFavorite)
    }

    @Test
    fun check_getPostDetail_whenReturnFailureFromRepositories() = runTest {
        `when`(postRepository.getPost(postId = 1)).thenReturn(
            Result.Failure()
        )

        val posts = sut.invoke(postId = 1)
        assertThat(posts).isInstanceOf(Result.Failure::class.java)
        assertThat(posts.data).isNull()
    }
}