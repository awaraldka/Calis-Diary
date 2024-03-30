package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

data class InterestedUserResponse(

    @SerializedName("result") val result: InterestedUserResult? =null,
    @SerializedName("responseMessage") val responseMessage: String? =null,
    @SerializedName("responseCode") val responseCode: Int? =null
)

data class InterestedUserResult(
    @SerializedName("docs") val docs : ArrayList<InterestedUserDocs>? = null,
    @SerializedName("total") val total : Int? = null,
    @SerializedName("limit") val limit : Int? = null,
    @SerializedName("page") val page : Int? = null,
    @SerializedName("pages") val pages : Int? = null,
)


data class InterestedUserDocs(
    @SerializedName("_id") val _id : String? = null,
    @SerializedName("createdAt") val createdAt : String? = null,
    @SerializedName("productId") val productId : InterestedUserProductId? = null,
    @SerializedName("serviceId") val serviceId : InterestedUserServiceId? = null,
    @SerializedName("petId") val petId : petIdInterestedUser? = null,
    @SerializedName("userId") val userId : IntrestedPetUserId,
    )

data class InterestedUserProductId(
    @SerializedName("productImage") val productImage : ArrayList<String>? = null,
    @SerializedName("productName") val productName : String? = null,
    @SerializedName("productGenerateId") val productGenerateId : String? = null
)

data class InterestedUserServiceId(
    @SerializedName("serviceImage") val serviceImage : ArrayList<String>? = null,
    @SerializedName("serviceName") val serviceName : String? = null,
    @SerializedName("serviceGenerateId") val serviceGenerateId : String? = null,

    )

data class petIdInterestedUser(
    @SerializedName("petName") val petName : String? = null,
    @SerializedName("breed") val breed : String? = null,
    @SerializedName("mediaUrls") val mediaUrls : ArrayList<MediaUrls>? = null,

    )