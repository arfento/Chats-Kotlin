package com.ims.chats_kotlin.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ims.chats_kotlin.data.db.entity.Message
import com.ims.chats_kotlin.databinding.ListItemMessageReceivedBinding
import com.ims.chats_kotlin.databinding.ListItemMessageSentBinding

class MessagesListAdapter internal constructor(
    private val viewModel: ChatViewModel,
    private val userID: String
) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2

    class ReceivedViewHolder(private val binding: ListItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(viewModel: ChatViewModel, item : Message){
                binding.viewmodel = viewModel
                binding.message = item
                binding.executePendingBindings()
            }

    }

    class SentViewHolder(private val binding: ListItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(viewModel: ChatViewModel, item: Message){
                binding.viewmodel = viewModel
                binding.message = item
                binding.executePendingBindings()
            }
    }

    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        return if (getItem(position).senderID != userID){
            holderTypeMessageReceived
        }else{
            holderTypeMessageSent
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){
            holderTypeMessageSent ->{
                val binding = ListItemMessageSentBinding.inflate(layoutInflater, parent, false)
                SentViewHolder(binding)
            }
            holderTypeMessageReceived ->{
                val binding = ListItemMessageReceivedBinding.inflate(layoutInflater, parent, false)
                ReceivedViewHolder(binding)
            }else -> {
                throw Exception("Error reader holder type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            holderTypeMessageSent -> (holder as SentViewHolder).bind(
                viewModel,
                getItem(position)
            )
            holderTypeMessageReceived -> (holder as ReceivedViewHolder).bind(
                viewModel,
                getItem(position)
            )

        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return newItem.epochTimeMs == oldItem.epochTimeMs
    }

}
