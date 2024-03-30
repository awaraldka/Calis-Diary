package com.callisdairy.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

object LocationClass {

    data class LocationData(val latitude: Double, val longitude: Double)

    fun displayLocationSettingsRequest(context: Activity) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> Log.i(
                    "LOCATION_TAG",
                    "All location settings are satisfied."
                )
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        "LOCATION_TAG",
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )



                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(context, 1)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.i("LOCATION_TAG", "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    "LOCATION_TAG",
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
        }
    }



    suspend fun getLocationBaseCurrency(fusedLocationClient: FusedLocationProviderClient?, context: Activity): String {
        val location = withContext(Dispatchers.IO) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {


            }
            fusedLocationClient?.lastLocation?.await()
        }

        return location?.let { it ->
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                if (addresses?.isNotEmpty() == true) {
                    val countryCode = addresses.getOrNull(0)?.countryCode
                    Currency.getInstance(countryCode?.let { Locale("", it) }).currencyCode
                } else {
                    ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        } ?: ""
    }


    fun getLocation(context: Activity, fusedLocationClient: FusedLocationProviderClient, callback: (LocationData?) -> Unit) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                callback(null)
                return
            }

            fusedLocationClient.lastLocation.addOnCompleteListener(context) { task ->
                if (task.isSuccessful && task.result != null) {
                    try {
                        val lastLocation = task.result
                        val latitude = lastLocation.latitude
                        val longitude = lastLocation.longitude
                        val address = lastLocation.longitude

                        callback(LocationData(latitude, longitude))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }


    fun getAddressDetailsFromLocation(
        context: Context,
        latitude: Double,
        longitude: Double
    ): AddressDetails {
        val geocoder = Geocoder(context)
        val addressDetails = AddressDetails()

        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val address = addresses[0]

                addressDetails.addressLine1 = address.getAddressLine(0)
                addressDetails.addressLine2 = address.getAddressLine(1)
                addressDetails.city = address.locality
                addressDetails.state = address.adminArea
                addressDetails.zipCode = address.postalCode
                addressDetails.country = address.countryName
            }
        } catch (e: IOException) {
            // Handle the exception, such as network or service errors
        }

        return addressDetails
    }






}
data class AddressDetails(
    var addressLine1: String? = null,
    var addressLine2: String? = null,
    var city: String? = null,
    var state: String? = null,
    var zipCode: String? = null,
    var country: String? = null
)
