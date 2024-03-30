package com.callisdairy.api.response

import com.callisdairy.Socket.MediaResult
import com.google.gson.annotations.SerializedName

class HomePageListResponse(
    @SerializedName("result") val result : HomePageListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int


)


class HomePageListResult(
    @SerializedName("docs") val docs : ArrayList<HomePageDocs>,
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("count") val count : Int,
    @SerializedName("totalPages") val totalPages : Int,
    @SerializedName("remainingItems") val remainingItems : Int,

)

class HomePageDocs(
    @SerializedName("status") val status : String,
    @SerializedName("tagPeople") val tagPeople : ArrayList<UserId>,
//    @SerializedName("metaWords") val metaWords : ArrayList<String>,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("_id") val _id : String,
    @SerializedName("title") val title : String,
    @SerializedName("caption") val caption : String,
    @SerializedName("address") val address : String,
    @SerializedName("userId") val userId : HomePageUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("likeCount") var likeCount : Int,
    @SerializedName("commentCount") var commentCount : Int,
    @SerializedName("isLiked") var isLiked : Boolean,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("topComments") val topComments : ArrayList<TopComments>,
)

class HomePageUserId(

    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("petPic") val petPic : String,
    @SerializedName("userType") val userType : String,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("name") val name : String,
    @SerializedName("coverPic") val coverPic : String,
)


class MediaUrls(
    @SerializedName("media") val media : MediaResultHome,
    @SerializedName("type") val type : String

)


class MediaResultHome (
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("mediaUrlMobile") val mediaUrlMobile : String,
    @SerializedName("mediaUrlWeb") val mediaUrlWeb : String
)


class TopComments(
    @SerializedName("type") val type : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userId") val userId : HomePageUserId,
    @SerializedName("postId") val postId : String,
    @SerializedName("comment") val comment : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("isLiked") val isLiked : Boolean,
)