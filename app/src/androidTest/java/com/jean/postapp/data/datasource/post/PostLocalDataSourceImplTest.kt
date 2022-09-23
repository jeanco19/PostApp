package com.jean.postapp.data.datasource.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.jean.postapp.providePostEntity
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.database.PostDao
import com.jean.postapp.data.database.PostDatabase
import com.jean.postapp.data.database.entity.PostEntity
import com.jean.postapp.data.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class PostLocalDataSourceImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: PostDatabase
    private lateinit var postDao: PostDao
    private lateinit var sut: PostLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
        postDao = database.postDao()
        sut = PostLocalDataSourceImpl(
            postDao,
            mainCoroutineRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun check_insertPost_toDatabase() = runTest {
        val postEntity = providePostEntity(isSeen = false, isFavorite = false)

        sut.insertPost(postEntity)
        val posts = sut.getAllPost() as Result.Success

        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts.data?.get(0)).isEqualTo(postEntity)
    }

    @Test
    fun check_insertSeenPost_toDatabase() = runTest {
        val postEntity = providePostEntity(isSeen = false, isFavorite = false)
        val seenPostEntity = providePostEntity(isSeen = true, isFavorite = false)

        sut.insertPost(postEntity)
        sut.insertSeenPost(seenPostEntity)
        val posts = sut.getAllPost() as Result.Success

        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts.data?.get(0)).isEqualTo(seenPostEntity)
        assertThat(posts.data?.get(0)?.isSeen).isTrue()
    }

    @Test
    fun check_insertFavoritePost_toDatabase() = runTest {
        val postEntity = providePostEntity(isSeen = true, isFavorite = true)
        val favoritePostEntity = providePostEntity(isSeen = true, isFavorite = true)

        sut.insertPost(postEntity)
        sut.insertFavoritePost(favoritePostEntity)

        val mutablePosts = mutableListOf<Result<List<PostEntity>>>()
        val collectJob = launch(mainCoroutineRule.testDispatcher) {
            sut.getFavoritePosts().toList(mutablePosts)
        }
        val posts = mutablePosts.first() as Result.Success

        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts.data?.get(0)).isEqualTo(favoritePostEntity)
        assertThat(posts.data?.get(0)?.isSeen).isTrue()
        assertThat(posts.data?.get(0)?.isFavorite).isTrue()

        collectJob.cancel()
    }

    @Test
    fun check_getPost_whenDatabaseHasPosts() = runTest {
        val postEntity = providePostEntity(isSeen = false, isFavorite = false)

        sut.insertPost(postEntity)
        val posts = sut.getPost(postId = 1)

        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEqualTo(postEntity)
        assertThat(posts.data?.id).isEqualTo(postEntity.id)
        assertThat(posts.data?.userId).isEqualTo(postEntity.userId)
        assertThat(posts.data?.title).isEqualTo(postEntity.title)
        assertThat(posts.data?.body).isEqualTo(postEntity.body)
        assertThat(posts.data?.isSeen).isEqualTo(postEntity.isSeen)
        assertThat(posts.data?.isFavorite).isEqualTo(postEntity.isFavorite)
    }

    @Test
    fun check_getPost_whenDatabaseIsEmpty() = runTest {
        val posts = sut.getPost(postId = 1)

        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts).isNotNull()
    }

    @Test
    fun check_getAllPost_whenDatabaseHasPosts() = runTest {
        val postEntity = providePostEntity(isSeen = false, isFavorite = false)

        sut.insertPost(postEntity)
        val posts = sut.getAllPost() as Result.Success

        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts.data?.get(0)).isEqualTo(postEntity)
        assertThat(posts.data?.get(0)?.id).isEqualTo(postEntity.id)
        assertThat(posts.data?.get(0)?.userId).isEqualTo(postEntity.userId)
        assertThat(posts.data?.get(0)?.title).isEqualTo(postEntity.title)
        assertThat(posts.data?.get(0)?.body).isEqualTo(postEntity.body)
        assertThat(posts.data?.get(0)?.isSeen).isEqualTo(postEntity.isSeen)
        assertThat(posts.data?.get(0)?.isFavorite).isEqualTo(postEntity.isFavorite)
    }

    @Test
    fun check_getAllPost_whenDatabaseIsEmpty() = runTest {
        val posts = sut.getAllPost()

        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }

    @Test
    fun check_getFavoritePosts_whenDatabaseHasData() = runTest {
        val postEntity = providePostEntity(isSeen = true, isFavorite = true)
        val favoritePostEntity = providePostEntity(isSeen = true, isFavorite = true)

        sut.insertPost(postEntity)
        sut.insertFavoritePost(favoritePostEntity)

        val mutablePost = mutableListOf<Result<List<PostEntity>>>()
        val collectJob = launch(mainCoroutineRule.testDispatcher) {
            sut.getFavoritePosts().toList(mutablePost)
        }
        val posts = mutablePost.first()

        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isNotEmpty()
        assertThat(posts.data?.get(0)).isEqualTo(favoritePostEntity)
        assertThat(posts.data?.get(0)?.id).isEqualTo(postEntity.id)
        assertThat(posts.data?.get(0)?.userId).isEqualTo(postEntity.userId)
        assertThat(posts.data?.get(0)?.title).isEqualTo(postEntity.title)
        assertThat(posts.data?.get(0)?.body).isEqualTo(postEntity.body)
        assertThat(posts.data?.get(0)?.isSeen).isTrue()
        assertThat(posts.data?.get(0)?.isFavorite).isTrue()

        collectJob.cancel()
    }

    @Test
    fun check_getFavoritePosts_whenDatabaseIsEmpty() = runTest {
        val mutablePost = mutableListOf<Result<List<PostEntity>>>()
        val collectJob = launch(mainCoroutineRule.testDispatcher) {
            sut.getFavoritePosts().toList(mutablePost)
        }
        val posts = mutablePost.first()

        assertThat(posts).isInstanceOf(Result.Success::class.java)
        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()

        collectJob.cancel()
    }

    @Test
    fun check_removePost_fromDatabase() = runTest {
        val postEntity = providePostEntity(isSeen = false, isFavorite = false)

        sut.insertPost(postEntity)
        sut.removePost(postEntity)
        val posts = sut.getAllPost() as Result.Success

        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }

    @Test
    fun check_removeFavoritePosts_fromDatabase() = runTest {
        val favoritePostEntity = providePostEntity(isSeen = true, isFavorite = true)

        sut.insertFavoritePost(favoritePostEntity)
        sut.removeFavoritePost(favoritePostEntity)

        val mutablePost = mutableListOf<Result<List<PostEntity>>>()
        val collectJob = launch(mainCoroutineRule.testDispatcher) {
            sut.getFavoritePosts().toList(mutablePost)
        }
        val posts = mutablePost.first() as Result.Success

        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()

        collectJob.cancel()
    }

    @Test
    fun check_removeAllPost_fromDatabase() = runTest {
        val postEntity = providePostEntity(isSeen = true, isFavorite = true)
        val favoritePostEntity = providePostEntity(isSeen = true, isFavorite = true)

        sut.insertPost(postEntity)
        sut.insertFavoritePost(favoritePostEntity)
        sut.removeAllPost()
        val posts = sut.getAllPost() as Result.Success

        assertThat(posts.data).isNotNull()
        assertThat(posts.data).isEmpty()
    }
}