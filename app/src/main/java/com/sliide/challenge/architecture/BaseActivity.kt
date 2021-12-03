package com.sliide.challenge.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.sliide.challenge.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    abstract val layoutResId: Int

    private lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private lateinit var plugins: Set<@JvmSuppressWildcards Plugin>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        applyPlugins()
    }

    override fun onDestroy() {
        super.onDestroy()
        plugins.forEach { it.clearPlugin() }
    }

    @Inject
    fun setAndroidInjector(androidInjector: DispatchingAndroidInjector<Any>) {
        this.androidInjector = androidInjector
    }

    @Inject
    fun setPlugins(plugins: Set<@JvmSuppressWildcards Plugin>) {
        this.plugins = plugins
    }

    private fun applyPlugins() {
        val view = findViewById<View>(R.id.coordinator_cl)
        this.plugins.forEach { it.applyPlugin(view) }
    }
}
