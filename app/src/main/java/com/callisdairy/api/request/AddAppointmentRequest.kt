package com.callisdairy.api.request

import com.google.gson.annotations.SerializedName

class AddAppointmentRequest {
    @SerializedName("appointmentDate") var appointmentDate:String = ""
    @SerializedName("slot") var slot:String = ""
    @SerializedName("docId") var docId:String = ""
    @SerializedName("petName") var petName:String = ""
    @SerializedName("mobileNumber") var mobileNumber:String = ""
    @SerializedName("landLine") var landLine:String = ""
    @SerializedName("faxNumber") var faxNumber:String = ""
    @SerializedName("vaccinationDate") var vaccinationDate:String = ""
    @SerializedName("reasonForVisit") var reasonForVisit:String = ""
    @SerializedName("medicalCondition") var medicalCondition:String = ""
    @SerializedName("symptoms") var symptoms:ArrayList<String> = arrayListOf()
    @SerializedName("currentMedication") var currentMedication:String = ""
    @SerializedName("dose") var dose:String = ""
    @SerializedName("diet") var diet:String = ""
    @SerializedName("consultingType") var consultingType:String = ""
    @SerializedName("howHere") var howHere:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("aboutGender") var aboutGender:String = ""

}
