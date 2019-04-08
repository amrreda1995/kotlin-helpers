package com.kotlin.helpers.managers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

interface RuntimePermissionsManagerInterface {
    fun areAllPermissionsGranted(permissions: Array<String>): Boolean

    fun isPermissionGranted(permission: String): Boolean

    fun showRequestPermissionsDialog(permissions: Array<String>, requestCode: Int)

    fun checkRequestPermissionsResult(grantResults: IntArray?, requestCode: Int, resultRequestCode: Int): Boolean
}

class RuntimePermissionsManager(private val context: Context) : RuntimePermissionsManagerInterface {

    override fun isPermissionGranted(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

        return true
    }

    override fun areAllPermissionsGranted(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return permissions.count { context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED } == 0
        }

        return true
    }

    override fun showRequestPermissionsDialog(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(context as Activity, permissions, requestCode)
    }

    override fun checkRequestPermissionsResult(
            grantResults: IntArray?,
            requestCode: Int,
            resultRequestCode: Int
    ): Boolean {
        if (resultRequestCode == requestCode && grantResults != null) {
            return grantResults.count { it != PackageManager.PERMISSION_GRANTED } == 0
        }

        return false
    }
}