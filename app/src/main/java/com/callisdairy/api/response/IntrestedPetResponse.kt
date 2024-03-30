package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class IntrestedPetResponse(
    @SerializedName("result") val result : IntrestedPetResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class IntrestedPetResult(
    @SerializedName("docs") val docs : ArrayList<IntrestedPetDocs>,
    @SerializedName("total") val total : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)


class IntrestedPetDocs(
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("petId") val petId : IntrestedPetPetId,
    @SerializedName("productId") val productId : ProductId,
    @SerializedName("userId") val userId : IntrestedPetUserId,
    @SerializedName("serviceId") val serviceId : ServiceId,
    @SerializedName("type") val type : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

class IntrestedPetUserId(
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


class IntrestedPetPetId(
    @SerializedName("petDescription") val petDescription : PetDescription,
    @SerializedName("favUser") val favUser : ArrayList<String>,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("price") val price : Number,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryId") val subCategoryId : String,
    @SerializedName("petName") val petName : String,
    @SerializedName("petImage") val petImage : ArrayList<String>,
    @SerializedName("dob") val dob : String,
    @SerializedName("origin") val origin : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("breed") val breed : String,
    @SerializedName("isLike") var isLike : Boolean,
    @SerializedName("language") val language : String,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("purchesStore") val purchesStore : String,
    @SerializedName("dietFreq") val dietFreq : String,
    @SerializedName("travel") val travel : String,
    @SerializedName("movie") val movie : String,
    @SerializedName("song") val song : String,
    @SerializedName("currency") var currency:String ,
    @SerializedName("celebrate") val celebrate : String,
    @SerializedName("veterinary") val veterinary : String,
    @SerializedName("veterinaryType") val veterinaryType : String,
    @SerializedName("doctorAppoint") val doctorAppoint : String,
    @SerializedName("petType") val petType : String,
    @SerializedName("favColour") val favColour : String,
    @SerializedName("habit") val habit : String,
    @SerializedName("favFood") val favFood : String,
    @SerializedName("favPlace") val favPlace : String,
//    @SerializedName("userId") val userId : IntrestedPetUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)


class PetDescription(
    @SerializedName("size") val size : String,
    @SerializedName("lastVaccinate") val lastVaccinate : String
)

class ProductId(
    @SerializedName("productImage") val productImage : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("productName") val productName : String,
    @SerializedName("description") val description : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryId") val subCategoryId : String,
    @SerializedName("price") val price : Number,
    @SerializedName("currency") val currency : String,
    @SerializedName("weight") val weight : String,
    @SerializedName("productGenerateId") val productGenerateId : String,
//    @SerializedName("userId") val userId : IntrestedPetUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("isLike") var isLike : Boolean,
    @SerializedName("__v") val __v : Int,

)

class ServiceId(
    @SerializedName("serviceImage") val serviceImage : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("serviceName") val serviceName : String,
    @SerializedName("description") val description : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryId") val subCategoryId : String,
    @SerializedName("price") val price : Number,
    @SerializedName("experience") val experience : Number,
    @SerializedName("serviceGenerateId") val serviceGenerateId : String,
    @SerializedName("experience_month") val experience_month : String = "",
//    @SerializedName("userId") val userId : IntrestedPetUserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("isLike") var isLike : Boolean

)