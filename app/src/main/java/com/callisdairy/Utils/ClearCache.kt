package com.callisdairy.Utils

import android.content.Context
import java.io.File


object ClearCache {

    fun clearApplicationCache(context: Context) {
        try {
            val cacheDir = context.cacheDir
            val applicationDir = cacheDir.parent?.let { File(it) }
            if (applicationDir!!.exists()) {
                val children = applicationDir.list()
                for (child in children!!) {
                    if (child != "lib") {
                        deleteDir(File(applicationDir, child))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            for (child in children!!) {
                val success = deleteDir(File(dir, child))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }






}