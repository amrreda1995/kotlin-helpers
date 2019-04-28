package com.kotlin.helpers.extensions

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}