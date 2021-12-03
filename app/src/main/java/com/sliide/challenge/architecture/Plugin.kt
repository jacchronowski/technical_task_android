package com.sliide.challenge.architecture

import android.view.View
import androidx.viewbinding.ViewBinding

interface Plugin {
    fun applyPlugin(view: View)
    fun clearPlugin()
}