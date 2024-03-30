package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ProductsResponse(
    @SerializedName("responseMessage") val responseMessage : String?= null,
    @SerializedName("result") val result : ProductsListResult,
    @SerializedName("responseCode") val responseCode : Int
)


class ProductsListResult(
    @SerializedName("docs") val docs : ArrayList<ProductsListDocs>,
    @SerializedName("total") val total : Int?= null,
    @SerializedName("limit") val limit : Int?= null,
    @SerializedName("page") val page : Int?= null,
    @SerializedName("pages") val pages : Int?= null
)

class ProductsListDocs(
    @SerializedName("productName") val productName : String?= null,
    @SerializedName("serviceName") val serviceName : String?= null,
    @SerializedName("approveStatus") val approveStatus : String?= null,
    @SerializedName("createdAt") val createdAt : String?= null,
    @SerializedName("productGenerateId") val productGenerateId : String?= null,
    @SerializedName("serviceGenerateId") val serviceGenerateId : String?= null,
    @SerializedName("subCategoryId") val subCategoryId : subCategoryIdResult?= null,
    @SerializedName("categoryId") val categoryId : categoryIdResult?= null,
    @SerializedName("_id") val _id : String?= null,


)

class subCategoryIdResult(
    @SerializedName("subCategoryName") val subCategoryName : String?= null,

    )
class categoryIdResult(
    @SerializedName("categoryName") val categoryName : String?= null,
    )