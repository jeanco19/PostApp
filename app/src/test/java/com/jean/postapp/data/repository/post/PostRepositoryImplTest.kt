package com.jean.postapp.data.repository.post

import com.google.common.truth.Truth.assertThat
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.datasource.post.PostLocalDataSource
import com.jean.postapp.data.datasource.post.PostRemoteDataSource
import com.jean.postapp.data.mapper.toCommentApiModel
import com.jean.postapp.data.mapper.toPostApiModel
import com.jean.postapp.data.mapper.toPostEntity
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.post.Comment
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.provideComment
import com.jean.postapp.providePost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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
class PostRepositoryImplTest {

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var postRemoteDataSource: PostRemoteDataSource
    private lateinit var postLocalDataSource: PostLocalDataSource
    private lateinit var sut: PostRepositoryImpl

    @Before
    fun setup() {
        postRemoteDataSource = Mockito.mock(PostRemoteDataSource::class.java)
        postLocalDataSource = Mockito.mock(PostLocalDataSource::class.java)
        sut = PostRepositoryImpl(
            postRemoteDataSource,
            postLocalDataSource,
            mainCoroutineRule.testDispatcher
        )
    }

    @Test
    fun check_insertFavoritePost_toLocalDataSource() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        // When user getting post list before insert a favorite post
        `when`(postLocalDataSource.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf()))
        )

        val emptyPost = sut.getFavoritePosts().toList().first()
        assertThat(emptyPost.data).isNotNull()
        assertThat(emptyPost.data).isEmpty()

        // When user getting post list after insert a favorite post
        sut.insertFavoritePost(expectedModel)
        `when`(postLocalDataSource.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel.toPostEntity())))
        )

        val posts = sut.getFavoritePosts().toList().first()
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts.data?.first()).isInstanceOf(Post::class.java)
        assertThat(posts.data?.first()?.isSeen).isTrue()
        assertThat(posts.data?.first()?.isFavorite).isTrue()
    }

    @Test
    fun check_insertSeenPost_toLocalDataSource() = runTest {
        val expectedModel = providePost(isSeen = false, isFavorite = false)

        // When user getting post list before insert a seen post
        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Success(listOf(expectedModel.toPostEntity()))
        )

        val post = sut.getAllPost(isRefreshing = false)
        assertThat(post).isInstanceOf(Result.Success::class.java)
        assertThat(post.data).isNotNull()
        assertThat(post.data).isNotEmpty()
        assertThat(post.data?.first()).isInstanceOf(Post::class.java)
        assertThat(post.data?.first()?.isSeen).isFalse()

        // When user getting post list after insert a seen post
        sut.insertSeenPost(providePost(isSeen = true, isFavorite = false))
        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Success(listOf(providePost(isSeen = true, isFavorite = false).toPostEntity()))
        )

        val seenPosts = sut.getAllPost(isRefreshing = false)
        assertThat(seenPosts).isInstanceOf(Result.Success::class.java)
        assertThat(seenPosts.data).isNotNull()
        assertThat(seenPosts.data).isNotEmpty()
        assertThat(seenPosts.data?.first()).isInstanceOf(Post::class.java)
        assertThat(seenPosts.data?.first()?.isSeen).isTrue()
    }

    @Test
    fun check_getPost_whenReturnDataFromDatasource() = runTest {
        val expectedModel = providePost(isSeen = false, isFavorite = false)

        `when`(postLocalDataSource.getPost(postId = 1)).thenReturn(
            Result.Success(expectedModel.toPostEntity())
        )

        val post = sut.getPost(postId = 1)
        assertThat(post).isInstanceOf(Result.Success::class.java)
        assertThat(post.data).isNotNull()
        assertThat(post.data).isInstanceOf(Post::class.java)
        assertThat(post.data?.id).isEqualTo(expectedModel.id)
        assertThat(post.data?.title).isEqualTo(expectedModel.title)
        assertThat(post.data?.body).isEqualTo(expectedModel.body)
        assertThat(post.data?.isSeen).isEqualTo(expectedModel.isSeen)
        assertThat(post.data?.isFavorite).isEqualTo(expectedModel.isFavorite)
    }

    @Test
    fun check_getPost_whenReturnFailureFromDatasource() = runTest {
        `when`(postLocalDataSource.getPost(postId = 1)).thenReturn(
            Result.Failure()
        )

        val post = sut.getPost(postId = 1)
        assertThat(post).isInstanceOf(Result.Failure::class.java)
        assertThat(post.data).isNull()
    }

    @Test
    fun check_getPostComments_whenReturnDataFromDatasource() = runTest {
        val expectedModel = provideComment()

        `when`(postRemoteDataSource.getComments(postId = 1)).thenReturn(
            Result.Success(listOf(expectedModel.toCommentApiModel()))
        )

        val comment = sut.getCommentPost(postId = 1)
        assertThat(comment).isInstanceOf(Result.Success::class.java)
        assertThat(comment.data).isNotNull()
        assertThat(comment.data).isNotEmpty()
        assertThat(comment.data?.first()).isInstanceOf(Comment::class.java)
        assertThat(comment.data?.first()?.id).isEqualTo(expectedModel.id)
        assertThat(comment.data?.first()?.name).isEqualTo(expectedModel.name)
        assertThat(comment.data?.first()?.body).isEqualTo(expectedModel.body)
    }

    @Test
    fun check_getPostComments_whenReturnEmptyDataFromDatasource() = runTest {
        `when`(postRemoteDataSource.getComments(postId = 1)).thenReturn(
            Result.Success(listOf())
        )

        val comment = sut.getCommentPost(postId = 1)
        assertThat(comment).isInstanceOf(Result.Success::class.java)
        assertThat(comment.data).isNotNull()
        assertThat(comment.data).isEmpty()
    }

    @Test
    fun check_getPostComments_whenReturnFailureFromDatasource() = runTest {
        `when`(postRemoteDataSource.getComments(postId = 1)).thenReturn(
            Result.Failure()
        )

        val comment = sut.getCommentPost(postId = 1)
        assertThat(comment).isInstanceOf(Result.Failure::class.java)
        assertThat(comment.data).isNull()
    }

    @Test
    fun check_getAllPost_whenReturnDataFromDatasource() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)

        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Success(listOf(expectedModel.toPostEntity()))
        )

        val posts = sut.getAllPost(isRefreshing = false)
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data?.first()).isInstanceOf(Post::class.java)
        assertThat(posts.data?.first()?.id).isEqualTo(expectedModel.id)
        assertThat(posts.data?.first()?.title).isEqualTo(expectedModel.title)
        assertThat(posts.data?.first()?.body).isEqualTo(expectedModel.body)
        assertThat(posts.data?.first()?.isSeen).isEqualTo(expectedModel.isSeen)
        assertThat(posts.data?.first()?.isFavorite).isEqualTo(expectedModel.isFavorite)
    }

    @Test
    fun check_getAllPost_whenReturnRefreshDataFromDatasource() = runTest {
        val expectedModel = providePost(isSeen = false, isFavorite = false)

        // When user refresh post list is called remote service
        `when`(postRemoteDataSource.getAllPosts()).thenReturn(
            Result.Success(listOf(expectedModel.toPostApiModel()))
        )

        // After call remote service the post are saving in local data
        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Success(listOf(expectedModel.toPostEntity()))
        )

        val posts = sut.getAllPost(isRefreshing = true)
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data?.first()).isInstanceOf(Post::class.java)
        assertThat(posts.data?.first()?.id).isEqualTo(expectedModel.id)
        assertThat(posts.data?.first()?.title).isEqualTo(expectedModel.title)
        assertThat(posts.data?.first()?.body).isEqualTo(expectedModel.body)
        assertThat(posts.data?.first()?.isSeen).isEqualTo(expectedModel.isSeen)
        assertThat(posts.data?.first()?.isFavorite).isEqualTo(expectedModel.isFavorite)
    }

    @Test
    fun check_getAllPost_whenReturnEmptyDataFromDatasource() = runTest {
        // When user getting post in first time in app
        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Success(listOf())
        )

        // When local data is empty call remote service
        `when`(postRemoteDataSource.getAllPosts()).thenReturn(
            Result.Success(listOf(providePost(isSeen = false, isFavorite = false).toPostApiModel()))
        )

        val posts = sut.getAllPost(isRefreshing = false)
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
    }

    @Test
    fun check_getAllPost_whenReturnFailureFromDatasource() = runTest {
        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Failure()
        )

        val posts = sut.getAllPost(isRefreshing = false)
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }

    @Test
    fun check_getFavoritesPost_whenReturnDataFromDatasource() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        `when`(postLocalDataSource.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel.toPostEntity())))
        )

        val posts = sut.getFavoritePosts().first()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts.data?.first()).isInstanceOf(Post::class.java)
        assertThat(posts.data?.first()?.id).isEqualTo(expectedModel.id)
        assertThat(posts.data?.first()?.title).isEqualTo(expectedModel.title)
        assertThat(posts.data?.first()?.body).isEqualTo(expectedModel.body)
        assertThat(posts.data?.first()?.isSeen).isTrue()
        assertThat(posts.data?.first()?.isFavorite).isTrue()
    }

    @Test
    fun check_getFavoritesPost_whenReturnEmptyDataFromDatasource() = runTest {
        `when`(postLocalDataSource.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf()))
        )

        val posts = sut.getFavoritePosts().first()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }

    @Test
    fun check_getFavoritesPost_whenReturnFailureFromDatasource() = runTest {
        `when`(postLocalDataSource.getFavoritePosts()).thenReturn(
            flowOf(Result.Failure())
        )

        val posts = sut.getFavoritePosts().first()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }

    @Test
    fun check_removePost_fromLocalDatasource() = runTest {
        val expectedModel = providePost(isSeen = false, isFavorite = false)

        // When user insert a new post in database
        sut.insertSeenPost(expectedModel)
        `when`(postLocalDataSource.getPost(postId = 1)).thenReturn(
            Result.Success(expectedModel.toPostEntity())
        )

        // When user remove the post from database and try to return the same post
        sut.removePost(expectedModel)
        `when`(postLocalDataSource.getPost(postId = 1)).thenReturn(
            Result.Failure()
        )

        val post = sut.getPost(postId = 1)
        assertThat(post).isInstanceOf(Result.Failure::class.java)
        assertThat(post.data).isNull()
    }

    @Test
    fun check_removeFavoritePost_fromLocalDatasource() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = true)

        // When user insert a favorite post in database
        sut.insertFavoritePost(expectedModel)
        `when`(postLocalDataSource.getFavoritePosts()).thenReturn(
            flowOf(Result.Success(listOf(expectedModel.toPostEntity())))
        )

        // When user remove a favorite post from database and check if still exist
        sut.removeFavoritePost(expectedModel)
        `when`(postLocalDataSource.getFavoritePosts()).thenReturn(
            flowOf(
                Result.Success(listOf(providePost(isSeen = true, isFavorite = false).toPostEntity()))
            )
        )

        val posts = sut.getFavoritePosts().first()
        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).doesNotContain(expectedModel)
    }

    @Test
    fun check_removeAllPost_fromLocalDatasource() = runTest {
        val expectedModel = providePost(isSeen = true, isFavorite = false)

        // When user insert a seen post in database
        sut.insertSeenPost(expectedModel)
        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Success(listOf(expectedModel.toPostEntity()))
        )

        // When user remove all posts from database and check if still exists
        sut.removeAllPost()
        `when`(postLocalDataSource.getAllPost()).thenReturn(
            Result.Success(listOf())
        )

        val posts = sut.getAllPost(isRefreshing = false)
        assertThat(posts).isInstanceOf(Result.Failure::class.java)
        assertThat(posts.data).isNull()
    }
}