package com.fernando.billit.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
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
                if (snapshot != null) {
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

}