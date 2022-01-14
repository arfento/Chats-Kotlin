package com.ims.chats_kotlin.ui.chat

import androidx.lifecycle.*
import com.ims.chats_kotlin.data.Result
import com.ims.chats_kotlin.data.db.entity.Chat
import com.ims.chats_kotlin.data.db.entity.Message
import com.ims.chats_kotlin.data.db.entity.UserInfo
import com.ims.chats_kotlin.data.db.remote.FirebaseReferenceChildObserver
import com.ims.chats_kotlin.data.db.remote.FirebaseReferenceValueObserver
import com.ims.chats_kotlin.data.db.repository.DatabaseRepository
import com.ims.chats_kotlin.ui.DefaultViewModel
import com.ims.chats_kotlin.util.addNewItem

class ChatViewModelFactory(
    private val myUserID: String,
    private val otherUserID: String,
    private val chatID: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(myUserID, otherUserID, chatID) as T
    }
}

class ChatViewModel(
    private val myUserID: String,
    private val otherUserID: String,
    private val chatID: String

) : DefaultViewModel() {

    private val dbRepository: DatabaseRepository = DatabaseRepository()

    private val _otherUser: MutableLiveData<UserInfo> = MutableLiveData()
    private val _addedMessage = MutableLiveData<Message>()

    private val fbRefMessagesChildObserver = FirebaseReferenceChildObserver()
    private val fbRefUserInfoObserver = FirebaseReferenceValueObserver()

    val messagesList = MediatorLiveData<MutableList<Message>>()
    val newMessageText = MutableLiveData<String>()
    val otherUser: LiveData<UserInfo> = _otherUser

    init {
        setupChat()
        checkAndUpdateLastMessageSeen()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefMessagesChildObserver.clear()
        fbRefUserInfoObserver.clear()
    }

    private fun checkAndUpdateLastMessageSeen() {
        dbRepository.loadChat(chatID) { result: Result<Chat> ->
            if (result is Result.Success && result.data != null) {
                result.data.lastMessage.let {
                    if (!it.seen && it.senderID != myUserID) {
                        it.seen = true
                        dbRepository.updateChatLastMessage(chatID, it)
                    }
                }
            }

        }
    }

    private fun setupChat() {
        dbRepository.loadAndObserveUserInfo(
            otherUserID,
            fbRefUserInfoObserver
        ) { result: Result<UserInfo> ->
            onResult(_otherUser, result)
            if (result is Result.Success && !fbRefMessagesChildObserver.isObserving()) {
                loadAndObserveNewMesssage()
            }

        }
    }

    private fun loadAndObserveNewMesssage() {
        messagesList.addSource(_addedMessage) {
            messagesList.addNewItem(it)
        }
        dbRepository.loadAndObserveMessagesAdded(
            chatID,
            fbRefMessagesChildObserver
        ) { result: Result<Message> ->
            onResult(_addedMessage, result)

        }
    }

    fun sendMessagePressed(){
        if (!newMessageText.value.isNullOrBlank()){
            val newMsg = Message(myUserID, newMessageText.value!!)
            dbRepository.updateNewMessage(chatID, newMsg)
            dbRepository.updateChatLastMessage(chatID, newMsg)
            newMessageText.value = null
        }
    }
}