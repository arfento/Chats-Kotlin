package com.ims.chats_kotlin.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ims.chats_kotlin.data.db.entity.UserInfo
import com.ims.chats_kotlin.data.db.entity.UserNotification
import com.ims.chats_kotlin.data.db.repository.DatabaseRepository
import com.ims.chats_kotlin.databinding.ListItemNotificationBinding
import com.ims.chats_kotlin.ui.DefaultViewModel

class NotificationsListAdapter internal constructor(private val viewModel: NotificationsViewModel) :
    ListAdapter<UserInfo, NotificationsListAdapter.ViewHolder>(UserInfoDiffCallback()) {
    class ViewHolder(private val binding: ListItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: NotificationsViewModel, item: UserInfo) {
            binding.viewmodel = viewModel
            binding.userinfo = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemNotificationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class UserInfoDiffCallback : DiffUtil.ItemCallback<UserInfo>() {
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem.id == newItem.id
    }
}