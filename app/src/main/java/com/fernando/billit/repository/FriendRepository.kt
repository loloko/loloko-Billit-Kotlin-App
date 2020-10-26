package com.fernando.billit.repository

import com.fernando.billit.model.FriendModel
import com.fernando.billit.util.FirebaseConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FriendRepository @Inject constructor(private val database: FirebaseFirestore) {

    suspend fun insertFriendDatabase(userId: String, friend: FriendModel): Boolean {

        return try {
            database.collection(FirebaseConstants.USER.USERS)
                .document(userId)
                .collection(FirebaseConstants.USER.FRIENDS)
                .document(friend.id)
                .set(friend.convertToHashMap())
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllFriends(userId: String): QuerySnapshot? {

        return try {
            database.collection(FirebaseConstants.USER.USERS)
                .document(userId)
                .collection(FirebaseConstants.USER.FRIENDS)
                .get()
                .await()

        } catch (e: Exception) {
            null
        }
    }

}