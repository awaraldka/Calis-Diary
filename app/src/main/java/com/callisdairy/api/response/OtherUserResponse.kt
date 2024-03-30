package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class OtherUserResponse(
    @SerializedName("result") val result : OtherUserResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class  OtherUserResult(
    @SerializedName("city") val city : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("coverPic") val coverPic : String,
    @SerializedName("petPic") val petPic : String,
    @SerializedName("isSocial") val isSocial : Boolean,
    @SerializedName("userType") val userType : String,
    @SerializedName("approveStatus") val approveStatus : String,
//    @SerializedName("followers") val followers : ArrayList<String>,
    @SerializedName("followersCount") val followersCount : Int,
//    @SerializedName("following") val following : ArrayList<String>,
    @SerializedName("interest") val interest : ArrayList<String>,
    @SerializedName("likesProduct") val likesProduct : ArrayList<String>,
    @SerializedName("likeServices") val likeServices : ArrayList<String>,
    @SerializedName("favouritePets") val favouritePets : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("likesUserCount") val likesUserCount : Int,
    @SerializedName("postLikesCount") val postLikesCount : Number,
    @SerializedName("followingCount") val followingCount : Int,
    @SerializedName("totalPosts") val totalPosts : Int=0,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String,
    @SerializedName("points") val points : Number,
    @SerializedName("countryCode") val countryCode : String,
    @SerializedName("mobileNumber") val mobileNumber : String,
    @SerializedName("email") val email : String,
    @SerializedName("address") val address : String,
    @SerializedName("password") val password : String,
    @SerializedName("dateOfBirth") val dateOfBirth : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("isYou") val isYou : Boolean,
    @SerializedName("isRequested") val isRequested : Boolean,
    @SerializedName("isFollowed") val isFollowed : Boolean,
    @SerializedName("zipCode") val zipCode : String,
    @SerializedName("countryIsoCode") val countryIsoCode : String,
    @SerializedName("stateIsoCode") val stateIsoCode : String,
    @SerializedName("otpTime") val otpTime : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("deviceType") val deviceType : String,
    @SerializedName("privacyType") val privacyType : String,
    @SerializedName("mutualFriends") val mutualFriends : ArrayList<MutualFriends>
)

class MutualFriends(
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("petPic") val petPic : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userName") val userName : String,
)