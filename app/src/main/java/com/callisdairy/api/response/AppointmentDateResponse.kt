package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

data class AppointmentDateResponse(

    @SerializedName("result") val result: List<AppointmentDateResult>,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val statusCode: Int
)

data class AppointmentDateResult(

    @SerializedName("allSlotTimes") val allSlotTimes: List<AppointmentAllSlotTimes>,
    @SerializedName("status") val status: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("date") val date: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("day") val day: String,
    @SerializedName("updatedAt") val updatedAt: String
)


data class AppointmentAllSlotTimes(

    @SerializedName("time") val time: String,
    @SerializedName("slotStatus") val slotStatus: Boolean,
    var flag: Boolean? = false,
    var flag1: Boolean? = false,
    var counter: Int = 0


)

