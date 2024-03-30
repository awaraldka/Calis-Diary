package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ListEventResponse(
    @SerializedName("result") val result : ListEventResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class ListEventResult(
    @SerializedName("docs") val docs : ArrayList<ListEventDocs>,
    @SerializedName("total") val total : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)

class ListEventDocs(
    @SerializedName("image") val image : ArrayList<String>,
    @SerializedName("status") val status : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("long") val long : Double,
    @SerializedName("_id") val _id : String,
    @SerializedName("description") val description : String,
    @SerializedName("eventName") val eventName : String,
    @SerializedName("address") val address : String,
    @SerializedName("country") val country : String,
    @SerializedName("state") val state : String,
    @SerializedName("city") val city : String,
    @SerializedName("date") val date : String,
    @SerializedName("userId") val userId : ListEventUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)
class ListEventUserId(
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
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
    @SerializedName("privacyType") val privacyType : String,
    @SerializedName("whoCanSee") val whoCanSee : ArrayList<String>,
    @SerializedName("status") val status : String,
    @SerializedName("points") val points : Number,
    @SerializedName("isOnline") val isOnline : Boolean,
//    @SerializedName("documents") val documents : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String,
    @SerializedName("mobileNumber") val mobileNumber : String,
    @SerializedName("address") val address : String,
    @SerializedName("zipCode") val zipCode : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("countryCode") val countryCode : String,
    @SerializedName("otp") val otp : String,
    @SerializedName("otpTime") val otpTime : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("coverPic") val coverPic : String,
    @SerializedName("petPic") val petPic : String
)