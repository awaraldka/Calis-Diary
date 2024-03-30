package com.callisdairy.api.request

import com.google.gson.annotations.SerializedName

class AddMissingPetRequest{
    @SerializedName("petId")
    var petId : String = ""
    @SerializedName("petName") var petName : String = ""
    @SerializedName("lastSeen")
    var lastSeen : String = ""
    @SerializedName("type") var type : String = ""
    @SerializedName("gender") var gender : String = ""
    @SerializedName("color") var color : String = ""
    @SerializedName("breed") var breed : String = ""
    @SerializedName("trackerID") var trackerID : String = ""
    @SerializedName("peculiarity") var peculiarity : String = ""
    @SerializedName("lat") var lat : Double = 0.0
    @SerializedName("long") var long : Double = 0.0
    @SerializedName("petImage") var petImage =  ArrayList<String>()
    @SerializedName("userDetails") var userDetails = AddMissingPetUserDetails()
}

class AddMissingPetUserDetails{
    @SerializedName("name") var name : String = ""
    @SerializedName("address") var address : String = ""
    @SerializedName("email") var email : String = ""
    @SerializedName("mobileNumber") var mobileNumber : String = ""
}
   
