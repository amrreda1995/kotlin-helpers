package com.kotlin.helpers.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * These extensions help you to load an image into ImageView easily using Glide library
 * All you need to use your image view anywhere and call any extension of them like this:
 * youImageView.load(yourURL)
 * @Note that you can load an image using URL, URI or drawableResourceId
 * */

enum class ImageViewMode {
    NORMAL, CENTER_CROP, FIT_CENTER
}

fun ImageView.load(url: String, mode: ImageViewMode = ImageViewMode.NORMAL) {
    val load = Glide.with(context).load(url)
    when (mode) {
        ImageViewMode.NORMAL -> load.into(this)
        ImageViewMode.CENTER_CROP -> load.centerCrop().into(this)
        ImageViewMode.FIT_CENTER -> load.fitCenter().into(this)
    }
}

fun ImageView.load(uri: Uri, mode: ImageViewMode = ImageViewMode.NORMAL) {
    val load = Glide.with(context).load(uri)
    when (mode) {
        ImageViewMode.NORMAL -> load.into(this)
        ImageViewMode.CENTER_CROP -> load.centerCrop().into(this)
        ImageViewMode.FIT_CENTER -> load.fitCenter().into(this)
    }
}

fun ImageView.load(resourceId: Int, mode: ImageViewMode = ImageViewMode.NORMAL) {
    val load = Glide.with(context).load(resourceId)
    when (mode) {
        ImageViewMode.NORMAL -> load.into(this)
        ImageViewMode.CENTER_CROP -> load.centerCrop().into(this)
        ImageViewMode.FIT_CENTER -> load.fitCenter().into(this)
    }
}