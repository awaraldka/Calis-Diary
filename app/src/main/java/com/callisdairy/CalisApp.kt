package com.callisdairy

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp


@UnstableApi @HiltAndroidApp
class CalisApp : Application(){

    lateinit var context: Context


    @OptIn(UnstableApi::class) override fun onCreate() {
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