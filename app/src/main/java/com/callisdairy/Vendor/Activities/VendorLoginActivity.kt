package com.callisdairy.Vendor.Activities

import RequestPermission
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.callisdairy.R
import com.callisdairy.UI.Activities.ForgotPassword
import com.callisdairy.Utils.CommonConverter
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.Constants
import com.callisdairy.api.request.LoginRequest
import com.callisdairy.api.response.KeysData
import com.callisdairy.databinding.ActivityVendorLoginBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.viewModel.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class VendorLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorLoginBinding
    private var passwordNotVisible = 0
    var flag  = "phone"
    var email = ""
    var Mobile = ""
    var FCMtoken = ""

    var phoneSaved = ""
    var emailSaved = ""
    var passwordSaved = ""
    var isRememberPhone = false
    var isRememberMail = false


    private val viewModel: LoginViewModel by viewModels()



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        viewModel.appConfig()

        getAllApiKeys()

        val text = this.resources.getString(R.string.don_t_have_a_account_signup)
        val secondName = "<font color=\"#6FCFB9\">${getString(R.string.sign_up)}</font>"

        binding.havingNoAccount.text = Html.fromHtml("$text $secondName", HtmlCompat.FROM_HTML_MODE_LEGACY)

        RequestPermission.requestMultiplePermissions(this)


        phoneSaved = SavedPrefManager.getStringPreferences(this,SavedPrefManager.phoneNumberVendor).toString()
        emailSaved = SavedPrefManager.getStringPreferences(this,SavedPrefManager.emailVendor).toString()
        passwordSaved = SavedPrefManager.getStringPreferences(this,SavedPrefManager.PasswordVendor).toString()
        isRememberPhone = SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.isRememberPhoneVendor)
        isRememberMail = SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.isRememberMailVendor)


        if (isRememberPhone){
            binding.etMobileNumber.setText(phoneSaved)
            binding.etPassword.setText(passwordSaved)
            binding.rememberCheck.isChecked = true
        }else{
            binding.rememberCheck.isChecked = false
        }


        if (flag == "mail"){
            binding.etEmail.addTextChangedListener(textWatcherEmail)
            binding.etEmailPassword.addTextChangedListener(textWatcherEmail)
        }else{
            binding.etMobileNumber.addTextChangedListener(textWatcher)
            binding.etPassword.addTextChangedListener(textWatcher)
        }


        click()

        getFCMToken()
        observeLoginResponse()

    }


    private fun click() {

        binding.emailSelected.setOnClickListener {
            flag  =  "mail"
            if (isRememberMail){
                binding.etEmail.setText(emailSaved)
                binding.etEmailPassword.setText(passwordSaved)
                binding.rememberCheck.isChecked = true
            }else{
                binding.rememberCheck.isChecked = false
            }

            binding.llMobileView.visibility  = View.GONE
            binding.llEmailView.visibility  = View.VISIBLE
            binding.emailSelected.setBackgroundResource(R.drawable.button_background)
            binding.emailTV.setTextColor(Color.parseColor("#FFFFFF"))

            binding.mobileSelected.setBackgroundResource(R.drawable.border_background)
            binding.mobileTV.setTextColor(Color.parseColor("#8BD9C7"))

            binding.etEmail.addTextChangedListener(textWatcherEmail)
            binding.etEmailPassword.addTextChangedListener(textWatcherEmail)
        }

        binding.mobileSelected.setOnClickListener {
            flag  =  "phone"
            if (isRememberPhone){
                binding.etMobileNumber.setText(phoneSaved)
                binding.etPassword.setText(passwordSaved)
                binding.rememberCheck.isChecked = true
            }else{
                binding.rememberCheck.isChecked = false
            }


            binding.llMobileView.visibility  = View.VISIBLE
            binding.llEmailView.visibility  = View.GONE

            binding.mobileSelected.setBackgroundResource(R.drawable.button_background)
            binding.mobileTV.setTextColor(Color.parseColor("#FFFFFF"))

            binding.emailSelected.setBackgroundResource(R.drawable.border_background)
            binding.emailTV.setTextColor(Color.parseColor("#8BD9C7"))


        }

        binding.MobilePasswordEye.setOnClickListener {
            binding.etPassword.setSelection(binding.etPassword.text.length)
            when (passwordNotVisible) {
                0 -> {
                    binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1


                }
                1 -> {
                    binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                    passwordNotVisible = 0
                }
                else -> {
                    binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1
                }
            }
        }

        binding.EmailPasswordEye.setOnClickListener {
            binding.etEmailPassword.setSelection(binding.etEmailPassword.text.length)

            when (passwordNotVisible) {
                0 -> {
                    binding.etEmailPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.EmailPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1


                }
                1 -> {
                    binding.etEmailPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.EmailPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                    passwordNotVisible = 0
                }
                else -> {
                    binding.etEmailPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.EmailPasswordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1
                }
            }
        }

        binding.havingNoAccount.setOnClickListener {
            val intent = Intent(this, VendorSignUpActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            intent.putExtra("from","vendor")
            startActivity(intent)
        }

        binding.LoginButton.setOnClickListener {

            if(flag  == "mail"){

                FormValidations.loginByMail(binding.etEmail,binding.tvEmail,binding.llEmailNumber,binding.etEmailPassword,
                    binding.tvEmailPassword,binding.llEmailPassWord, this)

                email = binding.etEmail.text.toString()
                val Pass = binding.etEmailPassword.text.toString()

                if(email.isNotEmpty() && email.matches(Regex(FormValidations.emailPattern)) && Pass.isNotEmpty() && Pass.length > 5){

                    val request = LoginRequest()

                    request.email =  email
                    request.password =  Pass
                    request.deviceToken = FCMtoken
                    request.deviceType = "Android"
                    request.userTypes = "VENDOR"
                    request.languageId = SavedPrefManager.getStringPreferences(this,
                        SavedPrefManager.Language).toString()
                    viewModel.loginApi(request)








                }
            }else{
                FormValidations.loginByPhone(binding.etMobileNumber,binding.tvMobile,binding.llMobileNumber,binding.etPassword,
                    binding.tvPassword,binding.llPassWord, this)

                Mobile = binding.etMobileNumber.text.toString()
                val Pass = binding.etPassword.text.toString()

                if(Mobile.isNotEmpty() && Mobile.length > 9 && Pass.isNotEmpty() && Pass.length > 5){

                    val request = LoginRequest()


                    request.email =  Mobile
                    request.password =  Pass
                    request.deviceToken = FCMtoken
                    request.deviceType = "Android"
                    request.userTypes = "VENDOR"
                    request.languageId = SavedPrefManager.getStringPreferences(this,
                        SavedPrefManager.Language).toString()

                    viewModel.loginApi(request)



                }
            }



        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                if (s.length == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FormValidations.loginByPhone(binding.etMobileNumber,binding.tvMobile,binding.llMobileNumber,binding.etPassword, binding.tvPassword,binding.llPassWord, this@VendorLoginActivity)
        }
    }


    private val textWatcherEmail = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FormValidations.loginByMail(binding.etEmail,binding.tvEmail,binding.llEmailNumber,binding.etEmailPassword, binding.tvEmailPassword,binding.llEmailPassWord, this@VendorLoginActivity)
        }
    }


    @SuppressLint("StringFormatInvalid")
    fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }
            FCMtoken = task.result

        })
    }

    private fun observeLoginResponse() {


        lifecycleScope.launch {
            viewModel._loginData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        Progresss.stop()
                        if(response.data!!.responseCode == 200) {
                            try{
                                SavedPrefManager.saveStringPreferences(this@VendorLoginActivity,SavedPrefManager.Token,response.data.result.token)
                                SavedPrefManager.saveStringPreferences(this@VendorLoginActivity,SavedPrefManager.userId,response.data.result._id)
                                SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isVendor,true)
                                SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isUser,false)
                                SavedPrefManager.saveStringPreferences(this@VendorLoginActivity, SavedPrefManager.VendorRole,response.data.result.userType)
                                SavedPrefManager.saveStringPreferences(this@VendorLoginActivity, SavedPrefManager.DeviceToken,FCMtoken)
                                SavedPrefManager.saveStringPreferences(this@VendorLoginActivity, SavedPrefManager.LOGIN_COUNT,response.data.result.noOfLoginCount.toString())


                                if(flag  == "mail" && binding.rememberCheck.isChecked){
                                    SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isRememberMailVendor,true)
                                    SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isRememberPhoneVendor,false)
                                    SavedPrefManager.saveStringPreferences(this@VendorLoginActivity, SavedPrefManager.emailVendor,email)
                                    SavedPrefManager.saveStringPreferences(this@VendorLoginActivity, SavedPrefManager.PasswordVendor,binding.etEmailPassword.text.toString())
                                }else if (flag  == "phone" && binding.rememberCheck.isChecked){
                                    SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isRememberMailVendor,false)
                                    SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isRememberPhoneVendor,true)
                                    SavedPrefManager.saveStringPreferences(this@VendorLoginActivity, SavedPrefManager.phoneNumberVendor,email)
                                    SavedPrefManager.saveStringPreferences(this@VendorLoginActivity, SavedPrefManager.PasswordVendor,binding.etPassword.text.toString())

                                }else{
                                    SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isRememberMailVendor,false)
                                    SavedPrefManager.savePreferenceBoolean(this@VendorLoginActivity, SavedPrefManager.isRememberPhoneVendor,false)
                                }


                                val intent = Intent(this@VendorLoginActivity, VendorContainerActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@VendorLoginActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@VendorLoginActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    private fun getAllApiKeys() {

        lifecycleScope.launch {
            viewModel._appConfigData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        if(response.data!!.responseCode == 200) {
                            try{
                                val decodeKeyJsonString  = CommonConverter.dynamicSaltResponseConverter(this@VendorLoginActivity, response.data.result)
                                val decodeKey = JSONObject(decodeKeyJsonString!!)

                                val keysData = KeysData(
                                    gMapKey = decodeKey.getString("G_MAP_KEY"),
                                    ipApiKey = decodeKey.getString("IP_API_Key"),
                                    appId = decodeKey.getString("appId"),
                                    appCertificate = decodeKey.getString("appCertificate"),
                                    customerKey = decodeKey.getString("customerKey"),
                                    customerSecret = decodeKey.getString("customerSecret")
                                )

                                Constants.initializeKeys(keysData)

                                SavedPrefManager.saveStringPreferences(this@VendorLoginActivity,SavedPrefManager.GMKEY,
                                    Constants.getKeysData()!!.gMapKey)
                                SavedPrefManager.saveStringPreferences(this@VendorLoginActivity,SavedPrefManager.AgoraAppId,
                                    Constants.getKeysData()!!.appId)





                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }

}