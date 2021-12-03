package com.sliide.challenge.feature.userList.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.sliide.challenge.architecture.Plugin
import com.sliide.challenge.di.ActivityScope
import com.sliide.challenge.feature.userList.UserListActivity
import com.sliide.challenge.feature.userList.UserListAdapter
import com.sliide.challenge.feature.userList.plugins.UserListDecorationPlugin
import com.sliide.challenge.feature.userList.plugins.UserListRoutingPlugin
import com.sliide.challenge.feature.userList.plugins.UserListViewReactionsPlugin
import com.sliide.challenge.feature.userList.viewModel.UserListActions
import com.sliide.challenge.feature.userList.viewModel.UserListData
import com.sliide.challenge.feature.userList.viewModel.UserListRouting
import com.sliide.challenge.feature.userList.viewModel.UserListVM
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module(
    includes = [
        UsersListActivityModule.Binders::class,
        UsersListActivityModule.Providers::class,
        UserListModule::class
    ]
)
abstract class UsersListActivityModule {

    @Module
    abstract class Binders {

        @ActivityScope
        @Binds
        abstract fun bindActivity(activity: UserListActivity): AppCompatActivity

        @ActivityScope
        @Binds
        abstract fun bindLifecycleOwner(activity: UserListActivity): LifecycleOwner

        @ActivityScope
        @Binds
        abstract fun bindUserListActions(viewModel: UserListVM): UserListActions

        @ActivityScope
        @Binds
        abstract fun bindUserListData(viewModel: UserListVM): UserListData

        @ActivityScope
        @Binds
        abstract fun bindUserListRouting(viewModel: UserListVM): UserListRouting

        @IntoSet
        @ActivityScope
        @Binds
        abstract fun bindUserListDecorationPlugin(plugin: UserListDecorationPlugin): Plugin

        @IntoSet
        @ActivityScope
        @Binds
        abstract fun bindUserListRoutingPlugin(plugin: UserListRoutingPlugin): Plugin

        @IntoSet
        @ActivityScope
        @Binds
        abstract fun bindUserListViewReactionsPlugin(plugin: UserListViewReactionsPlugin): Plugin
    }


    @Module
    class Providers {

        @ActivityScope
        @Provides
        fun provideViewModel(
            factory: ViewModelProvider.Factory,
            target: AppCompatActivity
        ) = ViewModelProvider(target, factory).get(UserListVM::class.java)

        @ActivityScope
        @Provides
        fun provideUserListDecorationPlugin(
            activity: AppCompatActivity,
            userListData: UserListData,
            lifecycleOwner: LifecycleOwner,
            adapter: UserListAdapter
        ) = UserListDecorationPlugin(activity, userListData, lifecycleOwner, adapter)

        @ActivityScope
        @Provides
        fun provideUserListRoutingPlugin(
            activity: AppCompatActivity,
            userListRouting: UserListRouting,
            lifecycleOwner: LifecycleOwner
        ) = UserListRoutingPlugin(activity, userListRouting, lifecycleOwner)

        @ActivityScope
        @Provides
        fun provideUserListViewReactionsPlugin(
            actions: UserListActions,
            adapter: UserListAdapter
        ) = UserListViewReactionsPlugin(actions, adapter)
    }
}