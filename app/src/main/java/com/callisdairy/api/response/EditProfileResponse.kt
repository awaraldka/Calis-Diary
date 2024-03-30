package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class EditProfileResponse {
    @SerializedName("result")
    val result: EditProfileResult = EditProfileResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class  EditProfileResult{

    @SerializedName("city") val city : String = ""
    @SerializedName("userName") val userName : String = ""
    @SerializedName("state") val state : String = ""
    @SerializedName("country") val country : String = ""
    @SerializedName("otpVerification") val otpVerification : Boolean = false
    @SerializedName("isBlocked") val isBlocked : Boolean = false
    @SerializedName("profilePic") var profilePic : String = ""
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
    @SerializedName("userDob") val userDob : String = ""
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
    @SerializedName("averageRating") val averageRating : Number = 0
    @SerializedName("stateIsoCode") val stateIsoCode : String = ""
    @SerializedName("otpTime") val otpTime : String = ""
    @SerializedName("createdAt") val createdAt : String = ""
    @SerializedName("updatedAt") val updatedAt : String = ""
    @SerializedName("__v") val __v : Int = 0
    @SerializedName("deviceType") val deviceType : String = ""
    @SerializedName("privacyType") val privacyType : String = ""
    @SerializedName("userRatingScores") val userRatingScores : userRatingScores = userRatingScores()
}

class userRatingScores(
    @SerializedName("fiveStars") val fiveStars : Int = 0,
    @SerializedName("fourStars") val fourStars : Int = 0,
    @SerializedName("threeStars") val threeStars : Int = 0,
    @SerializedName("twoStars") val twoStars : Int = 0,
    @SerializedName("oneStars") val oneStars : Int = 0,

)


data class doctorVetProfile(
    @SerializedName("userProfileImage") val userProfileImage:String? = null,
    @SerializedName("certificateOfDoctor") val certificateOfDoctor:String? = null,
    @SerializedName("firstName") val firstName:String? = null,
    @SerializedName("lastName") val lastName:String? = null,
    @SerializedName("middleName") val middleName:String? = null,
    @SerializedName("primarySpokenLanguage") val primarySpokenLanguage:String? = null,
    @SerializedName("clinicAddress") val clinicAddress:String? = null,
    @SerializedName("phoneNumber") val phoneNumber:String? = null,
    @SerializedName("emergencyNumber") val emergencyNumber:String? = null,
    @SerializedName("specialization") val specialization:String? = null,
    @SerializedName("experience") val experience:String? = null,
    @SerializedName("collegeUniversity") val collegeUniversity:String? = null,
    @SerializedName("degreeType") val degreeType:String? = null,
    @SerializedName("license") val license:String? = null,
    @SerializedName("licenseExpiry") val licenseExpiry:String? = null,
    @SerializedName("permit") val permit:String? = null,
    @SerializedName("permitExpiry") val permitExpiry:String? = null,
    @SerializedName("status") val status:String? = null,
    @SerializedName("_id") val _id:String? = null,
    @SerializedName("userId") val userId:String? = null,
    @SerializedName("clinicHours") val clinicHours:ArrayList<clinicHours> = arrayListOf()
)


class clinicHours{
    @SerializedName("closeTime") val closeTime:String = ""
    @SerializedName("day") val day:String =""
    @SerializedName("isSelected") val isSelected:Boolean = false
    @SerializedName("openTime")val openTime:String =""
}


