package com.callisdairy.Vendor.Activities

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.ModalClass.passVendorData
import com.callisdairy.R
import com.callisdairy.UI.Activities.PdfViewActivity
import com.callisdairy.UI.Activities.TermsAndConditionsActivity
import com.callisdairy.Utils.*
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.request.SignUpRequestVendor
import com.callisdairy.api.response.CountryList
import com.callisdairy.databinding.ActivityVendorSignUpBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.SignUpViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class VendorSignUpActivity : AppCompatActivity(), PopupItemClickListener, Finish {

    private lateinit var binding: ActivityVendorSignUpBinding
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    var flag = ""
    lateinit var adapter: openDialog
    var stateCode = ""
    var cityCode = ""
    var countryCode = ""
    var document = ""

    private var passwordNotVisible = 0
    var filterData: ArrayList<CountryList> = ArrayList()
    var imageFile: File? = null
    var photoURI: Uri? = null
    private var CAMERA: Int = 2
    var imagePath = ""
    private val GALLERY = 1

    var image: Uri? = null

    var role = ""

    var profilepic = ""
    var USER_IMAGE_UPLOADED_PROFILE = false
    private var base64: String? = null
    private val viewModel: SignUpViewModel by viewModels()

    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()

    val c = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        RequestPermission.requestMultiplePermissions(this)

        val text = this.resources.getString(R.string.already_have_account)
        val secondName = "<font color=\"#6FCFB9\">${getString(R.string.Login)}</font>"


        binding.havingAccount.text =
            Html.fromHtml("$text $secondName", HtmlCompat.FROM_HTML_MODE_LEGACY)


        binding.havingAccount.setOnClickListener {
            finishAfterTransition()
        }


        binding.addCertificate.setSafeOnClickListener {
            selectPdf()
        }




        binding.certificate.setSafeOnClickListener {
            val intent = Intent(this,PdfViewActivity::class.java)
            intent.putExtra("pdf",document)
            startActivity(intent)
        }





        binding.certificateCross.setSafeOnClickListener{
            binding.addCertificate.isVisible = true
            binding.certificate.isVisible = false
            document =""
        }


        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        c.set(year, month , date)
        binding.llDateOfBirth.setSafeOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->
                    val dateFormat = "$dayOfMonth-${monthOfYear + 1}-$year"
                    binding.txtDateOfBirth.text = DateFormat.dateFormatPicker(dateFormat)


                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }



        binding.tvTandC.setOnClickListener {
            val intent = Intent(this, TermsAndConditionsActivity::class.java)
            intent.putExtra("flag", "terms")
            startActivity(intent)
        }

        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(this, TermsAndConditionsActivity::class.java)
            intent.putExtra("flag", "privacy")
            startActivity(intent)
        }

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)

                if (item != "Select gender") {
                    binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                    binding.TvGender.visibility = View.GONE
                    binding.TvGender.text = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.vendorType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)

                if (item != "Select vendor type") {
                    if(item == "Vet/Doctor"){
                        role = "Doctor"
                        binding.buttonName.text = getString(R.string.next)
                    }else{
                        binding.buttonName.text = getString(R.string.signup)
                    }
                    binding.llVendorType.setBackgroundResource(R.drawable.white_border_background)
                    binding.TvVendorType.visibility = View.GONE
                    binding.TvVendorType.text = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.RegisterTandC.setOnClickListener {
            val checked = binding.RegisterTandC.isChecked

            if (checked) {
                binding.tvTerms.text = ""
                binding.tvTerms.isVisible = false
            } else {
                binding.tvTerms.isVisible = true
                binding.tvTerms.text = getString(R.string.accept_term_condition)
            }
        }


        binding.userImageSelected.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                RequestPermission.requestMultiplePermissions(this)
            } else {
                selectImage()
            }
        }

        binding.etFirstName.addTextChangedListener(textWatcher)
        binding.etLastName.addTextChangedListener(textWatcher)
        binding.etMail.addTextChangedListener(textWatcher)
        binding.etMobileNumber.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)
        binding.etCountry.addTextChangedListener(textWatcher)
        binding.etState.addTextChangedListener(textWatcher)
        binding.etZipCode.addTextChangedListener(textWatcher)
        binding.etCity.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)


        binding.llCity.setOnClickListener {

            if (binding.etState.text.isNotEmpty()) {
                flag = "City"
                openPopUp(flag)
            } else {
                binding.tvState.visibility = View.VISIBLE
                binding.tvState.text = getString(R.string.select_state)
                binding.tvState.setTextColor(Color.parseColor("#C63636"))
                binding.StateLL.setBackgroundResource(R.drawable.errordrawable)
            }


        }

        binding.StateLL.setOnClickListener {
            if (binding.etCountry.text.isNotEmpty()) {
                flag = "State"
                openPopUp(flag)
            } else {
                binding.tvCountry.visibility = View.VISIBLE
                binding.tvCountry.text = getString(R.string.select_city)
                binding.tvCountry.setTextColor(Color.parseColor("#C63636"))
                binding.llCountry.setBackgroundResource(R.drawable.errordrawable)
            }


        }

        binding.llCountry.setOnClickListener {
            flag = "Country"
            openPopUp(flag)
        }



        binding.MobilePasswordEye.setOnClickListener {
            when (passwordNotVisible) {
                0 -> {
                    binding.etPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1


                }
                1 -> {
                    binding.etPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.password_view))
                    passwordNotVisible = 0
                }
                else -> {
                    binding.etPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(resources.getDrawable(R.drawable.eye))
                    passwordNotVisible = 1
                }
            }
        }


        //        Observers
        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()
        observeSignUpResponse()
        observeUploadedImagesResponse()



        binding.SignUpButton.setOnClickListener {

            FormValidations.signUpVendor(
                binding.etFirstName, binding.tvFirstName, binding.firstNameLL,
                binding.etLastName, binding.tvLastName, binding.lastNameLL,
                binding.etMail, binding.tvEmail, binding.llEmail,
                binding.etMobileNumber, binding.tvMobileNumber, binding.llMobileNumber,
                binding.genderSpinner, binding.TvGender, binding.llGender,
                binding.etAddress, binding.tvAddress, binding.llAddress,
                binding.etCity, binding.tvCity, binding.llCity,
                binding.etState, binding.tvState, binding.StateLL,
                binding.etZipCode, binding.tvZip, binding.llZipCode,
                binding.etCountry, binding.tvCountry, binding.llCountry,
                binding.etPassword, binding.tvPassword, binding.llPassword,
                binding.RegisterTandC, binding.tvTerms,
                binding.tvDocument, this,
                profilepic, binding.userProfile,
                binding.llVendorType,
                binding.vendorType,
                binding.TvVendorType,
                binding.txtDateOfBirth,
                binding.llDateOfBirth,
                binding.tvDateOfBirth
            )

            if (binding.etFirstName.text.isNotEmpty() && binding.etFirstName.text.length > 2 && binding.etLastName.text.isNotEmpty()
                && binding.etLastName.text.length > 2 && profilepic.isNotEmpty() && binding.etMail.text.isNotEmpty() && binding.etMail.text.matches(
                    Regex(FormValidations.emailPattern)
                ) && binding.etMobileNumber.text.isNotEmpty() &&
                binding.etMobileNumber.text.length > 9
                && binding.genderSpinner.selectedItem != "Select gender" && binding.etCity.text.isNotEmpty()
                && binding.etState.text.isNotEmpty() && binding.etPassword.text.isNotEmpty() && binding.etPassword.text.length > 5
                && binding.etCountry.text.isNotEmpty()
                && binding.RegisterTandC.isChecked && binding.vendorType.selectedItem != "Select vendor type" && binding.txtDateOfBirth.text.isNotEmpty()
            ) {


                val request = SignUpRequestVendor()

                val usertype = when (binding.vendorType.selectedItem) {
                    "Product" -> {
                        "VENDORPRODUCT"
                    }
                    "Pet" -> {
                        "VENDORPET"
                    }
                    "Service" -> {
                        "VENDORSERVICE"
                    }
                    else -> {
                        "VENDORDOCTORVET"
                    }
                }




                request.name = "${binding.etFirstName.text} ${binding.etLastName.text}"
                request.countryCode = binding.ccpMyProfile.selectedCountryCode
                request.mobileNumber = binding.etMobileNumber.text.toString()
                request.email = binding.etMail.text.toString()
                request.address = binding.etAddress.text.toString()
                request.city = binding.etCity.text.toString()
                request.state = binding.etState.text.toString()
                request.country = binding.etCountry.text.toString()
                request.password = binding.etPassword.text.toString()
                request.userTypes = usertype
                request.gender = binding.genderSpinner.selectedItem.toString()
                request.zipCode = binding.etZipCode.text.toString()
                request.countryIsoCode = countryCode
                request.stateIsoCode = stateCode
                request.documents.media = document
                request.userDob = binding.txtDateOfBirth.text.toString()

                request.languageId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Language).toString()

                if (USER_IMAGE_UPLOADED_PROFILE) {
                    request.profilePic = "data:image/png;base64,${profilepic}"
                }

                if (!binding.certificate.isVisible){
                    androidExtension.alertBox("Please Upload Document",this)
                }else{
                    if (role == "Doctor"){

                        lifecycleScope.launch{
                            delay(3000)
                        }

                        val myObject = passVendorData("${binding.etFirstName.text} ${binding.etLastName.text}",
                            binding.ccpMyProfile.selectedCountryCode, binding.etMobileNumber.text.toString(),
                            binding.etMail.text.toString(), binding.etAddress.text.toString(),
                            binding.etCity.text.toString(), binding.etState.text.toString(),
                            binding.etCountry.text.toString(), binding.etPassword.text.toString(),usertype,
                            binding.genderSpinner.selectedItem.toString(),
                            binding.etZipCode.text.toString(),imageFile.toString(),countryCode,stateCode
                            ,document,
                            SavedPrefManager.getStringPreferences(this, SavedPrefManager.Language).toString(),imageFile.toString(),
                            binding.txtDateOfBirth.text.toString()
                        )

                        CommonForImages.base64Image = "data:image/png;base64,${profilepic}"

                        val intent = Intent(this, DoctorRoleActivity::class.java)
                        intent.putExtra("myObject", myObject)
                        startActivity(intent)
                    }else{
                        viewModel.signInApiVendor(request)
                    }
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
            FormValidations.signUpVendor(
                binding.etFirstName, binding.tvFirstName, binding.firstNameLL,
                binding.etLastName, binding.tvLastName, binding.lastNameLL,
                binding.etMail, binding.tvEmail, binding.llEmail,
                binding.etMobileNumber, binding.tvMobileNumber, binding.llMobileNumber,
                binding.genderSpinner, binding.TvGender, binding.llGender,
                binding.etAddress, binding.tvAddress, binding.llAddress,
                binding.etCity, binding.tvCity, binding.llCity,
                binding.etState, binding.tvState, binding.StateLL,
                binding.etZipCode, binding.tvZip, binding.llZipCode,
                binding.etCountry, binding.tvCountry, binding.llCountry,
                binding.etPassword, binding.tvPassword, binding.llPassword,
                binding.RegisterTandC, binding.tvTerms,
                binding.tvDocument, this@VendorSignUpActivity,
                profilepic, binding.userProfile,binding.llVendorType,
                binding.vendorType,
                binding.TvVendorType,
                binding.txtDateOfBirth,
                binding.llDateOfBirth,
                binding.tvDateOfBirth
            )

        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)


            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialougbackButton.setOnClickListener { dialog.dismiss() }


            val SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            when (flag) {
                "State" -> {
                    dialougTitle.text = "State"
                    viewModel.getStateListApi(countryCode)

                }
                "City" -> {
                    dialougTitle.text = flag
                    viewModel.getCityListApi(countryCode, stateCode)

                }
                "Country" -> {
                    dialougTitle.text = flag

                    viewModel.getCountryApi()

                }
            }


            SearchEditText.addTextChangedListener(textWatchers)



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    override fun getData(data: String, flag: String, code: String) {
        when (flag) {
            "City" -> {
                binding.etCity.text = data
                cityCode = data
                dialog.dismiss()

            }
            "State" -> {
                binding.etState.text = data
                stateCode = code
                binding.etCity.text = ""
                dialog.dismiss()
            }
            "Country" -> {
                binding.etCountry.text = data
                countryCode = code
                binding.etState.text = ""
                binding.etCity.text = ""
                dialog.dismiss()
            }

        }
    }


//    Country List Observer

    private fun observeCountryListResponse() {

        lifecycleScope.launchWhenCreated {

            viewModel._countryStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.statusCode == 200) {
                            try {
                                filterData = response.data.result
                                setAdapter(response.data.result)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@VendorSignUpActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@VendorSignUpActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

//    State List Observer

    private fun observeStateListResponse() {

        lifecycleScope.launch {
            viewModel._stateData.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.statusCode == 200) {
                            Progresss.stop()
                            try {
                                filterData = response.data.result
                                setAdapter(response.data.result)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@VendorSignUpActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@VendorSignUpActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

//    State List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launch {
            viewModel._citydata.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.statusCode == 200) {
                            try {
                                filterData = response.data.result
                                setAdapter(response.data.result)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@VendorSignUpActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@VendorSignUpActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    fun setAdapter(result: ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = openDialog(this, result, flag, this)
        recyclerView.adapter = adapter
    }

    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterData(s.toString())

        }
    }

    private fun filterData(searchText: String) {
        val filteredList: ArrayList<CountryList> = ArrayList()


        for (item in filterData) {
            try {
                if (item.name.lowercase().contains(searchText.lowercase())
                    || item.petCategoryName.lowercase().contains(searchText.lowercase())
                ) {
                    filteredList.add(item)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            adapter.filterList(filteredList)

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    //     Get Image From Camera and Gallery


    @SuppressLint("IntentReset")
    private fun selectImage() {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val CameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        CameraButton.setOnClickListener {


            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                try {
                    imageFile = createImageFile()!!
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.callisdairy.fileprovider",
                        imageFile!!
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA)



                    dialog.dismiss()
                }
            }
        }

        val GalleryButton = view.findViewById<ImageView>(R.id.choose_from_gallery)
        GalleryButton.setOnClickListener {

//
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)

            dialog.dismiss()
        }

        dialog.setContentView(view)


        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        imagePath = image.absolutePath
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {


                if (data != null) {
                    image = data.data!!
                    val path = getPathFromURI(image)

                    Log.d("TAG", "onActivityResult: $path")


                    val getRealPath = ImageRotation.getRealPathFromURI2(this, image!!)
                    val finalBitmap = ImageRotation.modifyOrientation(getBitmap(getRealPath)!!, getRealPath)

                    if (path != null) {
//                        imageFile = finalBitmap?.let { SaveImage(it) }
                        imageFile = File(getRealPath)

                        binding.userProfile.borderColor = Color.parseColor("#6FCFB9")
                        Glide.with(this).load(imageFile).into(binding.userProfile)
                        profilepic = finalBitmap?.let { bitmapToString(it) }.toString()
                        USER_IMAGE_UPLOADED_PROFILE = true

                    }


                }

            }
        } else if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                try {
                    imageFile = File(imagePath)
                    val finalBitmap = ImageRotation.modifyOrientation(getBitmap(imagePath)!!, imagePath)

                    Glide.with(this).load(imageFile).into(binding.userProfile)
                    binding.userProfile.borderColor = Color.parseColor("#6FCFB9")
                    profilepic = finalBitmap?.let { bitmapToString(it) }.toString()
                    USER_IMAGE_UPLOADED_PROFILE = true

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // check condition
        if (requestCode == 1 && grantResults.size > 0 && (grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            // When permission is granted
            // Call method
            selectImage()
        } else {
            // When permission is denied
            // Display toast
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitmap(path: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val f = path?.let { File(it) }
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = this.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    private fun bitmapToString(`in`: Bitmap): String {
        var options = 50
        var base64_value = ""
        val bytes = ByteArrayOutputStream()
        `in`.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
        while (bytes.toByteArray().size / 1024 > 400) {
            bytes.reset() //Reset baos is empty baos
            `in`.compress(Bitmap.CompressFormat.JPEG, options, bytes)
            options -= 10
        }
        base64 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(bytes.toByteArray())
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        base64_value = base64_value.replace("\n", "") + base64
        return base64_value
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pickPdf.launch(pdfIntent)
    }



    private val pickPdf = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val fileUris = result.data!!.data


                val path = ImageRotation.writeFileContent(fileUris!!,this)
                val fileSelected = File(path!!)



                val mimeType = if (CommonForImages.getMimeType(fileSelected.toString()) != null) {
                    CommonForImages.getMimeType(fileSelected.toString())
                } else {
                    CommonForImages.getMimeType(path)
                }
                val surveyBody = RequestBody.create(mimeType?.toMediaTypeOrNull(), fileSelected!!)
                requestMultiImagesAndVideos.add(
                    MultipartBody.Part.createFormData(
                        "uploaded_file",
                        fileSelected.name,
                        surveyBody
                    )
                )

                viewModel.uploadMultipleImagesApi(requestMultiImagesAndVideos)

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    private fun observeSignUpResponse() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._signInStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.statusCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishActivity(
                                        response.data.responseMessage,
                                        this@VendorSignUpActivity,
                                        this@VendorSignUpActivity
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@VendorSignUpActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@VendorSignUpActivity)
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }

    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url?.trim())
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    private fun observeUploadedImagesResponse() {

        lifecycleScope.launch {
            viewModel._uploadImagesData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {
                        if (response.data?.responseCode == 200) {
                            try {
                                binding.SignUpButton.isEnabled = true
                                binding.progressBar.isVisible = false
                                binding.add.isVisible = true
                                binding.addCertificate.isVisible = false
                                binding.certificate.isVisible = true
                                document = response.data.result[0].mediaUrl
//                                Glide.with(this@VendorSignUpActivity).load(R.drawable.pdf).into(binding.certificateImage)
                                RetrievePDFFromURL(this@VendorSignUpActivity,binding.certificateImage).execute(document)

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.progressBar.isVisible = true
                        binding.SignUpButton.isEnabled = true
                        binding.add.isVisible = false
                        response.message?.let { message ->
                        }
                    }

                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.add.isVisible = false
                        binding.SignUpButton.isEnabled = false
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }

    override fun finishActivity() {
        finishAfterTransition()
    }

}