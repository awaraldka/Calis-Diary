package com.callisdairy.UI.Activities.recordview.playback

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.widget.ImageView
import com.callisdairy.Interface.FinishAudioListener

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    override
    fun playFile(
        file: String, ownUserPlay: ImageView,
        ownUserPause: ImageView,
        otherUserPlay: ImageView,
        otherUserPause: ImageView,
        flag: String,
        index: Int,
        finishAudioListener : FinishAudioListener
    ) {

        player?.stop()
        player?.release()
        player = null

        MediaPlayer.create(context, Uri.parse(file)).apply {
            player = this
            start()
        }

        player!!.setOnCompletionListener(OnCompletionListener {
            finishAudioListener.finishAudio(flag)
            stop() // finish current activity
        })
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    companion object {
        private var player: MediaPlayer? = null
        fun releaseMediaPlayer() {
            player?.stop()
            player?.release()
            player = null
        }
    }
}