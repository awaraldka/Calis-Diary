package com.callisdairy.Utils

import android.content.Context


object CommonConverter {

    fun dynamicSaltResponseConverter(context: Context,response: String): String? {
        var convertedResponseClass: String? = null
        try {

            convertedResponseClass = AESUtil(context).decrypt(
                response,
                SavedPrefManager.getStringPreferences(context, SavedPrefManager.DYNAMIC_SALT)
            )
//            Log.d("", "======finalDecryptedData===> $convertedResponseClass")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertedResponseClass

    }



}