package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class OneToOneChatResponse {
    @SerializedName("response_code") val response_code : Int = 0
    @SerializedName("response_message") val response_message : String = ""
    @SerializedName("result") val result =  OneToOneChatResult()
    @SerializedName("chatHistory") val chatHistory = ArrayList<ChatHistoryResult>()
}


class OneToOneChatResult{
    @SerializedName("clearChatStatusSender") val clearChatStatusSender : Boolean =  false
    @SerializedName("clearChatStatusReceiver") val clearChatStatusReceiver : Boolean = false
    @SerializedName("status") val status : String = ""
    @SerializedName("_id") val _id :String = ""
    @SerializedName("senderId") val senderId :String = ""
    @SerializedName("receiverId") val receiverId :String = ""
    @SerializedName("messages") val messages = ArrayList<OneToOneChatMessages>()
    @SerializedName("createdAt") val createdAt :String = ""
    @SerializedName("updatedAt") val updatedAt :String = ""
    @SerializedName("__v") val __v : Int = 0
}

class OneToOneChatMessages{
    @SerializedName("mediaType") val mediaType :String = ""
    @SerializedName("messageStatus") val messageStatus :String = ""
    @SerializedName("disappear") val disappear : Boolean =  false
    @SerializedName("senderDelete") val senderDelete : Boolean =  false
    @SerializedName("receiverDelete") val receiverDelete : Boolean =  false
    @SerializedName("createdAt") val createdAt :String = ""
    @SerializedName("updatedAt") val updatedAt :String = ""
    @SerializedName("productId") val productId :String = ""
    @SerializedName("serviceId") val serviceId :String = ""
    @SerializedName("petId") val petId :String = ""
    @SerializedName("storyId") val storyId :String = ""
    @SerializedName("isCall") val isCall : Boolean =  false
    @SerializedName("_id") val _id :String = ""
    @SerializedName("message") val message :String = ""
    @SerializedName("receiverId") val receiverId : String = ""
}