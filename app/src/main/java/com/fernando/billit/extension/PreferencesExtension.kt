package com.fernando.billit.extension

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import kotlin.collections.HashSet

private const val PREFERENCE_NAME = "USER_PREFS"
private const val PREFERENCE_CURRENCY = "currency"
private const val PREFERENCE_SHARE_CODE = "share_code"


private fun mSharePreferences(context: Context): SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0)

fun Context.setPreferenceCurrency(value: String) {
    val editor = mSharePreferences(this).edit()
    editor.putString(PREFERENCE_CURRENCY, value)
    editor.apply()
}

fun Context.getPreferenceCurrency() {
    var currency = mSharePreferences(this).getString(PREFERENCE_CURRENCY, "")

    //if is empty, find the default cellphone currency
    if (currency.isNullOrEmpty()) {

        val defaultLocale: Locale = Locale.getDefault()
        val currencyLocale: Currency = Currency.getInstance(defaultLocale)
        currency = currencyLocale.currencyCode

        setPreferenceCurrency(currency)
    }

//    DataSingleton.mDefaultCurrency = currency!!
}

fun Context.setPreferenceShareCode(value: Set<String>) {
    val editor = mSharePreferences(this).edit()
    editor.putStringSet(PREFERENCE_SHARE_CODE, value)
    editor.apply()
}

fun Context.getPreferenceShareCode(): MutableSet<String> {
    return mSharePreferences(this).getStringSet(PREFERENCE_SHARE_CODE, HashSet()) ?: HashSet()
}
