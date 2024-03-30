package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ViewStatusResponse(
    @SerializedName("result") val result : ViewStatusResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class ViewStatusResult(
    @SerializedName("status") val status : Boolean,
    @SerializedName("_id") val _id : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("userId") val userId : String,
    @SerializedName("updatedAt") val updatedAt : String,

)