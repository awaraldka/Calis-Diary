package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ListCategoryResponse (
    @SerializedName("result") val result : ListCategoryResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val statusCode : Int
    )


class ListCategoryResult(
    @SerializedName("docs") val docs : ArrayList<ListCategoryDocs>,
    @SerializedName("total") val total : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages ") val pages : Int,
)


class ListCategoryDocs(
    @SerializedName("isSubCategoryCreated") val isSubCategoryCreated : Boolean,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("categoryName") val categoryName : String,
    @SerializedName("categoryType") val categoryType : String,
    @SerializedName("categoryImage") val categoryImage : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int,

)
