package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class LogInResponse(


    @SerializedName("result") val result : LoginResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class LoginResult(
    @SerializedName("_id") val _id : String,
    @SerializedName("email") val email : String,
    @SerializedName("token") val token : String,
    @SerializedName("profileType") val profileType : String,
    @SerializedName("name") val name : String,
    @SerializedName("profileId") val profileId : String,
    @SerializedName("userType") val userType : String,
    @SerializedName("status") val status : String,
    @SerializedName("otpVerification") val otpVerification : Boolean,
    @SerializedName("isSuggested") val isSuggested : Boolean,
    @SerializedName("isDefaultUserProfileSet") val isDefaultUserProfileSet : Boolean,
    @SerializedName("noOfLoginCount") val noOfLoginCount : Number,

    )