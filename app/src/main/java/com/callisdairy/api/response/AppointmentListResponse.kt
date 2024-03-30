package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

data class AppointmentListResponse(
    @SerializedName("result") val result: AppointmentListResult,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("responseCode") val responseCode: Int
)

data class AppointmentListResult(
    @SerializedName("docs") val docs: List<Appointment>,
    @SerializedName("total") val total: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int
)

data class Appointment(
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
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int

)

data class AppointmentListUserId(
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("deviceToken") val deviceToken: List<String?>,
    @SerializedName("petType") val petType: String,
    @SerializedName("languageId") val languageId: String,
    @SerializedName("webToken") val webToken: List<String?>,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("otpVerification") val otpVerification: Boolean,
    @SerializedName("profilePic") val profilePic: String,
    @SerializedName("isSocial") val isSocial: Boolean,
    @SerializedName("userType") val userType: String,
    @SerializedName("approveStatus") val approveStatus: String,
    @SerializedName("followers") val followers: List<String>,
    @SerializedName("followersCount") val followersCount: Int,
    @SerializedName("following") val following: List<String>,
    @SerializedName("interest") val interest: List<Any>,
    @SerializedName("likesProduct") val likesProduct: List<String>,
    @SerializedName("likeServices") val likeServices: List<String>,
    @SerializedName("favouritePets")val favouritePets: List<String>,
    @SerializedName("likesUser") val likesUser: List<Any>,
    @SerializedName("likesUserCount") val likesUserCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
    @SerializedName("privacyType") val privacyType: String,
    @SerializedName("whoCanSee") val whoCanSee: List<Any>,
    @SerializedName("status") val status: String,
    @SerializedName("points") val points: Number,
    @SerializedName("isOnline") val isOnline: Boolean,
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("address") val address: String,
    @SerializedName("zipCode") val zipCode: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("petPic") val petPic: String,
    @SerializedName("petCategoryId") val petCategoryId: String,
    @SerializedName("petCategoryName") val petCategoryName: String,
    @SerializedName("coverPic") val coverPic: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("otpTime") val otpTime: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int,
    @SerializedName("deviceType") val deviceType: Any?,
    @SerializedName("countryIsoCode") val countryIsoCode: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String,
    @SerializedName("stateIsoCode") val stateIsoCode: String
)

class Doctor(
    @SerializedName("docId")val docIdForDoctor:docId,
    @SerializedName("mobileNumber") val mobileNumber: String,
    @SerializedName("deviceToken") val deviceToken: List<String?>,
    @SerializedName("petType") val petType: String,
    @SerializedName("languageId") val languageId: String,
    @SerializedName("webToken") val webToken: List<String?>,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("otpVerification") val otpVerification: Boolean,
    @SerializedName("profilePic") val profilePic: String,
    @SerializedName("isSocial") val isSocial: Boolean,
    @SerializedName("userType") val userType: String,
    @SerializedName("approveStatus") val approveStatus: String,
    @SerializedName("followers") val followers: List<String>,
    @SerializedName("followersCount") val followersCount: Int,
    @SerializedName("following") val following: List<String>,
    @SerializedName("interest") val interest: List<Any>,
    @SerializedName("likesProduct") val likesProduct: List<String>,
    @SerializedName("likeServices") val likeServices: List<String>,
    @SerializedName("favouritePets")val favouritePets: List<String>,
    @SerializedName("likesUser") val likesUser: List<Any>,
    @SerializedName("likesUserCount") val likesUserCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
    @SerializedName("privacyType") val privacyType: String,
    @SerializedName("whoCanSee") val whoCanSee: List<Any>,
    @SerializedName("status") val status: String,
    @SerializedName("points") val points: Number,
    @SerializedName("isOnline") val isOnline: Boolean,
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("address") val address: String,
    @SerializedName("zipCode") val zipCode: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("petPic") val petPic: String,
    @SerializedName("petCategoryId") val petCategoryId: String,
    @SerializedName("petCategoryName") val petCategoryName: String,
    @SerializedName("coverPic") val coverPic: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("otpTime") val otpTime: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val version: Int,
    @SerializedName("deviceType") val deviceType: Any?,
    @SerializedName("countryIsoCode") val countryIsoCode: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String,
    @SerializedName("stateIsoCode") val stateIsoCode: String
)


class docId(
    @SerializedName("userProfileImage")
    val userProfileImage: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("middleName")
    val middleName: String,

    @SerializedName("primarySpokenLanguage")
    val primarySpokenLanguage: String,

    @SerializedName("clinicAddress")
    val clinicAddress: String,

    @SerializedName("clinicHours")
    val clinicHours: Array<ClinicHour>,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("emergencyNumber")
    val emergencyNumber: String,

    @SerializedName("specialization")
    val specialization: String,

    @SerializedName("experience")
    val experience: String,

    @SerializedName("collegeUniversity")
    val collegeUniversity: String,

    @SerializedName("degreeType")
    val degreeType: String,

    @SerializedName("license")
    val license: String,

    @SerializedName("licenseExpiry")
    val licenseExpiry: String,

    @SerializedName("permit")
    val permit: String,

    @SerializedName("permitExpiry")
    val permitExpiry: String,

    @SerializedName("lat")
    val lat: String,

    @SerializedName("long")
    val long: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("_id")
    val id: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("__v")
    val version: Int
)



data class ClinicHour(
    @SerializedName("closeTime")
    val closeTime: String,

    @SerializedName("day")
    val day: String,

    @SerializedName("isSelected")
    val isSelected: Boolean,

    @SerializedName("openTime")
    val openTime: String
)
