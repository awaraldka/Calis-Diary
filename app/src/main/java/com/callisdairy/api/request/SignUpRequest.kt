package com.callisdairy.api.request

import com.callisdairy.ModalClass.ClinicHours
import com.google.gson.annotations.SerializedName

class SignUpRequest {
    @SerializedName("name") var name: String = ""
    @SerializedName("countryCode") var countryCode: String = ""
    @SerializedName("mobileNumber") var mobileNumber: String = ""
    @SerializedName("email") var email: String = ""
    @SerializedName("address") var address: String = ""
    @SerializedName("city") var city: String = ""
    @SerializedName("state") var state: String = ""
    @SerializedName("country") var country: String = ""
    @SerializedName("password") var password: String = ""
    @SerializedName("userTypes") var userTypes: String = ""
    @SerializedName("userName") var userName: String = ""
    @SerializedName("dateOfBirth") var dateOfBirth: String = ""
    @SerializedName("gender") var gender: String = ""
    @SerializedName("zipCode") var zipCode: String = ""
    @SerializedName("petPic") var petPic: String = ""
    @SerializedName("profilePic") var profilePic: String = ""
    @SerializedName("countryIsoCode") var  countryIsoCode: String = ""
    @SerializedName("stateIsoCode") var  stateIsoCode: String = ""
    @SerializedName("petBreedId") var  petBreedId: String = ""
    @SerializedName("petType") var  petType: String = ""
    @SerializedName("petName") var  petName: String = ""
    @SerializedName("petCategoryId") var  petCategoryId: String = ""
    @SerializedName("languageId") var  languageId: String = ""
    @SerializedName("userDob") var  userDob: String = ""
}



class SignUpRequestVendor (
    @SerializedName("name") var name: String = "",
    @SerializedName("countryCode") var countryCode: String = "",
    @SerializedName("mobileNumber") var mobileNumber: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("state") var state: String = "",
    @SerializedName("country") var country: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("userTypes") var userTypes: String = "",
    @SerializedName("gender") var gender: String = "",
    @SerializedName("zipCode") var zipCode: String = "",
    @SerializedName("profilePic") var profilePic: String = "",
    @SerializedName("countryIsoCode") var  countryIsoCode: String = "",
    @SerializedName("stateIsoCode") var  stateIsoCode: String = "",
    @SerializedName("documents") var  documents:documents = documents(),
    @SerializedName("languageId") var  languageId: String = "",
    @SerializedName("userDob") var  userDob: String = ""
    )

class SignUpRequestVendorDoctor (
    @SerializedName("name") var name: String = "",
    @SerializedName("countryCode") var countryCode: String = "",
    @SerializedName("mobileNumber") var mobileNumber: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("userDob") var userDob: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("state") var state: String = "",
    @SerializedName("country") var country: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("userTypes") var userTypes: String = "",
    @SerializedName("gender") var gender: String = "",
    @SerializedName("zipCode") var zipCode: String = "",
    @SerializedName("profilePic") var profilePic: String = "",
    @SerializedName("countryIsoCode") var  countryIsoCode: String = "",
    @SerializedName("stateIsoCode") var  stateIsoCode: String = "",
    @SerializedName("documents") var  documents:documents = documents(),
    @SerializedName("languageId") var  languageId: String = "",
    @SerializedName("doctorvetUserObj") var  doctorvetUserObj: DoctorvetUserObj = DoctorvetUserObj(),

    )

class DoctorvetUserObj {
    @SerializedName("userProfileImage") var userProfileImage: String = ""
    @SerializedName("firstName") var firstName : String = ""
    @SerializedName("lastName") var lastName: String = ""
    @SerializedName("middleName") var middleName: String = ""
    @SerializedName("primarySpokenLanguage") var primarySpokenLanguage: String = ""
    @SerializedName("clinicAddress") var clinicAddress: String = ""
    @SerializedName("clinicHours") var clinicHours: List<ClinicHours> = listOf()
    @SerializedName("phoneNumber") var phoneNumber: String = ""
    @SerializedName("emergencyNumber") var emergencyNumber: String = ""
    @SerializedName("specialization") var specialization: String = ""
    @SerializedName("experience") var experience: String = ""
    @SerializedName("collegeUniversity") var collegeUniversity: String = ""
    @SerializedName("degreeType") var degreeType: String = ""
    @SerializedName("license") var license: String = ""
    @SerializedName("licenseExpiry") var licenseExpiry: String = ""
    @SerializedName("permit") var permit: String = ""
    @SerializedName("permitExpiry") var permitExpiry: String = ""
    @SerializedName("certificateOfDoctor") var certificateOfDoctor: String = ""
    @SerializedName("lat") var lat: Double = 0.0
    @SerializedName("long") var long: Double = 0.0
}

class documents{
    @SerializedName("media") var  media: String = ""

}

class LoginRequest{
    @SerializedName("email") var email:String = ""
    @SerializedName("password") var password:String = ""
    @SerializedName("deviceToken") var deviceToken:String = ""
    @SerializedName("deviceType") var deviceType:String = ""
    @SerializedName("languageId") var languageId:String = ""
    @SerializedName("userTypes") var userTypes:String = ""
}


class ForgotPassword{

}