package com.fernando.billit.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
import com.fernando.billit.SessionManager
import com.fernando.billit.extension.codeToBase64
import com.fernando.billit.extension.isEmailValid
import com.fernando.billit.model.UserModel
import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.util.AuthResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var sessionManager: SessionManager

    val user = UserModel()

    fun userResultObserver(): LiveData<AuthResource<UserModel>> = sessionManager.getAuthUser()

    fun registerUser() {

        if (user.name.isEmpty()) {
            setError(R.string.required_name)
            return
        }
        if (user.email.isEmpty()) {
            setError(R.string.required_email)
            return
        }
        if (!user.email.isEmailValid()) {
            setError(R.string.invalid_email)
            return
        }
        if (user.password.isEmpty()) {
            setError(R.string.required_password)
            return
        }
        if (user.retryPassword.isEmpty()) {
            setError(R.string.required_confirm_password)
            return
        }
        if (user.password != user.retryPassword) {
            setError(R.string.password_not_match)
            return
        }

        // Display loading message
        sessionManager.authenticate(AuthResource.Loading)


        // Create user ID by converting the email to Base64
        user.id = user.email.codeToBase64()

        // Execute in background
        CoroutineScope(IO).launch {
            val value = authRepository.registerUserWithEmail(user)

            if (value is AuthResource.Authenticated)
                authRepository.insertUserFirebaseDatabase(user)


            setValueToMainThread(value)
        }
    }

    // Set value in the Main Thread
    private suspend fun setValueToMainThread(value: AuthResource<UserModel>) {
        withContext(Main) {
            sessionManager.authenticate(value)
        }
    }

    private fun setError(@StringRes string: Int) {
        sessionManager.authenticate(AuthResource.Error(string))
    }

}