package com.callisdairy.FirebaseNotifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Vibrator
import androidx.core.app.NotificationManagerCompat
import com.callisdairy.UI.Activities.AgoraCalling.Calling
import com.callisdairy.UI.Activities.AgoraCalling.VoiceCallActivity
import com.callisdairy.Utils.SavedPrefManager

class NotificationBroadcastReceiver : BroadcastReceiver() {
    private var vib: Vibrator? = null
    var mMediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {

        if (intent != null) {
            val eventType = intent.getStringExtra("action")
            val roomId = intent.getStringExtra("channelId")
            val type = intent.getStringExtra("types")
            val notId = intent.getIntExtra("notificationId", 0)
            val token = intent.getStringExtra("accessToken")
            val profilePic = intent.getStringExtra("image")
            val userName = intent.getStringExtra("userName")

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notId)

            if (eventType != null) {
                if (type == "voice") {
                    val intent = Intent(context, VoiceCallActivity::class.java).apply {
                        putExtra("channelId", roomId)
                        putExtra("token", token)
                        putExtra("userName", userName)
                        putExtra("profilePic", profilePic)
                        SavedPrefManager.saveStringPreferences(context, SavedPrefManager.accessToken, token)
                        SavedPrefManager.saveStringPreferences(context, SavedPrefManager.channelId, roomId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                    NotificationManagerCompat.from(context).cancel(null, 321)
                } else if (type == "video") {
                    val intent = Intent(context, Calling::class.java).apply {
                        putExtra("channelId", roomId)
                        putExtra("token", token)
                        putExtra("userName", userName)
                        putExtra("profilePic", profilePic)
                        SavedPrefManager.saveStringPreferences(context, SavedPrefManager.accessToken, token)
                        SavedPrefManager.saveStringPreferences(context, SavedPrefManager.channelId, roomId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                    NotificationManagerCompat.from(context).cancel(null, 321)
                }
            }
        }
    }
}
