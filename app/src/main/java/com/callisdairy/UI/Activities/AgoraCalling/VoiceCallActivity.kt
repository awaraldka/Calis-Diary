package com.callisdairy.UI.Activities.AgoraCalling

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.ActivityVoiceCallBinding
import com.callisdairy.viewModel.AgoraVideoCallingViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VoiceCallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoiceCallBinding
    private val PERMISSION_REQ_ID_RECORD_AUDIO = 22
    var ring = MediaPlayer()
    var counter = 0
    var minute = 0
    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(permission),
                requestCode)
            return false
        }
        return true
    }

    // Fill the temp token generated on Agora Console.
    private var mRtcEngine: RtcEngine ?= null

    private val viewModel: AgoraVideoCallingViewModel by viewModels()


    var token = ""
    var receiverId = ""
    var from = ""
    private var accessToken = ""
    private var channelName = ""
    private var userImage = ""
    private var profilePic = ""
    private var userName = ""

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        RequestPermission.requestMultiplePermissions(this)

        intent.getStringExtra("receiverId")?.let {
            receiverId = it
        }

        intent.getStringExtra("userImage")?.let {
            userImage = it
        }

        intent.getStringExtra("profilePic")?.let {
            profilePic = it
        }


        intent.getStringExtra("from")?.let {
            from = it

        }

        intent.getStringExtra("userName")?.let {
            userName = it

        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
            initializeAndJoinChannel()
        }


        accessToken = SavedPrefManager.getStringPreferences(this,SavedPrefManager.accessToken).toString()
        channelName = SavedPrefManager.getStringPreferences(this,SavedPrefManager.channelId).toString()


        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()


        binding.buttonCall.setOnClickListener {
            mRtcEngine?.leaveChannel()
            RtcEngine.destroy()
            finishAfterTransition()
            ring.stop()
        }



        binding.speaker.setOnClickListener {
            binding.speaker.isVisible = false
            binding.speakerUnSelected.isVisible = true
            mRtcEngine!!.setEnableSpeakerphone(false)

        }

        binding.speakerUnSelected.setOnClickListener {
            binding.speaker.isVisible = true
            binding.speakerUnSelected.isVisible = false
            mRtcEngine!!.setEnableSpeakerphone(true)

        }


        if (from == "sender"){
            ring = MediaPlayer.create(this@VoiceCallActivity,R.raw.ringtone)
            ring.setAudioStreamType(AudioManager.STREAM_MUSIC)
            ring.start()
            mRtcEngine!!.setEnableSpeakerphone(false)
            Glide.with(this).load(userImage).into(binding.callingUser)
            binding.userName.text= "Calling $userName"
            viewModel.getTokenOrIdApi(token)


        }else{
            if (accessToken.isNotEmpty() && channelName.isNotEmpty()){
                Toast.makeText(this@VoiceCallActivity, "userName", Toast.LENGTH_SHORT).show()

                Glide.with(this).load(profilePic).into(binding.callingUser)
                binding.userName.text= "Calling $userName"
                joinChannel()
            }
        }




        observeAgoraResponse()
        observeNotifyAgoraResponse()



    }


    private val mRtcEventHandler = object : IRtcEngineEventHandler() {

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            runOnUiThread {
                if (from == "sender"){
                    viewModel.notifyUserApi(token, receiverId, "Voice")
                }
            }
        }



        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread {
                onRemoteUserLeft()
                binding.timer.text = "Call Ended"
            }
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                ring.stop()
                binding.connecting.text = "Connected"

                startTimeCounter()
            }
        }





    }



    private fun initializeAndJoinChannel() {
        try {
            val appId= SavedPrefManager.getStringPreferences(this,SavedPrefManager.AgoraAppId)
            mRtcEngine = RtcEngine.create(baseContext, appId, mRtcEventHandler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        joinChannel()
    }


    private fun joinChannel() {
        mRtcEngine!!.joinChannel(accessToken, channelName, "", 0)
    }



    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        ring.stop()
        SavedPrefManager.deleteChannelId(this)

    }


    private fun onRemoteUserLeft() {
        finishAfterTransition()
        ring.stop()
        binding.timer.text = "Call Ended"
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
                                androidExtension.alertBox(message, this@VoiceCallActivity)
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
                                androidExtension.alertBox(message, this@VoiceCallActivity)
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