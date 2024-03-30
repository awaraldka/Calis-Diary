package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class MissingPetResponse(

    @SerializedName("result") val result : MissingPetResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class MissingPetResult(
    @SerializedName("docs") val docs : ArrayList<MissingPetDocs>,
    @SerializedName("total") val total : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)

class MissingPetDocs(
    @SerializedName("userDetails") val userDetails : UserDetails,
    @SerializedName("petName") val petName : String,
    @SerializedName("lastSeen") val lastSeen : String,
    @SerializedName("type") val type : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("color") val color : String,
    @SerializedName("breed") val breed : String,
    @SerializedName("trackerID") val trackerID : String="",
    @SerializedName("petImage") val petImage : ArrayList<String>,
    @SerializedName("peculiarity") val peculiarity : String,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("petId") val petId : PetId,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

class UserDetails(
    @SerializedName("name") val name : String,
    @SerializedName("address") val address : String,
    @SerializedName("email") val email : String,
    @SerializedName("mobileNumber") val mobileNumber : String
)

class PetImage(
//    @SerializedName("asset_id") val asset_id : String,
//    @SerializedName("public_id") val public_id : String,
//    @SerializedName("version") val version : Int,
//    @SerializedName("version_id") val version_id : String,
//    @SerializedName("signature") val signature : String,
//    @SerializedName("width") val width : Int,
//    @SerializedName("height") val height : Int,
//    @SerializedName("format") val format : String,
//    @SerializedName("resource_type") val resource_type : String,
//    @SerializedName("created_at") val created_at : String,
//    @SerializedName("tags") val tags : ArrayList<String>,
//    @SerializedName("pages") val pages : Int,
//    @SerializedName("bytes") val bytes : Int,
//    @SerializedName("type") val type : String,
//    @SerializedName("etag") val etag : String,
//    @SerializedName("placeholder") val placeholder : Boolean,
    @SerializedName("url") val url : String,
//    @SerializedName("secure_url") val secure_url : String,
//    @SerializedName("folder") val folder : String,
//    @SerializedName("api_key") val api_api_key : Int
)

class PetId(
    @SerializedName("petDescription") val petDescription : PetDescription,
    @SerializedName("favUser") val favUser : ArrayList<String>,
    @SerializedName("petImage") val petImage : ArrayList<String>,
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
    @SerializedName("price") val price : Number,
    @SerializedName("userId") val userId : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

class UserId(
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