package com.fernando.billit.model

data class BillModel(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val amount: Double = 0.0,
    val currency: String = "",
    val access_code: String = "",
    val owner_id: String = "",
    val owner_name: String = "",
    val debts: MutableList<DebitPersonModel>? = null,
    val activities: MutableList<ActivitiesModel>? = null,
    val images: MutableList<String>? = null,
    val friend_string: String = "",
    val friends: MutableList<FriendModel> = ArrayList()
) {

    var isExpandable: Boolean = false
    var isShareCode: Boolean = false
    var searchString: String = ""


    fun convertToHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "title" to title,
            "date" to date,
            "amount" to amount,
            "currency" to currency,
            "access_code" to access_code,
            "owner_id" to owner_id,
            "owner_name" to owner_name,
            //"debts" to debts,
            //"activities" to access_code,
            //"images" to access_code,
            "friend_string" to friend_string
            //"friends" to access_code
        )
    }


}