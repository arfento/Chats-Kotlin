package com.ims.chats_kotlin.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ims.chats_kotlin.data.Event

class StartViewModel : ViewModel(){

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    private val _createAccountEvent = MutableLiveData<Event<Unit>>()

    val loginEvent : LiveData<Event<Unit>> = _loginEvent
    val createAcountEvent : LiveData<Event<Unit>> = _createAccountEvent

    fun goToLoginPressed(){
        _loginEvent.value = Event(Unit)
    }

    fun goToCreateAccountPressed(){
        _createAccountEvent.value = Event(Unit)
    }
}