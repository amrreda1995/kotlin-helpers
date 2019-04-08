package com.kotlin.helpers.managers

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

interface ScreenShotManagerInterface {
    fun getScreenShotBitmap(): Bitmap
    fun saveScreenShot(): String
    fun saveScreenShotToSpecificPath(folder: String): Boolean
}

class ScreenShotManager(
    private val context: Context,
    private val view: View
) : ScreenShotManagerInterface {
    private val internalStoragePicturesPath = "storage/emulated/0/Pictures/"

    override fun getScreenShotBitmap(): Bitmap {
        view.isDrawingCacheEnabled = true

        return view.drawingCache
    }

    override fun saveScreenShot(): String {
        val bitmap = getScreenShotBitmap()
        //to store in pictures folder directly
        return MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Screen", "screen")
    }

    override fun saveScreenShotToSpecificPath(folder: String): Boolean {
        val bitmap = getScreenShotBitmap()

        val directory = File(internalStoragePicturesPath + folder)
        if (!directory.exists()) {
            directory.mkdir()
        }

        val fullImagePath = File(directory, System.currentTimeMillis().toString() + ".png")

        val fos: FileOutputStream?

        return try {
            fos = FileOutputStream(fullImagePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            true
        } catch (e: FileNotFoundException) {
            false
        } catch (e: Exception) {
            false
        }
    }

}