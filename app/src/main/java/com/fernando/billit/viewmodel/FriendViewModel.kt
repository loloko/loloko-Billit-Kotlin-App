package com.fernando.billit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.model.FriendModel
import com.fernando.billit.repository.FriendRepository
import com.fernando.billit.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FriendViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var friendRepository: FriendRepository

    @Inject
    lateinit var sessionManager: SessionManager

    private var _friendResult = MutableLiveData<Resource<List<FriendModel>>>()

    fun friendResultObserver(): LiveData<Resource<List<FriendModel>>> = _friendResult

    fun addFriend(friend: FriendModel) {

        if (friend.name.isEmpty()) {
            _friendResult.value = Resource.error(R.string.required_name)
            return
        }

        // Display loading message
        _friendResult.value = Resource.loading()

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            // Insert new friend
            val isInserted = friendRepository.insertFriendDatabase(sessionManager.getCurrentUser().id, friend)

            // Fetch as friends
            if (isInserted)
                getFriends()
            else
                setValueToMainThread(Resource.error(R.string.error_add_friend))
        }
    }

    fun getAllFriends() {

        // Display loading message
        _friendResult.value = Resource.loading()

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {

            getFriends()
        }
    }

    fun deleteFriend(friendId: String) {

        // Display loading message
        _friendResult.value = Resource.loading()

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            // Delete friend
            val isDeleted = friendRepository.deleteFriend(sessionManager.getCurrentUser().id, friendId)

            if (isDeleted)
                getFriends()
            else
                setValueToMainThread(Resource.error(R.string.error_delete_friend))
        }
    }

    private suspend fun getFriends() {
        val query = friendRepository.getAllFriends(sessionManager.getCurrentUser().id)

        if (query != null) {
            val list = query.toObjects(FriendModel::class.java)

            setValueToMainThread(Resource.success(list))
        } else
            setValueToMainThread(Resource.success(null))
    }

    // Set value in the Main Thread
    private suspend fun setValueToMainThread(value: Resource<List<FriendModel>>) {
        withContext(Dispatchers.Main) {
            _friendResult.value = value
        }
    }

}