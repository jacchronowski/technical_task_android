package com.sliide.challenge.di.repository

import com.sliide.challenge.di.TestServiceLocator
import com.sliide.challenge.feature.userList.repository.UserListRepository
import com.sliide.challenge.feature.userList.repository.UserListRepositoryImpl
import com.sliide.challenge.network.executor.RequestExecutor
import com.sliide.challenge.network.services.AuthorizedUsersService
import com.sliide.challenge.network.services.UnauthorizedUsersService
import dagger.Module
import dagger.Provides

@Module
class TestRepositoryModule {

    @Provides
    fun provideUserListRepository(
        unauthorizedUsersService: UnauthorizedUsersService,
        authorizedUsersService: AuthorizedUsersService,
        requestExecutor: RequestExecutor
    ): UserListRepository {
        return TestServiceLocator.userListRepository ?: UserListRepositoryImpl(unauthorizedUsersService, authorizedUsersService, requestExecutor)
    }
}