package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

data class SearchPeopleResponse(
    @SerializedName("result"          ) var result          : Result? = Result(),
    @SerializedName("responseMessage" ) var responseMessage : String? = null,
    @SerializedName("responseCode"    ) var responseCode    : Int?    = null)


data class SearchDocs (

    @SerializedName("city"            ) var city            : String?           = null,
    @SerializedName("state"           ) var state           : String?           = null,
    @SerializedName("country"         ) var country         : String?           = null,
    @SerializedName("userName"        ) var userName        : String?           = null,
    @SerializedName("otpVerification" ) var otpVerification : Boolean?          = null,
    @SerializedName("profilePic"      ) var profilePic      : String?           = null,
    @SerializedName("isSocial"        ) var isSocial        : Boolean?          = null,
    @SerializedName("userType"        ) var userType        : String?           = null,
    @SerializedName("approveStatus"   ) var approveStatus   : String?           = null,
    @SerializedName("followers"       ) var followers       : ArrayList<String> = arrayListOf(),
    @SerializedName("followersCount"  ) var followersCount  : Int?              = null,
    @SerializedName("following"       ) var following       : ArrayList<String> = arrayListOf(),
    @SerializedName("interest"        ) var interest        : ArrayList<String> = arrayListOf(),
    @SerializedName("likesProduct"    ) var likesProduct    : ArrayList<String> = arrayListOf(),
    @SerializedName("likeServices"    ) var likeServices    : ArrayList<String> = arrayListOf(),
    @SerializedName("favouritePets"   ) var favouritePets   : ArrayList<String> = arrayListOf(),
    @SerializedName("likesUser"       ) var likesUser       : ArrayList<String> = arrayListOf(),
    @SerializedName("likesUserCount"  ) var likesUserCount  : Int?              = null,
    @SerializedName("followingCount"  ) var followingCount  : Int?              = null,
    @SerializedName("privacyType"     ) var privacyType     : String?           = null,
    @SerializedName("whoCanSee"       ) var whoCanSee       : ArrayList<String> = arrayListOf(),
    @SerializedName("status"          ) var status          : String?           = null,
    @SerializedName("points"          ) var points          : Int?              = null,
    @SerializedName("isOnline"        ) var isOnline        : Boolean?          = null,
    @SerializedName("documents"       ) var documents       : String?           = null,
    @SerializedName("_id"             ) var Id              : String?           = null,
    @SerializedName("name"            ) var name            : String?           = null,
    @SerializedName("email"           ) var email           : String?           = null,
    @SerializedName("password"        ) var password        : String?           = null,
    @SerializedName("mobileNumber"    ) var mobileNumber    : String?           = null,
    @SerializedName("address"         ) var address         : String?           = null,
    @SerializedName("zipCode"         ) var zipCode         : String?           = null,
    @SerializedName("gender"          ) var gender          : String?           = null,
    @SerializedName("countryCode"     ) var countryCode     : String?           = null,
    @SerializedName("otp"             ) var otp             : String?           = null,
    @SerializedName("otpTime"         ) var otpTime         : String?           = null,
    @SerializedName("createdAt"       ) var createdAt       : String?           = null,
    @SerializedName("updatedAt"       ) var updatedAt       : String?           = null,
    @SerializedName("__v"             ) var _v              : Int?              = null,
    @SerializedName("deviceToken"     ) var deviceToken     : String?           = null,
    @SerializedName("deviceType"      ) var deviceType      : String?           = null,
    @SerializedName("coverPic"        ) var coverPic        : String?           = null,
    @SerializedName("countryIsoCode"  ) var countryIsoCode  : String?           = null,
    @SerializedName("dateOfBirth"     ) var dateOfBirth     : String?           = null,
    @SerializedName("stateIsoCode"    ) var stateIsoCode    : String?           = null

)

data class Result (

    @SerializedName("docs" ) var docs : ArrayList<SearchDocs> = arrayListOf()

)
