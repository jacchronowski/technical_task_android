package com.sliide.challenge.feature.userList.plugins

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.sliide.challenge.architecture.Plugin
import com.sliide.challenge.databinding.UserListLayoutBinding
import com.sliide.challenge.feature.userList.UserListAdapter
import com.sliide.challenge.feature.userList.viewModel.UserListData
import com.sliide.challenge.model.state.UserListUIState

class UserListDecorationPlugin(

    private val context: Context,
    private val userListData: UserListData,
    private val lifecycleOwner: LifecycleOwner,
    private val adapter: UserListAdapter

) : Plugin {

    override fun applyPlugin(view: View) {
        val binding = UserListLayoutBinding.bind(view)
        initList(binding)
        initObservers(binding)
    }

    override fun clearPlugin() = Unit

    private fun initList(binding: UserListLayoutBinding) {
        binding.userListRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.userListRv.adapter = adapter
    }

    private fun initObservers(binding: UserListLayoutBinding) {
        userListData.listState.observe(lifecycleOwner) { state ->
            binding.loadingPb.isVisible = state is UserListUIState.Loading
            when(state) {
                is UserListUIState.DataItems -> updateList(state)
                is UserListUIState.Error -> showError(binding, state)
                else -> {}
            }
        }
    }

    private fun showError(binding: UserListLayoutBinding, state: UserListUIState.Error) {
        Snackbar.make(binding.coordinatorCl, state.errorResId, LENGTH_LONG).show()
    }

    private fun updateList(state: UserListUIState.DataItems) {
        return state.data.let { adapter.submitList(it) }
    }
}
