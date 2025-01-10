package com.callisdairy.Vendor.Fragmnets

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.ModalClass.DialogData
import com.callisdairy.R
import com.callisdairy.Utils.*
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.request.EditProfileVendorRequest
import com.callisdairy.api.response.CountryList
import com.callisdairy.databinding.FragmentEditVendorProfileBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.EditProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditVendorProfileFragment : Fragment(), PopupItemClickListener {
    private var _binding: FragmentEditVendorProfileBinding? = null
    private val binding get() = _binding!!

    var flag = ""
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: openDialog
    var token = ""
    var countryCode = ""
    var stateCode = ""
    var cityCode = ""
    var gender = ""
    var data = ArrayList<DialogData>()
    var filterData: ArrayList<CountryList> = ArrayList()


    var imageFile: File? = null
    var photoURI: Uri? = null
    private var CAMERA: Int = 2
    var imagePath = ""
    private val GALLERY = 1
    lateinit var image: Uri
    var profilepic = ""
    var USER_IMAGE_UPLOADED_PROFILE = false
    private var base64: String? = null



    val c = Calendar.getInstance()


    lateinit var backVendor: ImageView

    private val viewModel: EditProfileViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditVendorProfileBinding.inflate(layoutInflater, container, false)

        backVendor = activity?.findViewById(R.id.backVendor)!!


        binding.etFirstName.addTextChangedListener(textWatcher)
        binding.etLastName.addTextChangedListener(textWatcher)
        binding.etMobileNumber.addTextChangedListener(textWatcher)
        binding.etMail.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)
        binding.etCountry.addTextChangedListener(textWatcher)
        binding.etState.addTextChangedListener(textWatcher)
        binding.etZipCode.addTextChangedListener(textWatcher)
        binding.etCity.addTextChangedListener(textWatcher)




        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)!!

        backVendor.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

        binding.llCity.setOnClickListener {
            flag = "City"
            openPopUp(flag)
        }

        binding.StateLL.setOnClickListener {
            flag = "State"
            openPopUp(flag)
        }

        binding.llCountry.setOnClickListener {
            flag = "Country"
            openPopUp(flag)
        }


        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        c.set(year, month , date)
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



        binding.userImageSelected.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                RequestPermission.requestMultiplePermissions(requireContext())
            } else {
                selectImage()
            }
        }

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                gender = item.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


//         Observer APi's

        viewModel.editViewProfileApi(token)





        binding.doneButton.setOnClickListener {
            FormValidations.editProfileVendor(
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
                requireContext()
            )
            if (binding.etFirstName.text.isNotEmpty() && binding.etFirstName.text.length > 2 && binding.etLastName.text.isNotEmpty()
                && binding.etLastName.text.length > 2 && binding.etMail.text.isNotEmpty() && binding.etMail.text.matches(
                    Regex(
                        FormValidations.emailPattern
                    )
                ) && binding.etMobileNumber.text.isNotEmpty() && binding.etMobileNumber.text.length > 9
                && binding.genderSpinner.selectedItem != "Select gender" && binding.etCity.text.isNotEmpty() && binding.etState.text.isNotEmpty()
                && binding.etCountry.text.isNotEmpty()
            ) {

                val request = EditProfileVendorRequest()

                request.name = "${binding.etFirstName.text} ${binding.etLastName.text}"
                request.userTypes = "VENDOR"
                request.email = binding.etMail.text.toString()
                request.mobileNumber = binding.etMobileNumber.text.toString()
                request.countryCode = binding.ccpMyProfile.selectedCountryCode
                request.gender = gender
                request.address = binding.etAddress.text.toString()
                request.country = binding.etCountry.text.toString()
                request.state = binding.etState.text.toString()
                request.city = binding.etCity.text.toString()
                request.countryIsoCode = countryCode
                request.stateIsoCode = stateCode
                request.zipCode = binding.etZipCode.text.toString()
                request.userDob = binding.txtDateOfBirth.text.toString()


                if (USER_IMAGE_UPLOADED_PROFILE) {
                    request.profilePic = "data:image/png;base64,${profilepic}"
                } else {
                    request.profilePic = profilepic
                }


                viewModel.editProfileVendorApi(token, request)

            }
        }
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })

        observeEditViewProfileResponse()
        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()
        observeEditProfileResponse()

    }



    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                if (s.length == 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FormValidations.editProfileVendor(
                binding.etFirstName, binding.tvFirstName, binding.firstNameLL,
                binding.etLastName, binding.tvLastName, binding.lastNameLL,
                binding.etMail, binding.tvEmail, binding.llEmail,
                binding.etMobileNumber, binding.tvMobileNumber, binding.llMobileNumber,
                binding.genderSpinner, binding.TvGender, binding.llGender,
                binding.etAddress, binding.tvAddress, binding.llAddress,
                binding.etCity, binding.tvCity, binding.llCity,
                binding.etState, binding.tvState, binding.StateLL,
                binding.etZipCode, binding.tvZip, binding.llZipCode,
                binding.etCountry, binding.tvCountry, binding.llCountry,requireContext()
            )
        }
    }


