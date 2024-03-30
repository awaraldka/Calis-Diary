package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class CheckOnlineUserResponse {
    @SerializedName("response_code") val response_code : Int = 0
    @SerializedName("response_message") val response_message : String =  ""
    @SerializedName("result") val result = CheckOnlineUserResult()
}


class CheckOnlineUserResult{

    @SerializedName("isOnline") val isOnline : Boolean = false
    @SerializedName("_id") val _id : String = ""
}
