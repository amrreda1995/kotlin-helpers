package com.kotlin.helpers.extensions

import android.graphics.Typeface
import android.view.View
import android.widget.TextView

/**
 * By this extension you can set you custom font easily like this
 * yourTextView.setFont("Fonts/Custom_Font.ttf")
 * */

fun TextView.setFont(fontPath: String) {
    val typeFace = Typeface.createFromAsset(this.context.assets, fontPath)
    this.typeface = typeFace
}