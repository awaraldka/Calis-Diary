package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class MyPostResponse(
    @SerializedName("result") val result : MyPostResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class MyPostResult(
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("remainingItems") val remainingItems : Int,
    @SerializedName("count") val count : Int,
    @SerializedName("docs") val docs : ArrayList<MyPostDocs>,
    @SerializedName("totalPages") val totalPages : Int
)

class MyPostDocs(

    @SerializedName("images") val images : ArrayList<MediaImageVideo>,
    @SerializedName("videos") val videos : ArrayList<MediaImageVideo>,
    @SerializedName("status") val status : String,
//    @SerializedName("tagPeople") val tagPeople : ArrayList<String>,
    @SerializedName("metaWords") val metaWords : ArrayList<String>,
    @SerializedName("postType") val postType : String,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
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

class MediaImageVideo(
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("mediaUrlMobile") val mediaUrlMobile : String,
    @SerializedName("mediaUrlWeb") val mediaUrlWeb : String,
    @SerializedName("type") val type : String
)


