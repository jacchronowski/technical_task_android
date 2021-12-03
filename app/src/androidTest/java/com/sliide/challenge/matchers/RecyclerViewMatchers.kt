package com.sliide.challenge.matchers

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class RecyclerViewMatchers {

    companion object {

        fun withItemCount(itemCount: Int) = object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

            override fun describeTo(description: Description?) = Unit

            override fun matchesSafely(item: RecyclerView?): Boolean {
                return item?.adapter?.itemCount == itemCount
            }
        }

        fun <T : View> withListPosition(position: Int, @IdRes idRes: Int, matcher: Matcher<T>) = object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) = Unit

            override fun matchesSafely(recyclerView: RecyclerView?): Boolean {
                return recyclerView
                    ?.findViewHolderForAdapterPosition(position)
                    ?.itemView?.findViewById<T>(idRes)
                    ?.let { matcher.matches(it) } ?: false
            }
        }
    }
}