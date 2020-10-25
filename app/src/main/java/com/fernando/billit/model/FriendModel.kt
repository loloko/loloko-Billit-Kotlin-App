package com.fernando.billit.model


data class FriendModel(
    var id: String = "",
    var name: String = "",
    var amount: Double = 0.0
) {

    var hasChanged: Boolean = false


    fun convertToHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "amount" to amount
        )

    }

}