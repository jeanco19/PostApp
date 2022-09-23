package com.jean.postapp.data.datasource.post

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.jean.postapp.JsonFileLoader
import com.jean.postapp.MainCoroutineRule
import com.jean.postapp.createRetrofitService
import com.jean.postapp.data.network.retrofit.response.common.FailureApiResponse
import com.jean.postapp.data.network.retrofit.response.post.CommentApiResponse
import com.jean.postapp.data.network.retrofit.response.post.PostApiResponse
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
class PostRemoteDataSourceImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: PostRemoteDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(port = 8080)
        val baseUrl = mockWebServer.url(path = "/").toString()
        sut = PostRemoteDataSourceImpl(
            createRetrofitService(baseUrl),
            mainCoroutineRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun check_getAllPost_whenReturnSuccessResponse() = runTest {
        val localResponse = JsonFileLoader()
            .loadJsonString(file = "post/get-all-post-success.json") ?: ""

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(localResponse)
        )

        val mockResponse = Gson().fromJson(
            localResponse,
            PostApiResponse::class.java
        )

        val remoteResponse = sut.getAllPosts()

        assertThat(remoteResponse).isInstanceOf(Result.Success::class.java)
        assertThat(remoteResponse.data).isNotNull()
        assertThat(remoteResponse.data).isNotEmpty()
        assertThat(remoteResponse.data?.get(0)).isEqualTo(mockResponse[0])
        assertThat(remoteResponse.data?.get(0)?.id).isEqualTo(mockResponse[0].id)
        assertThat(remoteResponse.data?.get(0)?.userId).isEqualTo(mockResponse[0].userId)
        assertThat(remoteResponse.data?.get(0)?.title).isEqualTo(mockResponse[0].title)
        assertThat(remoteResponse.data?.get(0)?.body).isEqualTo(mockResponse[0].body)
    }

    @Test
    fun check_getAllPost_whenReturnFailureResponse() = runTest {
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

        val remoteResponse = sut.getAllPosts()

        assertThat(remoteResponse).isInstanceOf(Result.Failure::class.java)
        assertThat(remoteResponse.data).isNull()
        assertThat(mockResponse).isNotNull()
    }

    @Test
    fun check_getPostComments_whenReturnSuccessResponse() = runTest {
        val localResponse = JsonFileLoader()
            .loadJsonString(file = "post/get-post-comments-success.json") ?: ""

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(localResponse)
        )

        val mockResponse = Gson().fromJson(
            localResponse,
            CommentApiResponse::class.java
        )

        val remoteResponse = sut.getComments(postId = 1)

        assertThat(remoteResponse).isInstanceOf(Result.Success::class.java)
        assertThat(remoteResponse.data).isNotNull()
        assertThat(remoteResponse.data).isNotEmpty()
        assertThat(remoteResponse.data?.get(0)).isEqualTo(mockResponse[0])
        assertThat(remoteResponse.data?.get(0)?.id).isEqualTo(mockResponse[0].id)
        assertThat(remoteResponse.data?.get(0)?.postId).isEqualTo(mockResponse[0].postId)
        assertThat(remoteResponse.data?.get(0)?.name).isEqualTo(mockResponse[0].name)
        assertThat(remoteResponse.data?.get(0)?.email).isEqualTo(mockResponse[0].email)
        assertThat(remoteResponse.data?.get(0)?.body).isEqualTo(mockResponse[0].body)
    }

    @Test
    fun check_getPostComments_whenReturnFailureResponse() = runTest {
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

        val remoteResponse = sut.getComments(postId = 1)

        assertThat(remoteResponse).isInstanceOf(Result.Failure::class.java)
        assertThat(remoteResponse.data).isNull()
        assertThat(mockResponse).isNotNull()
    }
}