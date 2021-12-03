package com.sliide.challenge.network.executor

import com.sliide.challenge.model.responses.RequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class RequestExecutor @Inject constructor(){

    suspend fun <T : Any> executeRequest(block: suspend CoroutineScope.() -> Response<T>): RequestResult<T> {
        return coroutineScope {
            try {
                val response = block()
                if (response.isSuccessful) {
                    val data = response.success() ?: return@coroutineScope RequestResult.Error()
                    RequestResult.Success(data)
                } else {
                    RequestResult.Error(errorCode = response.code())
                }
            } catch (exception: IOException) {
                RequestResult.Error(isNetworkError = true)
            }
        }
    }
}

fun <T> Response<T>.success(): T? {
    return this.body() ?: Unit as? T
}