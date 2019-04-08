package com.kotlin.helpers.managers.location

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import com.kotlin.helpers.managers.RetrofitClientManagerInterface
import com.kotlin.helpers.enums.LanguageCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

interface FullAddressManagerInterface {
    var fullAddress: MutableLiveData<String>

    fun getFullAddressOfLocation(latitude: Double, longitude: Double)
}

class FullAddressManager(
    private val context: Context,
    private val retrofitClientManager: RetrofitClientManagerInterface,
    private val languageCode: LanguageCode
) : FullAddressManagerInterface {

    override var fullAddress = MutableLiveData<String>()

    private val locale = Locale(languageCode.code)
    private var geocoder = Geocoder(context, locale)

    override fun getFullAddressOfLocation(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val fullAddress = getAddressUsingGeocoder(latitude, longitude) ?: "No address found"

            withContext(Dispatchers.Main) {
                this@FullAddressManager.fullAddress.value = fullAddress
            }
        }
    }

    private fun getAddressUsingGeocoder(latitude: Double, longitude: Double): String? {
        val firstAddress = geocoder.getFromLocation(latitude, longitude, 1)
            .filterNotNull()
            .firstOrNull()

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

        return null
    }
}