package com.sliide.challenge.app

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class SliideTestRunner : AndroidJUnitRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, SliideChallengeTestApplication::class.java.name, context)
    }
}