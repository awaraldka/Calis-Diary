package com.callisdairy.api.OtherApi

import com.google.gson.annotations.SerializedName
import com.kanabix.api.LocationLatLngGeometry

class NearByLocationResponse(
    @SerializedName("html_attributions") val html_attributions : ArrayList<String>,
    @SerializedName("results") val results : ArrayList<NearByLocationResults>,
    @SerializedName("status") val status : String
)

class NearByLocationResults(
    @SerializedName("geometry") val geometry : LocationLatLngGeometry,
    @SerializedName("name") val name : String
)