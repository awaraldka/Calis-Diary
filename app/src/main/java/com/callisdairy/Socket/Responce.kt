package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class Responce {

    @SerializedName("result")
    lateinit var result: ResultChat

    @SerializedName("responseMessage")
    val responseMessage: String? = null

    @SerializedName("responseCode")
    val responseCode: Int? = null
}

data class ResultChat(
    @SerializedName("mediaUrl") val mediaUrl: String,
    @SerializedName("mediaType") val mediaType: String,
    @SerializedName("emailType") val emailType: String,
    @SerializedName("phoneNumberType") val phoneNumberType: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("otpVerification") val otpVerification: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("request") val request: String,
    @SerializedName("responseStatus") val responseStatus: String,
    @SerializedName("profileType") val profileType: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("email") val email: String,
    @SerializedName("fromEmail") val fromEmail: String,
    @SerializedName("toEmail") val toEmail: String,
    @SerializedName("description") val description: String,
    @SerializedName("reportType") val reportType: String,
    @SerializedName("password") val password: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("deviceType") val deviceType: String,
    @SerializedName("socialLinks") var socialLinks: SocialLinks,
    @SerializedName("profileResult") var profileResult: ProfileResult,
    @SerializedName("otp") val otp: Int,
    @SerializedName("otpTime") val otpTime: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("result") val otpresult: OtpResult,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val responseCode: Int,
    @SerializedName("__v") val __v: Int,
    @SerializedName("isReset") val isReset: Boolean,
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("followerId") val followerId: String,
    @SerializedName("postId") val postId: String,
    @SerializedName("userResult") val userResult: UserResult,
    @SerializedName("followerCount") val followerCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
    @SerializedName("docs") val docs: List<Docs>,
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("categoryResult") val categoryResult: ArrayList<CategoryResult>,
    @SerializedName("postResult") val postResult : PostResult,
    @SerializedName("likeCount") val likeCount : Int,
    @SerializedName("commentCount") val commentCount : Int,
    @SerializedName("isLike") val isLike : Boolean,
    @SerializedName("isSave") val isSave : Boolean,
    @SerializedName("isFollow") val isFollow : Boolean,
    @SerializedName("commentList") val commentList : List<CommentList>,
    @SerializedName("messages") val messages : ArrayList<MessagesChat>,
    @SerializedName("mediaResult") val mediaResult : List<MediaResult>,
    @SerializedName("location") val location : Location,
    @SerializedName("imageLinks") val imageLinks : List<String>,
    @SerializedName("thumbNail") val thumbNail : String,
    @SerializedName("shareCount") val shareCount : Int,
    @SerializedName("videoLink") val videoLink : String,
    @SerializedName("address") val address : String,
    @SerializedName("categoryId") val categoryId : String

)

data class Replies (

    @SerializedName("commentType") val commentType : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("postId") val postId : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("commentId") val commentId : String,
    @SerializedName("comment") val comment : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("isLike") val isLike : Boolean,
    @SerializedName("likeCount") val likeCount : Int

)

data class CommentList (

    @SerializedName("commentType") val commentType : String,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("postId") val postId : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("comment") val comment : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    @SerializedName("replies") val replies : List<Replies>,
    @SerializedName("replyCount") val replyCount : Int,
    @SerializedName("isLike") val isLike : Boolean,
    @SerializedName("likeCount") val likeCount : Int


    )

