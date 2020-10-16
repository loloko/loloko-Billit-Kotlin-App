package com.fernando.billit.viewmodel

import androidx.lifecycle.ViewModel
import com.fernando.billit.repository.AuthRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

}