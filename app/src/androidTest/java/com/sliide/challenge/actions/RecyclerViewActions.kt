package com.sliide.challenge.actions

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions

class RecyclerViewActions {

    companion object {

        fun atListPosition(position: Int, action: ViewAction): ViewAction {
            return RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, action)
        }
    }
}