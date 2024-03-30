package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ExploreListResponse (
    @SerializedName("responseCode") var statusCode:Int = 0,
    @SerializedName("responseMessage") var responseMessage:String = "",
    @SerializedName("result") var result:ExploreListResult
    )

class ExploreListResult(
    @SerializedName("docs") var docs:ArrayList<ExploreListDocs>  = ArrayList(),
    @SerializedName("total") val total : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("pages") val pages : Int
    )

class ExploreListDocs(
    @SerializedName("media") val media  : MediaResultExplore,
    @SerializedName("status") val status:String = "",
    @SerializedName("_id") val _id:String = "",
    @SerializedName("title") val title:String = "",
    @SerializedName("description") val description:String = "",
)

class MediaResultExplore (
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("mediaUrlMobile") val mediaUrlMobile : String,
    @SerializedName("mediaUrlWeb") val mediaUrlWeb : String,
    @SerializedName("mediaType") val mediaType : String
)
