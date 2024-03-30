package com.callisdairy.UI.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.LanguageAdapter
import com.callisdairy.Interface.LocalizationClick
import com.callisdairy.ModalClass.language
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Localization
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.VendorContainerActivity
import com.callisdairy.databinding.ActivityChangeLanuageBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.PetProfileViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChangeLanguageActivity : AppCompatActivity(), LocalizationClick {

    private lateinit var binding: ActivityChangeLanuageBinding


    private val viewModel: PetProfileViewModel by viewModels()


    var changeLanguage =""
    var from =""

    var flag = "English"
    lateinit var LanguageAdapter: LanguageAdapter
    var data:ArrayList<language>  = ArrayList()
    lateinit var socketInstance: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeLanuageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent?.getStringExtra("from")?.let { from = it  }



        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)


        flag = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Language).toString()

        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }
        
        binding.OkClick.setSafeOnClickListener {

            val token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()
            viewModel.switchLanguageApi(token,changeLanguage)

        }


        data.add(language(com.hbb20.R.drawable.flag_united_states_of_america,"English (United State)","en",flag))
        data.add(language(com.hbb20.R.drawable.flag_united_kingdom,"English(United Kingdom)","enm",flag))
        data.add(language(R.drawable.spain,"Española","es",flag))
        data.add(language(R.drawable.flag3,"中国人","zh",flag))
        data.add(language(R.drawable.russia,"Русский","ru",flag))
        data.add(language(R.drawable.flag5,"française","fr",flag))
        data.add(language(R.drawable.dutch,"deutsch","nl",flag))
        data.add(language(R.drawable.india,"हिंदी","hi",flag))
        data.add(language(R.drawable.india,"ಕನ್ನಡ","kn",flag))
        data.add(language(R.drawable.india,"বাংলা","bn",flag))
        data.add(language(R.drawable.india,"தமிழ்","ta",flag))
        data.add(language(R.drawable.vietnam,"Vietnamese","vi",flag))
        data.add(language(R.drawable.turki,"Türkçe","tr",flag))
        data.add(language(com.hbb20.R.drawable.flag_poland,"polski","pl",flag))
        data.add(language(com.hbb20.R.drawable.flag_portugal,"português","pt",flag))
        data.add(language(com.hbb20.R.drawable.flag_indonesia,"bahasa Indonesia","in",flag))
        data.add(language(com.hbb20.R.drawable.flag_romania,"Română","ro",flag))
        data.add(language(com.hbb20.R.drawable.flag_philippines,"pilipino","phi",flag))
        data.add(language(com.hbb20.R.drawable.flag_germany,"german","de",flag))
        data.add(language(com.hbb20.R.drawable.flag_italy,"essa","it",flag))

        setServiceListAdapter()



        observeResponsePetProfile()


    }

    private fun setServiceListAdapter() {
        binding.languageRecycler.layoutManager = LinearLayoutManager(this)
        LanguageAdapter = LanguageAdapter(this,data,this)
        binding.languageRecycler.adapter = LanguageAdapter
    }

    override fun getLanguage(code: String) {
        changeLanguage = code
    }


    private fun observeResponsePetProfile() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._changeLanguageData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    refreshPages()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            refreshPages()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@ChangeLanguageActivity)
                            }

                        }

                        is Resource.Loading -> {
                            Progresss.start(this@ChangeLanguageActivity)
                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }

        }


    }



    private fun refreshPages(){
        SavedPrefManager.savePreferenceBoolean(this,SavedPrefManager.isLanguage, true)
        SavedPrefManager.saveStringPreferences(this,SavedPrefManager.Language,changeLanguage)
        Localization.changeLanguage(this,changeLanguage)

        if (from== "vendor"){
            val intent = Intent(this, VendorContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }else{
            val intent = Intent(this, FragmentContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }




        finishAfterTransition()
    }




}