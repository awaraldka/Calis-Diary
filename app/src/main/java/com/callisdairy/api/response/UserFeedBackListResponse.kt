package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class UserFeedBackListResponse(
    @SerializedName("result") val result : UserFeedBackListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class UserFeedBackListResult(
    @SerializedName("docs") val docs : List<UserFeedBackListDocs>,
    @SerializedName("total") val total : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)


class UserFeedBackListDocs(
    @SerializedName("title") val title : String,
    @SerializedName("rating") val rating : Double,
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("appointmentId") val appointmentId : String,
    @SerializedName("doctorId") val doctorId : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)