package com.sliide.challenge.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sliide.challenge.di.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module(includes = [
    CoreModule.Providers::class
])
abstract class CoreModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Module
    class Providers {

        @Provides
        @Singleton
        fun provideViewModelFactory(providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory = ViewModelFactory(providers)
    }
}