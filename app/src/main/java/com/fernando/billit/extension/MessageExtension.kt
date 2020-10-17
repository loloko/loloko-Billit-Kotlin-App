package com.fernando.billit.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fernando.billit.R
import com.google.firebase.database.DatabaseReference
import kotlin.math.roundToInt

fun Context.toastMessage(
    @StringRes stringRef: Int? = null,
    text: String? = null,
    isWarning: Boolean = false
) {

    val toast = Toast(this)
    val view = inflate(R.layout.toast_custom)

    val textView = view.findViewById<TextView>(R.id.toast_message)
    textView.text = text ?: this.getString(stringRef!!)

    if (isWarning)
        view.setBackgroundColor(this.getColor(R.color.billit_red))

    toast.view = view
    toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
    toast.duration = Toast.LENGTH_LONG

    toast.show()
}

fun Context.createLoadingPopup(@StringRes msg: Int? = null): AlertDialog {
    val builder = AlertDialog.Builder(this)
    val view: View = inflate(R.layout.dialog_loading)

    val text = view.findViewById<TextView>(R.id.tv_dialog_msg)
    text.setText(msg ?: randomLoadingMessage())
    builder.setView(view)
    builder.setCancelable(false)
    val dialog = builder.create()
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return dialog
}

fun Context.createBasicDialog(@StringRes stringRef: Int) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(stringRef)
    builder.setCancelable(false)
    builder.setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
    builder.create().show()
}

fun Context.createDeletePopup(dbRef: DatabaseReference) {
    val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
    builder.setTitle(R.string.attention)
    builder.setMessage(R.string.delete_firebase)
    builder.setCancelable(false)
    builder.setPositiveButton(R.string.cancel) { dialog, _ -> dialog!!.dismiss() }
    builder.setNegativeButton(R.string.delete) { dialog, _ ->

        dbRef.removeValue()

        dialog.dismiss()
    }

    builder.create().show()
}

fun Activity.createFinishDialog(@StringRes msg: Int) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(msg)
    builder.setCancelable(false)
    builder.setPositiveButton(R.string.ok) { _, _ -> this.finish() }
    builder.create().show()
}

private fun randomLoadingMessage(): Int {
    val lines = intArrayOf(
        R.string.loading_1, R.string.loading_2,
        R.string.loading_4, R.string.loading_4,
        R.string.loading_5, R.string.loading_6,
        R.string.loading_7
    )
    return lines[Math.toIntExact((Math.random() * (lines.size - 1)).roundToInt().toLong())]
}


