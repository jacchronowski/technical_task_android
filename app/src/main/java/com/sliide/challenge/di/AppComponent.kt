package com.sliide.challenge.di

import android.app.Application
import com.sliide.challenge.SliideChallengeApplication
import com.sliide.challenge.di.viewModel.ViewModelModule
import com.sliide.challenge.network.di.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    CoreModule::class,
    AndroidSupportInjectionModule::class,
    ActivitiesModule::class,
    NetworkModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<SliideChallengeApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
