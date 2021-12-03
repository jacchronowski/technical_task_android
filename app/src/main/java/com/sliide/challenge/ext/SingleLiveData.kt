package com.sliide.challenge.ext
import androidx.lifecycle.LifecycleOwner

import androidx.lifecycle.MutableLiveData
import android.util.Log
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    override fun postValue(value: T?) {
        pending.set(true)
        super.postValue(value)
    }

    fun call() {
        postValue(null)
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}