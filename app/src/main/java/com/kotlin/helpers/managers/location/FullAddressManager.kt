package com.kotlin.helpers.managers.location

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.kotlin.helpers.enums.LanguageCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

interface FullAddressManagerInterface {
    var fullAddress: MutableLiveData<String>

    fun getFullAddressOfLocation(
        latitude: Double,
        longitude: Double,
        inLanguageCode: LanguageCode = LanguageCode.EN
    )
}

class FullAddressManager(
    private val context: Context
) : FullAddressManagerInterface {

    override var fullAddress = MutableLiveData<String>()

    private var chosenLanguage = LanguageCode.EN

    private var geocoder = Geocoder(context, Locale(chosenLanguage.code))

    override fun getFullAddressOfLocation(latitude: Double, longitude: Double, inLanguageCode: LanguageCode) {
        if (inLanguageCode != chosenLanguage) {
            geocoder = Geocoder(context, Locale(inLanguageCode.code))
            chosenLanguage = inLanguageCode
        }

        CoroutineScope(Dispatchers.IO).launch {
            val fullAddress = getAddressUsingGeocoder(latitude, longitude)
            withContext(Dispatchers.Main) {
                this@FullAddressManager.fullAddress.value = fullAddress
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

            return firstAddress?.getAddressLine(0)
        }

        return null
    }
}