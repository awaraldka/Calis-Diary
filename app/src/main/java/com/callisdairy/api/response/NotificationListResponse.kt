package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class NotificationListResponse(
    @SerializedName("result") val result: ResultNotification,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val responseCode: Int
)


class ResultNotification(
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("remainingItems") val remainingItems : Int,
    @SerializedName("count") val count : Int,
    @SerializedName("docs") val docs : ArrayList<NotificationListResult>,
    @SerializedName("totalPages") val totalPages : Int
)


class NotificationListResult(
    @SerializedName("isRead") val isRead: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("userId") val userId: NotificationUserId,
    @SerializedName("actionBy") val actionBy: actionBy,
    @SerializedName("postId") val postId: postIdNotification,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("notificationType") val notificationType: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int
)


class actionBy(
    @SerializedName("_id") val _id: String,
    @SerializedName("petPic") val petPic: String,
    @SerializedName("profilePic") val profilePic: String,
    @SerializedName("userName") val userName: String,

    )

class NotificationUserId(
    @SerializedName("city") val city: String,
    @SerializedName("petPic") val petPic: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
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
    @SerializedName("_id") val _id: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("address") val address: String,
    @SerializedName("zipCode") val zipCode: Number,
    @SerializedName("gender") val gender: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("otpTime") val otpTime: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("deviceType") val deviceType: String,
    @SerializedName("coverPic") val coverPic: String,
    @SerializedName("countryIsoCode") val countryIsoCode: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String,
    @SerializedName("stateIso      Code") val stateIsoCode: String
)

class postIdNotification(
    @SerializedName("status") val status : String,
    @SerializedName("postType") val postType : String,
    @SerializedName("address") val address : String,
    @SerializedName("userPostHide") val userPostHide : ArrayList<String>,
    @SerializedName("lat") val lat : Number,
    @SerializedName("long") val long : Number,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>,
    @SerializedName("_id") val _id : String,
    @SerializedName("title") val title : String,
    @SerializedName("caption") val caption : String,
    @SerializedName("petCategoryId") val petCategoryId : String,
    @SerializedName("userId") val userId : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)