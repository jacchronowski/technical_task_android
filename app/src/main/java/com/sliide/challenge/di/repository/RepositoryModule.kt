package com.sliide.challenge.di.repository

import com.sliide.challenge.feature.userList.repository.UserListRepository
import com.sliide.challenge.feature.userList.repository.UserListRepositoryImpl
import com.sliide.challenge.network.executor.RequestExecutor
import com.sliide.challenge.network.services.AuthorizedUsersService
import com.sliide.challenge.network.services.UnauthorizedUsersService
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideUserListRepository(
        unauthorizedUsersService: UnauthorizedUsersService,
        authorizedUsersService: AuthorizedUsersService,
        requestExecutor: RequestExecutor
    ): UserListRepository {
        return UserListRepositoryImpl(unauthorizedUsersService, authorizedUsersService, requestExecutor)
    }
}