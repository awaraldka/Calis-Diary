package com.callisdairy.Socket

import com.google.gson.annotations.SerializedName

class chatList {
    @SerializedName("response_code") val response_code : Int = 0
    @SerializedName("response_message") val response_message : String = ""
    @SerializedName("result") val result : ArrayList<chatListResult> = ArrayList<chatListResult>()

}

class chatListResult{
    @SerializedName("_id") val _id : String = ""
    @SerializedName("senderId") val senderId : chatListSenderId =  chatListSenderId()
    @SerializedName("receiverId") val receiverId : chatListReceiverId = chatListReceiverId()
//    @SerializedName("messages") val messages : chatListMessage = chatListMessage()
    @SerializedName("unReadCount") val unReadCount : Int = 0
}


class chatListSenderId{
    @SerializedName("_id") val _id : String = ""
    @SerializedName("profilePic") val profilePic : String = ""
    @SerializedName("petPic") val petPic : String = ""
    @SerializedName("name") val name : String = ""
}

class chatListReceiverId{
    @SerializedName("_id") val _id : String = ""
    @SerializedName("profilePic") val profilePic : String  = ""
    @SerializedName("petPic") val petPic : String = ""
    @SerializedName("name") val name : String = ""
}

class chatListMessage{
    @SerializedName("receiver") val receiver  =  ArrayList<String>()
}