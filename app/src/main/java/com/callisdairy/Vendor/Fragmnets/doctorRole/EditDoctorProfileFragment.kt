package com.callisdairy.Vendor.Fragmnets.doctorRole

import com.callisdairy.AdapterVendors.ClinicHoursSelectAdapter
import RequestPermission.Companion.requestMultiplePermissions
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
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
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Adapter.openDialogSpecialization
import com.callisdairy.Interface.LocationClick
import com.callisdairy.Interface.LocationClickNearBy
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.SpecializationClick
import com.callisdairy.ModalClass.ClinicHours
import com.callisdairy.ModalClass.Specialization
import com.callisdairy.R
import com.callisdairy.UI.Activities.PdfViewActivity
import com.callisdairy.Utils.CommonForImages
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Home
import com.callisdairy.Utils.ImageRotation
import com.callisdairy.Utils.ImageRotation.bitmapToString
import com.callisdairy.Utils.ImageRotation.getBitmap
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.RetrievePDFFromURL
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.OtherApi.NearByLocationResults
import com.callisdairy.api.request.EditProfileVetDoctorRequest
import com.callisdairy.api.response.CountryList
import com.callisdairy.databinding.FragmentEditDoctorProfileBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.callisdairy.viewModel.SignUpViewModel
import com.callisdairy.viewModel.VendorCommonViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.kanabix.api.LocationPrediction
import com.callisdairy.extension.androidExtension
import com.theartofdev.edmodo.cropper.CropImage
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
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class EditDoctorProfileFragment : Fragment(), PopupItemClickListener, SpecializationClick,
    LocationClick, LocationClickNearBy {

    private var _binding: FragmentEditDoctorProfileBinding? = null
    private val binding get() = _binding!!
    var token = ""
    var genderValue = ""
    var degree = ""
    private val viewModel: VendorCommonViewModel by viewModels()
    var countryCodeRequest = ""
    var stateCodeRequest = ""
    var cityCodeRequest = ""

    val c = Calendar.getInstance()

    var document = ""
    lateinit var backVendor: ImageView

    var dataClinicHours: List<ClinicHours> = listOf()
    var clinicHoursRequest: List<ClinicHours> = listOf()
    private lateinit var dialogClinic: Dialog
    private lateinit var recyclerViewClinic: RecyclerView
    private lateinit var tickClinic: LinearLayout
    lateinit var adapterClinic: ClinicHoursSelectAdapter
    lateinit var AdapterLocationCollege: LocationNearByAdapter

    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()


    var imageFile: File? = null
    var photoURI: Uri? = null
    private var CAMERA: Int = 2
    var imagePath = ""
    var image: Uri? = null
    var profilepic = ""
    var USER_IMAGE_UPLOADED_PROFILE = false

    var latitude = 0.0
    var longitude = 0.0

    var lat = 0.0
    var long = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null


    var flag = ""
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: openDialog
    private val viewModelSignup: SignUpViewModel by viewModels()
    var filterData: ArrayList<CountryList> = ArrayList()


    private lateinit var dialogSpecilization: Dialog
    private lateinit var recyclerViewSpecilization: RecyclerView
    lateinit var adapterSpecilization: openDialogSpecialization
    var filterDataSpecilization: ArrayList<Specialization> = ArrayList()
    val data = ArrayList<Specialization>()


    lateinit var AdapterLocation: LocationSelectAdapter

    private val viewModelLocation: GoogleLocationApiViewModel by viewModels()

    private val GALLERY = 1

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDoctorProfileBinding.inflate(layoutInflater, container, false)
        backVendor = activity?.findViewById(R.id.backVendor)!!
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)
            .toString()

        fusedLocationClient =
            requireActivity().let { LocationServices.getFusedLocationProviderClient(it) }
        getLocation()

        viewModel.viewProfileApi(token)

        backVendor.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

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

        binding.certificateCross.setSafeOnClickListener {
            binding.addCertificate.isVisible = true
            binding.certificate.isVisible = false
            document = ""
        }


        binding.addCertificate.setSafeOnClickListener {
            selectPdf()
        }


        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        c.set(year, month, date)
        binding.llDateOfBirth.setSafeOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
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




        binding.etCollege.addTextChangedListener(textWatcherCollege)


        binding.selectEndDate.setOnClickListener {
            DateFormat.dateSelectorFutureDates(requireContext(), binding.expirationDate)
        }
        binding.selectEndDatePermit.setOnClickListener {
            DateFormat.dateSelectorFutureDates(requireContext(), binding.expirationDatePermit)
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


        binding.llSpecialization.setOnClickListener {
            openPopUpSpecilization("")
        }


        binding.certificateImage.setSafeOnClickListener {
            val intent = Intent(requireContext(), PdfViewActivity::class.java)
            intent.putExtra("pdf", document)
            startActivity(intent)
        }


        binding.SignUpButton.setSafeOnClickListener {
            val request = EditProfileVetDoctorRequest()
            request.firstName = binding.etFirstName.text.toString()
            request.middleName = binding.etMiddleName.text.toString()
            request.lastName = binding.etLastName.text.toString()
            request.primarySpokenLanguage = binding.etPrimaryLanguage.text.toString()
            request.phoneNumber = binding.etMobileNumber.text.toString()
            request.gender = genderValue

            request.userDob = binding.txtDateOfBirth.text.toString()
            request.clinicAddress = binding.etAddress.text.toString()

            request.country = binding.etCountry.text.toString()
            request.state = binding.etState.text.toString()
            request.city = binding.etCity.text.toString()
            request.countryIsoCode = countryCodeRequest
            request.stateIsoCode = stateCodeRequest

            request.clinicHours = dataClinicHours
            request.emergencyNumber = binding.etEmergencyNumber.text.toString()
            request.specialization = binding.etSpecialization.text.toString()
            request.experience = binding.etPractice.text.toString()
            request.collegeUniversity = binding.etCollege.text.toString()
            request.degreeType = binding.degreeTypeSpinner.selectedItem.toString()
            request.license = binding.uploadDocumentTv.text.toString()
            request.licenseExpiry = binding.expirationDate.text.toString()
            request.permit = binding.PermitTv.text.toString()
            request.permitExpiry = binding.expirationDatePermit.text.toString()
            request.lat = latitude
            request.long = longitude
            request.certificateOfDoctor = document


            if (USER_IMAGE_UPLOADED_PROFILE) {
                request.profilePic = "data:image/png;base64,${profilepic}"
            } else {
                request.profilePic = profilepic
            }


            if (!binding.certificate.isVisible) {
                androidExtension.alertBox("Please Upload Document", requireContext())
            } else {
                viewModel.updateDoctorApi(token, request)
            }


        }



        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)

                if (item != "Select gender") {
                    genderValue = item.toString()
                    binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                    binding.TvGender.visibility = View.GONE
                    binding.TvGender.text = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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
                        degree = item.toString()
                        binding.llDegreeType.setBackgroundResource(R.drawable.white_border_background)
                        binding.TvDegreeType.visibility = View.GONE
                        binding.TvDegreeType.text = ""
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.userImageSelected.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestMultiplePermissions(requireContext())
            } else {
                selectImage()
            }
        }



        binding.etFrom.isSelected = true

        dataClinicHours = listOf(
            ClinicHours("Mon"),
            ClinicHours("Tue"),
            ClinicHours("Wed"),
            ClinicHours("Thu"),
            ClinicHours("Fri"),
            ClinicHours("Sat"),
            ClinicHours("Sun")
        )


        binding.llFrom.setSafeOnClickListener {
            openPopUpForClinic()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewProfileResponse()
        observeEditProfileResponse()
        observeCityListResponse()
        observeStateListResponse()
        observeCountryListResponse()
        observeLatLngResponse()
        observeResponceLocationList()
        observeUploadedImagesResponse()

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }

            })
    }


    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {


                                    response.data.result.apply {

                                        binding.etFirstName.setText(doctorVetProfile.firstName)
                                        binding.etLastName.setText(doctorVetProfile.lastName)
                                        binding.etMobileNumber.setText(doctorVetProfile.phoneNumber)
                                        binding.etAddress.setText(doctorVetProfile.clinicAddress)
                                        binding.etZipCode.setText(zipCode)
                                        binding.etCountry.text = country
                                        binding.etState.text = state
                                        binding.etCity.text = city
                                        binding.etMiddleName.setText(doctorVetProfile.middleName)
                                        binding.txtDateOfBirth.setText(response.data.result.userDob)
                                        binding.etPrimaryLanguage.setText(doctorVetProfile.primarySpokenLanguage)
                                        binding.etSpecialization.text =
                                            doctorVetProfile.specialization
                                        binding.etPractice.setText(doctorVetProfile.experience)
                                        binding.etEmergencyNumber.setText(doctorVetProfile.emergencyNumber)
                                        binding.ccpMyProfile.setCountryForPhoneCode(countryCode.toInt())
                                        binding.uploadDocumentTv.setText(doctorVetProfile.license)
                                        binding.expirationDate.text = doctorVetProfile.licenseExpiry
                                        binding.PermitTv.setText(doctorVetProfile.permit)
                                        binding.etCollege.setText(doctorVetProfile.collegeUniversity)
                                        binding.getCollegeName.isVisible = false

                                        binding.expirationDatePermit.text =
                                            doctorVetProfile.permitExpiry
                                        degree = doctorVetProfile.degreeType.toString()
                                        profilepic = doctorVetProfile.userProfileImage.toString()
                                        Glide.with(requireContext())
                                            .load(doctorVetProfile.userProfileImage)
                                            .placeholder(R.drawable.placeholder)
                                            .into(binding.userProfile)
                                        countryCodeRequest = countryIsoCode
                                        stateCodeRequest = stateIsoCode
                                        when (gender.lowercase()) {

                                            "male" -> {
                                                binding.genderSpinner.setSelection(1)
                                                genderValue = "Male"
                                                binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                                binding.TvGender.visibility = View.GONE
                                                binding.TvGender.text = ""
                                            }


                                            "female" -> {
                                                binding.genderSpinner.setSelection(2)
                                                genderValue = "Female"
                                                binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                                binding.TvGender.visibility = View.GONE
                                                binding.TvGender.text = ""
                                            }

                                            else -> {
                                                binding.genderSpinner.setSelection(3)
                                                genderValue = "Others"
                                                binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                                binding.TvGender.visibility = View.GONE
                                                binding.TvGender.text = ""
                                            }


                                        }


                                        val adapter = ArrayAdapter.createFromResource(
                                            requireContext(),
                                            R.array.degree_type,
                                            android.R.layout.simple_spinner_item
                                        )
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                        binding.degreeTypeSpinner.adapter = adapter

                                        val spinnerValues =
                                            resources.getStringArray(R.array.degree_type)
                                        val valueToSet = doctorVetProfile.degreeType
                                        var index = -1

                                        for (i in spinnerValues.indices) {
                                            if (spinnerValues[i] == valueToSet) {
                                                index = i
                                                break
                                            }
                                        }

                                        if (index != -1) {
                                            binding.degreeTypeSpinner.setSelection(index)
                                        }


                                        val daysOfWeek =
                                            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                                        dataClinicHours = daysOfWeek.map { day ->
                                            val clinicHour = doctorVetProfile.clinicHours.find {
                                                it.day.contains(day)
                                            }
                                            if (clinicHour != null) {
                                                ClinicHours(
                                                    day,
                                                    clinicHour.openTime,
                                                    clinicHour.closeTime,
                                                    clinicHour.isSelected
                                                )
                                            } else {
                                                ClinicHours(day, "", "", false)
                                            }
                                        }


                                        val clinicHoursRequest =
                                            dataClinicHours.filter { it.isSelected && it.openTime.isNotEmpty() && it.closeTime.isNotEmpty() }
                                        val formattedClinicHours =
                                            clinicHoursRequest.joinToString(", ") { "${it.day} (${it.openTime} - ${it.closeTime})" }
                                        binding.etFrom.text = formattedClinicHours


                                    }


                                    binding.progressBar.isVisible = false
                                    binding.add.isVisible = true
                                    binding.addCertificate.isVisible = false
                                    binding.certificate.isVisible = true
                                    document =
                                        response.data.result.doctorVetProfile.certificateOfDoctor!!
                                    RetrievePDFFromURL(
                                        requireContext(),
                                        binding.certificateImage
                                    ).execute(document)


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }
        }
    }

    private fun openPopUpForClinic() {
        try {
            val bindingPopup =
                LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialogClinic = DialogUtils().createDialog(requireContext(), bindingPopup.rootView, 0)!!
            recyclerViewClinic = bindingPopup.findViewById(R.id.popup_recyclerView)
            tickClinic = bindingPopup.findViewById(R.id.tick)
            recyclerViewClinic.layoutManager = LinearLayoutManager(requireContext())


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
            title.text = getString(R.string.clinic_hours)
            val backButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)
            backButton.setOnClickListener { dialogClinic.dismiss() }
            val search = bindingPopup.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            search.isVisible = false

            tickClinic.setOnClickListener {
                clinicHoursRequest =
                    dataClinicHours.filter { it.isSelected && it.openTime.isNotEmpty() && it.closeTime.isNotEmpty() }

                val formattedClinicHours =
                    clinicHoursRequest.joinToString(", ") { "${it.day} (${it.openTime} - ${it.closeTime})" }
                binding.etFrom.text = formattedClinicHours

                Log.d("TAG", "openPopUpForClinic:  ${clinicHoursRequest.size}")
                dialogClinic.dismiss()
            }

            dialogClinic.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Inside the com.callisdairy.AdapterVendors.ClinicHoursSelectAdapter
    private fun setAdapterClinic(result: List<ClinicHours>) {
        recyclerViewClinic.layoutManager = LinearLayoutManager(requireContext())
        adapterClinic = ClinicHoursSelectAdapter(requireContext(), result)
        recyclerViewClinic.adapter = adapterClinic
    }

    @SuppressLint("IntentReset")
    private fun selectImage() {
        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val CameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        CameraButton.setOnClickListener {


            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
                try {
                    imageFile = createImageFile()!!
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(
                        requireContext(),
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


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (resultCode == Activity.RESULT_CANCELED) {
                return
            }
        } catch (e: Exception) {
            Log.d("Exception===>", "Exception: $e")
        }


//        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
//            val images = ImagePicker.getImages(data)
//
//            val imageUrl = images.getOrNull(0)?.path
//            val imageFile = File(imageUrl)
//
//
//            val intent = CropImage.activity(Uri.fromFile(imageFile)).setInitialCropWindowPaddingRatio(0f).getIntent(requireContext())
//            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
//
//        }


        if (requestCode == CAMERA) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    imageFile = File(imagePath)
                    val intent = CropImage.activity(Uri.fromFile(imageFile))
                        .setInitialCropWindowPaddingRatio(0f).getIntent(requireContext())
                    startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

        }

        if (requestCode == GALLERY) {
            if (resultCode == AppCompatActivity.RESULT_OK) {



                if (data != null) {
                    image = data.data!!

                    val intent = CropImage.activity(image).setInitialCropWindowPaddingRatio(0f)
                        .getIntent(requireContext())
                    startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)


                }

            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data);
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val newUri = result.uri

                val getRealPath = ImageRotation.getRealPathFromURI2(requireContext(), newUri!!)
                val finalBitmap =
                    ImageRotation.modifyOrientation(getBitmap(getRealPath)!!, getRealPath)

                if (getRealPath != null) {
                    imageFile = File(getRealPath)

                    profilepic = finalBitmap?.let { bitmapToString(it) }.toString()
                    Glide.with(this).load(imageFile).into(binding.userProfile)
                    USER_IMAGE_UPLOADED_PROFILE = true


                }


            }
        }


    }

    private fun observeEditProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._profileUpdateDoctor.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishFragment(
                                        response.data.responseMessage,
                                        requireContext(),
                                        parentFragmentManager,
                                        requireActivity()
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }
        }
    }


    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireContext(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialougbackButton.setOnClickListener { dialog.dismiss() }


            val SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            when (flag) {
                "State" -> {
                    dialougTitle.text = "State"
                    viewModelSignup.getStateListApi(countryCodeRequest)

                }

                "City" -> {
                    dialougTitle.text = flag
                    viewModelSignup.getCityListApi(countryCodeRequest, stateCodeRequest)

                }

                "Country" -> {
                    dialougTitle.text = flag

                    viewModelSignup.getCountryApi()

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
                cityCodeRequest = data
                dialog.dismiss()

            }

            "State" -> {
                binding.etState.text = data
                stateCodeRequest = code
                binding.etCity.text = ""
                dialog.dismiss()
            }

            "Country" -> {
                binding.etCountry.text = data
                countryCodeRequest = code
                binding.etState.text = ""
                binding.etCity.text = ""
                dialog.dismiss()
            }

        }
    }


