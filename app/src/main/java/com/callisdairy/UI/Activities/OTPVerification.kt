package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.petProfile
import com.callisdairy.R
import com.callisdairy.Utils.GenericKeyEvent
import com.callisdairy.Utils.GenericTextWatcher
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Validations.FormValidations
import com.callisdairy.databinding.ActivityOtpverficationBinding
import com.callisdairy.viewModel.OtpVerifyViewModel
import com.google.gson.JsonObject
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat


@AndroidEntryPoint
class OTPVerification : AppCompatActivity(), Finish , petProfile {
    private lateinit var binding: ActivityOtpverficationBinding
    var flag = ""
    var from = ""
    var email = ""
    var number = ""
    var _id = ""
    var vendor = ""

    var token = ""

    private var lastClickTime: Long = 0

    private val viewModel: OtpVerifyViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverficationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        intent.getStringExtra("flag")?.let {
            flag = it
        }

        intent.getStringExtra("_id")?.let {
            _id = it
        }


        intent.getStringExtra("number")?.let {
            number = it
        }

        intent.getStringExtra("email")?.let {
            email = it
        }

        intent.getStringExtra("from")?.let {
            from = it
        }

        intent.getStringExtra("vendor")?.let {
            vendor = it
        }










        binding.et1.addTextChangedListener(GenericTextWatcher(binding.et1, binding.et2))
        binding.et2.addTextChangedListener(GenericTextWatcher(binding.et2, binding.et3))
        binding.et3.addTextChangedListener(GenericTextWatcher(binding.et3, binding.et4))
        binding.et4.addTextChangedListener(GenericTextWatcher(binding.et4, binding.et5))
        binding.et5.addTextChangedListener(GenericTextWatcher(binding.et5, binding.et6))
        binding.et6.addTextChangedListener(GenericTextWatcher(binding.et6, null))


        binding.et1.setOnKeyListener(GenericKeyEvent(binding.et1, null))
        binding.et2.setOnKeyListener(GenericKeyEvent(binding.et2, binding.et1))
        binding.et3.setOnKeyListener(GenericKeyEvent(binding.et3, binding.et2))
        binding.et4.setOnKeyListener(GenericKeyEvent(binding.et4, binding.et3))
        binding.et5.setOnKeyListener(GenericKeyEvent(binding.et5, binding.et4))
        binding.et6.setOnKeyListener(GenericKeyEvent(binding.et6, binding.et5))


        binding.et1.addTextChangedListener(textWatcher)
        binding.et2.addTextChangedListener(textWatcher)
        binding.et3.addTextChangedListener(textWatcher)
        binding.et4.addTextChangedListener(textWatcher)
        binding.et5.addTextChangedListener(textWatcher)
        binding.et6.addTextChangedListener(textWatcher)
        countdown()


