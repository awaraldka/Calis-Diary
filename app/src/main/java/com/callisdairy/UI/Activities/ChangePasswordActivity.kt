package com.callisdairy.UI.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.callisdairy.Interface.Finish
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations
import com.callisdairy.databinding.ActivityChangePasswordBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.ChangePasswordViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity(), Finish {

    private lateinit var binding: ActivityChangePasswordBinding
    private var passwordNotVisible = 0
    var from = ""

    lateinit var socketInstance: SocketManager
    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent?.getStringExtra("from")?.let{
            from = it
        }


        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)


        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }

        binding.etOldPassword.addTextChangedListener(textWatcher)
        binding.etNewPassword.addTextChangedListener(textWatcher)
        binding.etConfirmPassword.addTextChangedListener(textWatcher)

        binding.SubmitButton.setSafeOnClickListener {

            FormValidations.passwordChange(
                binding.etOldPassword,
                binding.llOldPassword,
                binding.tvOldPassword,
                binding.etNewPassword,
                binding.llPassWord,
                binding.tvNewPassword,
                binding.etConfirmPassword,
                binding.llConfirmPassWord,
                binding.tvConfirmPassword,
                this

            )
            if (binding.etOldPassword.text.isNotEmpty()
                && binding.etNewPassword.text.isNotEmpty()
                && binding.etNewPassword.text.length > 5
                && binding.etConfirmPassword.text.isNotEmpty()
                && binding.etNewPassword.text.length == binding.etConfirmPassword.text.length
            ) {

                val token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token)!!
                val jsonObject = JsonObject()
                jsonObject.addProperty("oldPassword",binding.etOldPassword.text.toString())
                jsonObject.addProperty("newPassword",binding.etConfirmPassword.text.toString())
               viewModel.changePasswordApi(token, jsonObject)
            }
        }

        binding.MobileOldPasswordEye.setSafeOnClickListener {
            if (passwordNotVisible == 0) {
                binding.etOldPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.oldPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                binding.etOldPassword.setSelection(binding.etOldPassword.length())
                passwordNotVisible = 1

            } else if (passwordNotVisible == 1) {
                binding.etOldPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.oldPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                binding.etOldPassword.setSelection(binding.etOldPassword.length())
                passwordNotVisible = 0
            } else {
                binding.etOldPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.oldPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                binding.etOldPassword.setSelection(binding.etOldPassword.length())
                passwordNotVisible = 1
            }
        }

        binding.MobilePasswordEye.setSafeOnClickListener {
            if (passwordNotVisible == 0) {
                binding.etNewPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                binding.etNewPassword.setSelection(binding.etNewPassword.length())
                passwordNotVisible = 1


            } else if (passwordNotVisible == 1) {
                binding.etNewPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                binding.etNewPassword.setSelection(binding.etNewPassword.length())
                passwordNotVisible = 0
            } else {
                binding.etNewPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                binding.etNewPassword.setSelection(binding.etNewPassword.length())
                passwordNotVisible = 1
            }
        }

        binding.ConfirmPasswordEye.setSafeOnClickListener {
            when (passwordNotVisible) {
                0 -> {
                    binding.etConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.ConfirmPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                    passwordNotVisible = 1


                }
                1 -> {
                    binding.etConfirmPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    binding.ConfirmPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                    binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                    passwordNotVisible = 0
                }
                else -> {
                    binding.etConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.ConfirmPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
                    passwordNotVisible = 1
                }
            }

        }



        observeChangePasswordResponse()

    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            FormValidations.passwordChange(
                binding.etOldPassword,
                binding.llOldPassword,
                binding.tvOldPassword,
                binding.etNewPassword,
                binding.llPassWord,
                binding.tvNewPassword,
                binding.etConfirmPassword,
                binding.llConfirmPassWord,
                binding.tvConfirmPassword,
                this@ChangePasswordActivity

            )
        }
    }


    private fun observeChangePasswordResponse() {


        lifecycleScope.launchWhenCreated {
            viewModel._changePasswordData.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        Progresss.stop()

                        if(response.data?.statusCode == 200) {
                            try {
                                androidExtension.alertBoxFinishActivity(response.data.responseMessage,this@ChangePasswordActivity,this@ChangePasswordActivity)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {

                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@ChangePasswordActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@ChangePasswordActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    override fun finishActivity() {
        finishAfterTransition()
    }


}