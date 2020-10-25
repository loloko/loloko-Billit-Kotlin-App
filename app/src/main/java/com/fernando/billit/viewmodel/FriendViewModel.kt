package com.fernando.billit.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.model.FriendModel
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.FriendRepository
import com.fernando.billit.util.AuthResource
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
            setError(R.string.required_name)
            return
        }

        // Display loading message
        _friendResult.value = Resource.loading()

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            // Insert new friend
            var isInserted = friendRepository.insertFriendDatabase(sessionManager.getCurrentUser().id, friend)

            // Fetch as friends
            if (isInserted) {
                getAllFriends()
            } else
                setValueToMainThread(Resource.error(R.string.error_sign_in))
        }
    }

    private fun geeee() {

    }

    fun getAllFriends() {

        // Display loading message
        _friendResult.value = Resource.loading()

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            // Insert new friend
            val query = friendRepository.getAllFriends(sessionManager.getCurrentUser().id)

            if (query != null) {
                val list = query.toObjects(FriendModel::class.java)

                setValueToMainThread(Resource.success(list))
            } else
                setValueToMainThread(Resource.success(null))
        }
    }

    // Set value in the Main Thread
    private suspend fun setValueToMainThread(value: Resource<List<FriendModel>>) {
        withContext(Dispatchers.Main) {
            _friendResult.value = value
        }
    }

    private fun setError(@StringRes string: Int) {
        _friendResult.value = Resource.error(string)
    }

}