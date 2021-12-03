package com.sliide.challenge.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sliide.challenge.model.responses.RequestResult
import com.sliide.challenge.network.executor.RequestExecutor
import com.sliide.challenge.ext.MainCoroutineRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RequestExecutorTest {

    @get:Rule
    val rule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var sut: RequestExecutor

    @Before
    fun setUp() {
        sut = RequestExecutor()
    }

    @Test
    fun `should return success when successful http response`() = runBlockingTest {
        //given
        val block : suspend CoroutineScope.() -> Response<Int> = { Response.success(4)}

        //when
        val result = sut.executeRequest(block)

        //then
        assert(result is RequestResult.Success)
    }

    @Test
    fun `should return failure when unsuccessful http response`() = runBlockingTest {
        //given
        val block : suspend CoroutineScope.() -> Response<Int> = { Response.error(400, "".toResponseBody() )}

        //when
        val result = sut.executeRequest(block)

        //then
        assert(result is RequestResult.Error && result.errorCode == 400)
    }

    @Test
    fun `should return network error`() = runBlockingTest {
        //given
        val block : suspend CoroutineScope.() -> Response<Int> = { throw IOException() }

        //when
        val result = sut.executeRequest(block)

        //then
        assert(result is RequestResult.Error && result.isNetworkError)
    }
}