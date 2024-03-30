package com.callisdairy.ModalClass.storymodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(val url: String, val storyDate: Long, val caption : String, val _id : String,val mediaType:String) : Parcelable {

    fun isVideo() = mediaType == "video"
}