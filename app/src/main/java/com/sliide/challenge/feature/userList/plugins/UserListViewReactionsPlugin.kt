package com.sliide.challenge.feature.userList.plugins

import android.view.View
import com.sliide.challenge.architecture.Plugin
import com.sliide.challenge.databinding.UserListLayoutBinding
import com.sliide.challenge.feature.userList.UserListAdapter
import com.sliide.challenge.feature.userList.viewModel.UserListActions

class UserListViewReactionsPlugin(
    private val actions: UserListActions,
    private val adapter: UserListAdapter
) : Plugin{

    override fun applyPlugin(view: View) {
        val binding = UserListLayoutBinding.bind(view)
        setupAddUserClick(binding)
        setupOnRemoveUserClick()
    }

    override fun clearPlugin() = Unit

    private fun setupOnRemoveUserClick() {
        adapter.onRemoveItemClick = actions::onRemoveUserClick
    }

    private fun setupAddUserClick(binding: UserListLayoutBinding) {
        binding.addUserFab.setOnClickListener {
            actions.onAddUserClick()
        }
    }
}
