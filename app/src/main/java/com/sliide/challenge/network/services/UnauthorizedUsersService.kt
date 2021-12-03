package com.sliide.challenge.network.services

import com.sliide.challenge.model.responses.User
import com.sliide.challenge.model.responses.BaseResponse
import com.sliide.challenge.model.responses.PaginationMeta
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

typealias UsersResponse = BaseResponse<PaginationMeta, List<User>>

interface UnauthorizedUsersService {

    @GET("users")
    suspend fun getUsersList(@Query("page") page: Long? = null) : Response<UsersResponse>

    companion object {
        const val TAG = "UnauthorizedUsersService"
    }
}