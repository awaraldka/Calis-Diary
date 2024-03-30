package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class RequestedListResponse(
    @SerializedName("result") val result : RequestedListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class RequestedListResult(
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("remainingItems") val remainingItems : Int,
    @SerializedName("count") val count : Int,
    @SerializedName("docs") val docs : List<RequestedListDocs>,
    @SerializedName("totalPages") val totalPages : Int
)

class RequestedListDocs(
    @SerializedName("status") val status : String,
    @SerializedName("requestStatus") val requestStatus : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userId") val userId : RequestedListUserId,
    @SerializedName("requestId") val requestId : RequestId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)


class RequestId(
    @SerializedName("userName") val userName : String,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String,
    @SerializedName("petPic") val petPic : String
)

class RequestedListUserId(
    @SerializedName("userName") val userName : String,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String,
    @SerializedName("petPic") val petPic : String
)