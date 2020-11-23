package com.fernando.billit.util

import androidx.annotation.StringRes


sealed class AuthResource<out T> {
    data class Authenticated<out T>(val data: T?) : AuthResource<T>()

    data class Error(@StringRes val message: Int) : AuthResource<Nothing>()

    object NotAuthenticated : AuthResource<Nothing>()

    object Loading : AuthResource<Nothing>()

    object Logout : AuthResource<Nothing>()

    object ResetPassword : AuthResource<Nothing>()
}

