package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

data class ViewPetResponse(
    @SerializedName("result") val result: ViewPetResult,
    @SerializedName("responseMessage") val responseMessage: String?= null,
    @SerializedName("responseCode") val responseCode: Int
)


data class ViewPetResult(
    @SerializedName("petDescription") val petDescription: ViewPetPetDescription,
    @SerializedName("description") val description: String,
    @SerializedName("favUser") val favUser: ArrayList<String>,
    @SerializedName("status") val status: String?= null,
    @SerializedName("isMarketPlace") val isMarketPlace: Boolean?= null,
    @SerializedName("_id") val _id: String?= null,
    @SerializedName("subCategoryId") val subCategoryResult: subCategoryDataResult? = null,
    @SerializedName("categoryId") val categoryResult:categoryDataResult? = null,
    @SerializedName("petName") val petName: String?= null,
    @SerializedName("petImage") val petImage: ArrayList<String>,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("dob") val dob: String?= null,
    @SerializedName("origin") val origin: String?= null,
    @SerializedName("gender") val gender: String?= null,
    @SerializedName("breed") val breed: String?= null,
    @SerializedName("language") val language: String?= null,
    @SerializedName("purchesStore") val purchesStore: String?= null,
    @SerializedName("price") val price: Number,
    @SerializedName("lat") val lat: Number,
    @SerializedName("long") val long: Number,
    @SerializedName("userId") val userId: ViewPetUserId,
    @SerializedName("createdAt") val createdAt: String?= null,
    @SerializedName("updatedAt") val updatedAt: String?= null,
    @SerializedName("__v") val __v: Int,
    @SerializedName("dietFreq") val dietFreq: String?= null,
    @SerializedName("travel") val travel: String?= null,
    @SerializedName("movie") val movie: String?= null,
    @SerializedName("song") val song: String?= null,
    @SerializedName("celebrate") val celebrate: String?= null,
    @SerializedName("veterinary") val veterinary: String?= null,
    @SerializedName("veterinaryType") val veterinaryType: String?= null,
    @SerializedName("doctorAppoint") val doctorAppoint: String?= null,
    @SerializedName("petType") val petType: String?= null,
    @SerializedName("favColour") val favColour: String?= null,
    @SerializedName("habit") val habit: String?= null,
    @SerializedName("favFood") val favFood: String?= null,
    @SerializedName("favPlace") val favPlace: String?= null,
    @SerializedName("experience") val experience: String?= null,
    @SerializedName("percentage") val percentage: Int?= null,
    @SerializedName("animalShelter") val animalShelter: String?= null,
    @SerializedName("medicalReport") val medicalReport: String?= null,
    @SerializedName("habits") val habits: String?= null,
    @SerializedName("awards") val awards: String?= null,
    @SerializedName("favoriteClimate") val favoriteClimate: String?= null,
    @SerializedName("placeOfBirth") val placeOfBirth: String?= null,
    @SerializedName("commonDiseases") val commonDiseases: String?= null,
    @SerializedName("insurance") val insurance: String?= null,
    @SerializedName("toy") val toy: String?= null,
    @SerializedName("petAddress") val petAddress: String?= null,
    @SerializedName("bestFriend") val bestFriend: String?= null,
    @SerializedName("placeOfTravel") val placeOfTravel: String?= null,
    @SerializedName("petCategoryId") val petCategoryId: petCategoryId?= null,
    @SerializedName("petBreedId") val petBreedIdRes: petBreedId?= null,


)

data  class ViewPetPetDescription(
    @SerializedName("size") val size: String?= null,
    @SerializedName("lastVaccinate") val lastVaccinate: String?= null
)

data class ViewPetUserId(
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


data class subCategoryDataResult(
    @SerializedName("subCategoryName") val subCategoryName: String?= null,
    @SerializedName("_id") val _id: String?= null,
)

data class categoryDataResult(
    @SerializedName("_id") val _id: String?= null,
    @SerializedName("categoryName") val categoryName: String?= null,

    )

data class petBreedId(
    @SerializedName("_id") val _id: String?= null,

    )