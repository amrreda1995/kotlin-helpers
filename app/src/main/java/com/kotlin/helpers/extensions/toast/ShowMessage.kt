package com.kotlin.helpers.extensions.toast

import android.app.Activity
import android.content.Context
import android.widget.Toast

fun Context.toast(message: String, displayLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, displayLength).show()
}

fun Context.toast(messageId: Int, displayLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, this.getString(messageId), displayLength).show()
}

fun String.showInToast(activity: Activity, displayLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, this, displayLength).show()
}

fun Int.showInToast(activity: Activity, displayLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, activity.getString(this), displayLength).show()
}