package com.callisdairy.UI.Fragments.autoPlayVideo

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.callisdairy.CalisApp
import com.callisdairy.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util


// extension function for show toast
fun Context.toast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

class PlayerViewAdapter {

    companion object{
        // for hold all players generated
        private var playersMap: MutableMap<Int, ExoPlayer>  = mutableMapOf()
        // for hold current player

        private var exoPlayer : SimpleExoPlayer? = null
        private lateinit var mediaDataSourceFactory: DataSource.Factory

        private var currentPlayingVideo: Pair<Int, ExoPlayer>? = null


        fun releaseAllPlayers(){
            playersMap.map {
                it.value.release()
            }
        }





        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int){
            playersMap[index]?.release()
        }

        // call when scroll to pause any playing player
        private fun pauseCurrentPlayingVideo(){
            if (currentPlayingVideo != null){
                currentPlayingVideo?.second?.playWhenReady = false
            }
        }

        fun playIndexThenPausePreviousPlayer(index: Int){
            if (playersMap[index]?.playWhenReady == false) {
                pauseCurrentPlayingVideo()
                playersMap[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap[index]!!)
            }

        }



        /*
        *  url is a url of stream video
        *  progressbar for show when start buffering stream
        * thumbnail for show before video start
        * */

        @JvmStatic
        fun PlayerView.loadVideo(url: String, callback: PlayerStateCallback, progressbar: ProgressBar, thumbnail: ImageView, item_index: Int? = null, autoPlay: Boolean = false) {
            if (url == null) return
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context)


            Log.e(TAG, "loadVideo: $item_index")

//            player.playWhenReady = true
            exoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL




            // When changing track, retain the latest frame instead of showing a black screen
            setKeepContentOnPlayerReset(true)

            // We'll show the controller, change to true if want controllers as pause and start
            this.useController = false

            // Provide url to load the video from here
//            val mediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory("Demo")).createMediaSource(Uri.parse(url))
            mediaDataSourceFactory = CacheDataSourceFactory(
                CalisApp.simpleCache,
                DefaultHttpDataSourceFactory(
                    Util.getUserAgent(
                        context,
                        Util.getUserAgent(context, context.getString(R.string.app_name))
                    )
                )
            )
            val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                Uri.parse(url)
            )
            exoPlayer?.prepare(mediaSource, false, false)


            this.player = exoPlayer

            // add player with its index to map
            if (playersMap.containsKey(item_index)) {
                playersMap.remove(item_index)?.let { exoPlayer ->
                    exoPlayer.release()
                }
            }

            if (item_index != null && exoPlayer != null) {
                playersMap[item_index] = exoPlayer as SimpleExoPlayer
            }




            exoPlayer?.addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)

                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            callback.onVideoBuffering(player)
                            progressbar.visibility = View.VISIBLE
                        }
                        Player.STATE_READY -> {
                            progressbar.visibility = View.GONE
                            thumbnail.visibility = View.GONE
                            callback.onVideoDurationRetrieved(this@loadVideo.player!!.duration, player)
                        }

                    }

                    if (playbackState == Player.STATE_READY && player.playWhenReady){
                        // [PlayerView] has started playing/resumed the video
                        callback.onStartedPlaying(player)
                    }
                }
            })

        }







    }






}