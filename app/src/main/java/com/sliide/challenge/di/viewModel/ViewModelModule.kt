package com.sliide.challenge.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sliide.challenge.di.repository.RepositoryModule
import com.sliide.challenge.feature.userList.repository.UserListRepository
import com.sliide.challenge.feature.userList.viewModel.UserListVM
import com.sliide.challenge.feature.userList.viewModel.UserListVMImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import javax.inject.Singleton

@Module(
    includes = [
        RepositoryModule::class
    ]
)
class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(UserListVM::class)
    fun provideUserListViewModel(repository: UserListRepository): ViewModel {
        return UserListVMImpl(repository)
    }
}