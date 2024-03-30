package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class AddPostResponse(
//    @SerializedName("result") val result : AddPostResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

