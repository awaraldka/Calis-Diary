package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class userOnlineResponse {
    @SerializedName("city") val city : String= ""
    @SerializedName("state") val state : String= ""
    @SerializedName("country") val country : String= ""
    @SerializedName("userName") val userName : String= ""
    @SerializedName("otpVerification") val otpVerification : Boolean = false
    @SerializedName("profilePic") val profilePic : String= ""
    @SerializedName("isSocial") val isSocial : Boolean = false
    @SerializedName("userType") val userType : String= ""
    @SerializedName("approveStatus") val approveStatus : String= ""
    @SerializedName("followersCount") val followersCount : Int = 0
    @SerializedName("likesUserCount") val likesUserCount : Int = 0
    @SerializedName("followingCount") val followingCount : Int = 0
    @SerializedName("privacyType") val privacyType : String= ""
    @SerializedName("status") val status : String= ""
    @SerializedName("points") val points : Number = 0
    @SerializedName("isOnline") val isOnline : Boolean = false
    @SerializedName("documents") val documents : String= ""
    @SerializedName("_id") val _id : String= ""
    @SerializedName("address") val address : String= ""
    @SerializedName("countryCode") val countryCode : Int = 0
    @SerializedName("countryIsoCode") val countryIsoCode : String= ""
    @SerializedName("dateOfBirth") val dateOfBirth : String= ""
    @SerializedName("email") val email : String= ""
    @SerializedName("gender") val gender : String= ""
    @SerializedName("mobileNumber") val mobileNumber : Int = 0
    @SerializedName("name") val name : String= ""
    @SerializedName("password") val password : String= ""
    @SerializedName("petPic") val petPic : String= ""
    @SerializedName("stateIsoCode") val stateIsoCode : String= ""
    @SerializedName("zipCode") val zipCode : Int = 0
    @SerializedName("coverPic") val coverPic : String= ""
    @SerializedName("otp") val otp : Int = 0
    @SerializedName("otpTime") val otpTime : Int = 0
    @SerializedName("createdAt") val createdAt : String= ""
    @SerializedName("updatedAt") val updatedAt : String= ""
    @SerializedName("__v") val __v : Int =0
}