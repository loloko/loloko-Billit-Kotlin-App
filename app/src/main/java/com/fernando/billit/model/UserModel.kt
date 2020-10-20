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
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = id
        hashMap["name"] = name
        hashMap["email"] = email
        hashMap["photo"] = photo

        return hashMap
    }

}