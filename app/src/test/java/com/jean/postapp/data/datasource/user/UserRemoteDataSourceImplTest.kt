package com.jean.postapp.data.datasource.user

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.JsonFileLoader
import com.jean.postapp.createRetrofitService
import com.jean.postapp.data.network.retrofit.response.author.AuthorApiModel
import com.jean.postapp.data.network.retrofit.response.common.FailureApiResponse
import com.jean.postapp.data.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class UserRemoteDataSourceImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: UserRemoteDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(port = 8080)
        val baseUrl = mockWebServer.url(path = "/").toString()
        sut = UserRemoteDataSourceImpl(
            createRetrofitService(baseUrl),
            mainCoroutineRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun check_getAuthorPost_whenReturnSuccessResponse() = runTest {
        val localResponse = JsonFileLoader()
            .loadJsonString(file = "user/get-author-post-success.json") ?: ""

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(localResponse)
        )

        val mockResponse = Gson().fromJson(
            localResponse,
            AuthorApiModel::class.java
        )

        val remoteResponse = sut.getAuthor(postId = 1)

        assertThat(remoteResponse).isInstanceOf(Result.Success::class.java)
        assertThat(remoteResponse.data).isNotNull()
        assertThat(remoteResponse.data?.id).isEqualTo(mockResponse.id)
        assertThat(remoteResponse.data?.name).isEqualTo(mockResponse.name)
        assertThat(remoteResponse.data?.username).isEqualTo(mockResponse.username)
        assertThat(remoteResponse.data?.email).isEqualTo(mockResponse.email)
        assertThat(remoteResponse.data?.phone).isEqualTo(mockResponse.phone)
        assertThat(remoteResponse.data?.website).isEqualTo(mockResponse.website)
    }

    @Test
    fun check_getAuthorPost_whenReturnFailureResponse() = runTest {
        val localResponse = JsonFileLoader()
            .loadJsonString(file = "common/common-failure.json") ?: ""

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody(localResponse)
        )

        val mockResponse = Gson().fromJson(
            localResponse,
            FailureApiResponse::class.java
        )

        val remoteResponse = sut.getAuthor(postId = 1)

        assertThat(remoteResponse).isInstanceOf(Result.Failure::class.java)
        assertThat(remoteResponse.data).isNull()
        assertThat(mockResponse).isNotNull()
    }
}