package com.kotlin.helpers.managers.location

import android.content.Context
import android.location.LocationManager

interface LocationFacadeManagerInterface {
    fun getLocationServicesManager(): LocationManager
    fun getUserLocationManager(): UserLocationManagerInterface
    fun getFullAddressManager(): FullAddressManagerInterface
}

class LocationFacadeManager(context: Context) : LocationFacadeManagerInterface {

    private val locationServicesManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val userLocationManager = UserLocationManager(context)

    private val fullAddressManager = FullAddressManager(context)

    override fun getLocationServicesManager(): LocationManager {
        return locationServicesManager
    }

    override fun getUserLocationManager(): UserLocationManagerInterface {
        return userLocationManager
    }

    override fun getFullAddressManager(): FullAddressManagerInterface {
        return fullAddressManager
    }
}