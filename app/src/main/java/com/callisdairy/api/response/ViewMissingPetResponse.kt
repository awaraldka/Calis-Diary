package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ViewMissingPetResponse(
    @SerializedName("result") val result : ViewMissingPetResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class ViewMissingPetResult(
    @SerializedName("userDetails") val userDetails : UserDetails,
    @SerializedName("petName") val petName : String,
    @SerializedName("lastSeen") val lastSeen : String,
    @SerializedName("type") val type : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("color") val color : String,
    @SerializedName("breed") val breed : String,
    @SerializedName("trackerID") val trackerID : String,
    @SerializedName("petImage") val petImage : ArrayList<String>,
    @SerializedName("peculiarity") val peculiarity : String,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)