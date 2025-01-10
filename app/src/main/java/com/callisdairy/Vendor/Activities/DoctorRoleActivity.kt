package com.callisdairy.Vendor.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.callisdairy.Adapter.LocationNearByAdapter
import com.callisdairy.Adapter.LocationSelectAdapter
import com.callisdairy.Adapter.openDialogSpecialization
import com.callisdairy.AdapterVendors.ClinicHoursSelectAdapter
import com.callisdairy.Interface.LocationClick
import com.callisdairy.Interface.LocationClickNearBy
import com.callisdairy.Interface.SpecializationClick
import com.callisdairy.ModalClass.ClinicHours
import com.callisdairy.ModalClass.Specialization
import com.callisdairy.ModalClass.passVendorData
import com.callisdairy.R
import com.callisdairy.Utils.CommonForImages
import com.callisdairy.Utils.CommonForImages.getMimeType
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Home
import com.callisdairy.Utils.ImageRotation
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.OtherApi.NearByLocationResults
import com.callisdairy.api.request.SignUpRequestVendorDoctor
import com.callisdairy.databinding.ActivityDoctorRoleBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.androidExtension.initPdfViewer
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.pdfreader.PdfViewerActivity
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.callisdairy.viewModel.SignUpViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.kanabix.api.LocationPrediction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

