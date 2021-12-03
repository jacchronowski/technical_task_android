package com.sliide.challenge.app

import android.app.Application
import com.sliide.challenge.di.DaggerTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SliideChallengeTestApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerTestAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}