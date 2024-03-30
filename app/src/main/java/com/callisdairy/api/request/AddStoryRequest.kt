package com.callisdairy.api.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

class AddStoryRequest {

    @SerializedName("storyArray") var storyArray=  ArrayList<story>()
}

class story{
    @SerializedName("image") var image = ArrayList<MultipartBody.Part>()
    @SerializedName("videos") var videos= ArrayList<MultipartBody.Part>()
    @SerializedName("caption") var caption:String = ""
}

class MediaRequest {
    @SerializedName("thumbnail")
    var thumbnail : String = ""
    @SerializedName("mediaUrlMobile")
    var mediaUrlMobile : String= ""
    @SerializedName("mediaUrlWeb")
    var mediaUrlWeb : String= ""
}
