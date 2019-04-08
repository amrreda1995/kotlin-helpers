package com.kotlin.helpers.managers.location

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng

interface UserLocationManagerInterface {
    var latLong: MutableLiveData<LatLng>
    fun getCurrentLocation()
}

class UserLocationManager(
    context: Context,
    private val getUpdatesOfLocation: Boolean
) : UserLocationManagerInterface,
    LocationCallback() {

    override var latLong = MutableLiveData<LatLng>()

    private val locationRequest by lazy {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 100
        locationRequest.smallestDisplacement = 1f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest
    }
    private var fusedLocationClient = FusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation() {
        fusedLocationClient.requestLocationUpdates(locationRequest, this, null)
    }

    override fun onLocationResult(locationResult: LocationResult?) {
        super.onLocationResult(locationResult)
        locationResult?.let { result ->
            val firstLocation = result.locations.firstOrNull()
            firstLocation?.let { location ->
                latLong.value = LatLng(location.latitude, location.longitude)
            }
        }

        if (!getUpdatesOfLocation) {
            fusedLocationClient.removeLocationUpdates(this)
        }
    }
}