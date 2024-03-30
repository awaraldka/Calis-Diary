package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class updateCommentResponse(
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int,
    @SerializedName("result") val updateCommentResult : updateCommentResult
)


class updateCommentResult(
    @SerializedName("comment") val comment : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("postId") val postId : String,
    @SerializedName("status") val status : String,
    @SerializedName("type") val type : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("userId") val userId : String,
    @SerializedName("__v") val __v : String,
    @SerializedName("_id") val _id : String,

    )