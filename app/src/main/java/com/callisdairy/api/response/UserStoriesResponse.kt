package com.callisdairy.api.response


import com.google.gson.annotations.SerializedName

class UserStoriesResponse  {
    @SerializedName("result")
    val result: UserStoriesResult = UserStoriesResult()
    @SerializedName("responseMessage")
    val responseMessage: String = ""
    @SerializedName("responseCode")
    val responseCode: Int = 0
}

class UserStoriesResult  {
    @SerializedName("docs")
    val docs: List<UserStoriesDoc> = listOf()
}

class UserStoriesDoc  {
//    @SerializedName("city")
//    val city: String = ""
//    @SerializedName("state")
//    val state: String = ""
//    @SerializedName("country")
//    val country: String = ""
    @SerializedName("userName")
    val userName: String = ""
//    @SerializedName("otpVerification")
//    val otpVerification: Boolean = false
    @SerializedName("profilePic")
    val profilePic: String = ""

    @SerializedName("petPic")
    val petPic: String = ""
//    @SerializedName("isSocial")
//    val isSocial: Boolean = false
//    @SerializedName("userType")
//    val userType: String = ""
//    @SerializedName("approveStatus")
//    val approveStatus: String = ""
//    @SerializedName("followers")
//    val followers: List<String> = listOf()
//    @SerializedName("followersCount")
//    val followersCount: Int = 0
//    @SerializedName("following")
//    val following: List<String> = listOf()
//    @SerializedName("interest")
//    val interest: List<UserStoriesAny> = listOf()
//    @SerializedName("likesProduct")
//    val likesProduct: List<UserStoriesAny> = listOf()
//    @SerializedName("likeServices")
//    val likeServices: List<UserStoriesAny> = listOf()
//    @SerializedName("favouritePets")
//    val favouritePets: List<UserStoriesAny> = listOf()
//    @SerializedName("likesUser")
//    val likesUser: List<UserStoriesAny> = listOf()
//    @SerializedName("likesUserCount")
//    val likesUserCount: Int = 0
//    @SerializedName("followingCount")
//    val followingCount: Int = 0
//    @SerializedName("privacyType")
//    val privacyType: String = ""
//    @SerializedName("whoCanSee")
//    val whoCanSee: List<UserStoriesAny> = listOf()
//    @SerializedName("status")
//    val status: String = ""
//    @SerializedName("points")
//    val points: Int = 0
    @SerializedName("isOnline")
    val isOnline: Boolean = false
//    @SerializedName("documents")
//    val documents: String = ""
    @SerializedName("_id")
    val id: String = ""
//    @SerializedName("address")
//    val address: String = ""
//    @SerializedName("countryCode")
//    val countryCode: String = ""
//    @SerializedName("countryIsoCode")
//    val countryIsoCode: String = ""
//    @SerializedName("dateOfBirth")
//    val dateOfBirth: String = ""
//    @SerializedName("email")
//    val email: String = ""
//    @SerializedName("gender")
//    val gender: String = ""
//    @SerializedName("mobileNumber")
//    val mobileNumber: String = ""
    @SerializedName("name")
    val name: String = ""
//    @SerializedName("password")
//    val password: String = ""
//    @SerializedName("petPic")
//    val petPic: String = ""
//    @SerializedName("stateIsoCode")
//    val stateIsoCode: String = ""
//    @SerializedName("zipCode")
//    val zipCode: String = ""
//    @SerializedName("coverPic")
//    val coverPic: String = ""
//    @SerializedName("otpTime")
//    val otpTime: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt")
    val updatedAt: String = ""
//    @SerializedName("__v")
//    val v: Int = 0
    @SerializedName("stories")
    val stories: List<UserStoriesStory> = listOf()

}


class UserStoriesStory  {
    @SerializedName("image")
    val image = videoResult()
    @SerializedName("videos")
    val videos = videoResult()
    @SerializedName("caption")
    val caption: String = ""
    @SerializedName("status")
    val status: String = ""
//    @SerializedName("seenUserId")
//    val seenUserId: List<UserStoriesAny> = listOf()
    @SerializedName("_id")
    val id: String = ""
    @SerializedName("userId")
    val userId: String = ""
    @SerializedName("createdAt")
    val createdAt: String = ""
    @SerializedName("updatedAt") val updatedAt: String = ""
    @SerializedName("__v")
    val v: Int = 0
}

class videoResult{
    @SerializedName("media") val media: String = ""
    @SerializedName("thumbnail") val thumbnail: String = ""
    @SerializedName("mediaType") val mediaType: String = ""
}

