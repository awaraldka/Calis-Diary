package com.callisdairy.api.response

import com.google.gson.annotations.SerializedName

class CategoryBasedResponse {
    @SerializedName("responseCode") var statusCode:Int = 0
    @SerializedName("responseMessage") var responseMessage:String = ""
    @SerializedName("result") var result =  CategoryBasedResult()
}


class CategoryBasedResult{
    @SerializedName("petCategoryType") var petCategoryType:ArrayList<String> = arrayListOf()
}


