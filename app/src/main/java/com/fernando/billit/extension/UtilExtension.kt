package com.fernando.billit.extension

import android.app.ActionBar
import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.fernando.billit.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.codeToBase64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)
        .replace("([\\n\\r])".toRegex(), "")
}

fun Double.formatDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}

fun Context.createDivider(): View {
    val v = View(this)
    v.layoutParams = LinearLayout.LayoutParams(
        ActionBar.LayoutParams.MATCH_PARENT, 3
    )
    v.setBackgroundColor(getColor(R.color.grey))

    return v
}

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun getCurrentDate(): String {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
}

fun getCurrentTime(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Context.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}