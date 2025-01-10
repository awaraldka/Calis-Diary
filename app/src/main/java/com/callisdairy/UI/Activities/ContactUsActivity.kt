package com.callisdairy.UI.Activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.Interface.Finish
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations
import com.callisdairy.databinding.ActivityContactUsBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.viewModel.StaticContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactUsActivity : AppCompatActivity(), Finish {

    private lateinit var binding:ActivityContactUsBinding

    private val viewModel : StaticContentViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }



        binding.submitButton.setOnClickListener {
            if (validateFields()){
                val token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()
                viewModel.contactUsApi(token=token,name=binding.etName.text.toString(),
                    email=binding.etEmailAddress.text.toString(),mobileNumber=binding.etMobileNumber.text.toString(),reason=binding.etCaption.text.toString())
            }
        }





        observeResponseContactUs()


    }


    private fun validateFields():Boolean{
        binding.tvMessage.isVisible = false
        binding.tvMobileNumber.isVisible = false
        binding.tvAddress.isVisible = false
        binding.tvName.isVisible = false

        binding.llName.setBackgroundResource(R.drawable.white_border_background)
        binding.llEmailAddress.setBackgroundResource(R.drawable.white_border_background)
        binding.llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
        binding.llMessage.setBackgroundResource(R.drawable.white_border_background)

        if(binding.etName.text.isEmpty()){
            binding.tvName.isVisible = true
            binding.tvName.text = "*Please enter full name"
            binding.llName.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if(binding.etEmailAddress.text.isEmpty()){
            binding.tvAddress.isVisible = true
            binding.tvAddress.text = "*Please enter email address"
            binding.llEmailAddress.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if(!binding.etEmailAddress.text.matches(Regex(FormValidations.emailPattern))){
            binding.tvAddress.isVisible = true
            binding.tvAddress.text = "*Please enter valid email address"
            binding.llEmailAddress.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if(binding.etMobileNumber.text.isEmpty()){
            binding.tvMobileNumber.isVisible = true
            binding.tvMobileNumber.text = "*Please enter phone number"
            binding.llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if(binding.etCaption.text.isEmpty()){
            binding.tvMessage.isVisible = true
            binding.tvMessage.text = "*please enter reason"
            binding.llMessage.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else{
            return true
        }
    }

    private fun observeResponseContactUs() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._contactUsData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishActivity(response.data.responseMessage,this@ContactUsActivity,this@ContactUsActivity)
                                }catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                            androidExtension.alertBox(message, this@ContactUsActivity)
                            }
                        }

                        is Resource.Loading -> {
                                Progresss.start(this@ContactUsActivity)
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }

        }


    }

    override fun finishActivity() {
        finishAfterTransition()
    }


}