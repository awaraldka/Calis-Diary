package com.callisdairy.UI.Activities

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Validations.FormValidations
import com.callisdairy.databinding.ActivityResetPasswordBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.viewModel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private var passwordNotVisible = 0
    var token = ""
    private val viewModel: ResetPasswordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getStringExtra("token")?.let {
            token = it
        }

        binding.etNewPassword.addTextChangedListener(textWatcher)
        binding.etConfirmPassword.addTextChangedListener(textWatcher)



        binding.LoginButton.setOnClickListener {

            FormValidations.changePassword(
                binding.etNewPassword,binding.llPassWord,binding.tvNewPassword,
                binding.etConfirmPassword,binding.llConfirmPassWord,binding.tvConfirmPassword,this

            )


            if ( binding.etNewPassword.text.isNotEmpty() &&  binding.etNewPassword.text.length > 5 && binding.etConfirmPassword.text.isNotEmpty()
                 && binding.etNewPassword.text.length  == binding.etConfirmPassword.text.length ){
                viewModel.resetPasswordApi(token,binding.etNewPassword.text.toString(),binding.etConfirmPassword.text.toString())
            }




        }

        binding.MobilePasswordEye.setOnClickListener {
            when (passwordNotVisible) {
                0 -> {
                    binding.etNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1


                }
                1 -> {
                    binding.etNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                    passwordNotVisible = 0
                }
                else -> {
                    binding.etNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1
                }
            }
        }

        binding.ConfirmPasswordEye.setOnClickListener {
            when (passwordNotVisible) {
                0 -> {
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.ConfirmPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1


                }
                1 -> {
                    binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.ConfirmPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                    passwordNotVisible = 0
                }
                else -> {
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.ConfirmPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1
                }
            }
        }



        observeOTPResponse()


    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            FormValidations.changePassword(
                binding.etNewPassword,binding.llPassWord,binding.tvNewPassword,
                binding.etConfirmPassword,binding.llConfirmPassWord,binding.tvConfirmPassword,this@ResetPassword

            )
        }
    }



    private fun observeOTPResponse() {


        lifecycleScope.launchWhenCreated {
            viewModel._resetPasswordData.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        Progresss.stop()

                        if(response.data?.statusCode == 200) {
                            try {

                                finishDone(response.data.responseMessage)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {

                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@ResetPassword)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@ResetPassword)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    private fun finishDone(message: String) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        builder.setPositiveButton("ok") { _, _ ->
            finishAfterTransition()
            alertDialog!!.dismiss()

        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }


}