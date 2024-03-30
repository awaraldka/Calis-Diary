package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ViewPostResponse(
    @SerializedName("result") val result : ViewPostResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class ViewPostResult(

    @SerializedName("status") val status : String,
    @SerializedName("tagPeople") val tagPeople : ArrayList<UserId>,
    @SerializedName("metaWords") val metaWords : ArrayList<String>,
    @SerializedName("postType") val postType : String,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("images") val images : ArrayList<MediaImageVideo>,
    @SerializedName("videos") val videos : ArrayList<MediaImageVideo>,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("_id") val _id : String,
    @SerializedName("title") val title : String,
    @SerializedName("caption") val caption : String,
    @SerializedName("address") val address : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("likeCount") val likeCount : Int,
    @SerializedName("commentCount") val commentCount : Int,
    @SerializedName("isLiked") val isLiked : Boolean
)