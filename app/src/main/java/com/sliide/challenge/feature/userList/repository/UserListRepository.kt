package com.sliide.challenge.feature.userList.repository

import com.sliide.challenge.R
import com.sliide.challenge.model.Result
import com.sliide.challenge.model.requests.UserBody
import com.sliide.challenge.model.responses.*
import com.sliide.challenge.network.executor.RequestExecutor
import com.sliide.challenge.network.services.AuthorizedUsersService
import com.sliide.challenge.network.services.UnauthorizedUsersService
import com.sliide.challenge.network.services.UsersResponse

private const val USER_LIST_ERROR = "USER_LIST_ERROR"
private const val REMOVE_ADD_USER_ERROR = "USER_LIST_ERROR"
private const val REMOVE_REMOVE_USER_ERROR = "USER_LIST_ERROR"

interface UserListRepository {

    suspend fun getUsersList() : UserListResult
    suspend fun removeUser(userId: Long) : UserResult
    suspend fun addUser(name: String, email: String): AddUserResult
}

class UserListRepositoryImpl(
    private val unauthorizedUsersService: UnauthorizedUsersService,
    private val authorizedUsersService: AuthorizedUsersService,
    private val requestExecutor: RequestExecutor
) : UserListRepository {

    override suspend fun getUsersList(): UserListResult {
        val result = requestExecutor.executeRequest { unauthorizedUsersService.getUsersList() }
        if (result is RequestResult.Error) {
            return UserListResult.Error(R.string.error_user_list)
        }
        val success = result as RequestResult.Success<UsersResponse>
        val lastPage = success.data.meta?.pagination?.pages
        val lastPageResult = requestExecutor.executeRequest { unauthorizedUsersService.getUsersList(lastPage) }
        return if (lastPageResult is RequestResult.Success) {
            UserListResult.Success(lastPageResult.data.data ?: listOf())
        } else {
            UserListResult.Error(R.string.error_user_list)
        }
    }

    override suspend fun removeUser(userId: Long) : UserResult{
        val result = requestExecutor.executeRequest {
            authorizedUsersService.deleteUser(userId)
        }
        return if (result is RequestResult.Success) {
            UserResult.Success(userId)
        } else {
            UserResult.Error(R.string.error_user_add)
        }
    }

    override suspend fun addUser(name: String, email: String): AddUserResult {
        val result = requestExecutor.executeRequest {
            authorizedUsersService.addUser(UserBody(
                email = email,
                name = name,
                gender = Gender.MALE,
                status = Status.ACTIVE
            ))
        }
        return if (result is RequestResult.Success && result.data.data != null) {
            AddUserResult.Success(result.data.data)
        } else {
            AddUserResult.Error(R.string.error_user_remove)
        }
    }
}

sealed class UserListResult : Result {
    data class Success(val data: List<User>): UserListResult()
    data class Error(val errorResId: Int) : UserListResult()
}

sealed class UserResult : Result {
    data class Success(val userId: Long): UserResult()
    data class Error(val errorResId: Int) : UserResult()
}

sealed class AddUserResult : Result {
    data class Success(val user: User): AddUserResult()
    data class Error(val errorResId: Int) : AddUserResult()
}