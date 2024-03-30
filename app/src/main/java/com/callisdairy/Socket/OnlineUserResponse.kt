package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class OnlineUserResponse {
    @SerializedName("response_code") val response_code : Int = 0
    @SerializedName("response_message") val response_message : String = ""
    @SerializedName("result") val result = ArrayList<OnlineUserResult>()
}

class OnlineUserResult{
    @SerializedName("userId") val userId : String= ""
    @SerializedName("status") val status : String= ""
    @SerializedName("socketId") val socketId : String= ""
    @SerializedName("userName") val userName : String= ""
    @SerializedName("profilePic") val profilePic : String= ""
}