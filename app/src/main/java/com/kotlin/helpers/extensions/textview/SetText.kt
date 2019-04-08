package com.kotlin.helpers.extensions.textview

import android.view.View
import android.widget.TextView

fun TextView.text(text: String) {
    this.visibility = View.VISIBLE
    this.text = text
}