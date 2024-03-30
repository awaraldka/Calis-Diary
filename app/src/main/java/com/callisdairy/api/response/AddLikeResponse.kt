package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class AddLikeResponse(
    @SerializedName("result") val result : AddLikeResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class AddLikeResult(
    @SerializedName("likeType") val likeType : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userId") val userId : String,
    @SerializedName("postId") val postId : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)