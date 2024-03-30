package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class OthersPostListResponse(
    @SerializedName("result") val result: OthersPostListResult,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val responseCode: Int
)

class OthersPostListResult(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("docs") val docs: ArrayList<OthersPostListDocs>,
    @SerializedName("count") val count: Int,
    @SerializedName("totalPages") val totalPages: Int

)

class OthersPostListDocs(
//    @SerializedName("images") val images : ArrayList<String>,
//    @SerializedName("videos") val videos : ArrayList<String>,
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
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("likeCount") val likeCount : Int,
    @SerializedName("commentCount") val commentCount : Int,
    @SerializedName("topComments") val topComments : List<String>,
    @SerializedName("isLiked") val isLiked : Boolean
)