//    Country List Observer

    private fun observeCountryListResponse() {

        lifecycleScope.launchWhenCreated {

            viewModelSignup._countryStateFlow.collect { response ->

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
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(requireContext())
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
            viewModelSignup._stateData.collect { response ->

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
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(requireContext())
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
            viewModelSignup._citydata.collect { response ->

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
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(requireContext())
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    fun setAdapter(result: ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = openDialog(requireContext(), result, flag, this)
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


    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUpSpecilization(flag: String) {

        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialogSpecilization =
                DialogUtils().createDialog(requireContext(), binding.rootView, 0)!!
            recyclerViewSpecilization = binding.findViewById(R.id.popup_recyclerView)
            recyclerViewSpecilization.layoutManager = LinearLayoutManager(requireContext())


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
            setAdapterSpecilization(data)






            SearchEditText.addTextChangedListener(textWatchersSpecilization)



            dialogSpecilization.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun setAdapterSpecilization(result: ArrayList<Specialization>) {
        recyclerViewSpecilization.layoutManager = LinearLayoutManager(requireContext())
        adapterSpecilization = openDialogSpecialization(requireContext(), result, this)
        recyclerViewSpecilization.adapter = adapterSpecilization
    }

    private val textWatchersSpecilization = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().isEmpty()) {
                setAdapterSpecilization(data)
            } else {
                filterDataSpecilization(s.toString())
            }


        }
    }

    private fun filterDataSpecilization(searchText: String) {
        val filteredList: ArrayList<Specialization> = ArrayList()

        for (item in filterDataSpecilization) {
            try {
                if (item.name.lowercase().contains(searchText.lowercase())) {
                    filteredList.add(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        try {
            adapterSpecilization.filterList(filteredList)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun clickValue(name: String) {
        binding.etSpecialization.text = name
        dialog.dismiss()
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
                    SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.GMKEY)
                        .toString()
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
        binding.getLocationBySearch.layoutManager = LinearLayoutManager(requireContext())
        AdapterLocation = LocationSelectAdapter(requireContext(), predictions, this)
        binding.getLocationBySearch.adapter = AdapterLocation
    }

    override fun getLocation(locationName: String) {
        binding.etAddress.setText(locationName)
        binding.etAddress.requestFocus()
        binding.etAddress.isFocusableInTouchMode = true
        Home.showKeyboard(requireActivity())
        binding.etAddress.setSelection(binding.etAddress.text.length)
        binding.getLocationBySearch.isVisible = false
        viewModelLocation.getLatLng(
            locationName,
            SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.GMKEY)
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
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->

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


    private val textWatcherCollege = object : TextWatcher {
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
                        requireContext(),
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
        val queue = Volley.newRequestQueue(requireContext())
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
        binding.getCollegeName.layoutManager = LinearLayoutManager(requireContext())
        AdapterLocationCollege = LocationNearByAdapter(requireContext(), predictions, this)
        binding.getCollegeName.adapter = AdapterLocationCollege
    }

    override fun getLocationNearBy(locationName: String) {
        binding.etCollege.setText(locationName)
        Home.showKeyboard(requireActivity())
        binding.getCollegeName.isVisible = false
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


                    val path = ImageRotation.writeFileContent(fileUris!!, requireContext())
                    val fileSelected = File(path!!)


                    val mimeType =
                        if (CommonForImages.getMimeType(fileSelected.toString()) != null) {
                            CommonForImages.getMimeType(fileSelected.toString())
                        } else {
                            CommonForImages.getMimeType(path)
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
//                                Glide.with(this@DoctorRoleActivity).load(R.drawable.pdf).into(binding.certificateImage)
                                RetrievePDFFromURL(
                                    requireContext(),
                                    binding.certificateImage
                                ).execute(document)

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


}