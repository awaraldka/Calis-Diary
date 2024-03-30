package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class VetOrDoctorResponse {
    @SerializedName("responseCode") var responseCode:Int = 0
    @SerializedName("responseMessage") var responseMessage:String = ""
    @SerializedName("result") var result =  VetOrDoctorResult()
}



class VetOrDoctorResult{
    @SerializedName("page") val page: Int = 0
    @SerializedName("limit") val limit: Int = 0
    @SerializedName("remainingItems") val remainingItems: Int = 0
    @SerializedName("count") val count: Int = 0
    @SerializedName("docs") val docs: List<VetOrDoctorDocs> = listOf()
    @SerializedName("totalPages") val totalPages: Int =0
}
class VetOrDoctorDocs{
    @SerializedName("city") val city : String = ""
    @SerializedName("userName") val userName : String = ""
    @SerializedName("state") val state : String = ""
    @SerializedName("country") val country : String = ""
    @SerializedName("otpVerification") val otpVerification : Boolean = false
    @SerializedName("profilePic") val profilePic : String = ""
    @SerializedName("coverPic") val coverPic : String = ""
    @SerializedName("petPic") val petPic : String = ""
    @SerializedName("isSocial") val isSocial : Boolean = false
    @SerializedName("userType") val userType : String = ""
    @SerializedName("approveStatus") val approveStatus : String = ""
    @SerializedName("followersCount") val followersCount : Int= 0
    @SerializedName("likesUserCount") val likesUserCount : Int = 0
    @SerializedName("postLikesCount") val postLikesCount : Number = 0
    @SerializedName("followingCount") val followingCount : Int = 0

    @SerializedName("doctorVetProfile") val doctorVetProfile:doctorVetProfile  = doctorVetProfile()


    @SerializedName("status") val status : String = ""
    @SerializedName("_id") val _id : String = ""
    @SerializedName("name") val name : String = ""
    @SerializedName("points") val points : Number = 0
    @SerializedName("totalPosts") val totalPosts : Number = 0
    @SerializedName("countryCode") val countryCode : String = ""
    @SerializedName("mobileNumber") val mobileNumber : String = ""
    @SerializedName("email") val email : String = ""
    @SerializedName("address") val address : String = ""
    @SerializedName("password") val password : String = ""
    @SerializedName("dateOfBirth") val dateOfBirth : String = ""
    @SerializedName("gender") val gender : String = ""
    @SerializedName("isYou") val isYou : Boolean = false
    @SerializedName("isRequested") val isRequested : Boolean = false
    @SerializedName("isFollowed") val isFollowed : Boolean = false
    @SerializedName("zipCode") val zipCode : String = ""
    @SerializedName("countryIsoCode") val countryIsoCode : String = ""
    @SerializedName("stateIsoCode") val stateIsoCode : String = ""
    @SerializedName("otpTime") val otpTime : String = ""
    @SerializedName("createdAt") val createdAt : String = ""
    @SerializedName("updatedAt") val updatedAt : String = ""
    @SerializedName("__v") val __v : Int = 0
    @SerializedName("deviceType") val deviceType : String = ""
    @SerializedName("privacyType") val privacyType : String = ""
}




