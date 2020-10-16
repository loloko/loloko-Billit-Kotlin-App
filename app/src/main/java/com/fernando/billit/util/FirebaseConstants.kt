package com.fernando.billit.util

object FirebaseConstants {

    object USER {
        const val USERS = "users"
        const val FRIENDS = "friends"
    }

    object BILL {
        const val BILLS = "bills"
        const val DEBTS = "debts"
        const val FRIENDS = "friends"
        const val IMAGES = "images"
        const val OWNER_ID = "owner_id"
        const val AMOUNT = "amount"
        const val ACTIVITIES = "activities"
        const val SHARE_CODE = "access_code"
    }

    object LOG {
        const val CREATE_EDIT = 1
        const val ATTACH_IMAGE = 2
        const val PAID_IN_BILL = 3
        const val PAY_DEBT = 4
    }

    object STORAGE {
        const val IMAGES = "images"
        const val BILLS = "bills"
        const val PROFILE = "profile"
    }

}