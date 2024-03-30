package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class CommentListResponse(
    @SerializedName("result") val result: CommentListResult,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val responseCode: Int
)


class CommentListResult(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("remainingItems") val remainingItems: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("docs") val docs: ArrayList<CommentListDocs>,
    @SerializedName("mediaUrls") val mediaUrls: ArrayList<MediaUrls>,
    @SerializedName("totalPages") val totalPages: Int
)


class CommentListDocs(

    @SerializedName("type") val type: String,
    @SerializedName("status") val status: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("userId") val userId: CommentListUserId,
//    @SerializedName("postId") val postId: postIdResult,
    @SerializedName("comment") var comment: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("likeCount") var likeCount: Int,
    @SerializedName("repliesCount") var repliesCount: Int,
    @SerializedName("isLiked") var isLiked: Boolean
)


class CommentListUserId(
    @SerializedName("city") val city: String,
    @SerializedName("petPic") val petImage: String,
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
    @SerializedName("zipCode") val zipCode: String,
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
)


class postIdResult(
    @SerializedName("mediaUrls") val mediaUrls: ArrayList<MediaUrlsComment>

)


class MediaUrlsComment(
    @SerializedName("media") val media: MediaResultComment,
    @SerializedName("type") val type: String

)


class MediaResultComment(

    @SerializedName("type") val type: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("mediaUrlMobile") val mediaUrlMobile: String,
    @SerializedName("mediaUrlWeb") val mediaUrlWeb: String
)
