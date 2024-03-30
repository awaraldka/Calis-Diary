package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName
import java.util.Currency

class ProductListResponse(
    @SerializedName("result") val result : ProductListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val statusCode : Int
)

class ProductListResult(
    @SerializedName("docs") val docs : ArrayList<ProductListDocs>,
    @SerializedName("total") val total : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("totalPages") val totalPages : Int,
    @SerializedName("remainingItems") val remainingItems : Int
)


class ProductListDocs(
    @SerializedName("productImage") val productImage : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("isLike") var isLike : Boolean,
    @SerializedName("isInterested") var isInterested : Boolean,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("productName") val productName : String,
    @SerializedName("description") val description : String,
//    @SerializedName("categoryId") val categoryId : ProductListCategoryId,
//    @SerializedName("subCategoryId") val subCategoryId : ProductListSubCategoryId,
    @SerializedName("price") val price : Number,
    @SerializedName("currency") val currency : Number,
    @SerializedName("weight") val weight : String,
    @SerializedName("productGenerateId") val productGenerateId : String,
    @SerializedName("userId") val userId : ProductListUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)


class ProductListCategoryId(
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


class ProductListSubCategoryId(
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("subCategoryName") val subCategoryName : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryImage") val subCategoryImage : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)


class ProductListUserId(

    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
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
    @SerializedName("petPic") val petPic : String,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("countryCode") val countryCode : String,
    @SerializedName("mobileNumber") val mobileNumber : String,
    @SerializedName("email") val email : String,
    @SerializedName("address") val address : String,
    @SerializedName("password") val password : String,
    @SerializedName("dateOfBirth") val dateOfBirth : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("zipCode") val zipCode : String,
    @SerializedName("otp") val otp : String,
    @SerializedName("otpTime") val otpTime : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("deviceType") val deviceType : String
)