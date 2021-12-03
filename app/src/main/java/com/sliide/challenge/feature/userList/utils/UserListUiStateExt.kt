package com.sliide.challenge.feature.userList.utils

import com.sliide.challenge.R
import com.sliide.challenge.feature.userList.repository.AddUserResult
import com.sliide.challenge.feature.userList.repository.UserListResult
import com.sliide.challenge.feature.userList.repository.UserResult
import com.sliide.challenge.model.responses.User
import com.sliide.challenge.model.state.UserListUIState
import com.sliide.challenge.model.ui.UserUI
import com.sliide.challenge.model.utils.toUserUI
import com.sliide.challenge.ext.getTimeAgo
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun MutableStateFlow<List<UserUI>>.removeUser(userId: Long): List<UserUI> {
    val value = this.value
    val newList = value
        .map { it.copy(timeAgo = it.creationDate.getTimeAgo()) }
        .toMutableList()
        .apply {
            this.find { it.id == userId }?.let {
                remove(it)
            }
        }
    this.emit(newList)
    return newList
}

suspend fun MutableStateFlow<List<UserUI>>.addUser(user: User): List<UserUI> {
    val value = this.value
    val newList = value
        .map { it.copy(timeAgo = it.creationDate.getTimeAgo()) }
        .toMutableList()
        .apply { add(0, user.toUserUI())}
    this.emit(newList)
    return newList
}

suspend fun UserListResult.state(userList: MutableStateFlow<List<UserUI>>): UserListUIState {
    return when (this) {
        is UserListResult.Success -> UserListUIState.DataItems(userList.state(this.data))
        is UserListResult.Error -> UserListUIState.Error(this.errorResId)
    }
}

suspend fun MutableStateFlow<List<UserUI>>.state(data: List<User>) : List<UserUI> {
    val newList = data.map { it.toUserUI() }
    this.emit(newList)
    return newList
}

suspend fun MutableStateFlow<List<UserUI>>.stateWithLoadingItem(userId: Long): UserListUIState {
    return UserListUIState.DataItems( data = this.value.map { it.copy(isLoading = it.id == userId) })
}

suspend fun AddUserResult.stateAddedItem(userList: MutableStateFlow<List<UserUI>>): UserListUIState {
    return when(this) {
        is AddUserResult.Success -> UserListUIState.DataItems(userList.addUser(user = user))
        is AddUserResult.Error -> UserListUIState.Error(this.errorResId)
    }
}

suspend fun UserResult.stateRemovedItem(userList: MutableStateFlow<List<UserUI>>): UserListUIState {
    return when(this) {
        is UserResult.Success -> UserListUIState.DataItems(userList.removeUser(userId = userId))
        is UserResult.Error -> UserListUIState.Error(this.errorResId)
    }
}