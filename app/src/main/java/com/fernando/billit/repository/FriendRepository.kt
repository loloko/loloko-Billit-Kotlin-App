package com.fernando.billit.repository

import com.fernando.billit.R
import com.fernando.billit.model.FriendModel
import com.fernando.billit.util.FirebaseConstants
import com.fernando.billit.util.ResultResource
import com.google.firebase.firestore.FirebaseFirestore
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

    suspend fun getAllFriends(userId: String): ResultResource<List<FriendModel>> {
        return try {

            val query = database.collection(FirebaseConstants.USER.USERS)
                .document(userId)
                .collection(FirebaseConstants.USER.FRIENDS)
                .get()
                .await()

            if (query != null)
                ResultResource.Success(query.toObjects(FriendModel::class.java))
            else
                ResultResource.Success(null)

        } catch (e: Exception) {
            ResultResource.Error(R.string.no_friend_found)
        }
    }

    suspend fun deleteFriend(userId: String, friendId: String): Boolean {

        return try {
            database.collection(FirebaseConstants.USER.USERS)
                .document(userId)
                .collection(FirebaseConstants.USER.FRIENDS)
                .document(friendId)
                .delete()
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

}