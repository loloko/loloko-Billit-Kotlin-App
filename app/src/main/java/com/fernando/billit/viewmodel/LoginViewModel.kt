package com.fernando.billit.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
import com.google.firebase.auth.AuthCredential
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
        sessionManager.authenticate(AuthResource.Loading)

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            // Sign in
            var value = authRepository.signInWithEmail(email, password)

            if (value is AuthResource.Authenticated) {

                // Get user in the database to use in the session
                val snapshot = authRepository.getUserById(value.data!!.id)
                // User exist in database
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(UserModel::class.java)

                    value = AuthResource.Authenticated(user)
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
        sessionManager.authenticate(AuthResource.Error(string))
    }

    fun signInWithCredentials(credentials: AuthCredential) {

        // Display loading message
        sessionManager.authenticate(AuthResource.Loading)

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            var value = authRepository.signInWithCredentials(credentials)

            if (value is AuthResource.Authenticated) {
                val snapshot = authRepository.getUserById(value.data?.id!!)

                // If User exist in database, Get it to put in the session
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(UserModel::class.java)

                    value = AuthResource.Authenticated(user)
                } else {
                    // If it is the first time, it will insert the user data in the database
                    if (!authRepository.insertUserFirebaseDatabase(value.data!!))
                        value = AuthResource.Error(R.string.error_sign_in)
                }
            }

            setValueToMainThread(value)
        }
    }

    fun isUserAlreadySignIn() {

        // Execute in background
        CoroutineScope(Dispatchers.IO).launch {
            var user = authRepository.isUserAlreadySignIn()

            // If user exist, fetch info from database
            if (user != null) {
                val snapshot = authRepository.getUserById(user.id)

                // If User exist in database, Get it to put in the session
                if (snapshot != null && snapshot.exists()) {
                    user = snapshot.toObject(UserModel::class.java)

                    setValueToMainThread(AuthResource.Authenticated(user))
                }
            }
        }
    }

}