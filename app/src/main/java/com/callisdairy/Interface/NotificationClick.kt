package com.callisdairy.Interface

interface NotificationClick {
    fun notificationDelete(id: String, position: Int)
    fun viewUsersProfile(id: String,userName:String)
    fun readNotifications(
        notificationType: String,
        postIds: String,
        notificationIds: String,
        description: String
    )
}


interface FriendListClick{
    fun acceptRequest(_id: String, position: Int)
    fun rejectRequest(_id: String, position: Int)
}