package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ImageUploadResponse(
    @SerializedName("result") val result : ArrayList<ImageUploadResult>,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)
class ImageUploadResult(

    @SerializedName("mediaUrlMobile") val mediaUrl : String,
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("type") val mediaType : String
)