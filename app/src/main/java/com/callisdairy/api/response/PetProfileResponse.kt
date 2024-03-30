package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class PetProfileResponse(
    @SerializedName("result") val result : ArrayList<PetProfileResult>,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)

class PetProfileResult(
    @SerializedName("userProfileImage") val userProfileImage : String,
    @SerializedName("name") val name : String,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("userId") val userId : HomePageUserId,
    @SerializedName("petCategoryId") val petCategoryId : petCategoryId,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("isDefaultUserProfile") val isDefaultUserProfile: Boolean   ,
)

class petCategoryId(
    @SerializedName("petCategoryType") val petCategoryType : ArrayList<String>,
    @SerializedName("_id") val _id :String,
    @SerializedName("petCategoryName") val petCategoryName :String,

    )