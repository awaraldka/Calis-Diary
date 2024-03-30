package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class BannerListResponse(
    @SerializedName("result") val result : BannerListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class BannerListResult(
    @SerializedName("docs") val docs : ArrayList<BannerListDocs>,
    @SerializedName("totalPages") val totalPages : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)

class BannerListDocs(

    @SerializedName("bannerImage") val bannerImage : ArrayList<BannerImage>,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("bannerName") val bannerName : String,
    @SerializedName("description") val description : String,
    @SerializedName("link") val link : String,
    @SerializedName("duration") val duration : String,
    @SerializedName("thumbNail") val thumbNail : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

class BannerImage (

@SerializedName("type") val type : String,
@SerializedName("thumbnail") val thumbnail : String,
@SerializedName("mediaUrlMobile") val mediaUrlMobile : String,
@SerializedName("mediaUrlWeb") val mediaUrlWeb : String,
)

