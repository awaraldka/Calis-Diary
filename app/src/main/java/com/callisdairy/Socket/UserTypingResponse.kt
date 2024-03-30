package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class UserTypingResponse {
    @SerializedName("response_code") val response_code : Int = 0
    @SerializedName("response_message") val response_message : String = ""
    @SerializedName("result") val result = UserTypingResult()
}

class UserTypingResult{
    @SerializedName("userId") val userId : String = ""
    @SerializedName("status") val status : Boolean = false

}