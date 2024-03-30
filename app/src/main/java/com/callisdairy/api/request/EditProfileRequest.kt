package com.callisdairy.api.request

import com.callisdairy.ModalClass.ClinicHours
import com.google.gson.annotations.SerializedName

class EditProfileRequest{
    @SerializedName("name") var name:String = ""
    @SerializedName("countryCode") var countryCode:String = ""
    @SerializedName("mobileNumber") var mobileNumber:String = ""
    @SerializedName("email") var email:String = ""
    @SerializedName("address") var address:String = ""
    @SerializedName("city") var city:String = ""
    @SerializedName("state") var state:String = ""
    @SerializedName("country") var country:String = ""
    @SerializedName("userTypes") var userTypes:String = ""
    @SerializedName("dateOfBirth") var dateOfBirth:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("zipCode") var zipCode:String = ""
    @SerializedName("profilePic") var profilePic:String = ""
    @SerializedName("petPic") var petPic :String = ""
    @SerializedName("countryIsoCode") var countryIsoCode:String = ""
    @SerializedName("stateIsoCode") var stateIsoCode:String = ""
    @SerializedName("userName") var userName:String = ""
    @SerializedName("userDob") var userDob:String = ""
}
class EditProfileVendorRequest{
    @SerializedName("name") var name:String = ""
    @SerializedName("countryCode") var countryCode:String = ""
    @SerializedName("mobileNumber") var mobileNumber:String = ""
    @SerializedName("email") var email:String = ""
    @SerializedName("address") var address:String = ""
    @SerializedName("city") var city:String = ""
    @SerializedName("state") var state:String = ""
    @SerializedName("country") var country:String = ""
    @SerializedName("userTypes") var userTypes:String = ""
    @SerializedName("dateOfBirth") var dateOfBirth:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("zipCode") var zipCode:String = ""
    @SerializedName("userDob") var userDob:String = ""
    @SerializedName("profilePic") var profilePic:String = ""
    @SerializedName("petPic") var petPic :String = ""
    @SerializedName("countryIsoCode") var countryIsoCode:String = ""
    @SerializedName("stateIsoCode") var stateIsoCode:String = ""
}
class EditProfileVetDoctorRequest{
    @SerializedName("firstName") var firstName:String = ""
    @SerializedName("lastName") var lastName:String = ""
    @SerializedName("userDob") var userDob:String = ""
    @SerializedName("middleName") var middleName:String = ""
    @SerializedName("primarySpokenLanguage") var primarySpokenLanguage:String = ""
    @SerializedName("clinicAddress") var clinicAddress:String = ""
    @SerializedName("phoneNumber") var phoneNumber:String = ""
    @SerializedName("emergencyNumber") var emergencyNumber:String = ""
    @SerializedName("specialization") var specialization:String = ""
    @SerializedName("experience") var experience:String = ""
    @SerializedName("collegeUniversity") var collegeUniversity:String = ""
    @SerializedName("degreeType") var degreeType:String = ""
    @SerializedName("license") var license:String = ""
    @SerializedName("licenseExpiry") var licenseExpiry:String = ""
    @SerializedName("permit") var permit:String = ""
    @SerializedName("permitExpiry") var permitExpiry:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("city") var city:String = ""
    @SerializedName("state") var state:String = ""
    @SerializedName("country") var country:String = ""
    @SerializedName("countryIsoCode") var countryIsoCode:String = ""
    @SerializedName("stateIsoCode") var stateIsoCode:String = ""
    @SerializedName("userProfileImage") var profilePic:String = ""
    @SerializedName("certificateOfDoctor") var certificateOfDoctor:String = ""
    @SerializedName("clinicHours") var clinicHours:List<ClinicHours> = listOf()
    @SerializedName("lat") var lat: Double = 0.0
    @SerializedName("long") var long: Double = 0.0
}

  


