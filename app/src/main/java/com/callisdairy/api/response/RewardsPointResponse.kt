package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class RewardsPointResponse(
    @SerializedName("result") val result : RewardsPointResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class RewardsPointResult(
    @SerializedName("signUp") val signUp : Int,
    @SerializedName("login") val login : Int,
    @SerializedName("share") val share : Int,
    @SerializedName("like") val like : Int,
    @SerializedName("post") val post : Int,
    @SerializedName("story") val story : Int,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)