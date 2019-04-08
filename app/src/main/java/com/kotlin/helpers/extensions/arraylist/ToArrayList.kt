package com.kotlin.helpers.extensions.arraylist

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}