package com.kotlin.helpers.managers.location

import android.content.Context
import android.location.LocationManager

/**
 * @LocationFacadeManager class was written in Facade design pattern to provide different tasks which are related to Location
 * task like getting a reference of locationServicesManager, fullAddressManager and userLocationManager
 * */

interface LocationFacadeManagerInterface {

    val locationServicesManager: LocationManager

    val userLocationManager: UserLocationManager

    val fullAddressManager: FullAddressManager
}

class LocationFacadeManager(val context: Context) : LocationFacadeManagerInterface {

    override val locationServicesManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override val userLocationManager = UserLocationManager(context)

    override val fullAddressManager = FullAddressManager(context)
}