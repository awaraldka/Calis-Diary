package com.callisdairy.ModalClass

import com.callisdairy.api.request.documents
import com.callisdairy.api.response.PetImage
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class VendorHomeModalClass(
    var name : String,
    var image:Int,
    var total:Int
)



class TrackingDevice(
    var name: String,
    var url:String
)


    data class passVendorData(val name: String, val countryCode: String
    , val mobileNumber: String, val email: String,val address: String,val city: String,
                        val state: String,val country: String,val password: String,
                        val userTypes: String, val gender: String,
                        val zipCode: String,val profilePic: String,val countryIsoCode: String,
                        val stateIsoCode: String,val documents: String,val languageId: String,val profileLoad:String, val dob:String ) : Serializable



class Specialization(
    var name: String,
)


class ClinicHours(
    var day:String,
    var openTime: String = "",
    var closeTime: String = "",
    var isSelected: Boolean = false
)
class ClinicHoursRequest(
    var days:String,
    var openTime: String = "",
    var closeTime: String = "",
    var isSelected: Boolean = false
)

class ProblemSymptoms(
    var name:String,
    var isSelected: Boolean = false,
    var other:String = ""
)


class feedbackDetails(
    val name: String,
    val Ratings: String,
    val feedback: String,
    val Date: String,
    val userImage: String,
    val petImage: String,
): Serializable




