package com.callisdairy.UI.Activities


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.callisdairy.CalisApp
import com.callisdairy.R
import com.callisdairy.databinding.ActivityWatchBinding

class WatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchBinding

    private var exoPlayer : ExoPlayer? = null

    lateinit var handler : Handler
    var url = ""
    private lateinit var mediaDataSourceFactory: DataSource.Factory


    @OptIn(UnstableApi::class) @SuppressLint("ResourceAsColor")
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


        exoPlayer = ExoPlayer.Builder(this).build()
        binding.itemVideoExoplayer.player = exoPlayer
        binding.itemVideoExoplayer.keepScreenOn = true
        binding.itemVideoExoplayer.controllerAutoShow = false
        exoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL
        binding.itemVideoExoplayer.useController = false
        binding.itemVideoExoplayer.controllerHideOnTouch = true


        exoPlayer?.playWhenReady = true

        mediaDataSourceFactory = CalisApp.simpleCache?.let {
            CacheDataSource.Factory()
                .setCache(it) // Set the cache
                .setUpstreamDataSourceFactory(
                    DefaultHttpDataSource.Factory(
                        //                    Util.getUserAgent(this, getString(R.string.app_name))
                    )
                )
        }!!

        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory!!)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

        exoPlayer?.prepare(mediaSource, false, false)


        binding.itemVideoExoplayer.player = exoPlayer



        exoPlayer?.addListener(object : Player.Listener {

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