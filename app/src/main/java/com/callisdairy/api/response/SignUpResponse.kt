package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class SignUpResponse {
    @SerializedName("responseCode") var statusCode:Int = 0
    @SerializedName("responseMessage") var responseMessage:String = ""
    @SerializedName("result") var result =  SignUpResult()

}

class SignUpResult{

    @SerializedName("city") val city : String = ""
    @SerializedName("state") val state : String = ""
    @SerializedName("country") val country : String = ""
    @SerializedName("otpVerification") val otpVerification : Boolean = false
    @SerializedName("isVerify") val isVerify : Boolean = false
    @SerializedName("userType") val userType : String = ""
    @SerializedName("approveStatus") val approveStatus : String = ""
    @SerializedName("followers") val followers = ArrayList<String>()
    @SerializedName("followersCount") val followersCount : Int = 0
    @SerializedName("following") val following = ArrayList<String>()
    @SerializedName("interest") val interest = ArrayList<String>()
    @SerializedName("likesProduct") val likesProduct = ArrayList<String>()
    @SerializedName("likesUser") val likesUser = ArrayList<String>()
    @SerializedName("likesUserCount") val likesUserCount : Int = 0
    @SerializedName("followingCount") val followingCount : Int = 0
    @SerializedName("status") val status : String = ""
    @SerializedName("_id") val _id : String = ""
    @SerializedName("name") val name : String = ""
    @SerializedName("countryCode") val countryCode : String = ""
    @SerializedName("mobileNumber") val mobileNumber : String = ""
    @SerializedName("email") val email : String = ""
    @SerializedName("address") val address : String = ""
    @SerializedName("password") val password : String = ""
    @SerializedName("dateOfBirth") val dateOfBirth : String = ""
    @SerializedName("gender") val gender : String = ""
    @SerializedName("zipCode") val zipCode : String = ""
    @SerializedName("token") val token : String = ""
    @SerializedName("otpTime") val otpTime : String = ""
    @SerializedName("createdAt") val createdAt : String = ""
    @SerializedName("updatedAt") val updatedAt : String = ""
    @SerializedName("__v") val __v : Int = 0

}