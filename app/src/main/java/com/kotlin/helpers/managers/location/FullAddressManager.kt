package com.kotlin.helpers.managers.location

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.kotlin.helpers.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @FullAddressManager class helps you get the full address of specific latitude, longitude of a location
 * @Note that you have to setup and observer for its liveData "fullAddress" because you will receive
 * the full address through it
 * */

enum class LanguageCode(val code: String) {
    EN("en"), AR("ar")
}

interface FullAddressManagerInterface {
    var fullAddress: MutableLiveData<String>

    fun getFullAddressOfLocation(
        latitude: Double, longitude: Double, inLanguageCode: LanguageCode = LanguageCode.EN
    )
}

class FullAddressManager(val context: Context) : FullAddressManagerInterface {

    override var fullAddress = MutableLiveData<String>()

    private var chosenLanguage = LanguageCode.EN

    private var geocoder = Geocoder(context, Locale(chosenLanguage.code))

    override fun getFullAddressOfLocation(latitude: Double, longitude: Double, inLanguageCode: LanguageCode) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fullAddress = getAddressUsingGeocoder(latitude, longitude)

                withContext(Dispatchers.Main) {
                    fullAddress?.let {
                        this@FullAddressManager.fullAddress.value = fullAddress
                    } ?: run {
                        this@FullAddressManager.fullAddress.value = context.getString(R.string.no_address_found)
                    }
                }
            } catch (ex: Exception) {
//                Log.e("hhh", ex.message)
                withContext(Dispatchers.Main) {
                    this@FullAddressManager.fullAddress.value = context.getString(R.string.no_address_found)
                }
            }
        }
    }

    private fun getAddressUsingGeocoder(latitude: Double, longitude: Double): String? {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses.isNotEmpty()) {
            val firstAddress = addresses.filterNotNull().firstOrNull()

            firstAddress?.let {
                return arrayOf(
                    it.countryName,
                    it.adminArea,
                    it.subAdminArea,
                    it.locality,
                    it.featureName
                )
                    .filterNotNull()
                    .filter { string ->
                        string != "Unnamed Road"
                    }
                    .joinToString(", ")
            }
        }

        return null
    }
}