package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.callisdairy.R
import com.callisdairy.UI.Fragments.autoPlayVideo.toast
import com.callisdairy.Utils.CommonConverter
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.request.LoginRequest
import com.callisdairy.databinding.ActivityLoginBinding
import com.callisdairy.viewModel.LoginViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.Constants
import com.callisdairy.api.response.KeysData
import com.callisdairy.extension.setSafeOnClickListener
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var passwordNotVisible = 0
    var flag  = "phone"
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    var email = ""
    var Mobile = ""
    var FCMtoken = ""
    var phoneSaved = ""
    var emailSaved = ""
    var passwordSaved = ""
    var isRememberPhone = false
    var isRememberMail = false

    private val viewModel: LoginViewModel by viewModels()
    var callBackManager: CallbackManager? = null


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        callBackManager = CallbackManager.Factory.create()
        val text = this.resources.getString(R.string.don_t_have_a_account_signup)
        val secondName = "<font color=\"#6FCFB9\">${getString(R.string.sign_up)}</font>"

        binding.havingNoAccount.text = Html.fromHtml("$text $secondName",HtmlCompat.FROM_HTML_MODE_LEGACY)

        RequestPermission.requestMultiplePermissions(this)
        isRememberPhone = SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.isRememberPhone)
        isRememberMail = SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.isRememberMail)



         phoneSaved = SavedPrefManager.getStringPreferences(this,SavedPrefManager.phoneNumber).toString()
         emailSaved = SavedPrefManager.getStringPreferences(this,SavedPrefManager.email).toString()
         passwordSaved = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Password).toString()

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


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("328789434986-ob0vcq2qq6r9s8gfa6l7ikrog219liv6.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.goggleSigning.setSafeOnClickListener {
            signIn()
        }

        binding.facebookSigning.setSafeOnClickListener {
            facebookSignIn()
        }


        viewModel.appConfig()




        click()

        getFCMToken()
        observeLoginResponse()
        observeSocialLoginResponse()
        getAllApiKeys()
    }

    private fun click() {

        binding.emailSelected.setSafeOnClickListener {
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

        binding.mobileSelected.setSafeOnClickListener {
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

        binding.MobilePasswordEye.setSafeOnClickListener {
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

        binding.EmailPasswordEye.setSafeOnClickListener {
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

        binding.havingNoAccount.setSafeOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        binding.forgotPassword.setSafeOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        binding.LoginButton.setSafeOnClickListener {

            if(flag  == "mail"){

                FormValidations.loginByMail(binding.etEmail,binding.tvEmail,binding.llEmailNumber,binding.etEmailPassword,
                    binding.tvEmailPassword,binding.llEmailPassWord, this)

                email = binding.etEmail.text.toString()
                val pass = binding.etEmailPassword.text.toString()

                if(email.isNotEmpty() && email.matches(Regex(FormValidations.emailPattern)) && pass.isNotEmpty() && pass.length > 5){

                    SavedPrefManager.saveStringPreferences(this, SavedPrefManager.email,email)
                    SavedPrefManager.saveStringPreferences(this, SavedPrefManager.Password,pass)

                    val request = LoginRequest()

                    request.email =  email
                    request.password =  pass
                    request.deviceToken = FCMtoken
                    request.deviceType = "Android"
                    request.userTypes = "USER"
                    request.languageId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Language).toString()
                    viewModel.loginApi(request)

                }
            }else{
                FormValidations.loginByPhone(binding.etMobileNumber,binding.tvMobile,binding.llMobileNumber,binding.etPassword,
                    binding.tvPassword,binding.llPassWord, this)

                Mobile = binding.etMobileNumber.text.toString()
                val pass = binding.etPassword.text.toString()

                if(Mobile.isNotEmpty() && Mobile.length > 9 && pass.isNotEmpty() && pass.length > 5){


                    SavedPrefManager.saveStringPreferences(this, SavedPrefManager.phoneNumber,Mobile)
                    SavedPrefManager.saveStringPreferences(this, SavedPrefManager.Password,pass)


                    val request = LoginRequest()


                    request.email =  Mobile
                    request.password =  pass
                    request.deviceToken = FCMtoken
                    request.deviceType = "Android"
                    request.userTypes = "USER"
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
            FormValidations.loginByPhone(binding.etMobileNumber,binding.tvMobile,binding.llMobileNumber,binding.etPassword, binding.tvPassword,binding.llPassWord, this@Login)
        }
    }


    private val textWatcherEmail = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FormValidations.loginByMail(binding.etEmail,binding.tvEmail,binding.llEmailNumber,binding.etEmailPassword, binding.tvEmailPassword,binding.llEmailPassWord, this@Login)
        }
    }


    private fun observeLoginResponse() {


        lifecycleScope.launch {
            viewModel._loginData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        Progresss.stop()
                        if(response.data!!.responseCode == 200) {
                            try {
                                if (!response.data.result.otpVerification){



                                    val intent = Intent(this@Login, OTPVerification::class.java)
                                    if(flag  == "mail"){
                                        intent.putExtra("flag", "mailText")
                                        intent.putExtra("email", email)
                                    }else{
                                        intent.putExtra("flag", "numberText")
                                        intent.putExtra("number", Mobile)
                                    }
                                    intent.putExtra("from","Login")
                                    intent.putExtra("_id", response.data.result._id)
                                    startActivity(intent)
                                }else if(!response.data.result.isSuggested){
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.Token,response.data.result.token)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.userId,response.data.result._id)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.DeviceToken,FCMtoken)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileId,response.data.result.profileId)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileType,response.data.result.profileType)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isSuggestion,false)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isUser,true)
                                    if(flag  == "mail" && binding.rememberCheck.isChecked){
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberMail,true)
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberPhone,false)
                                    }else if (flag  == "phone" && binding.rememberCheck.isChecked){
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberMail,false)
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberPhone,true)
                                    }else{
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberMail,false)
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberPhone,false)
                                    }




                                    val intent = Intent(this@Login,SuggestionListActivity::class.java)
                                    intent.putExtra("from","")
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }else{
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.Token,response.data.result.token)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.userId,response.data.result._id)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileType,response.data.result.profileType)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileId,response.data.result.profileId)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.DeviceToken,FCMtoken)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isSuggestion,true)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isUser,true)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isVendor,false)
                                    if(flag  == "mail" && binding.rememberCheck.isChecked){
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberMail,true)
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberPhone,false)
                                    }else if (flag  == "phone" && binding.rememberCheck.isChecked){
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberMail,false)
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberPhone,true)
                                    }else{
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberMail,false)
                                        SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isRememberPhone,false)
                                    }

                                    val intent = Intent(this@Login, FragmentContainerActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)

                                }

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@Login)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@Login)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }



    @SuppressLint("StringFormatInvalid")
    fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }
            FCMtoken = task.result


            println("Token>>>>>>>>>>>>>>> $FCMtoken")

        })
    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun facebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(this, arrayListOf("public_profile", "email"));
        LoginManager.getInstance()
            .registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken!!
                    facebookLogin(accessToken)

                }

                override fun onCancel() {
                    Toast.makeText(this@Login, "Cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(
                        this@Login,
                        "Server not responding, try after some time!!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })
    }

    fun facebookLogin(accessToken: AccessToken) {
        val graphRequest = GraphRequest.newMeRequest(accessToken) { obj, response ->
            if (response != null) {
                try {

                    provideData(obj, accessToken)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        val param = Bundle()
        param.putString("fieleds", "name,email,id")
        graphRequest.parameters = param
        graphRequest.executeAsync()
    }

    private fun provideData(obj: JSONObject, accessToken: AccessToken) {

        Log.d("facebook token:", accessToken.token)
        Log.d("facebook userId:", accessToken.userId)
        var profile = Profile.getCurrentProfile()

        var facebookToken = accessToken.token
        var fullName = "${obj.getString("first_name").toString()} ${obj.getString("last_name").toString()}"
        LOGIN_WITH_GMAIL(obj.getString("id").toString(), fullName, obj.getString("email").toString())
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID",googleId)
            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)
            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)
            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)
            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)
            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)
            var fullName = "$googleFirstName $googleLastName"

            LOGIN_WITH_GMAIL(googleId,fullName,googleEmail)

        } catch (e: ApiException) {

            toast(e.toString())
            // com.callisdairy.UI.Sign in was unsuccessful
            Log.e(
                "failed code=", e.toString()
            )
        }
    }

    private fun LOGIN_WITH_GMAIL(googleId: String, fullName: String, googleEmail: String) {
        val request = JsonObject().apply {
            addProperty("socialId", googleId)
            addProperty("socialType", "GMAIL")
            addProperty("deviceType", "Android")
            addProperty("deviceToken", FCMtoken)
            addProperty("name", fullName)
            addProperty("email", googleEmail)
            addProperty("userTypes", "USER")

        }
        viewModel.socialLoginApi(request)
    }

    private fun observeSocialLoginResponse() {

        lifecycleScope.launch {
            viewModel._socialLoginData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        Progresss.stop()
                        if(response.data!!.responseCode == 200) {
                            try {
                                if(!response.data.result.isDefaultUserProfileSet){
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.Token,response.data.result.token)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.userId,response.data.result._id)
                                    showDialogAddProfile(response.data.result.isSuggested)

                                } else if(response.data.result.isDefaultUserProfileSet && !response.data.result.isSuggested){
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.Token,response.data.result.token)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.userId,response.data.result._id)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.DeviceToken,FCMtoken)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isSuggestion,false)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isUser,true)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileId,response.data.result.profileId)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileType,response.data.result.profileType)

                                    val intent = Intent(this@Login,SuggestionListActivity::class.java)
                                    intent.putExtra("from","")
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)

                                }else{
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.Token,response.data.result.token)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.userId,response.data.result._id)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.DeviceToken,FCMtoken)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileType,response.data.result.profileType)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isSuggestion,true)
                                    SavedPrefManager.savePreferenceBoolean(this@Login, SavedPrefManager.isUser,true)
                                    SavedPrefManager.saveStringPreferences(this@Login, SavedPrefManager.profileId,response.data.result.profileId)
                                    val intent = Intent(this@Login, FragmentContainerActivity::class.java)
                                    startActivity(intent)
                                    finishAfterTransition()
                                }

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@Login)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@Login)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    fun showDialogAddProfile(isSuggested: Boolean) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage("Please add pet profile.")
        builder.setPositiveButton("ok") { dialogInterface, which ->
            val intent = Intent(this@Login,AddPetProfileActivity::class.java)
            intent.putExtra("from","AddPetProfileActivity")
            intent.putExtra("isSuggested",isSuggested)
            intent.putExtra("loginKey","social")
            startActivity(intent)
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }



    private fun getAllApiKeys() {

        lifecycleScope.launch {
            viewModel._appConfigData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        if(response.data!!.responseCode == 200) {
                            try{
                            val decodeKeyJsonString  = CommonConverter.dynamicSaltResponseConverter(this@Login, response.data.result)
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

                                SavedPrefManager.saveStringPreferences(this@Login,SavedPrefManager.GMKEY,Constants.getKeysData()!!.gMapKey)
                                SavedPrefManager.saveStringPreferences(this@Login,SavedPrefManager.AgoraAppId,Constants.getKeysData()!!.appId)





                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                        }
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