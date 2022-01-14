package com.ims.chats_kotlin.ui.start.createAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ims.chats_kotlin.data.Event
import com.ims.chats_kotlin.data.Result
import com.ims.chats_kotlin.data.db.entity.User
import com.ims.chats_kotlin.data.db.repository.AuthRepository
import com.ims.chats_kotlin.data.db.repository.DatabaseRepository
import com.ims.chats_kotlin.data.model.CreateUser
import com.ims.chats_kotlin.ui.DefaultViewModel
import com.ims.chats_kotlin.util.isEmailValid
import com.ims.chats_kotlin.util.isTextValid

class CreateAccountViewModel : DefaultViewModel() {

    private val dbRespository = DatabaseRepository()
    private val authRepository = AuthRepository()
    private val mIsCreatedEvent = MutableLiveData<Event<FirebaseUser>>()

    val isCreatedEvent: LiveData<Event<FirebaseUser>> = mIsCreatedEvent
    val displayNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser =
            CreateUser(displayNameText.value!!, emailText.value!!, passwordText.value!!)

        authRepository.createUser(createUser) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success){
                mIsCreatedEvent.value = Event(result.data!!)
                dbRespository.updateNewUser(User().apply {
                    info.id = result.data.uid
                    info.displayName = createUser.displayName
                })
            }
            if (result is Result.Success || result is Result.Error) isCreatingAccount.value = false
        }
    }

    fun createAccountPressed(){
        if (!isTextValid(2, displayNameText.value)) {
            mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        createAccount()
    }
}