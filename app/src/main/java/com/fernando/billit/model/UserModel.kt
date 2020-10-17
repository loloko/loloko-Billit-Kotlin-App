package com.fernando.billit.model

import com.google.firebase.database.Exclude

data class UserModel(
    var id: String = ""
) {

    var name: String = ""
    var email: String = ""
    var photo: String = ""

    private var _password: String = ""
    private var _retryPassword: String = ""

    var password: String
        @Exclude get() {
            return _password
        }
        set(value) {
            _password = value
        }

    var retryPassword: String
        @Exclude get() {
            return _retryPassword
        }
        set(value) {
            _retryPassword = value
        }

}