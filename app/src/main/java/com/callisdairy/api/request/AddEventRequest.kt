package com.callisdairy.api.request

import com.google.gson.annotations.SerializedName

class AddEventRequest{
    @SerializedName("description") var description:String = ""
    @SerializedName("eventName") var eventName:String = ""
    @SerializedName("address") var address:String = ""
    @SerializedName("country") var country:String = ""
    @SerializedName("state") var state:String = ""
    @SerializedName("city") var city:String = ""
    @SerializedName("date") var date:String = ""
    @SerializedName("image") var image = ArrayList<String>()

}
class EditEventRequest{
    @SerializedName("description") var description:String = ""
    @SerializedName("eventId") var eventId:String = ""
    @SerializedName("eventName") var eventName:String = ""
    @SerializedName("address") var address:String = ""
    @SerializedName("country") var country:String = ""
    @SerializedName("state") var state:String = ""
    @SerializedName("city") var city:String = ""
    @SerializedName("date") var date:String = ""
    @SerializedName("image") var image = ArrayList<String>()

}