package com.sliide.challenge.model.responses

import com.sliide.challenge.model.Result

sealed class RequestResult<T> : Result {

    data class Success<T>(val data: T) : RequestResult<T>()
    data class Error<T>(val errorCode: Int = 0, val isNetworkError: Boolean = false) : RequestResult<T>()
}
