package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class ChatHistoryResponse {
    @SerializedName("response_code") val response_code : Int = 0
    @SerializedName("response_message") val response_message : String= ""
    @SerializedName("result") val result = ArrayList<ChatHistoryResult>()
}

class ChatHistoryResult{
    @SerializedName("_id") val _id : String =""
    @SerializedName("senderId") val senderId : ChatHistorySenderId = ChatHistorySenderId()
    @SerializedName("receiverId") val receiverId : ChatHistoryReceiverId = ChatHistoryReceiverId()
    @SerializedName("messages") val messages : ArrayList<ChatHistoryMessages> = ArrayList()
    @SerializedName("unReadCount") val unReadCount : Int = 0
}

class ChatHistorySenderId{
    @SerializedName("_id") val _id : String =""
    @SerializedName("profilePic") val profilePic : String = ""
    @SerializedName("name") val name : String = ""
    @SerializedName("petPic") val petPic : String = ""
}

class ChatHistoryReceiverId{
    @SerializedName("_id") val _id : String = ""
    @SerializedName("profilePic") val profilePic : String= ""
    @SerializedName("name") val name : String= ""
    @SerializedName("petPic") val petPic : String= ""
}


class ChatHistoryMessages{
    @SerializedName("mediaType") val mediaType : String =""
    @SerializedName("attachment") val attachment : String =""
    @SerializedName("messageStatus") val messageStatus : String=""
    @SerializedName("disappear") val disappear : Boolean= false
    @SerializedName("senderDelete") val senderDelete : Boolean = false
    @SerializedName("receiverDelete") val receiverDelete : Boolean= false
    @SerializedName("createdAt") val createdAt : String=""
    @SerializedName("updatedAt") val updatedAt : String=""
    @SerializedName("productId") val productId : String=""
    @SerializedName("serviceId") val serviceId : String=""
    @SerializedName("petId") val petId : String=""
    @SerializedName("storyId") val storyId : String=""
    @SerializedName("isCall") val isCall : Boolean= false
    @SerializedName("_id") val _id : String=""
    @SerializedName("message") val message : String=""
    @SerializedName("receiverId") val receiverId : String=""
}