package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class chatDataList {
    @SerializedName("response_code") val response_code : Int = 0
    @SerializedName("response_message") val response_message : String = ""
    @SerializedName("result") val result : ArrayList<chatDataResult> = ArrayList()

}

class chatDataResult{


    @SerializedName("lastConversation") val lastConversation : ArrayList<LastConversation> = arrayListOf()
    @SerializedName("_id") val _id : String = ""
    @SerializedName("senderId") val senderId : chatDataSenderId =  chatDataSenderId()
    @SerializedName("receiverId") val receiverId : chatDataReceiverId = chatDataReceiverId()
    @SerializedName("messages") val messages : ArrayList<chatDataMessage> = ArrayList()
    @SerializedName("unReadCount") val unReadCount : Int = 0
}


class chatDataSenderId{
    @SerializedName("_id") val _id : String = ""
    @SerializedName("profilePic") val profilePic : String = ""
    @SerializedName("petPic") val petPic : String = ""
    @SerializedName("name") val name : String = ""
    @SerializedName("userType") val userType : String = ""
}

class chatDataReceiverId{
    @SerializedName("_id") val _id : String = ""
    @SerializedName("profilePic") val profilePic : String  = ""
    @SerializedName("petPic") val petPic : String  = ""
    @SerializedName("name") val name : String = ""
    @SerializedName("userType") val userType : String = ""
}

class chatDataMessage{
    @SerializedName("mediaType") val mediaType : String= ""
    @SerializedName("messageStatus") val messageStatus : String= ""
    @SerializedName("disappear") val disappear : Boolean= false
    @SerializedName("senderDelete") val senderDelete : Boolean= false
    @SerializedName("receiverDelete") val receiverDelete : Boolean= false
    @SerializedName("createdAt") val createdAt : String= ""
    @SerializedName("updatedAt") val updatedAt : String= ""
    @SerializedName("productId") val productId : String= ""
    @SerializedName("serviceId") val serviceId : String= ""
    @SerializedName("petId") val petId : String= ""
    @SerializedName("storyId") val storyId : String= ""
    @SerializedName("isCall") val isCall : Boolean= false
    @SerializedName("_id") val _id : String= ""
    @SerializedName("message") val message : String= ""
    @SerializedName("receiverId") val receiverId : String = ""
}


class LastConversation{
    @SerializedName("_id") val _id : String = ""
    @SerializedName("date") val date : String = ""
    @SerializedName("senderId") val senderId : String = ""
    @SerializedName("receiverId") val receiverId : String = ""
    @SerializedName("lastMessage") val lastMessage : String = ""
    @SerializedName("createdAt") val createdAt : String = ""
    @SerializedName("updatedAt") val updatedAt : String = ""
    @SerializedName("__v") val __v : Int = 0
}