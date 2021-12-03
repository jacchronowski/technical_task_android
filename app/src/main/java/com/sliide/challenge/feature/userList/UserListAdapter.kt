package com.sliide.challenge.feature.userList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sliide.challenge.R
import com.sliide.challenge.databinding.UserItemLayoutBinding
import com.sliide.challenge.model.ui.UserUI

class UserListAdapter : ListAdapter<UserUI, UserViewHolder>(DiffCallback) {

    var onRemoveItemClick : ((Long)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { user ->
            holder.binding.userNameTv.text = user.name
            holder.binding.userEmailTv.text = user.email
            holder.binding.userCreationTimeTv.text = user.timeAgo
            holder.binding.userActionPb.isVisible = user.isLoading
            holder.binding.root.setOnLongClickListener {
                Log.d("UserListAdapter", "onRemoveItemClick: ${onRemoveItemClick != null}")
                onRemoveItemClick?.invoke(user.id)
                false
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<UserUI>() {
        override fun areItemsTheSame(oldItem: UserUI, newItem: UserUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserUI, newItem: UserUI): Boolean {
            return oldItem == newItem
        }
    }
}

class UserViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
    val binding: UserItemLayoutBinding = UserItemLayoutBinding.bind(containerView)
}