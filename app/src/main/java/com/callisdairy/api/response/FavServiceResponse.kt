package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class FavServiceResponse(
    @SerializedName("result") val result : FavServiceResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class FavServiceResult(
    @SerializedName("docs") val docs : ArrayList<FavServiceDocs>,
    @SerializedName("totalPages") val totalPages : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)

class FavServiceDocs(
    @SerializedName("serviceImage") val serviceImage : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("status") val status : String,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("_id") val _id : String,
    @SerializedName("serviceName") val serviceName : String,
    @SerializedName("description") val description : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryId") val subCategoryId : String,
    @SerializedName("price") val price : Number,
    @SerializedName("experience") val experience : Int,
    @SerializedName("experience_month") val experience_month : String,
    @SerializedName("serviceGenerateId") val serviceGenerateId : String,
    @SerializedName("userId") val userId : FavServiceUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("isInterested") var isInterested : Boolean,
    @SerializedName("isLike") var isLike : Boolean,
    val isLiked:Boolean = true
)

class FavServiceUserId(
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("petPic") val petPic : String,
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
    @SerializedName("zipCode") val zipCode : String,
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