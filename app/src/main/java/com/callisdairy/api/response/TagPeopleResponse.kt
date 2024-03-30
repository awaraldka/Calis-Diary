package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class TagPeopleResponse(
    @SerializedName("result") val result : TagPeopleResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val statusCode : Int
)

class TagPeopleResult(
    @SerializedName("docs") val docs : ArrayList<TagPeopleDocs>
)

class TagPeopleDocs(
    @SerializedName("_id") val _id : String,
    @SerializedName("name") val name : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("petPic") val petPic : String,
    @SerializedName("profilePic") val profilePic : String,
    var isSelected : Boolean = false
)