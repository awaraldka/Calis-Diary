package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class AddEventResponse(

    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)