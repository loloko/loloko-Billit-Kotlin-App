package com.fernando.billit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.fernando.billit.extension.TAG
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.AuthResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val cachedUser: MediatorLiveData<AuthResource<UserModel>>? = MediatorLiveData()

    fun authenticateWithId(source: LiveData<AuthResource<UserModel>>?) {
        if (cachedUser != null) {
            cachedUser.value = AuthResource.loading()

            cachedUser.addSource(source!!, Observer { userAuthResource ->
                cachedUser.value = userAuthResource
                cachedUser.removeSource(source)

                if (userAuthResource.status == AuthResource.AuthStatus.ERROR) {
                    cachedUser.value = AuthResource.logout()
                }
            })
        }
    }

    fun logOut() {
        Log.d(TAG, "logOut: logging out...")
        cachedUser!!.value = AuthResource.logout()
    }

    fun getAuthUser(): LiveData<AuthResource<UserModel>>? {
        return cachedUser
    }

}

