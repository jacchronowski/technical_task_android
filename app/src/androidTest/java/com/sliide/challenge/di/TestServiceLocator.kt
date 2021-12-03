package com.sliide.challenge.di

import com.sliide.challenge.feature.userList.repository.UserListRepository

class TestServiceLocator {

    companion object {
        var userListRepository: UserListRepository? = null

        fun clear() {
            userListRepository = null
        }
    }
}