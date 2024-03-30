package com.callisdairy.FirebaseNotifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.callisdairy.R
import com.callisdairy.UI.Activities.AgoraCalling.Calling
import com.callisdairy.UI.Activities.AgoraCalling.VoiceCallActivity
import com.callisdairy.UI.Activities.SplashScreen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessaging : FirebaseMessagingService() {

    private lateinit var mNotificationManagerCompat: NotificationManagerCompat
    private lateinit var vib: Vibrator
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var context: Context

    private lateinit var title: String
    private lateinit var doctorName: String
    private lateinit var channelName: String

    private lateinit var accessToken: String
    private lateinit var channelId: String
    private lateinit var profilePic: String
    private lateinit var name: String
    private lateinit var type: String

    private val vibrationPattern = longArrayOf(1000, 1000)

    @SuppressLint("InvalidWakeLockTag")
    private fun turnOnScreen() {
        var screenLock: PowerManager.WakeLock? = null
        if (getSystemService(POWER_SERVICE) != null) {
            screenLock = (getSystemService(POWER_SERVICE) as PowerManager).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG"
            )
            screenLock.acquire(10 * 60 * 1000L /*10 minutes*/)
            screenLock.release()
        }
    }

    override fun onNewToken(token: String) {

        Log.d("FCM Token", token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        mNotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        println("onMessageReceived :${remoteMessage.data} ")
        Log.e("notificationPayload", remoteMessage.data.toString())
        turnOnScreen()

        remoteMessage.data.let {

            title = it["title"].toString()
            val message = it["message"]
            val body: String = it["body"].toString()

            accessToken = it["token"].toString()
            channelId = it["channelId"].toString()
            profilePic = it["profilePic"].toString()
            name = it["name"].toString()
            type = it["callType"]?.lowercase().toString()

            if (channelName == null) {
                val broadCastIntent = Intent("Missed_Call")
                broadCastIntent.action = "OPEN_NEW_ACTIVITY"
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent)

                val intent = Intent(this, SplashScreen::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val pendingIntent = PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )

                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_new)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelId = createNotificationChannel()
                    builder.setChannelId(channelId)
                }

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                mNotificationManagerCompat.notify(1, builder.build())
            } else {
                when (type) {
                    "voice" -> {
                        Log.e("GOT THE CALL", "INVOICE")

                        val intent = Intent(this, Calling::class.java)
                        intent.putExtra("channel_id", channelId)
                        intent.putExtra("patient_id", accessToken)
                        intent.putExtra("name", name)
                        intent.putExtra("type", type)
                        intent.putExtra("profilePic", profilePic)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                    }
                    "video" -> {
                        Log.e("GOT THE CALL", "VIDEO")

                        val intent = Intent(this, VoiceCallActivity::class.java)
                        intent.putExtra("channel_id", channelId)
                        intent.putExtra("patient_id", accessToken)
                        intent.putExtra("name", name)
                        intent.putExtra("type", type)
                        intent.putExtra("profilePic", profilePic)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                    }
                    else -> {
                        val broadCastIntent = Intent("Missed_Call")
                        broadCastIntent.action = "OPEN_NEW_ACTIVITY"
                        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent)

                        val intent = Intent(this, SplashScreen::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                        val pendingIntent = PendingIntent.getActivity(
                            this, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                        )

                        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.notification_new)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val channelId = createNotificationChannel()
                            builder.setChannelId(channelId)
                        }

                        mNotificationManagerCompat.notify(1, builder.build())
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        val channelDescription = "This is the default channel for notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.description = channelDescription

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Set the vibration pattern for the notification channel
        notificationChannel.vibrationPattern = vibrationPattern

        // Set the sound for the notification channel
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        notificationChannel.setSound(soundUri, audioAttributes)

        notificationManager.createNotificationChannel(notificationChannel)

        return channelId
    }

    companion object {
        private const val CHANNEL_ID = "default_channel_id"
    }
}
