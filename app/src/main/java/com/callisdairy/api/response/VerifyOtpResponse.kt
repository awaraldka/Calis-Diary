package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName


class VerifyOtpResponse{
    @SerializedName("responseCode") var statusCode:Int = 0
    @SerializedName("responseMessage") var responseMessage:String = ""
    @SerializedName("result") var result =  VerifyOtpResult()
}


class VerifyOtpResult {
    @SerializedName("_id") var _id:String = ""
    @SerializedName("email") var email:String = ""
    @SerializedName("countryCode") var countryCode:String = ""
    @SerializedName("mobileNumber") var mobileNumber:String = ""
    @SerializedName("token") var token:String = ""
    @SerializedName("otpVerification") var otpVerification:Boolean = false
}