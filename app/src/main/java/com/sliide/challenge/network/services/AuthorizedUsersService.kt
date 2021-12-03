package com.sliide.challenge.network.services

import com.sliide.challenge.model.requests.UserBody
import com.sliide.challenge.model.responses.User
import com.sliide.challenge.model.responses.BaseResponse
import com.sliide.challenge.model.responses.PaginationMeta
import retrofit2.Response
import retrofit2.http.*

interface AuthorizedUsersService {

    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") page: Long) : Response<Unit>

    @POST("users")
    suspend fun addUser(@Body body: UserBody) : Response<BaseResponse<Any, User>>

    companion object {
        const val TAG = "AuthorizedUsersService"
    }
}