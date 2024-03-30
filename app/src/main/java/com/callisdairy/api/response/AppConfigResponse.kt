package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class AppConfigResponse(
    @SerializedName("result") var result:String,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


data class KeysData(
    @SerializedName("G_MAP_KEY") val gMapKey: String,
    @SerializedName("IP_API_Key") val ipApiKey: String,
    @SerializedName("appId") val appId: String,
    @SerializedName("appCertificate") val appCertificate: String,
    @SerializedName("customerKey") val customerKey: String,
    @SerializedName("customerSecret") val customerSecret: String
)
