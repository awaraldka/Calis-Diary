package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class GetSubscriptionDetailsResponse {
    @SerializedName("data") val result : GetSubscriptionDetailsResult =GetSubscriptionDetailsResult()
    @SerializedName("responseMessage") val responseMessage : String = ""
    @SerializedName("status") val status : Boolean = false
    @SerializedName("responseCode") val responseCode : Int = 0
}

class GetSubscriptionDetailsResult{
    @SerializedName("daysLeft") val daysLeft : String = ""

}