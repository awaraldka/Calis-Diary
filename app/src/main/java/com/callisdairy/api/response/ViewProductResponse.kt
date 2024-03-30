package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ViewProductResponse(
    @SerializedName("result") val result : ViewProductResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class ViewProductResult(
    @SerializedName("productImage") val productImage : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("status") val status : String,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("_id") val _id : String,
    @SerializedName("productName") val productName : String,
    @SerializedName("description") val description : String,
    @SerializedName("categoryId") val categoryId :ViewProductCategoryId,
    @SerializedName("subCategoryId") val subCategoryId : ViewProductSubCategoryId,
    @SerializedName("price") val price : Number,
    @SerializedName("weight") val weight : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("productGenerateId") val productGenerateId : String,
    @SerializedName("userId") val userId : ViewProductUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)


class ViewProductCategoryId(
    @SerializedName("isSubCategoryCreated") val isSubCategoryCreated : Boolean,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("categoryName") val categoryName : String,
    @SerializedName("categoryType") val categoryType : String,
    @SerializedName("categoryImage") val categoryImage : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

class ViewProductSubCategoryId(
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("subCategoryName") val subCategoryName : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryImage") val subCategoryImage : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)




class ViewProductUserId(
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