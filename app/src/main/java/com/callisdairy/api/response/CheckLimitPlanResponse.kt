package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class CheckLimitPlanResponse {


    @SerializedName("result") val result: CheckLimitPlanResult = CheckLimitPlanResult()
    @SerializedName("responseMessage") val responseMessage: String = ""
    @SerializedName("responseCode") val responseCode:Int = 0
}


class CheckLimitPlanResult{
    @SerializedName("remainingEvent") val remainingEvent:Number = 0
    @SerializedName("remainingMissedPet") val remainingMissedPet:Number = 0
    @SerializedName("remainingProductCount") val remainingProductCount:Number = 0
    @SerializedName("remainingPetCount") val remainingPetCount:Number = 0
    @SerializedName("remainingServiceCount") val remainingServiceCount:Number = 0



    @SerializedName("totalEvent") val totalEvent:Number = 0
    @SerializedName("totalMissedPet") val totalMissedPet:Number = 0
    @SerializedName("totalPetCount") val totalPetCount:Number = 0
    @SerializedName("totalServiceCount") val totalServiceCount:Number = 0
    @SerializedName("totalProductCount") val totalProductCount:Number = 0




}