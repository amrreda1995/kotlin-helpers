package com.kotlin.helpers.managers.searching.files

import android.content.Context
import androidx.core.content.ContextCompat
import com.kotlin.helpers.managers.searching.files.data.classes.DeviceFile
import java.io.File
import java.util.*

enum class FileType(val type: String) {
    PDF(".pdf")
}

interface SearchingForDeviceFilesManagerInterface {
    fun getAllFiles(fileType: FileType): ArrayList<DeviceFile>
    fun getAllFilesAtDirectory(fileType: FileType, directory: File)
}

class SearchingForDeviceFilesManager(
    private val context: Context
) : SearchingForDeviceFilesManagerInterface {
    private val deviceFiles = ArrayList<DeviceFile>()

    override fun getAllFiles(fileType: FileType): ArrayList<DeviceFile> {
        deviceFiles.clear()

        val files = ContextCompat.getExternalFilesDirs(context, null)
        files.forEach {
            getAllFilesAtDirectory(
                fileType,
                File(
                    it.absolutePath
                        .replace("/Android/data/", "")
                        .replace("${context.packageName}/files", "")
                )
            )
        }

        return deviceFiles
    }

    override fun getAllFilesAtDirectory(fileType: FileType, directory: File) {
        val files = directory.listFiles()

        files.filterNotNull().forEach {

            if (it.isDirectory) {  // it is a folder...
                getAllFilesAtDirectory(fileType, it)
            } else {  // it is a file...
                if (it.name.endsWith(FileType.PDF.type)) {
                    deviceFiles.add(
                        DeviceFile(
                            it.name,
                            it.absolutePath
                        )
                    )
                }
            }

        }
    }
}

//        for (f in getExternalFilesDirs("/"))
//            if (Environment.isExternalStorageRemovable(f))
//                Log.e("path", f.parentFile.parentFile.parentFile.parent)

//        for (f in getExternalFilesDirs("/"))
//                Log.e("path", f.parentFile.parentFile.parentFile.parent)