data class MessagesChat (

    @SerializedName("mediaType") val mediaType : String,
    @SerializedName("messageStatus") val messageStatus : String,
    @SerializedName("message") val message : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("receiverId") val receiverId : String,
    @SerializedName("createdAt") val createdAt : String


)
data class ProfileResult (

    @SerializedName("socialLinks") val socialLinks : SocialLinks,
    @SerializedName("emailType") val emailType : String,
    @SerializedName("phoneNumberType") val phoneNumberType : String,
    @SerializedName("userType") val userType : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
    @SerializedName("status") val status : String,
    @SerializedName("profileType") val profileType : String,
    @SerializedName("notification") val notification : Boolean,
    @SerializedName("inActive") val inActive : Boolean,
    @SerializedName("_id") val _id : String,
    @SerializedName("bio") val bio : String,
    @SerializedName("deviceType") val deviceType : String,
    @SerializedName("email") val email : String,
    @SerializedName("fullName") val fullName : String,
    @SerializedName("password") val password : String,
    @SerializedName("profilePic") val profilePic : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("otp") val otp : Int,
    @SerializedName("otpTime") val otpTime : Long,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,
    )

data class PostResult (

    @SerializedName("location") val location : Location,
    @SerializedName("mediaType") val mediaType : String,
    @SerializedName("description") val description : String,
    @SerializedName("imageLinks") val imageLinks : List<String>,
    @SerializedName("status") val status : String,
    @SerializedName("thumbNail") val thumbNail : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("videoLink") val videoLink : String,
    @SerializedName("userId") val userId : UserId,
    @SerializedName("categoryId") val categoryId : CategoryId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("address") val address: String,

    @SerializedName("__v") val __v : Int
)

data class Location(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<Double>
)

data class UserId(

    @SerializedName("_id") val _id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("profilePic") val profilePic: String
)

data class CategoryId(

    @SerializedName("_id") val _id: String,
    @SerializedName("categoryName") val categoryName: String
)

data class Docs(
    @SerializedName("status") val status: String,
    @SerializedName("request") val request: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("followerIdd") val followerIdd: FollowerId,
    @SerializedName("followerId") val followerId: String,
    @SerializedName("userId") val userId: UserId,
    @SerializedName("postId") val postId: PostId,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,

    @SerializedName("location") val location : Location,
    @SerializedName("mediaType") val mediaType : String,
    @SerializedName("description") val description : String,
    @SerializedName("imageLinks") val imageLinks : List<String>,
    @SerializedName("thumbNail") val thumbNail : String,
    @SerializedName("videoLink") val videoLink : String,
    @SerializedName("categoryId") val categoryId : String

)


data class PostId(

    @SerializedName("location") val location: Location,
    @SerializedName("mediaType") val mediaType: String,
    @SerializedName("imageLinks") val imageLinks: List<String>,
    @SerializedName("status") val status: String,
    @SerializedName("thumbNail") val thumbNail: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("videoLink") val videoLink: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("description") val description : String,
    @SerializedName("shareCount") val shareCount : Int,
    @SerializedName("address") val address : String
)


data class FollowerId(
    @SerializedName("_id") val _id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("profilePic") val profilePic: String
)

data class UserResult(
    @SerializedName("socialLinks") val socialLinks: SocialLinks,
    @SerializedName("emailType") val emailType: String,
    @SerializedName("phoneNumberType") val phoneNumberType: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("otpVerification") val otpVerification: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("profileType") val profileType: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("deviceType") val deviceType: String,
    @SerializedName("profilePic") val profilePic: String,
    @SerializedName("otp") val otp: Int,
    @SerializedName("otpTime") val otpTime: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("isReset") val isReset: Boolean,
    @SerializedName("token") val token: String,
    @SerializedName("bio") val bio: String
)

data class OtpResult(
    @SerializedName("_id") val _id: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("otpVerification") val otpVerification: Boolean,
    @SerializedName("token") val token: String
)

data class SocialLinks(
    @SerializedName("facebook") val facebook: String,
    @SerializedName("twitter") val twitter: String,
    @SerializedName("instagram") val instagram: String,
    @SerializedName("youtube") val youtube: String
)


data class CategoryResult(

    @SerializedName("status") val status: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("image") val image: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    var flag: Boolean =  false,
//    ,
//    var flag: Boolean

)

data class MediaResult (
    @SerializedName("mediaUrl") val mediaUrl : String,
    @SerializedName("mediaType") val mediaType : String
)