@AndroidEntryPoint
class DoctorRoleActivity : AppCompatActivity(), SpecializationClick, LocationClick,
    LocationClickNearBy {


    private lateinit var binding: ActivityDoctorRoleBinding
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    var flag = ""
    lateinit var adapter: openDialogSpecialization
    lateinit var adapterClinic: ClinicHoursSelectAdapter
    var stateCode = ""
    var cityCode = ""
    var countryCode = ""
    var document = ""
    var filterData: ArrayList<Specialization> = ArrayList()
    val data = ArrayList<Specialization>()

    var latitude = 0.0
    var longitude = 0.0

    var lat = 0.0
    var long = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null


    var dataClinicHours: List<ClinicHours> = listOf()
    var clinicHoursRequest: List<ClinicHours> = listOf()
    private lateinit var dialogClinic: Dialog
    private lateinit var recyclerViewClinic: RecyclerView
    private lateinit var tickClinic: LinearLayout

    val FILE_BROWSER_CACHE_DIR = "FILE_BROWSER_CACHE_DIR"
    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()

    lateinit var AdapterLocation: LocationSelectAdapter
    lateinit var AdapterLocationCollege: LocationNearByAdapter

    private val viewModel: SignUpViewModel by viewModels()
    private val viewModelLocation: GoogleLocationApiViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        fusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }
        getLocation()



        binding.etFrom.isSelected = true
        val intent = intent
        val myObject = intent.getSerializableExtra("myObject") as? passVendorData



        binding.llCity.isEnabled = false
        binding.StateLL.isEnabled = false
        binding.llCountry.isEnabled = false
        binding.etFirstName.isEnabled = false
        binding.etLastName.isEnabled = false
        binding.etMobileNumber.isEnabled = false
        binding.genderSpinner.isEnabled = false
        binding.llGender.isEnabled = false
        binding.etZipCode.isEnabled = false
        binding.ccpMyProfile.isEnabled = false



        binding.etAddress.addTextChangedListener(textWatcherLocation)
        binding.etCollege.addTextChangedListener(textWatcherCollege)



        binding.addCertificate.setSafeOnClickListener {
            selectPdf()
        }


        binding.certificateCross.setSafeOnClickListener {
            binding.addCertificate.isVisible = true
            document = ""
            binding.certificate.isVisible = false
        }


        binding.certificateImage.setSafeOnClickListener {
            PdfViewerActivity.launchPdfFromUrl(
                context = this, pdfUrl = document,
                pdfTitle = "Title", directoryName = "dir",titleName = "", enableDownload = true)


//            val intent = Intent(this, PdfViewActivity::class.java)
//            intent.putExtra("pdf", document)
//            startActivity(intent)
        }



        dataClinicHours = listOf(
            ClinicHours("Mon"),
            ClinicHours("Tue"),
            ClinicHours("Wed"),
            ClinicHours("Thu"),
            ClinicHours("Fri"),
            ClinicHours("Sat"),
            ClinicHours("Sun")
        )


        binding.etFirstName.setText(myObject?.name?.split(" ")?.getOrNull(0))
        binding.etLastName.setText(myObject?.name?.split(" ")?.getOrNull(1))
        binding.etMobileNumber.setText(myObject?.mobileNumber)
        val position = when (myObject?.gender) {
            "Male" -> {
                1
            }

            "Female" -> {
                2
            }

            else -> {
                3
            }
        }
        binding.genderSpinner.setSelection(position, true)
        binding.etCountry.text = myObject?.country
        binding.etState.text = myObject?.state
        binding.etCity.text = myObject?.city
        binding.etZipCode.setText(myObject?.zipCode)
        Glide.with(this).load(myObject?.profileLoad).into(binding.userProfile)
        myObject?.countryCode?.let { binding.ccpMyProfile.setCountryForPhoneCode(it.toInt()) }

        binding.etFrom.isSelected = true


        binding.degreeTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val item = parent.getItemAtPosition(pos)

                    if (item != "Select degree type") {
                        binding.llDegreeType.setBackgroundResource(R.drawable.white_border_background)
                        binding.TvDegreeType.visibility = View.GONE
                        binding.TvDegreeType.text = ""
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        binding.llSpecialization.setOnClickListener {
            openPopUp("")
        }


        binding.llFrom.setOnClickListener {
            openPopUpForClinic()
        }



        binding.selectEndDate.setOnClickListener {
            DateFormat.dateSelectorFutureDates(
                this,
                binding.expirationDate
            )
        }
        binding.selectEndDatePermit.setOnClickListener {
            DateFormat.dateSelectorFutureDates(
                this,
                binding.expirationDatePermit
            )
        }


        binding.etPrimaryLanguage.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)
        binding.etFrom.addTextChangedListener(textWatcher)
        binding.etEmergencyNumber.addTextChangedListener(textWatcher)
        binding.etSpecialization.addTextChangedListener(textWatcher)
        binding.etPractice.addTextChangedListener(textWatcher)
        binding.etCollege.addTextChangedListener(textWatcher)
        binding.uploadDocumentTv.addTextChangedListener(textWatcher)
        binding.expirationDate.addTextChangedListener(textWatcher)
        binding.PermitTv.addTextChangedListener(textWatcher)
        binding.expirationDatePermit.addTextChangedListener(textWatcher)


        binding.SignUpButton.setSafeOnClickListener {
            if (FormValidations.doctorsRegistration(
                    binding.firstNameLL,
                    binding.etFirstName,
                    binding.tvFirstName,
                    binding.middleNameLL,
                    binding.etMiddleName,
                    binding.tvMiddleName,
                    binding.lastNameLL,
                    binding.etLastName,
                    binding.tvLastName,
                    binding.primaryLanguageLL,
                    binding.etPrimaryLanguage,
                    binding.tvPrimaryLanguage,
                    binding.llMobileNumber,
                    binding.etMobileNumber,
                    binding.tvMobileNumber,
                    binding.llGender,
                    binding.genderSpinner,
                    binding.TvGender,
                    binding.etAddress,
                    binding.llAddress,
                    binding.tvAddress,
                    binding.llCountry,
                    binding.etCountry,
                    binding.tvCountry,
                    binding.StateLL,
                    binding.etState,
                    binding.tvState,
                    binding.llZipCode,
                    binding.etZipCode,
                    binding.tvZip,
                    binding.llCity,
                    binding.etCity,
                    binding.tvCity,
                    binding.llFrom,
                    binding.etFrom,
                    binding.tvFrom,
                    binding.llEmergencyNumber,
                    binding.etEmergencyNumber,
                    binding.tvEmergencyNumber,
                    binding.llSpecialization,
                    binding.etSpecialization,
                    binding.tvSpecialization,
                    binding.llPractice,
                    binding.etPractice,
                    binding.tvPractice,
                    binding.llCollege,
                    binding.etCollege,
                    binding.tvCollege,
                    binding.uploadDocumentLL,
                    binding.uploadDocumentTv,
                    binding.tvDocument,
                    binding.selectEndDate,
                    binding.expirationDate,
                    binding.tvEndDate,
                    this,
                    binding.llDegreeType,
                    binding.degreeTypeSpinner,
                    binding.TvDegreeType,
                    binding.uploadDocumentLL,
                    binding.uploadDocumentTv,
                    binding.tvDocument,
                    binding.selectEndDate,
                    binding.expirationDate,
                    binding.tvEndDate,
                    binding.PermitLL,
                    binding.PermitTv,
                    binding.tvPermit,
                    binding.selectEndDatePermit,
                    binding.expirationDatePermit,
                    binding.tvEndDatePermit

                )
            ) {

                val request = SignUpRequestVendorDoctor()

                request.name = myObject!!.name
                request.countryCode = myObject.countryCode
                request.mobileNumber = myObject.mobileNumber
                request.email = myObject.email
                request.address = myObject.address
                request.userDob = myObject.dob
                request.city = myObject.city
                request.state = myObject.state
                request.country = myObject.country
                request.password = myObject.password
                request.userTypes = myObject.userTypes
                request.gender = myObject.gender
                request.zipCode = myObject.zipCode
                request.countryIsoCode = myObject.countryIsoCode
                request.stateIsoCode = myObject.stateIsoCode
                request.documents.media = myObject.documents
                request.languageId = myObject.languageId
                request.profilePic = CommonForImages.base64Image.toString()

                request.doctorvetUserObj.firstName =
                    myObject.name.split(" ").getOrNull(0).toString()
                request.doctorvetUserObj.lastName = myObject.name.split(" ").getOrNull(1).toString()
                request.doctorvetUserObj.phoneNumber = myObject.mobileNumber
                request.doctorvetUserObj.middleName = binding.etMiddleName.text.toString()
                request.doctorvetUserObj.primarySpokenLanguage =
                    binding.etPrimaryLanguage.text.toString()
                request.doctorvetUserObj.clinicAddress = binding.etAddress.text.toString()
                request.doctorvetUserObj.clinicHours = clinicHoursRequest
                request.doctorvetUserObj.emergencyNumber = binding.etEmergencyNumber.text.toString()
                request.doctorvetUserObj.specialization = binding.etSpecialization.text.toString()
                request.doctorvetUserObj.experience = binding.etPractice.text.toString()
                request.doctorvetUserObj.collegeUniversity = binding.etPractice.text.toString()
                request.doctorvetUserObj.degreeType =
                    binding.degreeTypeSpinner.selectedItem.toString()
                request.doctorvetUserObj.license = binding.uploadDocumentTv.text.toString()
                request.doctorvetUserObj.licenseExpiry = binding.expirationDate.text.toString()
                request.doctorvetUserObj.permit = binding.PermitTv.text.toString()
                request.doctorvetUserObj.permitExpiry = binding.expirationDatePermit.text.toString()
                request.doctorvetUserObj.certificateOfDoctor = document
                request.doctorvetUserObj.lat = latitude
                request.doctorvetUserObj.long = longitude


//                viewModel.signInApiVendorDoctor(request)

                if (!binding.certificate.isVisible) {
                    androidExtension.alertBox("Please Upload Document", this)
                } else {
                    viewModel.signInApiVendorDoctor(request)
                }


            }
        }

        observeSignUpResponse()
        observeUploadedImagesResponse()
        observeLatLngResponse()
        observeResponceLocationList()
    }

    private fun isValidFields() {
        FormValidations.doctorsRegistration(
            binding.firstNameLL,
            binding.etFirstName,
            binding.tvFirstName,
            binding.middleNameLL,
            binding.etMiddleName,
            binding.tvMiddleName,
            binding.lastNameLL,
            binding.etLastName,
            binding.tvLastName,
            binding.primaryLanguageLL,
            binding.etPrimaryLanguage,
            binding.tvPrimaryLanguage,
            binding.llMobileNumber,
            binding.etMobileNumber,
            binding.tvMobileNumber,
            binding.llGender,
            binding.genderSpinner,
            binding.TvGender,
            binding.etAddress,
            binding.llAddress,
            binding.tvAddress,
            binding.llCountry,
            binding.etCountry,
            binding.tvCountry,
            binding.StateLL,
            binding.etState,
            binding.tvState,
            binding.llZipCode,
            binding.etZipCode,
            binding.tvZip,
            binding.llCity,
            binding.etCity,
            binding.tvCity,
            binding.llFrom,
            binding.etFrom,
            binding.tvFrom,
            binding.llEmergencyNumber,
            binding.etEmergencyNumber,
            binding.tvEmergencyNumber,
            binding.llSpecialization,
            binding.etSpecialization,
            binding.tvSpecialization,
            binding.llPractice,
            binding.etPractice,
            binding.tvPractice,
            binding.llCollege,
            binding.etCollege,
            binding.tvCollege,
            binding.uploadDocumentLL,
            binding.uploadDocumentTv,
            binding.tvDocument,
            binding.selectEndDate,
            binding.expirationDate,
            binding.tvEndDate,
            this, binding.llDegreeType,
            binding.degreeTypeSpinner,
            binding.TvDegreeType,
            binding.uploadDocumentLL,
            binding.uploadDocumentTv,
            binding.tvDocument,
            binding.selectEndDate,
            binding.expirationDate,
            binding.tvEndDate,
            binding.PermitLL,
            binding.PermitTv,
            binding.tvPermit,
            binding.selectEndDatePermit,
            binding.expirationDatePermit,
            binding.tvEndDatePermit

        )
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
            isValidFields()
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

            data.clear()
            data.add(Specialization("Animal Welfare"))
            data.add(Specialization("Behavior"))
            data.add(Specialization("Cardiology"))
            data.add(Specialization("Critical Care"))
            data.add(Specialization("Dentistry"))
            data.add(Specialization("Dermatology"))
            data.add(Specialization("General Practice"))
            data.add(Specialization("Internal Medicine"))
            data.add(Specialization("Lab Animal Medicine"))
            data.add(Specialization("Microbiology"))
            data.add(Specialization("Nutrition"))
            data.add(Specialization("Ophthalmology"))
            data.add(Specialization("Pathology"))
            data.add(Specialization("Pharmacology"))
            data.add(Specialization("Poultry"))
            data.add(Specialization("Preventive Medicine"))
            data.add(Specialization("Radiology"))
            data.add(Specialization("Rehabilitation Medicine"))
            data.add(Specialization("Surgery"))
            data.add(Specialization("Theriogenology"))
            data.add(Specialization("Toxicology"))
            data.add(Specialization("Practitioner"))
            data.add(Specialization("Zoo Medicine"))
            setAdapter(data)






            SearchEditText.addTextChangedListener(textWatchers)



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun setAdapter(result: ArrayList<Specialization>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = openDialogSpecialization(this, result, this)
        recyclerView.adapter = adapter
    }

    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().isEmpty()) {
                setAdapter(data)
            } else {
                filterData(s.toString())
            }


        }
    }

    private fun filterData(searchText: String) {
        val filteredList: ArrayList<Specialization> = ArrayList()

        for (item in filterData) {
            try {
                if (item.name.lowercase().contains(searchText.lowercase())) {
                    filteredList.add(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        try {
            adapter.filterList(filteredList)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun clickValue(name: String) {
        binding.etSpecialization.text = name
        dialog.dismiss()
    }


    private fun openPopUpForClinic() {
        try {
            val bindingPopup = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialogClinic = DialogUtils().createDialog(this, bindingPopup.rootView, 0)!!
            recyclerViewClinic = bindingPopup.findViewById(R.id.popup_recyclerView)
            tickClinic = bindingPopup.findViewById(R.id.tick)
            recyclerViewClinic.layoutManager = LinearLayoutManager(this)


            // Iterate through the data and set the selected state for each ClinicHours object
            for (hour in clinicHoursRequest) {
                val selectedHour = clinicHoursRequest.find {
                    it.isSelected && it.openTime.isNotEmpty() && it.closeTime.isNotEmpty()
                }
                if (selectedHour != null) {
                    hour.isSelected = selectedHour.isSelected
                    hour.openTime = selectedHour.openTime
                    hour.closeTime = selectedHour.closeTime
                }
            }


            setAdapterClinic(dataClinicHours)

            tickClinic.isVisible = true

            val title = bindingPopup.findViewById<TextView>(R.id.popupTitle)
            val search = bindingPopup.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            title.text = getString(R.string.clinic_hours)
            search.isVisible = false
            val backButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)
            backButton.setOnClickListener { dialogClinic.dismiss() }


            tickClinic.setOnClickListener {
                clinicHoursRequest =
                    dataClinicHours.filter { it.isSelected && it.openTime.isNotEmpty() && it.closeTime.isNotEmpty() }
                val formattedClinicHours =
                    clinicHoursRequest.joinToString(", ") { "${it.day} (${it.openTime} - ${it.closeTime})" }
                binding.etFrom.text = formattedClinicHours
                dialogClinic.dismiss()
            }

            dialogClinic.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setAdapterClinic(result: List<ClinicHours>) {
        recyclerViewClinic.layoutManager = LinearLayoutManager(this)
        adapterClinic = ClinicHoursSelectAdapter(this, result)
        recyclerViewClinic.adapter = adapterClinic
    }


    private fun observeSignUpResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._signInStateFlow.collect { response ->

                    when(response) {

                        is Resource.Success -> {
                            CommonForImages.base64Image = null
                            Progresss.stop()
                            if (response.data?.statusCode == 200) {
                                try {

                                    alertBoxFinishActivity(
                                        response.data.responseMessage,
                                        this@DoctorRoleActivity
                                    )


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            CommonForImages.base64Image = null
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@DoctorRoleActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@DoctorRoleActivity)
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }


    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pickPdf.launch(pdfIntent)
    }


    private val pickPdf =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val fileUris = result.data!!.data


                    val path = ImageRotation.writeFileContent(fileUris!!, this)
                    val fileSelected = File(path!!)


                    val mimeType = if (getMimeType(fileSelected.toString()) != null) {
                        getMimeType(fileSelected.toString())
                    } else {
                        getMimeType(path)
                    }
                    val surveyBody =
                        RequestBody.create(mimeType?.toMediaTypeOrNull(), fileSelected!!)
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
                                initPdfViewer(document,this@DoctorRoleActivity,binding.certificateImage)

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


    fun alertBoxFinishActivity(message: String, context: Context) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade
        builder.setPositiveButton("ok") { _, _ ->
            val intent = Intent(this@DoctorRoleActivity, VendorLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            alertDialog!!.dismiss()

        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }


//     Search location


    val textWatcherLocation = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(string: Editable?) {
            if (string.toString() != "") {
                binding.getLocationBySearch.isVisible = true
                viewModelLocation.getLocationApi(
                    Uri.encode(string.toString()),
                    SavedPrefManager.getStringPreferences(
                        this@DoctorRoleActivity,
                        SavedPrefManager.GMKEY
                    ).toString()
                )

            } else {
                binding.getLocationBySearch.isVisible = false
            }

        }

    }


    private fun observeResponceLocationList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelLocation._LocationStateFlow.collect { response ->

                    when (response) {
                        is Resource.Success -> {
                            try {
                                setAdapterLocation(response.data!!.predictions)
                            } catch (e: Exception) {
                                e.printStackTrace()
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

    private fun setAdapterLocation(predictions: ArrayList<LocationPrediction>) {
        binding.getLocationBySearch.layoutManager = LinearLayoutManager(this)
        AdapterLocation = LocationSelectAdapter(this, predictions, this)
        binding.getLocationBySearch.adapter = AdapterLocation
    }

    override fun getLocation(locationName: String) {
        binding.etAddress.setText(locationName)
        binding.etAddress.requestFocus()
        binding.etAddress.isFocusableInTouchMode = true
        Home.showKeyboard(this)
        binding.etAddress.setSelection(binding.etAddress.text.length)
        binding.getLocationBySearch.isVisible = false
        viewModelLocation.getLatLng(
            locationName,
            SavedPrefManager.getStringPreferences(this@DoctorRoleActivity, SavedPrefManager.GMKEY)
                .toString()
        )
    }

    private fun observeLatLngResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelLocation._latLngStateFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {
                                latitude = response.data?.results?.get(0)?.geometry?.location?.lat!!
                                longitude = response.data.results[0].geometry.location.lng

                            } catch (e: Exception) {
                                e.printStackTrace()
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

    //   Colleges and Universties
    private fun getLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->

                if (task.isSuccessful && task.result != null) {
                    try {
                        val lastLocation = task.result
                        lat = (lastLocation)!!.latitude
                        long = (lastLocation).longitude


                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    val textWatcherCollege = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(string: Editable?) {
            if (string.toString() != "") {
                binding.getCollegeName.isVisible = true
                val location = "$lat $long"
                getCollegesAndUniversityApi(
                    query = "$string university and colleges in ${binding.etCountry.text}",
                    key = SavedPrefManager.getStringPreferences(
                        this@DoctorRoleActivity,
                        SavedPrefManager.GMKEY
                    ).toString()
                )


            } else {
                binding.getCollegeName.isVisible = false
            }

        }

    }

    private fun getCollegesAndUniversityApi(query: String, key: String) {

        val url =
            ("https://maps.googleapis.com/maps/api/place/textsearch/json?query=$query&key=$key")
        val queue = Volley.newRequestQueue(this)
        val stateReq =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    var location: JSONObject
                    try {
                        try {
                            Log.d("tag", "response.toString() => ${response.toString()}")


                            var objectData = JSONObject(response.toString())
                            val jsonArrayData: JSONArray = objectData.getJSONArray("results")
                            val jsonObject = jsonArrayData[0] as JSONObject
                            var data = ArrayList<NearByLocationResults>()

                            val gson = Gson()
                            val finalResult = ArrayList<NearByLocationResults>()
                            val finalResponse = gson.fromJson(
                                jsonArrayData.toString(),
                                Array<NearByLocationResults>::class.java
                            )
                            finalResult.addAll(finalResponse)
                            setAdapterColleges(finalResult)


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        location = response!!.getJSONArray("results").getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")

                    } catch (e1: JSONException) {
                        e1.printStackTrace()
                    }
                }
            ) { error -> Log.d("Error.Response", error.toString()) }

        queue.add(stateReq)


    }

    private fun setAdapterColleges(predictions: ArrayList<NearByLocationResults>) {
        binding.getCollegeName.layoutManager = LinearLayoutManager(this)
        AdapterLocationCollege = LocationNearByAdapter(this, predictions, this)
        binding.getCollegeName.adapter = AdapterLocationCollege
    }

    override fun getLocationNearBy(locationName: String) {
        binding.etCollege.setText(locationName)
        Home.showKeyboard(this)
        binding.getCollegeName.isVisible = false
    }


}