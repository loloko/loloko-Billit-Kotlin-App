package com.fernando.billit.model

data class UserModel(
    var id: String = ""
) {

    var name: String = ""
    var email: String = ""
    var photo: String = ""

    var password: String = ""
    var retryPassword: String = ""


    fun convertToHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "photo" to photo
        )
    }

}