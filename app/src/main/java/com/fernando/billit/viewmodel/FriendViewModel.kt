package com.fernando.billit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.model.FriendModel
import com.fernando.billit.repository.FriendRepository
import javax.inject.Inject

class FriendViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var friendRepository: FriendRepository

    private var _friendResult = MediatorLiveData<List<FriendModel>>()

    fun friendResultObserver(): LiveData<List<FriendModel>> = _friendResult

}