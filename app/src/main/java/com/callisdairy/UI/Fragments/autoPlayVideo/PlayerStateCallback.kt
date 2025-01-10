package com.callisdairy.UI.Fragments.autoPlayVideo

import androidx.media3.common.Player

interface PlayerStateCallback {

    fun onVideoDurationRetrieved(duration: Long, player: Player)

    fun onVideoBuffering(player: Player)

    fun onStartedPlaying(player: Player)

    fun onFinishedPlaying(player: Player)
}