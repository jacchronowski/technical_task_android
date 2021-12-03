package com.sliide.challenge.di

import android.app.Application
import com.sliide.challenge.SliideChallengeApplication
import com.sliide.challenge.app.SliideChallengeTestApplication
import com.sliide.challenge.di.viewModel.TestViewModelModule
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
    TestViewModelModule::class
])
interface TestAppComponent : AndroidInjector<SliideChallengeTestApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }
}
