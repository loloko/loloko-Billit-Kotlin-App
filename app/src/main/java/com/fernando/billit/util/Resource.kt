package com.fernando.billit.util

import androidx.annotation.StringRes

class Resource<T>(val status: Status, val data: T?, val message: Int?) {
    enum class Status {
        ERROR, LOADING, SUCCESS
    }

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(@StringRes msg: Int): Resource<T> {
            return Resource(Status.ERROR, null, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

    }

}