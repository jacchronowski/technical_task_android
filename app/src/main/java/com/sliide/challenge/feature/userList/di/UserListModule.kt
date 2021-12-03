package com.sliide.challenge.feature.userList.di

import com.sliide.challenge.di.ActivityScope
import com.sliide.challenge.feature.userList.UserListAdapter
import dagger.Module
import dagger.Provides

@Module
class UserListModule {

    @ActivityScope
    @Provides
    fun provideUserListAdapter() = UserListAdapter()
}
