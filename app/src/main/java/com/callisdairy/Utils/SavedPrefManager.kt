package com.callisdairy.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager

class SavedPrefManager(var context: Context) {
    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor


    private fun getBooleanValue(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }


    private fun setBooleanValue(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private fun getStringValue(key: String): String? {
        return preferences.getString(key, "")
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private fun setStringValue(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    private fun getIntValue(key: String): Int {
        return preferences.getInt(key, 0)
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    private fun setIntValue(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     * Retrieving the value from the preference for the respective key.
     *
     * @param key : Key for which the value is to be retrieved
     * @return return value for the respective key as string.
     */
    fun getLongValue(key: String?): Long {
        return preferences.getLong(key, 0L)
    }

    /**
     * Saving the preference
     *
     * @param key   : Key of the preference.
     * @param value : Value of the preference.
     */
    fun setLongValue(key: String?, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    /**
     * Remove the preference for the particular key
     *
     * @param key : Key for which the preference to be cleared.
     */
    fun removeFromPreference(key: String?) {
        editor.remove(key)
        editor.commit()
    }

    companion object {
        //preferences variables
        const val Token = "Token"
        const val profileType = "profileType"
        const val userId = "userId"
        const val accessToken = "accessToken"
        const val channelId = "channelId"
        const val isSuggestion = "isSuggestion"
        const val isUser = "isUser"
        const val isVendor = "isVendor"
        const val VendorRole = "VendorRole"
        const val DeviceToken = "DeviceToken"
        const val TRACKING_LAT = "trackingLat"
        const val TRACKING_LONG = "trackingLong"
        const val Language = "Language"
        const val isLanguage = "false"
        const val profileId = "profileId"
        const val DYNAMIC_SALT = "DYNAMIC_SALT"
        const val GMKEY = "GMKEY"
        const val AgoraAppId = "AgoraAppId"

        const val isRememberMail = "isRememberMail"
        const val isRememberPhone = "isRememberPhone"
        const val phoneNumber = "phoneNumber"
        const val email = "email"
        const val Password = "Password"
        const val LOGIN_COUNT = "LOGIN_COUNT"
        const val DAYS = "DAYS"

        const val isRememberMailVendor = "isRememberMailVendor"
        const val isRememberPhoneVendor = "isRememberPhoneVendor"
        const val phoneNumberVendor = "phoneNumberVendor"
        const val emailVendor = "emailVendor"
        const val PasswordVendor = "PasswordVendor"





        @SuppressLint("StaticFieldLeak")
        private var instance: SavedPrefManager? = null
        private const val PREF_HIGH_QUALITY = "pref_high_quality"


        fun getInstance(context: Context): SavedPrefManager? {
            if (instance == null) {
                synchronized(SavedPrefManager::class.java) {
                    if (instance == null) {
                        instance = SavedPrefManager(context)
                    }
                }
            }
            return instance
        }


        fun saveStringPreferences(context: Context?, key: String, value: String?): String {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply()
            }
            return key
        }

        fun saveIntPreferences(context: Context?, key: String?, value: Int?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            if (value != null) {
                editor.putInt(key, value)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply()
            }
        }

        fun saveFloatPreferences(context: Context?, key: String?, value: Float) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putFloat(key, value)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply()
            }
        }

        /*
  This method is used to get string values from shared preferences.
   */
        fun getStringPreferences(context: Context?, key: String?): String? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(key, "")
        }

        /*
     This method is used to get string values from shared preferences.
      */
        fun getIntPreferences(context: Context?, key: String?): Int {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getInt(key, 0)
        }

        fun savePreferenceBoolean(context: Context?, key: String?, b: Boolean) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, b)
            editor.apply()
        }

        /*
      This method is used to get string values from shared preferences.
       */
        fun getBooleanPreferences(context: Context?, key: String?): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean(key, false)
        }

        /**
         * Removes all the fields from SharedPrefs
         */
        fun clearPrefs(context: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        fun deleteAllKeys(context: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.remove("Token")
            editor.remove("MyRewardPoints")
            editor.remove("channelId")
            editor.remove("accessToken")
            editor.remove("userId")
            editor.remove("isSuggestion")
            editor.remove("profileType")
            editor.remove("isVendor")
            editor.remove("isUser")
            editor.remove("VendorRole")
            editor.remove("DeviceToken")
            editor.remove("DYNAMIC_SALT")
            editor.remove("GMKEY")
            editor.remove("AgoraAppId")
            editor.remove("LOGIN_COUNT")
            editor.remove("DAYS")
            editor.apply()
        }

        fun deleteChannelId(context: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()


            editor.remove("channelId")
            editor.remove("accessToken")

            editor.apply()
        }



    }


    init {
        preferences = context.getSharedPreferences("Calli", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }
}