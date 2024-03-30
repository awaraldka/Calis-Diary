package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class AddToIntrestedResponse(
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)