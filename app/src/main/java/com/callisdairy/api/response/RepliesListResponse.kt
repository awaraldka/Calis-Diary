package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class RepliesListResponse(
    @SerializedName("result") val result : RepliesListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class RepliesListResult(
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("remainingItems") val remainingItems : Int,
    @SerializedName("count") val count : Int,
    @SerializedName("docs") val docs : ArrayList<RepliesListDocs>,
    @SerializedName("totalPages") val totalPages : Int
)

class RepliesListDocs(
    @SerializedName("type") val type : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userId") val userId : RepliesListUserId,
    @SerializedName("commentId") val commentId : String,
    @SerializedName("reply") var reply : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

class RepliesListUserId(
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
    @SerializedName("petPic") val petPic : String,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("isSocial") val isSocial : Boolean,
    @SerializedName("userType") val userType : String,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("followers") val followers : ArrayList<String>,
    @SerializedName("followersCount") val followersCount : Int,
    @SerializedName("following") val following : ArrayList<String>,
    @SerializedName("interest") val interest : ArrayList<String>,
    @SerializedName("likesProduct") val likesProduct : ArrayList<String>,
    @SerializedName("likeServices") val likeServices : ArrayList<String>,
    @SerializedName("favouritePets") val favouritePets : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("likesUserCount") val likesUserCount : Int,
    @SerializedName("followingCount") val followingCount : Int,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String,
    @SerializedName("mobileNumber") val mobileNumber : String,
    @SerializedName("address") val address : String,
    @SerializedName("zipCode") val zipCode : Number,
    @SerializedName("gender") val gender : String,
    @SerializedName("countryCode") val countryCode : String,
    @SerializedName("otp") val otp : String,
    @SerializedName("otpTime") val otpTime : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("deviceType") val deviceType : String,
    @SerializedName("coverPic") val coverPic : String,
    @SerializedName("countryIsoCode") val countryIsoCode : String,
    @SerializedName("dateOfBirth") val dateOfBirth : String,
    @SerializedName("stateIsoCode") val stateIsoCode : String
)