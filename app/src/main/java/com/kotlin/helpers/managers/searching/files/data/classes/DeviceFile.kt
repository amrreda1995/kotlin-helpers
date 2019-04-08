package com.kotlin.helpers.managers.searching.files.data.classes

import android.os.Parcel
import android.os.Parcelable

class DeviceFile(var name: String?, var directory: String?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(directory)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeviceFile> {
        override fun createFromParcel(parcel: Parcel): DeviceFile {
            return DeviceFile(parcel)
        }

        override fun newArray(size: Int): Array<DeviceFile?> {
            return arrayOfNulls(size)
        }
    }
}