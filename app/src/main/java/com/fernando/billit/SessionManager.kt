package com.fernando.billit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fernando.billit.extension.TAG
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.AuthResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val cachedUser: MutableLiveData<AuthResource<UserModel>> = MutableLiveData()

    fun authenticate(source: AuthResource<UserModel>) {
        cachedUser.value = source
    }

    fun logOut() {
        Log.d(TAG, "logOut: logging out...")
        cachedUser!!.value = AuthResource.logout()
    }

    fun getAuthUser(): LiveData<AuthResource<UserModel>> {
        return cachedUser
    }

    // Always will have an user
    fun getCurrentUser(): UserModel {
        val user = cachedUser.value?.data

        return user!!
    }

}

