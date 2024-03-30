package com.callisdairy.UI.Activities


import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.callisdairy.CalisApp
import com.callisdairy.R
import com.google.android.exoplayer2.Player
import com.callisdairy.databinding.ActivityWatchBinding
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util

class WatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchBinding

    private var exoPlayer : SimpleExoPlayer? = null

    lateinit var handler : Handler
    var url = ""
    private lateinit var mediaDataSourceFactory: DataSource.Factory


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        binding = ActivityWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getStringExtra("url")?.let {
            url = it
        }



        binding.closeVideo.setOnClickListener {
            finishAfterTransition()
        }

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        binding.itemVideoExoplayer.player = exoPlayer
        binding.itemVideoExoplayer.keepScreenOn = true
        binding.itemVideoExoplayer.controllerAutoShow = false
        exoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL
        binding.itemVideoExoplayer.useController = false
        binding.itemVideoExoplayer.controllerHideOnTouch = true


        exoPlayer?.playWhenReady = true

        mediaDataSourceFactory = CacheDataSourceFactory(
            CalisApp.simpleCache,
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    this,
                    Util.getUserAgent(this, getString(R.string.app_name))
                )
            )
        )
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            Uri.parse(url)
        )
        exoPlayer?.prepare(mediaSource, false, false)


        binding.itemVideoExoplayer.player = exoPlayer



        exoPlayer?.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.thumbnail.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        binding.progressBar.visibility = View.GONE
                        binding.thumbnail.visibility = View.GONE
                    }
                }

            }
        })


    }






    // pause or release the player prevent memory leak
    override fun onStop() {
        super.onStop()
        exoPlayer?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.stop()
    }
}