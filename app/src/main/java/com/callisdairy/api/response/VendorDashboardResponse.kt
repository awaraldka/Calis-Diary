package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class VendorDashboardResponse {
    @SerializedName("responseCode") var statusCode:Int = 0
    @SerializedName("responseMessage") var responseMessage:String = ""
    @SerializedName("result") var result =  VendorDashboardResult()
}

class VendorDashboardResult{
    @SerializedName("totalProduct") var totalProduct:Int = 0
    @SerializedName("approveProduct") var approveProduct:Int = 0
    @SerializedName("rejectProduct") var rejectProduct:Int = 0
    @SerializedName("pendingProduct") var pendingProduct:Int = 0
    @SerializedName("totalService") var totalService:Int = 0
    @SerializedName("approveService") var approveService:Int = 0
    @SerializedName("rejectService") var rejectService:Int = 0
    @SerializedName("pendingService") var pendingService:Int = 0
    @SerializedName("clientInterestedInService") var clientInterestedInService:Int = 0
    @SerializedName("clientInterestedInProduct") var clientInterestedInProduct:Int = 0
    @SerializedName("clientInterestedInPet") var clientInterestedInPet:Int = 0
    @SerializedName("totalPet") var totalPet:Int = 0
    @SerializedName("totalPublished") var totalPublished:Int = 0
    @SerializedName("totalUnpublished") var totalUnpublished:Int = 0
    @SerializedName("totalAppointments") var totalAppointments:Int = 0
    @SerializedName("totalCompletedAppointments") var totalCompletedAppointments:Int = 0
    @SerializedName("totalApproved") var totalApproved:Int = 0
    @SerializedName("totalPending") var totalPending:Int = 0
    @SerializedName("totalUpcoming") var totalUpcoming:Int = 0
    @SerializedName("totalTelehealth") var totalTelehealth:Int = 0
    @SerializedName("totalInperson") var totalInperson:Int = 0
    @SerializedName("totalInpersonConfirmed") var totalInpersonConfirmed:Int = 0
    @SerializedName("totalTelehealthConfirmed") var totalTelehealthConfirmed:Int = 0

}

