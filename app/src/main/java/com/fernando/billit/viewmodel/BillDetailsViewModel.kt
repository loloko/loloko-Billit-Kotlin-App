package com.fernando.billit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.model.FriendModel
import com.fernando.billit.repository.FriendRepository
import com.fernando.billit.util.ResultResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BillDetailsViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var friendRepository: FriendRepository

    @Inject
    lateinit var sessionManager: SessionManager

    var friendListResult = MutableLiveData<List<FriendModel>>()

    fun friendListObserver(): LiveData<List<FriendModel>> = friendListResult
}