//     Get Image From Camera and Gallery


    @SuppressLint("IntentReset", "InflateParams")
    private fun selectImage() {
        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val CameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        CameraButton.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
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
            if (resultCode == AppCompatActivity.RESULT_OK) {



                if (data != null) {
                    image = data.data!!
                    setImageToPlaceHolder(image)
                }

            }
        }
        else if (requestCode == CAMERA) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                try {

                    imageFile = File(imagePath)
                    setImageToPlaceHolder(Uri.fromFile(imageFile))
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }
    }

    fun setImageToPlaceHolder(newUri:Uri){
        val getRealPath = ImageRotation.getRealPathFromURI2(requireContext(), newUri!!)
        val finalBitmap = ImageRotation.modifyOrientation(getBitmap(getRealPath)!!, getRealPath)

        if (getRealPath != null) {
            imageFile = File(getRealPath)

            binding.userProfile.borderColor = Color.parseColor("#6FCFB9")
            Glide.with(this).load(imageFile).into(binding.userProfile)
            profilepic = finalBitmap?.let { bitmapToString(it) }.toString()
            USER_IMAGE_UPLOADED_PROFILE = true


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
            Toast
                .makeText(
                    requireContext(),
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    fun getBitmap(path: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val f = File(path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    fun bitmapToString(`in`: Bitmap): String {
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


//   Dropdowns

    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireContext(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val title = binding.findViewById<TextView>(R.id.popupTitle)
            val backButton = binding.findViewById<ImageView>(R.id.BackButton)
            backButton.setOnClickListener { dialog.dismiss() }


            val searchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)


            when (flag) {
                "State" -> {
                    title.text = getString(R.string.state)
                    viewModel.getStateListApi(countryCode)

                }
                "City" -> {
                    title.text = getString(R.string.city)
                    viewModel.getCityListApi(countryCode, stateCode)

                }
                "Country" -> {
                    title.text =getString(R.string.country)
                    viewModel.getCountryApi()

                }
            }
            searchEditText.addTextChangedListener(textWatchers)



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
                binding.etCity.text = ""
                binding.etState.text = ""

                dialog.dismiss()
            }
        }
    }


//    Edit Profile Api Observer

    private fun observeEditProfileResponse() {


        lifecycleScope.launchWhenCreated {
            viewModel._editProfileVendorData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {
                                finishDone(response.data.responseMessage)
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

    private fun finishDone(message: String) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        builder.setPositiveButton("ok") { _, _ ->
            activity?.finishAfterTransition()
            alertDialog!!.dismiss()

        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }


//    View Profile Api Response

    private fun observeEditViewProfileResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._editProfileViewData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {

                                val newString = response.data.result.name.split(" ")

                                if (newString.size > 1) {
                                    binding.etFirstName.setText(newString[0])
                                    binding.etLastName.setText(newString[1])
                                } else {
                                    binding.etFirstName.setText(response.data.result.name)
                                }
                                binding.etMail.setText(response.data.result.email)
                                binding.txtDateOfBirth.setText(response.data.result.userDob)
                                binding.etAddress.setText(response.data.result.address)
                                binding.etMobileNumber.setText(response.data.result.mobileNumber)
                                binding.etCountry.text = response.data.result.country
                                binding.etState.text = response.data.result.state
                                binding.etCity.text = response.data.result.city
                                binding.etZipCode.setText(response.data.result.zipCode)
                                binding.ccpMyProfile.setCountryForPhoneCode(response.data.result.countryCode.toInt())
                                countryCode = response.data.result.countryIsoCode
                                stateCode = response.data.result.stateIsoCode

                                when (response.data.result.gender.lowercase()) {

                                    "male" -> {
                                        binding.genderSpinner.setSelection(1)
                                        gender = "Male"
                                        binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                        binding.TvGender.visibility = View.GONE
                                        binding.TvGender.text = ""
                                    }


                                    "female" -> {
                                        binding.genderSpinner.setSelection(2)
                                        gender = "Female"
                                        binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                        binding.TvGender.visibility = View.GONE
                                        binding.TvGender.text = ""
                                    }
                                    else -> {
                                        binding.genderSpinner.setSelection(3)
                                        gender = "Others"
                                        binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                        binding.TvGender.visibility = View.GONE
                                        binding.TvGender.text = ""
                                    }


                                }

                                Glide.with(requireContext()).load(response.data.result.profilePic)
                                    .placeholder(R.drawable.placeholder).into(binding.userProfile)

                                profilepic = response.data.result.profilePic


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

        lifecycleScope.launchWhenCreated {
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

//    City List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launchWhenCreated {
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
                if (item.name.lowercase().contains(searchText.lowercase())) {
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


}