package com.sliide.challenge.di.viewModel

import androidx.lifecycle.ViewModel
import com.sliide.challenge.di.repository.TestRepositoryModule
import com.sliide.challenge.feature.userList.repository.UserListRepository
import com.sliide.challenge.feature.userList.viewModel.UserListVM
import com.sliide.challenge.feature.userList.viewModel.UserListVMImpl
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(
    includes = [
        TestRepositoryModule::class
    ]
)
class TestViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(UserListVM::class)
    fun provideUserListViewModel(repository: UserListRepository): ViewModel {
        return UserListVMImpl(repository)
    }
}