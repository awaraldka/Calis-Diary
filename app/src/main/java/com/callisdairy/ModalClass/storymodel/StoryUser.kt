package com.callisdairy.ModalClass.storymodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize
data class StoryUser(val username: String, val profilePicUrl: String, val stories: ArrayList<Story>) : Parcelable