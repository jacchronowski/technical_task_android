package com.sliide.challenge.ext

import android.app.Dialog

fun Dialog?.isShowing() : Boolean {
    return this != null && this.isShowing
}