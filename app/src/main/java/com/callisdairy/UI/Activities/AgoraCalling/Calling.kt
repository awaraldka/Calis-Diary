package com.callisdairy.UI.Activities.AgoraCalling

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.SurfaceView
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.R
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.ActivityCallingBinding
import com.callisdairy.viewModel.AgoraVideoCallingViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.coroutines.launch

private const val TAG = "Calling"


@AndroidEntryPoint
class Calling : AppCompatActivity() {

    private lateinit var binding: ActivityCallingBinding

    private val PERMISSION_REQUEST_ID = 7

    // Ask for Android device permissions at runtime.
    private val ALL_REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE
    )

    private var mEndCall = false
    private var mMuted = false
    private var remoteView: SurfaceView? = null
    private var localView: SurfaceView? = null
    private lateinit var rtcEngine: RtcEngine

    private var accessToken = ""
    private var channelName = ""

    var token = ""
    var receiverId = ""
    var from = ""
    var userName = ""

    var counter = 0
    var minute = 0


    private val viewModel: AgoraVideoCallingViewModel by viewModels()



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RequestPermission.requestMultiplePermissions(this)

        intent.getStringExtra("userName")?.let {
            userName = it

        }


        intent.getStringExtra("receiverId")?.let {
            receiverId = it

        }


        intent.getStringExtra("from")?.let {
            from = it

        }


        accessToken = SavedPrefManager.getStringPreferences(this,SavedPrefManager.accessToken).toString()
        channelName = SavedPrefManager.getStringPreferences(this,SavedPrefManager.channelId).toString()


        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkSelfPermission(ALL_REQUESTED_PERMISSIONS[0], PERMISSION_REQUEST_ID) &&
            checkSelfPermission(ALL_REQUESTED_PERMISSIONS[1], PERMISSION_REQUEST_ID
            ) && checkSelfPermission(ALL_REQUESTED_PERMISSIONS[2], PERMISSION_REQUEST_ID)
        ) {
            initAndJoinChannel()
        }


        token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()





        if (from == "sender"){
            binding.userName.text= "Calling $userName"
            viewModel.getTokenOrIdApi(token)
        }else{
            binding.userName.text= "Calling $userName"
            if (accessToken.isNotEmpty() && channelName.isNotEmpty()){
                joinChannel()
                startCall()

            }
        }






        binding.buttonCall.setOnClickListener {
            if (mEndCall) {
                startCall()
                mEndCall = false
                binding.buttonCall.setImageResource(R.drawable.btn_endcall)
                binding.buttonMute.visibility = VISIBLE
                binding.buttonSwitchCamera.visibility = VISIBLE
            } else {
                endCall()
                mEndCall = true
                binding.buttonCall.setImageResource(R.drawable.btn_startcall)
                binding.buttonMute.visibility = INVISIBLE
                binding.buttonSwitchCamera.visibility = INVISIBLE
            }
        }

        binding.buttonSwitchCamera.setOnClickListener {
            rtcEngine.switchCamera()
        }

        binding.buttonMute.setOnClickListener {
            mMuted = !mMuted
            rtcEngine.muteLocalAudioStream(mMuted)
            val res: Int = if (mMuted) {
                R.drawable.btn_mute
            } else {
                R.drawable.btn_unmute
            }

            binding.buttonMute.setImageResource(res)
        }


        observeAgoraResponse()
        observeNotifyAgoraResponse()

    }

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            runOnUiThread {
                if (from == "sender"){
//                    toast("Video")
                    viewModel.notifyUserApi(token, receiverId, "Video")
                }
            }
        }

        /*
         * Listen for the onFirstRemoteVideoDecoded callback.
         * This callback occurs when the first video frame of a remote user is received and decoded after the remote user successfully joins the channel.
         * You can call the setupRemoteVideoView method in this callback to set up the remote video view.
         */
        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            runOnUiThread {
                setupRemoteVideoView(uid)
                binding.connecting.text = "Connected"
                startTimeCounter()
            }
        }

        /*
        * Listen for the onUserOffline callback.
        * This callback occurs when the remote user leaves the channel or drops offline.
        */
        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread {
                onRemoteUserLeft()
                binding.timer.text = "Call Ended"
            }
        }
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, ALL_REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ID) {
            if (
                grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                grantResults[2] != PackageManager.PERMISSION_GRANTED
            ) {

                Toast.makeText(applicationContext, "Permissions needed", Toast.LENGTH_LONG).show()
                finish()
                return
            }
            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initAndJoinChannel()
        }
    }

    private fun initAndJoinChannel() {

        // This is our usual steps for joining
        // a channel and starting a call.
        initRtcEngine()
        setupVideoConfig()
        setupLocalVideoView()

    }

    // Initialize the RtcEngine object.
    private fun initRtcEngine() {
        try {
            val appId= SavedPrefManager.getStringPreferences(this,SavedPrefManager.AgoraAppId)
//            toast("appId=> $appId")
            rtcEngine = RtcEngine.create(baseContext, appId, mRtcEventHandler)
        } catch (e: Exception) {
            Log.d(TAG, "initRtcEngine: $e")
        }
    }

        private fun setupLocalVideoView() {

            localView = RtcEngine.CreateRendererView(baseContext)
            localView!!.setZOrderMediaOverlay(true)
            binding.localVideoView.addView(localView)

            // Set the local video view.
            rtcEngine.setupLocalVideo(VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
        }

    private fun setupRemoteVideoView(uid: Int) {

//        if (binding.remoteVideoView.childCount > 1) {
//            return
//        }
        remoteView = RtcEngine.CreateRendererView(baseContext)
        binding.remoteVideoView.addView(remoteView)

        rtcEngine.setupRemoteVideo(VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_FILL, uid))
    }

    private fun setupVideoConfig() {

        rtcEngine.enableVideo()

        rtcEngine.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

        private fun joinChannel() {
        val token = getString(R.string.agora_token)
        // Join a channel with a token.
//            toast("accessToken=> $accessToken")
//            toast("channelName=> $channelName")
        rtcEngine.joinChannel(accessToken, channelName, "Extra Optional Data", 0)
    }

    private fun startCall() {
        setupLocalVideoView()
        joinChannel()
    }

    private fun endCall() {
        removeLocalVideo()
        removeRemoteVideo()
        leaveChannel()
        finishAfterTransition()
        SavedPrefManager.deleteChannelId(this)
    }

    private fun removeLocalVideo() {
        if (localView != null) {
            binding.localVideoView.removeView(localView)

        }
        localView = null
    }

    private fun removeRemoteVideo() {
        if (remoteView != null) {
            binding.remoteVideoView.removeView(remoteView)

        }
        remoteView = null
    }

    private fun leaveChannel() {
        rtcEngine.leaveChannel()
    }

    private fun onRemoteUserLeft() {
        finishAfterTransition()
        removeRemoteVideo()
        SavedPrefManager.deleteChannelId(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mEndCall) {
            leaveChannel()
        }
        RtcEngine.destroy()
        SavedPrefManager.deleteChannelId(this)
    }





    private fun observeAgoraResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.getTokenOrIdData.collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            if(response.data?.responseCode == 200) {
                                try {

                                    accessToken = response.data.AgoraCallingResult.token
                                    channelName = response.data.AgoraCallingResult.channelId

                                    joinChannel()
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@Calling)
                            }
                        }

                        is Resource.Loading -> {
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }
    private fun observeNotifyAgoraResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.callUsersData.collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            if(response.data?.responseCode == 200) {
                                try {

                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@Calling)
                            }
                        }

                        is Resource.Loading -> {
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }


    fun startTimeCounter() {
        object : CountDownTimer(Long.MAX_VALUE, 1000) {
            private var hours = 0
            private var minutes = 0
            private var seconds = 0

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                seconds++

                if (seconds == 60) {
                    seconds = 0
                    minutes++
                }

                if (minutes == 60) {
                    minutes = 0
                    hours++
                }

                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                binding.timer.text = formattedTime
            }

            override fun onFinish() {
                binding.timer.text = "Call Ended"
            }
        }.start()
    }


}