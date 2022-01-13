package com.ims.chats_kotlin.data.db.repository

import com.google.firebase.auth.FirebaseUser
import com.ims.chats_kotlin.data.Result
import com.ims.chats_kotlin.data.db.remote.FirebaseAuthSource
import com.ims.chats_kotlin.data.db.remote.FirebaseAuthStateObserver
import com.ims.chats_kotlin.data.model.CreateUser
import com.ims.chats_kotlin.data.model.Login

class AuthRepository {
    private val firebaseAuthService = FirebaseAuthSource()

    fun observeAuthState(
        stateObserver: FirebaseAuthStateObserver,
        b: ((Result<FirebaseUser>) -> Unit)
    ) {
        firebaseAuthService.attachAuthStateObserver(stateObserver, b)
    }

    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.loginWithEmailAndPassword(login)
            .addOnSuccessListener {
                b.invoke(Result.Success(it.user))
            }.addOnFailureListener {
                b.invoke(Result.Error(msg = it.message))
            }
    }

    fun createUser(createUser: CreateUser, b: ((Result<FirebaseUser>) -> Unit)){
        b.invoke(Result.Loading)
        firebaseAuthService.createUser(createUser)
            .addOnSuccessListener {
                b.invoke(Result.Success(it.user))
            }.addOnFailureListener{
                b.invoke(Result.Error(msg = it.message))
            }
    }

    fun logoutUser(){
        firebaseAuthService.logout()
    }
}