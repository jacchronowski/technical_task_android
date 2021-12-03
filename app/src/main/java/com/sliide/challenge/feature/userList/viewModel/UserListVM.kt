package com.sliide.challenge.feature.userList.viewModel

import androidx.lifecycle.*
import com.sliide.challenge.R
import com.sliide.challenge.feature.userList.repository.UserListRepository
import com.sliide.challenge.feature.userList.utils.*
import com.sliide.challenge.model.state.UserListUIState
import com.sliide.challenge.model.ui.UserUI
import com.sliide.challenge.ext.SingleLiveEvent
import com.sliide.challenge.ext.isEmailValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface UserListActions {

    fun onAddUserClick()
    fun onRemoveUserClick(userId: Long)
}

interface UserListData {
    val listState: LiveData<UserListUIState>
}

interface UserListRouting {
    val showAddUserDialogEvent: SingleLiveEvent<Unit>
    val showRemoveUserDialogEvent: SingleLiveEvent<Long>
    val dismissAddUserDialogEvent: SingleLiveEvent<Unit>

    fun onRemoveUserConfirmClick(userId: Long)
    fun onAddUserConfirmClick(name: String?, email: String?)
}

abstract class UserListVM : ViewModel(), UserListActions, UserListData, UserListRouting

class UserListVMImpl(

    private val repository: UserListRepository

) : UserListVM() {

    private val userList = MutableStateFlow<List<UserUI>>(listOf())

    override val showAddUserDialogEvent = SingleLiveEvent<Unit>()
    override val showRemoveUserDialogEvent = SingleLiveEvent<Long>()
    override val dismissAddUserDialogEvent = SingleLiveEvent<Unit>()

    override val listState = MutableLiveData<UserListUIState>(UserListUIState.Loading)

    init {
        viewModelScope.launch {
            listState.postValue(repository.getUsersList().state(userList))
        }
    }

    override fun onAddUserClick() {
        showAddUserDialogEvent.call()
    }

    override fun onRemoveUserClick(userId: Long) {
        showRemoveUserDialogEvent.postValue(userId)
    }

    override fun onRemoveUserConfirmClick(userId: Long) {
        viewModelScope.launch {
            listState.postValue(userList.stateWithLoadingItem(userId))
            listState.postValue(repository.removeUser(userId).stateRemovedItem(userList))
        }
    }

    override fun onAddUserConfirmClick(name: String?, email: String?) {
        if (name.isNullOrBlank() || email.isNullOrBlank()) {
            viewModelScope.launch { listState.postValue(UserListUIState.Error(R.string.error_please_enter_data)) }
            return
        } else if (!email.isEmailValid()) {
            viewModelScope.launch { listState.postValue(UserListUIState.Error(R.string.error_email_is_not_valid)) }
            return
        }
        dismissAddUserDialogEvent.call()
        viewModelScope.launch {
            listState.postValue(UserListUIState.Loading)
            listState.postValue(repository.addUser(name, email).stateAddedItem(userList))
        }
    }
}
