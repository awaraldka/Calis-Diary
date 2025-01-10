package com.callisdairy.Utils

import android.app.Activity
import android.content.Context
import java.util.Locale

object Localization {



    fun changeLanguage(context: Context, changeLanguage:String){
        try {

            when(changeLanguage) {
                "de" ->{
                    setLocal(context as Activity, "de")
                }

                "es" ->{
                    setLocal(context as Activity, "es")
                }

                "fr" ->{
                    setLocal(context as Activity, "fr")
                }

                "hi" ->{
                    setLocal(context as Activity, "hi")
                }

                "in" ->{
                    setLocal(context as Activity, "in")
                }

                "it" ->{
                    setLocal(context as Activity, "it")
                }

                "nl" ->{
                    setLocal(context as Activity, "nl")
                }

                "phi" ->{
                    setLocal(context as Activity, "phi")
                }

                "pl" ->{
                    setLocal(context as Activity, "pl")
                }

                "pt" ->{
                    setLocal(context as Activity, "pt")
                }

                "ro" ->{
                    setLocal(context as Activity, "ro")
                }

                "ru" ->{
                    setLocal(context as Activity, "ru")
                }

                "tr" ->{
                    setLocal(context as Activity, "tr")
                }

                "vi" ->{
                    setLocal(context as Activity, "vi")
                }

                "zh" ->{
                    setLocal(context as Activity, "zh")
                }

                else -> {
                    setLocal(context as Activity, "En")
                }
            }


        }catch (e:Exception){
            e.printStackTrace()
        }

    }





    private fun setLocal(activity: Activity, langCode: String?) {
        val locale = langCode?.let { Locale(it) }
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}