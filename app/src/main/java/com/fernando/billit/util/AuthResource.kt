package com.fernando.billit.util

import androidx.annotation.StringRes

class AuthResource<T>(val status: AuthStatus, val data: T?, val message: Int?) {
    enum class AuthStatus {
        AUTHENTICATED, ERROR, LOADING, NOT_AUTHENTICATED
    }

    companion object {

        fun <T> authenticated(data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.AUTHENTICATED, data, null)
        }

        fun <T> error(@StringRes msg: Int): AuthResource<T> {
            return AuthResource(AuthStatus.ERROR, null, msg)
        }

        fun <T> loading(): AuthResource<T> {
            return AuthResource(AuthStatus.LOADING, null, null)
        }

        fun <T> logout(): AuthResource<T> {
            return AuthResource(AuthStatus.NOT_AUTHENTICATED, null, null)
        }
    }
}