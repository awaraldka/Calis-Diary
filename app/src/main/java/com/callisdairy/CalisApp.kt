package com.callisdairy

import android.app.Application
import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CalisApp : Application(){

    lateinit var context: Context


    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this);
        EmojiManager.install(GoogleEmojiProvider())
        val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024)
        val databaseProvider: DatabaseProvider = ExoDatabaseProvider(this)
        if (simpleCache == null) {
            simpleCache = SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, databaseProvider)
        }




    }

    companion object {
        var simpleCache: SimpleCache? = null

    }


}