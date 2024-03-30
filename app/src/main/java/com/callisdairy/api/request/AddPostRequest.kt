package com.callisdairy.api.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

class AddPostRequest {


    @SerializedName("address")
    var address: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("metaWords")
    var metaWords = ArrayList<String>()

    @SerializedName("tagPeople")
    var tagPeople = ArrayList<String>()

    @SerializedName("files")
    var file = ArrayList<MultipartBody.Part>()

    @SerializedName("caption")
    var caption : String = ""

    @SerializedName("lat")
    var lat :Double = 0.0

    @SerializedName("long")
    var long :Double = 0.0



}