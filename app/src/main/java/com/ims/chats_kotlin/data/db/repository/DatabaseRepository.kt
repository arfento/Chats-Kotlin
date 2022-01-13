package com.ims.chats_kotlin.data.db.repository

import com.ims.chats_kotlin.data.Result
import com.ims.chats_kotlin.data.db.entity.*
import com.ims.chats_kotlin.data.db.remote.FirebaseDataSource
import com.ims.chats_kotlin.data.db.remote.FirebaseReferenceChildObserver
import com.ims.chats_kotlin.data.db.remote.FirebaseReferenceValueObserver
import com.ims.chats_kotlin.util.wrapSnapshotToArrayList
import com.ims.chats_kotlin.util.wrapSnapshotToClass

class DatabaseRepository {
    private val firebaseDatabaseService = FirebaseDataSource()

    //region update
    fun updateUserStatus(userID: String, status: String) {
        firebaseDatabaseService.updateUserStatus(userID, status)
    }

    fun updateNewMessage(messageID: String, message: Message) {
        firebaseDatabaseService.pushNewMessage(messageID, message)
    }

    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }

    fun updateNewFriend(myUser: UserFriend, otherUser: UserFriend) {
        firebaseDatabaseService.updateNewFriend(myUser, otherUser)
    }

    fun updateNewSentRequest(userID: String, userRequest: UserRequest) {
        firebaseDatabaseService.updateNewSentRequest(userID, userRequest)
    }

    fun updateNewNotification(otherUserID: String, userNotification: UserNotification) {
        firebaseDatabaseService.updateNewNotification(otherUserID, userNotification)
    }

    fun updateChatLastMessage(chatID: String, message: Message) {
        firebaseDatabaseService.updateLastMessage(chatID, message)
    }

    fun updateNewChat(chat: Chat) {
        firebaseDatabaseService.updateNewChat(chat)
    }

    fun updateUserProfileImageUrl(userID: String, url: String) {
        firebaseDatabaseService.updateUserProfileImageUrl(userID, url)
    }

    //endregion

    //regionremove
    fun removeNotification(userID: String, notificationID: String) {
        firebaseDatabaseService.removeNotification(userID, notificationID)
    }

    fun removeFriend(userID: String, friendID: String) {
        firebaseDatabaseService.removeFriend(userID, friendID)
    }

    fun removeSendRequest(otherUserID: String, myUserID: String) {
        firebaseDatabaseService.removeSentRequest(otherUserID, myUserID)
    }

    fun removeChat(chatID: String) {
        firebaseDatabaseService.removeChat(chatID)
    }

    fun removeMessages(messageID: String) {
        firebaseDatabaseService.removeMessages(messageID)
    }

    //end remove

    //region load single

    fun loadUser(userID: String, b: ((Result<User>) -> Unit)) {
        firebaseDatabaseService.loadUserTask(userID)
            .addOnSuccessListener {
                b.invoke(Result.Success(wrapSnapshotToClass(User::class.java, it)))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))
            }
    }

    fun loadUserInfo(userID: String, b: ((Result<UserInfo>) -> Unit)) {
        firebaseDatabaseService.loadUserInfoTask(userID)
            .addOnSuccessListener {
                b.invoke(Result.Success(wrapSnapshotToClass(UserInfo::class.java, it)))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))
            }
    }

    fun loadChat(userID: String, b: ((Result<Chat>) -> Unit)) {
        firebaseDatabaseService.loadChatTask(userID)
            .addOnSuccessListener {
                b.invoke(Result.Success(wrapSnapshotToClass(Chat::class.java, it)))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))
            }
    }
    //endregion

    //region Load List

    fun loadUsers(b: ((Result<MutableList<User>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadUsersTask()
            .addOnSuccessListener {
                val userList = wrapSnapshotToArrayList(User::class.java, it)
                b.invoke(Result.Success(userList))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))
            }
    }

    fun loadFriends(userID: String, b: ((Result<MutableList<UserFriend>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadFriendsTask(userID)
            .addOnSuccessListener {
                val friendList = wrapSnapshotToArrayList(UserFriend::class.java, it)
                b.invoke(Result.Success(friendList))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))
            }
    }

    fun loadNotifications(userID: String, b: ((Result<MutableList<UserNotification>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadNotificationsTask(userID)
            .addOnSuccessListener {
                val notificationList = wrapSnapshotToArrayList(UserNotification::class.java, it)
                b.invoke(Result.Success(notificationList))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))
            }
    }
    //endregion

    //#region Load and Observe
    fun loadAndObserveUser(
        userID: String, observer: FirebaseReferenceValueObserver,
        b : ((Result<User>) -> Unit)
    ){
        firebaseDatabaseService.attachUserObserver(User::class.java, userID, observer, b)
    }

    fun loadAndObserveUserInfo(
        userID: String, observer: FirebaseReferenceValueObserver,
        b : ((Result<UserInfo>) -> Unit)
    ){
        firebaseDatabaseService.attachUserInfoObserver(UserInfo::class.java, userID, observer, b)
    }

    fun loadAndObserveUserNotifications(
        userID: String, observer: FirebaseReferenceValueObserver,
        b : ((Result<MutableList<UserNotification>>) -> Unit)
    ){
        firebaseDatabaseService.attachUserNotificationsObserver(
            UserNotification::class.java, userID, observer, b)
    }

    fun loadAndObserveMessagesAdded(
        userID: String, observer: FirebaseReferenceChildObserver,
        b : ((Result<Message>) -> Unit)
    ){
        firebaseDatabaseService.attachMessagesObserver(Message::class.java, userID, observer, b)
    }

    fun loadAndObserveChat(
        userID: String, observer: FirebaseReferenceValueObserver,
        b : ((Result<Chat>) -> Unit)
    ){
        firebaseDatabaseService.attachChatObserver(Chat::class.java, userID, observer, b)
    }


}