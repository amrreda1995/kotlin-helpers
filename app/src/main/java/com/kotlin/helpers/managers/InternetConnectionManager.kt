package com.kotlin.helpers.managers

import android.content.Context
import android.net.ConnectivityManager

/**
 * @InternetConnectionManager class helps you check for your internet connection
 * */

interface InternetConnectionManagerInterface {
    val isConnectedToInternet: Boolean
}

class InternetConnectionManager(val context: Context) : InternetConnectionManagerInterface {

    override val isConnectedToInternet: Boolean
        get() {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.activeNetworkInfo

            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
}