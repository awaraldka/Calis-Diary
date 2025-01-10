package com.callisdairy.UI.Activities.recordview.playback

import android.widget.ImageView
import com.callisdairy.Interface.FinishAudioListener

interface AudioPlayer {
    fun playFile(
        file: String, ownUserPlay: ImageView,
        ownUserPause: ImageView,
        otherUserPlay: ImageView,
        otherUserPause: ImageView,
        flag: String,
        index : Int,
        finishAudioListener : FinishAudioListener
    )

    fun stop()
}