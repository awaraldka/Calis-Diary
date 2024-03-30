package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Base64
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.lifecycleScope
import com.callisdairy.Utils.Localization
import com.callisdairy.databinding.ActivitySplashScreenBinding
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.VendorContainerActivity
import com.callisdairy.Vendor.Activities.VendorLoginActivity
import com.callisdairy.Vendor.ChooseTypeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    var token = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sha1 = byteArrayOf(
            0xA6.toByte(),
            0x45.toByte(),
            0x1E.toByte(),
            0x37.toByte(),
            0x78.toByte(),
            0x95.toByte(),
            0x63.toByte(),
            0x40.toByte(),
            0x50.toByte(),
            0x3E.toByte(),
            0x37.toByte(),
            0x8D.toByte(),
            0x2F.toByte(),
            0x38.toByte(),
            0x85.toByte(),
            0x98.toByte(),
            0x0C.toByte(),
            0xD3.toByte(),
            0xE8.toByte(),
            0x01.toByte()
        )
        println("keyhashGooglePlaySignIn:" + Base64.encodeToString(sha1, Base64.NO_WRAP))

        val locale: Locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)!!



        val countryBaseLocalization =  ArrayList<String>()
        countryBaseLocalization.add("de")
        countryBaseLocalization.add("es")
        countryBaseLocalization.add("fr")
        countryBaseLocalization.add("hi")
        countryBaseLocalization.add("in")
        countryBaseLocalization.add("it")
        countryBaseLocalization.add("nl")
        countryBaseLocalization.add("phi")
        countryBaseLocalization.add("pl")
        countryBaseLocalization.add("pt")
        countryBaseLocalization.add("ro")
        countryBaseLocalization.add("ru")
        countryBaseLocalization.add("tr")
        countryBaseLocalization.add("vi")
        countryBaseLocalization.add("zh")
        countryBaseLocalization.add("enm")




        val isSelected =  SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.isLanguage)



        if (isSelected){
            val changeLanguage = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Language).toString()
            Localization.changeLanguage(this,changeLanguage)
        }else{
            for (i in countryBaseLocalization.indices){
                if (countryBaseLocalization[i] == locale.language){
                    SavedPrefManager.saveStringPreferences(this,SavedPrefManager.Language,countryBaseLocalization[i])
                    Localization.changeLanguage(this,countryBaseLocalization[i])
                }else{
                    SavedPrefManager.saveStringPreferences(this,SavedPrefManager.Language,"en")
                    Localization.changeLanguage(this,"en")

                }
            }
        }







        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }


    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            delay(1000)
            callApis()

        }
    }



    private fun callApis(){

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()
        val checkStatus = SavedPrefManager.getBooleanPreferences(this, SavedPrefManager.isSuggestion)
        val isUser = SavedPrefManager.getBooleanPreferences(this, SavedPrefManager.isUser)
        val isVendor = SavedPrefManager.getBooleanPreferences(this, SavedPrefManager.isVendor)


        if (isUser){
            if (token.isNotEmpty()  && !checkStatus){
                val intent = Intent(this@SplashScreen, SuggestionListActivity::class.java)
                intent.putExtra("from","")
                startActivity(intent)
                finishAfterTransition()
            }else if (token.isNotEmpty() && checkStatus){
                val intent = Intent(this@SplashScreen, FragmentContainerActivity::class.java)
                startActivity(intent)
                finishAfterTransition()
            }

        }else if (isVendor){
            val intent = Intent(this@SplashScreen, VendorContainerActivity::class.java)
            startActivity(intent)
            finishAfterTransition()
        }else{
            SavedPrefManager.deleteAllKeys(this@SplashScreen)
            val intent = Intent(this@SplashScreen, ChooseTypeActivity::class.java)
            startActivity(intent)
            finishAfterTransition()
        }

    }


}
