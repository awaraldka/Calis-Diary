package com.callisdairy.UI.Activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.callisdairy.Adapter.AddPetProfileAdapter
import com.callisdairy.Interface.AsPerGoPay
import com.callisdairy.Interface.PetProfileClick
import com.callisdairy.ModalClass.petProfileClass
import com.callisdairy.R
import com.callisdairy.UI.Activities.subscription.CardDetailsActivity
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Home.data
import com.callisdairy.Utils.Home.suggestedUserData
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.ActivityChangeProfileBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.androidExtension.initLoader
import com.callisdairy.viewModel.PetProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangeProfileActivity : AppCompatActivity() , PetProfileClick, AsPerGoPay {

    private lateinit var binding: ActivityChangeProfileBinding
    var token = ""
    var petCategoryId = ""
    var name = ""
    lateinit var petProfileAdapter: AddPetProfileAdapter
    private val viewModel: PetProfileViewModel by viewModels()

    var datas  = ArrayList<petProfileClass>()
    lateinit var dialogs: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade




        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()



        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }


        binding.ChangeProfile.setOnClickListener {
            SavedPrefManager.saveStringPreferences(this,SavedPrefManager.profileType,name)
            SavedPrefManager.saveStringPreferences(this, SavedPrefManager.profileId,petCategoryId)

            viewModel.switchProfileApi(token, petCategoryId)
        }



        observeResponsePetProfile()
        observeResponseSwitchPetProfile()
    }


    override fun onStart() {
        super.onStart()
        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()
        viewModel.viewProfileApi(token)
    }

    private fun observeResponsePetProfile() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    datas.clear()
                                    val listData = response.data.result
                                    for (i in listData.indices){

                                        if (listData[i].isDefaultUserProfile){
                                            petCategoryId = listData[i].petCategoryId._id
                                        }

                                        datas.add(petProfileClass(listData[i].userProfileImage,listData[i].name,
                                            listData[i].petCategoryId._id,listData[i].petCategoryId.petCategoryType[0],
                                            listData[i].petCategoryId.petCategoryName,
                                            listData[i].isDefaultUserProfile,addNewProfile = false))
                                    }


//                                    if (response.data.result.size < 4){
                                        datas.add(petProfileClass("","","","","",false,
                                            addNewProfile = true
                                        ))
//                                    }
                                    binding.ChangeProfile.isVisible = response.data.result.size > 0


                                    PostAdapter(datas)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.ChangeProfile.isVisible = false
                            response.message?.let { message ->
                            androidExtension.alertBox(message, this@ChangeProfileActivity)
                            }

                        }

                        is Resource.Loading -> {
                            binding.ChangeProfile.isVisible = false
                            Progresss.start(this@ChangeProfileActivity)
                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }

        }


    }


    private fun PostAdapter(result: ArrayList<petProfileClass>) {
        binding.petProfileRecycler.layoutManager = GridLayoutManager(this, 2)
        petProfileAdapter = AddPetProfileAdapter(this, result,this)
        binding.petProfileRecycler.adapter = petProfileAdapter
    }

    override fun petProfileUpdate(petId: String, text: String) {
        petCategoryId = petId
        name = text
    }

    override fun checkPlan() {

        if (datas.size < 5){
            val intent = Intent(this, AddPetProfileActivity::class.java)
            intent.putExtra("from","AddPetProfileActivity")
            startActivity(intent)

            return
        }

        androidExtension.addMorePetProfile(this,this)




    }


    private fun observeResponseSwitchPetProfile() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._switchProfileData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    changeProfileViewPopUp()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
//                            androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            Progresss.start(this@ChangeProfileActivity)
                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }

        }


    }





   private fun changeProfileViewPopUp() {
        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.switch_profile, null)
            dialogs = DialogUtils().createDialog(this, binding.rootView, 0)!!
            val lottie = binding.findViewById<LottieAnimationView>(R.id.loader)

            lottie.initLoader(true)

            CoroutineScope(Dispatchers.IO).launch {

                delay(3000)
                refreshPage()
                data.clear()
                suggestedUserData.clear()


                dialogs.dismiss()
            }

            dialogs.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun refreshPage() {
        val intent = Intent(this, FragmentContainerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun payNow() {
        val intent = Intent(this, CardDetailsActivity::class.java)
        intent.putExtra("totalCost","5")
        intent.putExtra("screen","Pet Profile")
        startActivity(intent)

    }


}