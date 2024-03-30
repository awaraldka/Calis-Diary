package com.kanabix.api


import com.google.gson.annotations.SerializedName

class LocationLatLngResponse {
    @SerializedName("results")
    val results: ArrayList<LocationLatLngResult> = ArrayList()
    @SerializedName("status")
    val status: String = ""

}

class LocationLatLngResult {
    @SerializedName("geometry")
    val geometry: LocationLatLngGeometry = LocationLatLngGeometry()

    val address_components: List<AddressComponent> = listOf()


}


data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)


class LocationLatLngGeometry {
    @SerializedName("location")
    val location: LocationLatLngLocation = LocationLatLngLocation()
//    @SerializedName("location_type")
//    val locationType: String = ""
}

class LocationLatLngLocation {
    @SerializedName("lat")
    val lat: Double = 0.0
    @SerializedName("lng")
    val lng: Double = 0.0
}