package com.sliide.challenge.di

import com.sliide.challenge.feature.userList.UserListActivity
import com.sliide.challenge.feature.userList.di.UsersListActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [UsersListActivityModule::class])
    abstract fun contributeUserListActivity(): UserListActivity
}