        when (flag) {

            "mailText" -> {
                val email = "<font color=\"black\">$email</font>"
                binding.TitleView.text = getString(R.string.account_verification)
                binding.descView.text = Html.fromHtml(
                    "${getString(R.string.desc_mail)} $email. ${getString(R.string.desc_mail_valid)}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            "SignUp" -> {
                val number = "<font color=\"black\">$number</font>"
                val email = "<font color=\"black\">$email</font>"
                binding.TitleView.text = ""
                binding.descView.text = Html.fromHtml(
                    "${getString(R.string.desc_mail)} $number ${getString(R.string.or)} $email. ${
                        getString(R.string.desc_mail_valid)
                    }", HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            "numberText" -> {
                val number = "<font color=\"black\">$number</font>"
                binding.TitleView.text = getString(R.string.account_verification)
                binding.descView.text = Html.fromHtml(
                    "${getString(R.string.desc_mail)} $number. ${getString(R.string.desc_mail_valid)}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

        }



        binding.submmitButton.setOnClickListener {


            FormValidations.otpVerification(
                binding.et1,
                binding.ET1,
                binding.et2,
                binding.ET2,
                binding.et3,
                binding.ET3,
                binding.et4,
                binding.ET4,
                binding.et5,
                binding.ET5,
                binding.et6,
                binding.ET6,
                binding.textView,
                this@OTPVerification

            )

            if (binding.et1.text.isNotEmpty() && binding.et2.text.isNotEmpty() && binding.et3.text.isNotEmpty() &&
                binding.et4.text.isNotEmpty() && binding.et5.text.isNotEmpty() && binding.et6.text.isNotEmpty()
            ) {

                val jsonObject = JsonObject()
                val otpCode = binding.et1.text.toString() + binding.et2.text.toString() +
                        binding.et3.text.toString() + binding.et4.text.toString() +
                        binding.et5.text.toString() + binding.et6.text.toString()

                jsonObject.addProperty("_id", _id)
                jsonObject.addProperty("otp", otpCode)


                viewModel.verifyOtpApi(jsonObject)

            }


        }


        binding.ResendCodeAgain.setOnClickListener {
            onClick(binding.ResendCodeAgain)

            if (binding.et1.text.isNotEmpty()) {
                binding.et1.text = Editable.Factory.getInstance().newEditable("")
            }
            if (binding.et2.text.isNotEmpty()) {
                binding.et2.text = Editable.Factory.getInstance().newEditable("")
            }
            if (binding.et3.text.isNotEmpty()) {
                binding.et3.text = Editable.Factory.getInstance().newEditable("")
            }
            if (binding.et4.text.isNotEmpty()) {
                binding.et4.text = Editable.Factory.getInstance().newEditable("")
            }
            if (binding.et5.text.isNotEmpty()) {
                binding.et5.text = Editable.Factory.getInstance().newEditable("")
            }
            if (binding.et6.text.isNotEmpty()) {
                binding.et6.text = Editable.Factory.getInstance().newEditable("")
            }

            if (vendor == "vendor"){
                val jsonObject = JsonObject()
                jsonObject.addProperty("email", email)
                jsonObject.addProperty("userTypes", "VENDOR")

                viewModel.resendOtpApi(jsonObject)
            }else{
                val jsonObject = JsonObject()
                jsonObject.addProperty("email", email)
                jsonObject.addProperty("userTypes", "USER")

                viewModel.resendOtpApi(jsonObject)
            }


        }

        observeOTPResponse()
        observeResendOTPResponse()

    }

    private fun countdown() {

        object : CountDownTimer(180000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.ResendCodeAgain.visibility = View.GONE
                binding.ResendCode.visibility = View.VISIBLE
                binding.ResendCode.isEnabled = false
                val f: DecimalFormat = DecimalFormat("00")

                var hour = (millisUntilFinished / 3600000) % 24;

                val min = (millisUntilFinished / 60000) % 60;

                var sec = (millisUntilFinished / 1000) % 60;
                binding.timer.text = f.format(min) + ":" + f.format(sec)

            }

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                binding.timer.text = "00:00"
                binding.ResendCodeAgain.visibility = View.VISIBLE
                binding.ResendCode.visibility = View.GONE
                binding.ResendCode.isEnabled = true
            }
        }.start()
    }


    private fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            FormValidations.otpVerification(
                binding.et1,
                binding.ET1,
                binding.et2,
                binding.ET2,
                binding.et3,
                binding.ET3,
                binding.et4,
                binding.ET4,
                binding.et5,
                binding.ET5,
                binding.et6,
                binding.ET6,
                binding.textView,
                this@OTPVerification

            )

        }
    }


//    OTP VerifyResponse


    private fun observeOTPResponse() {


        lifecycleScope.launchWhenCreated {
            viewModel._otpVerifyData.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        Progresss.stop()

                        if (response.data?.statusCode == 200) {
                            try {

                                when (from) {
                                    "Forgot" -> {
                                        val intent = Intent(this@OTPVerification, ResetPassword::class.java)
                                        if (number.isEmpty()){
                                            intent.putExtra("token", email)
                                        }else{
                                            intent.putExtra("token", number)
                                        }

                                        startActivity(intent)
                                        finishAfterTransition()
                                    }
                                    "SignUp" -> {
                                        token = response.data.result.token
                                        androidExtension.showDialogAddProfile(this@OTPVerification,this@OTPVerification)
                                    }
                                    "Login" -> {
                                        androidExtension.alertBoxFinishActivity(
                                            getString(R.string.account_verify_successfully),
                                            this@OTPVerification,
                                            this@OTPVerification
                                        )
                                    }
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {

                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@OTPVerification)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@OTPVerification)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }


//    Resend OTP

    private fun observeResendOTPResponse() {


        lifecycleScope.launchWhenCreated {
            viewModel._resendOtpData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()

                        if (response.data?.statusCode == 200) {
                            try {
                                countdown()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {

                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@OTPVerification)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@OTPVerification)
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

    override fun addProfile() {
        val intent = Intent(this,AddPetProfileActivity::class.java)
        intent.putExtra("token",token)
        startActivity(intent)
        finishAfterTransition()
    }

    override fun notAddingNow() {
        finishAfterTransition()
    }

}