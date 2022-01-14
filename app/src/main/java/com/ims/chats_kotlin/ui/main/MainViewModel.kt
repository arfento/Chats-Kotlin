package com.ims.chats_kotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ims.chats_kotlin.App
import com.ims.chats_kotlin.data.Result
import com.ims.chats_kotlin.data.db.entity.UserNotification
import com.ims.chats_kotlin.data.db.remote.FirebaseAuthStateObserver
import com.ims.chats_kotlin.data.db.remote.FirebaseReferenceConnectedObserver
import com.ims.chats_kotlin.data.db.remote.FirebaseReferenceValueObserver
import com.ims.chats_kotlin.data.db.repository.AuthRepository
import com.ims.chats_kotlin.data.db.repository.DatabaseRepository

class MainViewModel : ViewModel() {

    private val dbRepository = DatabaseRepository()
    private val authRepository = AuthRepository()

    private val _userNotificationList = MutableLiveData<MutableList<UserNotification>>()

    private val fbRefNotificationObserver = FirebaseReferenceValueObserver()
    private val fbAuthStateObserver = FirebaseAuthStateObserver()
    private val fbRefConnectedObserver = FirebaseReferenceConnectedObserver()
    private var userID = App.mvUserID

    var userNotificationList: LiveData<MutableList<UserNotification>> = _userNotificationList

    init {
        setupAuthObserver()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefNotificationObserver.clear()
        fbRefConnectedObserver.clear()
        fbAuthStateObserver.clear()
    }

    private fun setupAuthObserver() {
        authRepository.observeAuthState(fbAuthStateObserver) { result: Result<FirebaseUser> ->
            if (result is Result.Success) {
                userID = result.data!!.uid
                startObservingNotifications()
                fbRefConnectedObserver.start(userID)
            } else {
                fbRefConnectedObserver.clear()
                stopObservingNotifications()
            }

        }
    }

    private fun stopObservingNotifications() {
        dbRepository.loadAndObserveUserNotifications(
            userID,
            fbRefNotificationObserver
        ) { result: Result<MutableList<UserNotification>> ->
            if (result is Result.Success) {
                _userNotificationList.value = result.data
            }

        }

    }

    private fun startObservingNotifications() {
        fbRefNotificationObserver.clear()

    }
}