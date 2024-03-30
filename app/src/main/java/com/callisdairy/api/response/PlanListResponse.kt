package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class PlanListResponse {
    @SerializedName("result") val result : PlanListResult= PlanListResult()
    @SerializedName("responseMessage") val responseMessage : String = ""
    @SerializedName("responseCode") val responseCode : Int = 0
}

class PlanListResult{
    @SerializedName("docs") val docs : List<PlanListDoc> = listOf()
}


class  PlanListDoc{
    @SerializedName("planName") val planName:String = ""
    @SerializedName("description") val description:String = ""
    @SerializedName("_id") val id:String = ""
    @SerializedName("missingPetCount") val missingPetCount:Number = 0
    @SerializedName("productCount") val productCount:Number = 0
    @SerializedName("serviceCount") val serviceCount:Number = 0
    @SerializedName("petCount") val petCount:Number = 0
    @SerializedName("eventCount") val eventCount:Number = 0
    @SerializedName("userType") val userType:String = ""
    @SerializedName("type") val type:String = ""
}