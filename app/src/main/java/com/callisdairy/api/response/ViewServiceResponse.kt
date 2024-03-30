package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

data class ViewServiceResponse(
    @SerializedName("result") val result: ViewServiceResult,
    @SerializedName("responseMessage") val responseMessage: String?= null,
    @SerializedName("responseCode") val responseCode: Int
)

data class ViewServiceResult(
    @SerializedName("serviceImage") val serviceImage : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("approveStatus") val approveStatus : String?= null,
    @SerializedName("status") val status : String?= null,
    @SerializedName("_id") val _id : String?= null,
    @SerializedName("serviceName") val serviceName : String?= null,
    @SerializedName("description") val description : String?= null,
    @SerializedName("categoryId") val categoryId : CategoryId,
    @SerializedName("subCategoryId") val subCategoryId : SubCategoryId,
    @SerializedName("price") val price : Number,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("currency") val currency : String,
    @SerializedName("experience") val experience : Int,
    @SerializedName("experience_month") val experience_month : Int,
    @SerializedName("serviceGenerateId") val serviceGenerateId : String?= null,
    @SerializedName("userId") val userId : ServiceUserId,
    @SerializedName("createdAt") val createdAt : String?= null,
    @SerializedName("updatedAt") val updatedAt : String?= null,
    @SerializedName("__v") val __v : Int
)

data class CategoryId(
    @SerializedName("isSubCategoryCreated") val isSubCategoryCreated : Boolean,
    @SerializedName("status") val status : String?= null,
    @SerializedName("_id") val _id : String?= null,
    @SerializedName("categoryName") val categoryName : String?= null,
    @SerializedName("categoryType") val categoryType : String?= null,
    @SerializedName("categoryImage") val categoryImage : String?= null,
    @SerializedName("createdAt") val createdAt : String?= null,
    @SerializedName("updatedAt") val updatedAt : String?= null,
    @SerializedName("__v") val __v : Int
)

data class SubCategoryId(
    @SerializedName("status") val status : String?= null,
    @SerializedName("_id") val _id : String?= null,
    @SerializedName("subCategoryName") val subCategoryName : String?= null,
    @SerializedName("categoryId") val categoryId : String?= null,
    @SerializedName("subCategoryImage") val subCategoryImage : String?= null,
    @SerializedName("createdAt") val createdAt : String?= null,
    @SerializedName("updatedAt") val updatedAt : String?= null,
    @SerializedName("__v") val __v : Int
)

data class ServiceUserId(
    @SerializedName("city") val city: String?= null,
    @SerializedName("state") val state: String?= null,
    @SerializedName("country") val country: String?= null,
    @SerializedName("userName") val userName: String?= null,
    @SerializedName("otpVerification") val otpVerification: Boolean,
    @SerializedName("profilePic") val profilePic: String?= null,
    @SerializedName("petPic") val petPic: String?= null,
    @SerializedName("isSocial") val isSocial: Boolean,
    @SerializedName("userType") val userType: String?= null,
    @SerializedName("approveStatus") val approveStatus: String?= null,
    @SerializedName("followers") val followers: ArrayList<String>,
    @SerializedName("followersCount") val followersCount: Int,
    @SerializedName("following") val following: ArrayList<String>,
    @SerializedName("interest") val interest: ArrayList<String>,
    @SerializedName("likesProduct") val likesProduct: ArrayList<String>,
    @SerializedName("likeServices") val likeServices: ArrayList<String>,
    @SerializedName("favouritePets") val favouritePets: ArrayList<String>,
    @SerializedName("likesUser") val likesUser: ArrayList<String>,
    @SerializedName("likesUserCount") val likesUserCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
    @SerializedName("status") val status: String?= null,
    @SerializedName("points") val points: Number,
    @SerializedName("_id") val _id: String?= null,
    @SerializedName("name") val name: String?= null,
    @SerializedName("email") val email: String?= null,
    @SerializedName("password") val password: String?= null,
    @SerializedName("mobileNumber") val mobileNumber: String?= null,
    @SerializedName("address") val address: String?= null,
    @SerializedName("zipCode") val zipCode: String?= null,
    @SerializedName("gender") val gender: String?= null,
    @SerializedName("countryCode") val countryCode: String?= null,
    @SerializedName("otp") val otp: String?= null,
    @SerializedName("otpTime") val otpTime: String?= null,
    @SerializedName("createdAt") val createdAt: String?= null,
    @SerializedName("updatedAt") val updatedAt: String?= null,
    @SerializedName("__v") val __v: Int
)