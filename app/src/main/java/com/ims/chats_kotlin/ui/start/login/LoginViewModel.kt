package com.ims.chats_kotlin.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.ims.chats_kotlin.data.Event
import com.ims.chats_kotlin.data.Result
import com.ims.chats_kotlin.data.db.repository.AuthRepository
import com.ims.chats_kotlin.data.model.Login
import com.ims.chats_kotlin.ui.DefaultViewModel
import com.ims.chats_kotlin.util.isEmailValid
import com.ims.chats_kotlin.util.isTextValid

class LoginViewModel : DefaultViewModel() {

    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()

    val isLoggedInEvent : LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isLoggingIn = MutableLiveData<Boolean>()

    private fun login(){
        isLoggingIn.value = true
        val login = Login(emailText.value!!, passwordText.value!!)

        authRepository.loginUser(login){ result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) _isLoggedInEvent.value = Event(result.data!!)
            if (result is Result.Success || result is Result.Error) isLoggingIn.value = false
        }
    }

    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString())){
            mSnackBarText.value = Event("Invalid Email format")
            return
        }
        if (!isTextValid(6, passwordText.value)){
            mSnackBarText.value = Event("Password is too short")
            return
        }

        login()
    }
}