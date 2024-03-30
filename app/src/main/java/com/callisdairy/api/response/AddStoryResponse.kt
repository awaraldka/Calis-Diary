package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class AddStoryResponse(
    @SerializedName("result") val result : ArrayList<AddStoryResult>,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class AddStoryResult(
//    @SerializedName("image") val image : String,
//    @SerializedName("videos") val videos : String,
//    @SerializedName("caption") val caption : String,
//    @SerializedName("status") val status : String,
//    @SerializedName("seenUserId") val seenUserId : String,
//    @SerializedName("_id") val _id : String,
//    @SerializedName("userId") val userId : String,
//    @SerializedName("createdAt") val createdAt : String,
//    @SerializedName("updatedAt") val updatedAt : String,
//    @SerializedName("__v") val __v : Int
    )

