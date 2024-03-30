package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class PetCategoryListResponse(
    @SerializedName("result") val result : PetCategoryListResult,
    @SerializedName("responseMessage") val responseMessage : String,
    @SerializedName("responseCode") val responseCode : Int
)


class PetCategoryListResult(
    @SerializedName("docs") val docs : ArrayList<PetCategoryListDocs>,
    @SerializedName("total") val total : Int,
    @SerializedName("limit") val limit : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int
)

class PetCategoryListDocs(
    @SerializedName("isSubCategoryCreated") val isSubCategoryCreated : Boolean,
    @SerializedName("status") val status : String,
    @SerializedName("_id") val _id : String,
    @SerializedName("categoryType") val categoryType : String,
    @SerializedName("categoryName") val categoryName : String,
    @SerializedName("subCategoryName") val subCategoryName : String,
    @SerializedName("categoryImage") val categoryImage : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)