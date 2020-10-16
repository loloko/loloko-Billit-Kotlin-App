package com.fernando.billit.viewmodel

import androidx.lifecycle.ViewModel
import com.fernando.billit.repository.AuthRepository
import javax.inject.Inject

class RegisterViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    fun registerUser() {
        authRepository.registerUserWithEmail("fernandocris@outlook.com", "1234567890")
    }

}