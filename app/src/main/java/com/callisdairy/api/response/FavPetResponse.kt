package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class FavPetResponse(
    @SerializedName("result") val result : FavPetResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class FavPetResult(
    @SerializedName("docs") val docs : ArrayList<FavPetDocs>,
    @SerializedName("totalPages") val totalPages : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)

class FavPetDocs(
    @SerializedName("petDescription") val petDescription : FavPetDescription,
    @SerializedName("favUser") val favUser : ArrayList<String>,
    @SerializedName("petImage") val petImage : ArrayList<String>,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryId") val subCategoryId : String,
    @SerializedName("petName") val petName : String,
    @SerializedName("dob") val dob : String,
    @SerializedName("origin") val origin : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("breed") val breed : String,
    @SerializedName("language") val language : String,
    @SerializedName("purchesStore") val purchesStore : String,
    @SerializedName("description") val description : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("price") val price : Number,
    @SerializedName("userId") val userId : FavUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("isInterested") var isInterested : Boolean,
    val isLiked:Boolean = true
)

class FavPetDescription(
    @SerializedName("size") val size : String,
    @SerializedName("lastVaccinate") val lastVaccinate : String
)

class FavUserId(
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