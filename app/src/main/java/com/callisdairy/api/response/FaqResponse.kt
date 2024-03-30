package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class FaqResponse(
    @SerializedName("result") val result : ArrayList<FaqResult>,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class FaqResult(
    @SerializedName("status") val status:String,
    @SerializedName("_id") val _id:String,
    @SerializedName("question") val question:String,
    @SerializedName("answer") val answer:String,
    @SerializedName("createdAt") val createdAt:String,
    @SerializedName("updatedAt") val updatedAt:String,
    @SerializedName("__v") val __v:Int,
    var expand : Boolean = false

)