package com.fernando.billit.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fernando.billit.R
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

class ForgotPasswordViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    private val _userResult = MutableLiveData<AuthResource<UserModel>>()

    fun userResultObserver(): LiveData<AuthResource<UserModel>> = _userResult

    fun sendResetPasswordEmail(email: String) {

        if (email.isEmpty()) {
            setError(R.string.required_email)
            return
        }
        if (!email.isEmailValid()) {
            setError(R.string.invalid_email)
            return
        }

        // Display loading popup
        _userResult.value = AuthResource.loading()

        // Execute in background
        CoroutineScope(IO).launch {
            val value = authRepository.sendResetPasswordEmail(email)

            setValueToMainThread(value)
        }
    }

    // Set value in the Main Thread
    private suspend fun setValueToMainThread(value: AuthResource<UserModel>) {
        withContext(Main) {
            _userResult.value = value
        }
    }

    private fun setError(@StringRes string: Int) {
        _userResult.value = AuthResource.error(string)
    }

}