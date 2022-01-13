package com.ims.chats_kotlin.data.db.repository

import android.net.Uri
import com.ims.chats_kotlin.data.db.remote.FirebaseStorageSource
import com.ims.chats_kotlin.data.Result
class StorageRepository {
    private val firebaseStorageService = FirebaseStorageSource()

    fun updateUserProfileImage(userID : String, byteArray: ByteArray, b : (Result<Uri>) -> Unit){
        b.invoke(Result.Loading)
        firebaseStorageService.uploadUserImage(userID, byteArray)
            .addOnSuccessListener {
                b.invoke(Result.Success(it))
            }.addOnFailureListener {
                b.invoke(Result.Error(it.message))
            }
    }
}