package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class GlobalSearchResponse(
    @SerializedName("result") val result : GlobalSearchResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class GlobalSearchResult(
//    @SerializedName("products") val products : ArrayList<GlobalSearchProducts>,
//    @SerializedName("pets") val pets : ArrayList<GlobalSearchPets>,
//    @SerializedName("services") val services : ArrayList<GlobalSearchServices>,
//    @SerializedName("categories") val categories : ArrayList<GlobalSearchCategories>,
//    @SerializedName("sub_categories") val sub_categories : ArrayList<GlobalSearchSub_categories>,
    @SerializedName("users") val users : ArrayList<GlobalSearchUsers>
)

class GlobalSearchProducts(
    @SerializedName("productImage") val productImage : ArrayList<String>,
    @SerializedName("likesUser") val likesUser : ArrayList<String>,
    @SerializedName("approveStatus") val approveStatus : String,
    @SerializedName("status") val status : String,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("_id") val _id : String,
    @SerializedName("productName") val productName : String,
    @SerializedName("description") val description : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryId") val subCategoryId : String,
    @SerializedName("price") val price : Number,
    @SerializedName("weight") val weight : Int,
    @SerializedName("productGenerateId") val productGenerateId : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)



class GlobalSearchPets(
//    @SerializedName("petDescription") val petDescription : PetDescription,
    @SerializedName("favUser") val favUser : ArrayList<String>,
    @SerializedName("petImage") val petImage : ArrayList<String>,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("categoryId") val categoryId : CategoryId,
    @SerializedName("subCategoryId") val subCategoryId : SubCategoryId,
    @SerializedName("petName") val petName : String,
    @SerializedName("dob") val dob : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("breed") val breed : String,
    @SerializedName("language") val language : String,
    @SerializedName("purchesStore") val purchesStore : String,
    @SerializedName("description") val description : String,
    @SerializedName("price") val price : Int,
    @SerializedName("dietFreq") val dietFreq : String,
    @SerializedName("travel") val travel : String,
    @SerializedName("movie") val movie : String,
    @SerializedName("song") val song : String,
    @SerializedName("celebrate") val celebrate : String,
    @SerializedName("veterinary") val veterinary : String,
    @SerializedName("veterinaryType") val veterinaryType : String,
    @SerializedName("doctorAppoint") val doctorAppoint : String,
    @SerializedName("petType") val petType : String,
    @SerializedName("favColour") val favColour : String,
    @SerializedName("habit") val habit : String,
    @SerializedName("favFood") val favFood : String,
    @SerializedName("favPlace") val favPlace : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int

)

class GlobalSearchServices(
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
    @SerializedName("serviceGenerateId") val serviceGenerateId : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

class GlobalSearchCategories(
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
class GlobalSearchSub_categories(
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("subCategoryName") val subCategoryName : String,
    @SerializedName("categoryId") val categoryId : String,
    @SerializedName("subCategoryImage") val subCategoryImage : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)
class GlobalSearchUsers(
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("coverPic") val coverPic: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("otpVerification") val otpVerification: Boolean,
    @SerializedName("profilePic") val profilePic: String,
    @SerializedName("isSocial") val isSocial: Boolean,
    @SerializedName("userType") val userType: String,
    @SerializedName("approveStatus") val approveStatus: String,
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
    @SerializedName("status") val status: String,
    @SerializedName("points") val points: Number,
    @SerializedName("_id") val _id: String,
    @SerializedName("privacyType") val privacyType: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("address") val address: String,
    @SerializedName("zipCode") val zipCode: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("otpTime") val otpTime: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("isFollowed") var isFollow: Boolean,
    @SerializedName("isRequested") var isRequested: Boolean,
    @SerializedName("petPic") val petPic: String,
)