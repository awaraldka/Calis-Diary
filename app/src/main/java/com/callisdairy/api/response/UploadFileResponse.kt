package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class UploadFileResponse(
    @SerializedName("result") val result : ArrayList<UploadFileResult>,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class UploadFileResult(
    @SerializedName("mediaUrl") var mediaUrl:MediaResultHome,
    @SerializedName("mediaType") var mediaType:String

)