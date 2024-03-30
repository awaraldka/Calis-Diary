package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class RewardsResponseVendor(
    @SerializedName("result") val result : RewardsResponseResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class RewardsResponseResult(
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("total") val total : Int,
    @SerializedName("count") val count : Int,
    @SerializedName("docs") val docs : ArrayList<RewardsResponseDocs>,
    @SerializedName("pages") val pages : Int
)

class RewardsResponseDocs(
    @SerializedName("userName") val userName : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("transferStatus") val transferStatus : String,
    @SerializedName("status") val status : String,
    @SerializedName("points") val points : String,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
)