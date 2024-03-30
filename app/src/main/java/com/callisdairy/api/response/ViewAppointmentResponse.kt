package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class ViewAppointmentResponse (
    @SerializedName("result") val result: ViewAppointment,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val responseCode: Int
)



data class ViewAppointment(
    @SerializedName("isBlocked") val isBlocked:Boolean,
    @SerializedName("symptoms") val symptoms: List<String>,
    @SerializedName("status") val status: String,
    @SerializedName("appointmentStatus") val appointmentStatus: String,
    @SerializedName("_id") val id: String,
    @SerializedName("appointmentDate") val appointmentDate: String,
    @SerializedName("email") val email: String,
    @SerializedName("docId") val docId: Doctor,
    @SerializedName("slot") val slot: String,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("consultingType") val consultingType: String,
    @SerializedName("userId") val userId: AppointmentListUserId,
    @SerializedName("petName") val petName: String,
    @SerializedName("landLine") val landLine: String,
    @SerializedName("faxNumber") val faxNumber: String,
    @SerializedName("vaccinationDate") val vaccinationDate: String,
    @SerializedName("reasonForVisit") val reasonForVisit: String,
    @SerializedName("medicalCondition") val medicalCondition: String,
    @SerializedName("currentMedication") val currentMedication: String,
    @SerializedName("dose") val dose: String,
    @SerializedName("diet") val diet: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("aboutGender") val aboutGender: String,
    @SerializedName("howHere") val howHere: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int,
    @SerializedName("isRated") val isRated: Boolean,
    @SerializedName("feedback") val feedback: feedback,

)

class feedback(
    @SerializedName("rating")val rating:String,
    @SerializedName("message")val message:String
)

