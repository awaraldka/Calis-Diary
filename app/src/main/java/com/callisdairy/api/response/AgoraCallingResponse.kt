package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class AgoraCallingResponse(
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int,
    @SerializedName("result") val AgoraCallingResult: AgoraCallingResult
)

class AgoraCallingResult(
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userId") val userId : String,
    @SerializedName("uid") val uid : String,
    @SerializedName("channelId") val channelId : String,
    @SerializedName("expirationTimeInSeconds") val expirationTimeInSeconds : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("token") val token : String,
    @SerializedName("updatedAt") var updatedAt : String,
    @SerializedName("__v") val __v : Int,
)

