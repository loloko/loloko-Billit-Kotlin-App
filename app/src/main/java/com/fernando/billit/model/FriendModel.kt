package com.fernando.billit.model

import java.io.Serializable


data class FriendModel(
    var id: String = "",
    var name: String = "",
    var amount: Double = 0.0
) : Serializable {

    var hasChanged: Boolean = false


    fun convertToHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "amount" to amount
        )

    }

}