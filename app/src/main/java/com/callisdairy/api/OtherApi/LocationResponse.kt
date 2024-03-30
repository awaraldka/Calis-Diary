package com.kanabix.api


import com.google.gson.annotations.SerializedName

class LocationResponse {

    @SerializedName("predictions")
    val predictions: ArrayList<LocationPrediction> = ArrayList()
    @SerializedName("status")
    val status: String = ""
}

class LocationPrediction {
    @SerializedName("description")
    val description: String = ""
//    @SerializedName("matched_substrings")
//    val matchedSubstrings: ArrayList<LocationMatchedSubstring> = ArrayList()
//    @SerializedName("place_id")
//    val placeId: String = ""
//    @SerializedName("reference")
//    val reference: String = ""
//    @SerializedName("structured_formatting")
//    val structuredFormatting: LocationStructuredFormatting = LocationStructuredFormatting()
//    @SerializedName("terms")
//    val terms: ArrayList<LocationTerm> = ArrayList()
//    @SerializedName("types")
//    val types: ArrayList<String> = ArrayList()
}

//class LocationMatchedSubstring {
//    @SerializedName("length")
//    val length: Int = 0
//    @SerializedName("offset")
//    val offset: Int = 0
//}
//
//class LocationStructuredFormatting {
//    @SerializedName("main_text")
//    val mainText: String = ""
//    @SerializedName("main_text_matched_substrings")
//    val mainTextMatchedSubstrings: ArrayList<LocationMainTextMatchedSubstring> = ArrayList()
//    @SerializedName("secondary_text")
//    val secondaryText: String = ""
//
//}
//
//class LocationMainTextMatchedSubstring {
//    @SerializedName("length")
//    val length: Int = 0
//    @SerializedName("offset")
//    val offset: Int = 0
//}
//
//class LocationTerm {
//    @SerializedName("offset")
//    val offset: Int = 0
//    @SerializedName("value")
//    val value: String = ""
//}