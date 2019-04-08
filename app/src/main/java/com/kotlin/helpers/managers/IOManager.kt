package com.kotlin.helpers.managers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import java.io.*

interface IOManagerInterface {
    fun writeToFile(data: String?, file: String)
    fun appendToFile(data: String?, file: String)
    fun readFromFile(file: String): String
    fun checkDirectoryExistence(directoryPath: String): Boolean
    fun encode(bitmap: Bitmap, fileName: String): String
    fun decode(imageView: ImageView, fileName: String): Bitmap?
}

class IOManager(private val context: Context) : IOManagerInterface {

    override fun writeToFile(data: String?, file: String) {
        val fileName = trimFileName(file)
        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
            if (data != null) {
                outputStreamWriter.write(data)
            }
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }

        checkDirectoryExistence(fileName)
    }

    override fun appendToFile(data: String?, file: String) {
        val fileName = trimFileName(file)
        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND))
            if (data != null) {
                outputStreamWriter.write(data)
            }
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }

        checkDirectoryExistence(fileName)
    }

    override fun readFromFile(file: String): String {
        val fileName = trimFileName(file)
        checkDirectoryExistence(fileName)
        var data = ""
        try {
            val inputStream = context.openFileInput(fileName)

            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString = ""
                val stringBuilder = StringBuilder()

                receiveString = bufferedReader.readLine()
                while (receiveString != null) {
                    stringBuilder.append(receiveString)
                }

                inputStream.close()
                data = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }

        return data
    }

    override fun checkDirectoryExistence(directoryPath: String): Boolean {
        return File(directoryPath).exists()
    }

    override fun encode(bitmap: Bitmap, fileName: String): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded = "" + Base64.encodeToString(byteArray, Base64.DEFAULT)
        writeToFile(encoded, fileName)
        return encoded
    }

    override fun decode(imageView: ImageView, fileName: String): Bitmap? {
        return try {
            val encodedString = readFromFile(trimFileName(fileName))
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            imageView.setImageBitmap(bitmap)
            bitmap
        } catch (e: Exception) {
            e.message
            null
        }

    }

    private fun trimFileName(file: String): String {
        val stringList = file.split("\\$|\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return stringList[stringList.size - 1]
    }
}