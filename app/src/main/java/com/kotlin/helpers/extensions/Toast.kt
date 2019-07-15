package com.kotlin.helpers.extensions

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

/**
 * Writing Toast line code is boring somehow, so you can easily call toast("Hello world") in
 * you activity from anywhere you want
 * @Note: for fragments you call activity?.toast("Hello world")
 * */

fun Context.toast(message: String, displayLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, displayLength).show()
}

fun Context.toast(messageId: Int, displayLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, this.getString(messageId), displayLength).show()
}

fun String.showInToast(activity: Activity, displayLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, this, displayLength).show()
}