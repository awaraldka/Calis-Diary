package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class FollowingUserListResponse(
    @SerializedName("result") val result : FollowingUserListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class FollowingUserListResult(
    @SerializedName("docs") val following : ArrayList<FollowingUserListFollowing>,
    @SerializedName("count") val count : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("remainingItems") val remainingItems : Int,
    @SerializedName("totalPages") val totalPages : Int
)

class FollowingUserListFollowing(
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("isSocial") val isSocial : Boolean,
    @SerializedName("isYou") val isYou : Boolean,
    @SerializedName("isFollowed") var isFollowed : Boolean,
    @SerializedName("isRequested") var isRequested : Boolean,
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
    @SerializedName("privacyType") val privacyType : String,
    @SerializedName("whoCanSee") val whoCanSee : ArrayList<String>,
    @SerializedName("status") val status : String,
    @SerializedName("points") val points : Number,
    @SerializedName("isOnline") val isOnline : Boolean,
    @SerializedName("_id") val _id : String,
    @SerializedName("address") val address : String,
    @SerializedName("countryCode") val countryCode : String,
    @SerializedName("countryIsoCode") val countryIsoCode : String,
    @SerializedName("dateOfBirth") val dateOfBirth : String,
    @SerializedName("email") val email : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("mobileNumber") val mobileNumber : String,
    @SerializedName("name") val name : String,
    @SerializedName("password") val password : String,
    @SerializedName("petPic") val petPic : String,
    @SerializedName("stateIsoCode") val stateIsoCode : String,
    @SerializedName("zipCode") val zipCode : String,
    @SerializedName("coverPic") val coverPic : String,
    @SerializedName("otpTime") val otpTime : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)