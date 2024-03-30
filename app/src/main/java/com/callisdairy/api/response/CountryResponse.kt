package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class CountryResponse {
    @SerializedName("responseCode") var statusCode:Int = 0
    @SerializedName("responseMessage") var responseMessage:String = ""
    @SerializedName("result") var result =  ArrayList<CountryList>()
}


class CountryList{
    @SerializedName("status") var status:String = ""
    @SerializedName("_id") var _id:String = ""
    @SerializedName("name") var name:String = ""
    @SerializedName("isoCode") var isoCode:String = ""
    @SerializedName("petCategoryName") var petCategoryName:String= ""
    @SerializedName("petBreedName") var petBreedName:String= ""
    @SerializedName("flag") var flag:String = ""
    @SerializedName("currency") var currency:String = ""
    @SerializedName("__v") var __v:Int = 0
    @SerializedName("createdAt") var createdAt:String = ""
    @SerializedName("updatedAt") var updatedAt:String = ""
    @SerializedName("countryCode") var countryCode:String = ""
    @SerializedName("latitude") var latitude:String = ""
    @SerializedName("longitude") var longitude:String = ""
    var isSelected : Boolean = false
    var productName : String = ""
}


