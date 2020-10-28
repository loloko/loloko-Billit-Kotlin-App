package com.fernando.billit.util

import androidx.annotation.StringRes

sealed class ResultResource<out T> {
    data class Success<out T>(val data: T?) : ResultResource<T>() // Status success and data of the result
    data class Error(@StringRes val msg: Int) : ResultResource<Nothing>() // Status Error an error message
    object Loading : ResultResource<Nothing>() // Status to display loading popup

}