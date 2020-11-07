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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FriendModel

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}