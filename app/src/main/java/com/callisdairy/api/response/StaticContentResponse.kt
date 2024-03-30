package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class StaticContentResponse(
    @SerializedName("result") val result : StaticContentResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class StaticContentResult(
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,

)