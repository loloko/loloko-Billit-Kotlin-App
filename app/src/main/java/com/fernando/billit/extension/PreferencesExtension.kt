package com.fernando.billit.extension

import android.annotation.SuppressLint
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


@SuppressLint("ApplySharedPref")
fun String.save(applicationContext: Context, value: Map<String, Any>, clear: Boolean = false, now: Boolean = false) {
    val sp = applicationContext.getSharedPreferences(this, Context.MODE_PRIVATE).edit()
    if (clear)
        sp.clear()
    value.keys.forEach { key ->
        val v = value[key]
        if (v != null) {
            when (v) {
                is String -> sp.putString(key, v)
                is Float -> sp.putFloat(key, v)
                is Long -> sp.putLong(key, v)
                is Int -> sp.putInt(key, v)
                is Boolean -> sp.putBoolean(key, v)
            }
        }
    }
    if (now)
        sp.commit()
    else
        sp.apply()
}

fun String.load(applicationContext: Context): Map<String, Any> {
    val sp = applicationContext.getSharedPreferences(this, Context.MODE_PRIVATE)
    val keys = sp.all.keys
    val result = hashMapOf<String, Any>()
    keys.map { key ->
        val v = sp.all[key]
        if (v != null)
            result[key] = v
    }
    return result
}
