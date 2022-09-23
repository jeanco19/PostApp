package com.jean.postapp.data.repository.user

import com.google.common.truth.Truth.assertThat
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.data.datasource.user.UserRemoteDataSource
import com.jean.postapp.data.mapper.toAuthorApiModel
import com.jean.postapp.data.util.Result
import com.jean.postapp.domain.model.user.Author
import com.jean.postapp.provideAuthor
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
class UserRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var userRemoteDatasource: UserRemoteDataSource
    private lateinit var sut: UserRepositoryImpl

    @Before
    fun setup() {
        userRemoteDatasource = Mockito.mock(UserRemoteDataSource::class.java)
        sut = UserRepositoryImpl(
            userRemoteDatasource,
            mainCoroutineRule.testDispatcher
        )
    }

    @Test
    fun check_getAuthorPost_whenReturnDataFromDatasource() = runTest {
        val expectedModel = provideAuthor()

        `when`(userRemoteDatasource.getAuthor(postId = 1)).thenReturn(
            Result.Success(expectedModel.toAuthorApiModel())
        )

        val comment = sut.getAuthorPost(postId = 1)
        assertThat(comment).isInstanceOf(Result.Success::class.java)
        assertThat(comment.data).isNotNull()
        assertThat(comment.data).isInstanceOf(Author::class.java)
        assertThat(comment.data?.id).isEqualTo(expectedModel.id)
        assertThat(comment.data?.name).isEqualTo(expectedModel.name)
        assertThat(comment.data?.email).isEqualTo(expectedModel.email)
        assertThat(comment.data?.phone).isEqualTo(expectedModel.phone)
        assertThat(comment.data?.website).isEqualTo(expectedModel.website)
    }

    @Test
    fun check_getAuthorPost_whenReturnFailureFromDatasource() = runTest {
        `when`(userRemoteDatasource.getAuthor(postId = 1)).thenReturn(
            Result.Failure()
        )

        val comment = sut.getAuthorPost(postId = 1)
        assertThat(comment).isInstanceOf(Result.Failure::class.java)
        assertThat(comment.data).isNull()
    }
}