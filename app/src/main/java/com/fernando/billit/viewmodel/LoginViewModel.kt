package com.fernando.billit.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.extension.TAG
import com.fernando.billit.extension.codeToBase64
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.firestore.DocumentSnapshot
import com.squareup.okhttp.internal.DiskLruCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var sessionManager: SessionManager

    fun userResultObserver(): LiveData<AuthResource<UserModel>> = sessionManager.getAuthUser()

    fun signInWithEmail(email: String, password: String) {

        if (email.isEmpty()) {
            setError(R.string.required_email)
            return
        }
        if (password.isEmpty()) {
            setError(R.string.required_password)
            return
        }

        // Display loading message
        sessionManager.authenticate(AuthResource.loading())


        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            // Sign in
            var value = authRepository.signInWithEmail(email, password)

            if (value.status == AuthResource.AuthStatus.AUTHENTICATED) {

                // Get user in the database to use in the session
                val snapshot = authRepository.getUserById(value.data!!.id)
                // User exist in database
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(UserModel::class.java)

                    value = AuthResource.authenticated(user)
                }
            }

            setValueToMainThread(value)
        }
    }

    // Set value in the Main Thread
    private suspend fun setValueToMainThread(value: AuthResource<UserModel>) {
        withContext(Dispatchers.Main) {
            sessionManager.authenticate(value)
        }
    }

    private fun setError(@StringRes string: Int) {
        sessionManager.authenticate(AuthResource.error(string))
    }

    fun signInWithFacebook(credentials: AuthCredential) {

        // Display loading message
        sessionManager.authenticate(AuthResource.loading())

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            var value = authRepository.signInWithCredentials(credentials)

            if (value.status == AuthResource.AuthStatus.AUTHENTICATED) {
                val snapshot = authRepository.getUserById(value.data?.id!!)

                // If User exist in database, Get it to put in the session
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(UserModel::class.java)

                    value = AuthResource.authenticated(user)
                } else {
                    // If it is the first time, it will insert the user data in the database
                    if (!authRepository.insertUserFirebaseDatabase(value.data!!))
                        value = AuthResource.error(R.string.error_sign_in)
                }
            }

            setValueToMainThread(value)
        }
    }

}