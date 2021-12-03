package com.sliide.challenge.model.state

import com.sliide.challenge.model.ui.UserUI

sealed class UserListUIState {
    object Loading : UserListUIState()
    data class DataItems(val data: List<UserUI>) : UserListUIState()
    data class Error(val errorResId: Int) : UserListUIState()
}