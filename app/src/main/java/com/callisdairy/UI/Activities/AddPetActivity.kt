package com.callisdairy.UI.Activities

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.database.Cursor
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
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.AddPetViewAdapter
import com.callisdairy.Adapter.LocationNearByAdapter
import com.callisdairy.Adapter.LocationSelectAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Adapter.openDialogCatgeory
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.LocationClick
import com.callisdairy.Interface.LocationClickNearBy
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.RemoveImage
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.UI.Fragments.autoPlayVideo.toast
import com.callisdairy.Utils.CommonForImages
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Home
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.OtherApi.NearByLocationResults
import com.callisdairy.api.request.AddPetRequest
import com.callisdairy.api.request.MediaRequestHome
import com.callisdairy.api.request.UpdatePetRequest
import com.callisdairy.api.request.mediaUrls
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.PetCategoryListDocs
import com.callisdairy.databinding.ActivityAddPetBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.AddPetViewModel
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kanabix.api.LocationPrediction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class AddPetActivity : AppCompatActivity(), RemoveImage, PopupItemClickListener, Finish,
    LocationClickNearBy, LocationClick {

    private lateinit var binding: ActivityAddPetBinding
    val c: Calendar = Calendar.getInstance()

    var flag = ""
    var token = ""
    lateinit var socketInstance: SocketManager
    private var fusedLocationClient: FusedLocationProviderClient? = null


    private var flagForDetails = false
    private var flagForBack = false
    private var latitude = 0.0
    private var longitude = 0.0


    private var latRequest = 0.0
    private var longRequest = 0.0

    private var categoryId = ""
    var subCategoryId = ""
    var petId = ""
    var genderRequest = ""
    var petTypeId = ""
    var petBreedId = ""

    private lateinit var imagesSelected: AddPetViewAdapter
    lateinit var image: Uri
    var imageFile: File? = null


    var filterDataPetBeed = ArrayList<CountryList>()



    //    Uploaded Images Ur
    var imageArray: ArrayList<mediaUrls> = ArrayList()
    private var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()
    var filterData: ArrayList<PetCategoryListDocs> = ArrayList()

    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: openDialogCatgeory
    private lateinit var newAdapterLocation: LocationNearByAdapter

    private lateinit var adapterLocation: LocationSelectAdapter

    private lateinit var adapterCategory: openDialog


    private val viewModel: AddPetViewModel by viewModels()
    private val viewModelLocation: GoogleLocationApiViewModel by viewModels()


    @SuppressLint("SetTextI18n", "IntentReset", "NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        fusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }
        getLocation()

        petTypeId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.profileId).toString()

        val loginVendor = SavedPrefManager.getBooleanPreferences(this, SavedPrefManager.isVendor)



        if (loginVendor) {
            binding.llPetType.isEnabled = true
        } else {
            binding.llPetType.isEnabled = false
            val petType = SavedPrefManager.getStringPreferences(this, SavedPrefManager.profileType)
            binding.etPetType.text = petType
        }


        intent.getStringExtra("flag")?.let {
            flag = it
            binding.title.text = flag
        }

        intent.getStringExtra("id")?.let {
            petId = it
        }

        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token)!!
        apiRequestAndValidation()


        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)


                 if(item != "Select gender"){
                     genderRequest = item.toString()
                     binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                     binding.tvGender.visibility = View.GONE
                     binding.tvGender.text = ""
                 }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }




        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }


        binding.llPetType.setSafeOnClickListener {
            flag = "Pet Type"
            openPopUp(flag)
        }


        binding.llBreed.setOnClickListener {
            if(binding.etPetType.text.isEmpty()){
                toast("Please select pet type.")
                return@setOnClickListener
            }

            flag = "Pet Breed"
            openPopUp(flag)


        }

        binding.etAnimalShelter.addTextChangedListener(textWatcherLocation)




        setAdapterImages()

        binding.etAddress.addTextChangedListener(textWatcherAddressLocation)



        binding.etPetName.addTextChangedListener(textWatcher)
        binding.txtDateOfBirth.addTextChangedListener(textWatcher)
        binding.etOrigin.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)
        binding.etSize.addTextChangedListener(textWatcher)
        binding.etDescription.addTextChangedListener(textWatcher)

        binding.etSize.addTextChangedListener(textWatcherRemoveZero)





        binding.addPost.setSafeOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                RequestPermission.requestMultiplePermissions(this)
            } else {
                val config = ImagePickerConfig {
                    mode = ImagePickerMode.MULTIPLE
                    language = "EN"
                    returnMode =  ReturnMode.NONE
                    isIncludeAnimation = true
                    isFolderMode = true
                    isIncludeVideo = false
                    isOnlyVideo = false
                    arrowColor = Color.WHITE
                    folderTitle = "Folder"
                    imageTitle = "Tap to select"
                    doneButtonText = "DONE"
                    limit = 4
                    isShowCamera = true
                    savePath = ImagePickerSavePath("Camera")
                    savePath = ImagePickerSavePath(Environment.getExternalStorageDirectory().path, isRelative = false)

                }

                launcher.launch(config)


            }
        }


        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        c.set(year, month, date)

        binding.llDOBCalender.setSafeOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->
                    val dateFormat =
                        DateFormat.dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")
                    binding.txtDateOfBirth.text = dateFormat

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }

        binding.llCelebrateCalender.setSafeOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    val dateFormat =
                        DateFormat.dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")
                    binding.txtCelebrate.text = dateFormat

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }


        binding.llVaccination.setSafeOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    val dateFormat =
                        DateFormat.dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")
                    binding.txtVaccination.text = dateFormat

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }

        binding.llAppointment.setSafeOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    val dateFormat =
                        DateFormat.dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")
                    binding.txtAppointment.text = dateFormat

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.minDate = c.timeInMillis

            datePickerDialog.show()
        }







        observeUploadedImagesResponse()
        observeAddPetResponse()
        observeUpdatePetResponse()
        observeCategoryListResponse()
        observeSubCategoryListResponse()
        observeViewPetResponse()
        observeResponseLocationNewList()
        observePetCategoryListResponse()
        observeLatLngResponse()
        observeResponseLocationList()
        observePetBreedListResponse()
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            FormValidations.addPetDetails(
                binding.etPetName,
                binding.llPetName,
                binding.tvPetName,
                binding.txtDateOfBirth,
                binding.llDateOfBirth,
                binding.tvDateOfBirth,
                binding.etOrigin,
                binding.llOrigin,
                binding.tvOrigin,
                binding.genderSpinner,
                binding.llGender,
                binding.tvGender,
                this@AddPetActivity,
                imageArray.size,
                binding.imagesView,
                binding.llAddImage,
                binding.llAddress,
                binding.etAddress,
                binding.tvAddress

            )


        }
    }


    private val launcher = registerImagePicker { result: List<Image> ->
        if (imageArray.size >= 4) {
            toast("Limit already have reached.")
        } else {
            val remainingSlots = 4 - imageArray.size
            val n = minOf(result.size, remainingSlots)
            for (i in 0 until n) {
                val imageUrl = result[i].path
                val mimeType = CommonForImages.getMimeType(imageUrl)
                val imageFile = File(imageUrl)
                val surveyBody = imageFile.asRequestBody(mimeType?.toMediaTypeOrNull())
                requestMultiImagesAndVideos.add(
                    MultipartBody.Part.createFormData(
                        "files",
                        imageFile.name,
                        surveyBody
                    )
                )
            }
            if (requestMultiImagesAndVideos.isNotEmpty()){
                viewModel.uploadMultipleImagesApi(requestMultiImagesAndVideos)
            }

        }
    }




//     Add Pet Observer

    private fun observeAddPetResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addPetData.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishActivity(
                                        response.data.responseMessage,
                                        this@AddPetActivity,
                                        this@AddPetActivity
                                    )

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {

                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@AddPetActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddPetActivity)
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }
        }
    }

    private fun observeUpdatePetResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._updatePetData.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishActivity(
                                        response.data.responseMessage,
                                        this@AddPetActivity,
                                        this@AddPetActivity
                                    )

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {

                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@AddPetActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddPetActivity)
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }

            }
        }
    }

    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(columnIndex)
        }
        cursor.close()
        return res
    }


//    Upload Multiple Images Observer


    @SuppressLint("NotifyDataSetChanged")
    private fun observeUploadedImagesResponse() {

        lifecycleScope.launch {
            viewModel._uploadImagesData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {
                        binding.ProgressBarScroll.isVisible = false
                        binding.addPost.isVisible = true
                        if (response.data?.responseCode == 200) {
                            try {

                                requestMultiImagesAndVideos.clear()
                                for (i in 0 until response.data.result.size) {
                                    val request = MediaRequestHome()

                                    request.mediaUrlMobile = response.data.result[i].mediaUrl
                                    request.thumbnail = response.data.result[i].thumbnail
                                    request.mediaUrlWeb = response.data.result[i].mediaUrl
                                    imageArray.add(
                                        mediaUrls(
                                            request,
                                            response.data.result[i].mediaType
                                        )
                                    )
                                }

                                imagesSelected.notifyDataSetChanged()
                                binding.llAddImage.isVisible = imageArray.size < 4

                                binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)
                                binding.imagesView.text = ""
                                binding.imagesView.isVisible = false


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.ProgressBarScroll.isVisible = false
                        binding.addPost.isVisible = true
                        requestMultiImagesAndVideos.clear()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddPetActivity)
                        }
                    }

                    is Resource.Loading -> {
                        binding.ProgressBarScroll.isVisible = true
                        binding.addPost.isVisible = false
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }

    private fun setAdapterImages() {
        binding.PostRecyclerView.layoutManager = GridLayoutManager(this, 2)
        imagesSelected = AddPetViewAdapter(this, imageArray, this)
        binding.PostRecyclerView.adapter = imagesSelected


    }

    override fun deleteImage(adapterPosition: Int) {
        imageArray.removeAt(adapterPosition)
        imagesSelected.notifyItemRemoved(adapterPosition)
        imagesSelected.notifyItemRangeChanged(adapterPosition, imageArray.size)
        binding.llAddImage.isVisible = imageArray.size < 4
    }


    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)


            val dialogTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialogBackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialogBackButton.setSafeOnClickListener { dialog.dismiss() }


            val searchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            dialogTitle.text = flag

            when (flag) {
                "Category" -> {
                    viewModel.categoryListApi("PET")
                    searchEditText.addTextChangedListener(textWatchers)
                }

                "Pet Type" -> {
                    viewModel.getPetCategoryApi()
                }

                "Pet Breed" -> {
                    viewModel.petBreedListApi(petCategoryId = petTypeId)
                    searchEditText.addTextChangedListener(textWatchers)
                }

                else -> {
                    viewModel.listSubCategoryApi(categoryId)
                    searchEditText.addTextChangedListener(textWatchers)
                }
            }





            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    override fun getData(data: String, flag: String, code: String) {
        when (flag) {
            "Pet Type" -> {
                binding.etPetType.text = data
                petTypeId = code
                dialog.dismiss()
            }

            "Pet Breed" -> {
                binding.etBreed.text = data
                petBreedId = code
                dialog.dismiss()
            }

        }
    }


//    category List Observer

    private fun observeCategoryListResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._categoryData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {
                                filterData.clear()
                                filterData = response.data.result.docs
                                setAdapter(response.data.result.docs, "Category")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddPetActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    private fun observeSubCategoryListResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._listSubCategoryData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {
                                filterData.clear()
                                filterData = response.data.result.docs
                                setAdapter(response.data.result.docs, "Sub Category")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddPetActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    private fun setAdapter(result: ArrayList<PetCategoryListDocs>, flag1: String) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = openDialogCatgeory(this, result, flag1, this)
        recyclerView.adapter = adapter
    }


    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            if (flag== "Pet Breed"){
                filterDataPetBreed(s.toString())
            }else{
                filterData(s.toString())
            }

        }
    }

    private fun filterDataPetBreed(searchText: String) {
        val filteredList = filterDataPetBeed.filter { item ->
            try {
                item.name.contains(searchText, ignoreCase = true) ||
                        item.petBreedName.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        adapterCategory.filterList(filteredList as ArrayList<CountryList>)
    }


    private fun filterData(searchText: String) {
        val filteredList: ArrayList<PetCategoryListDocs> = ArrayList()


        for (item in filterData) {
            try {
                if (item.categoryName.lowercase().contains(searchText.lowercase())) {
                    filteredList.add(item)
                }
                if (item.subCategoryName.lowercase().contains(searchText.lowercase())) {
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

    override fun finishActivity() {
        finishAfterTransition()
    }


//    View Pet Api Observer

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewPetResponse() {

        lifecycleScope.launch {
            viewModel._petDescriptionData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {


                                with(response.data.result) {

                                    for (i in mediaUrls.indices) {
                                        val request = MediaRequestHome()

                                        request.mediaUrlMobile = mediaUrls[i].media.mediaUrlMobile
                                        request.thumbnail = mediaUrls[i].media.thumbnail
                                        request.mediaUrlWeb = mediaUrls[i].media.mediaUrlMobile
                                        imageArray.add(mediaUrls(request, mediaUrls[i].type))
                                    }
                                    categoryId = categoryResult?._id.toString()
                                    subCategoryId = subCategoryResult?._id.toString()
                                    petTypeId =petCategoryId?._id!!
                                    if (petBreedIdRes!= null){
                                        petBreedId = petBreedIdRes?._id!!
                                    }
                                    binding.llAddImage.isVisible = imageArray.size < 4
                                    setAdapterImages()

                                    if (imageArray.size != 0) {
                                        binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)
                                        binding.imagesView.text = ""
                                        binding.imagesView.isVisible = false
                                    }

                                    setEditFieldValue(binding.etPetName, petName)
                                    setTextFieldValue(binding.txtDateOfBirth,dob)
                                    setEditFieldValue(binding.etOrigin, origin)
                                    setEditFieldValue(binding.etSize, petDescription.size)
                                    setEditFieldValue(binding.etDescription, description)
                                    setTextFieldValue(binding.etBreed, breed)
                                    setEditFieldValue(binding.etPurchasedStore, purchesStore)
                                    setTextFieldValue(binding.etPetType, petCategoryId.petCategoryName)
                                    setTextFieldValue(binding.txtCelebrate, celebrate)
                                    setEditFieldValue(binding.etmovie, movie)
                                    setEditFieldValue(binding.etSong, song)
                                    setEditFieldValue(binding.etToy, toy)
                                    setEditFieldValue(binding.etColor, favColour)
                                    setEditFieldValue(binding.etFood, favFood)
                                    setEditFieldValue(binding.etPlace, favPlace)
                                    setTextFieldValue(binding.txtVaccination, petDescription.lastVaccinate)
                                    setTextFieldValue(binding.txtAppointment, doctorAppoint)
                                    setEditFieldValue(binding.etTravel, travel)
                                    setEditFieldValue(binding.etLanguage, language)
                                    setEditFieldValue(binding.etDiet, dietFreq)
                                    setEditFieldValue(binding.etAnimalShelter, animalShelter)
                                    setEditFieldValue(binding.etMedicalReport, medicalReport)
                                    setEditFieldValue(binding.etHabits, habits)
                                    setEditFieldValue(binding.etAwards, awards)
                                    setEditFieldValue(binding.etFavoriteClimate, favoriteClimate)
                                    setEditFieldValue(binding.etPOB, placeOfBirth)
                                    setEditFieldValue(binding.etCommonDiseases, commonDiseases)
                                    setEditFieldValue(binding.etInsurance, insurance)
                                    setEditFieldValue(binding.etBestFriend, bestFriend)
                                    setEditFieldValue(binding.etPOT, placeOfTravel)
                                    setEditFieldValue(binding.etVerterinary, veterinary)
                                    setEditFieldValue(binding.etAddress,petAddress)
                                    binding.getLocationBySearch.isVisible = false





                                    when (gender?.lowercase()) {

                                        "male" -> {
                                            binding.genderSpinner.setSelection(1)
                                            genderRequest = getString(R.string.male)
                                            binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                            binding.tvGender.visibility = View.GONE
                                            binding.tvGender.text = ""
                                        }


                                        "female" -> {
                                            binding.genderSpinner.setSelection(2)
                                            genderRequest = getString(R.string.female)
                                            binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                            binding.tvGender.visibility = View.GONE
                                            binding.tvGender.text = ""
                                        }

                                        else -> {
                                            binding.genderSpinner.setSelection(3)
                                            genderRequest = getString(R.string.others)
                                            binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                            binding.tvGender.visibility = View.GONE
                                            binding.tvGender.text = ""
                                        }

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
                            androidExtension.alertBox(message, this@AddPetActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }


    private val textWatcherRemoveZero = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            if (s.toString().length == 1 && s.toString().startsWith("0")) {
                s.clear()
            }
        }
    }


    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url?.trim())
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }


//    Api For shelter


    private val textWatcherLocation = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(string: Editable?) {
            if (string.toString() != "") {
                binding.SearchFunctionality.isVisible = true
                val location = "$latitude $longitude"
                viewModelLocation.getNearByLocationApi(
                    keyword = string.toString(), location = location,
                    radius = 1500, type = "pet_store", key = SavedPrefManager.getStringPreferences(this@AddPetActivity,SavedPrefManager.GMKEY).toString()
                )

            } else {
                binding.SearchFunctionality.isVisible = false
            }

        }

    }


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
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation).longitude


                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun observeResponseLocationNewList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelLocation._getNearByLocationFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {
                            try {
                                setAdapterLocation(response.data!!.results)
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

    private fun setAdapterLocation(predictions: ArrayList<NearByLocationResults>) {
        binding.SearchFunctionality.layoutManager = LinearLayoutManager(this)
        newAdapterLocation = LocationNearByAdapter(this, predictions, this)
        binding.SearchFunctionality.adapter = newAdapterLocation
    }

    override fun getLocationNearBy(locationName: String) {
        binding.etAnimalShelter.setText(locationName)
        Home.showKeyboard(this)
        binding.SearchFunctionality.isVisible = false
    }


    //    Pet Category List Observer

    private fun observePetCategoryListResponse() {

        lifecycleScope.launchWhenCreated {

            viewModel._petCategoryData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.statusCode == 200) {
                            try {
//                                filterData = response.data.result
                                setAdapterCategory(response.data.result)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddPetActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetActivity)
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }

    private fun setAdapterCategory(result: ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapterCategory = openDialog(this, result, flag, this)
        recyclerView.adapter = adapterCategory
    }


//     Search location


    private val textWatcherAddressLocation = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(string: Editable?) {
            if (string.toString() != "") {
                binding.getLocationBySearch.isVisible = true
                viewModelLocation.getLocationApi(Uri.encode(string.toString()), SavedPrefManager.getStringPreferences(this@AddPetActivity,SavedPrefManager.GMKEY).toString())

            } else {
                binding.getLocationBySearch.isVisible = false
            }

        }

    }


    private fun observeResponseLocationList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelLocation._LocationStateFlow.collect { response ->

                    when (response) {
                        is Resource.Success -> {
                            try {
                                setAdapterAddressLocation(response.data!!.predictions)
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

    private fun setAdapterAddressLocation(predictions: ArrayList<LocationPrediction>) {
        binding.getLocationBySearch.layoutManager = LinearLayoutManager(this)
        adapterLocation = LocationSelectAdapter(this, predictions, this)
        binding.getLocationBySearch.adapter = adapterLocation
    }

    override fun getLocation(locationName: String) {
        binding.etAddress.setText(locationName)
        binding.etAddress.requestFocus()
        binding.etAddress.isFocusableInTouchMode = true
        Home.showKeyboard(this)
        binding.etAddress.setSelection(binding.etAddress.text.length)
        binding.getLocationBySearch.isVisible = false
        viewModelLocation.getLatLng(locationName, SavedPrefManager.getStringPreferences(this@AddPetActivity,SavedPrefManager.GMKEY).toString())
    }

    private fun observeLatLngResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelLocation._latLngStateFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {
                                latRequest = response.data?.results?.get(0)?.geometry?.location?.lat!!
                                longRequest = response.data.results[0].geometry.location.lng

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


    private fun apiRequestAndValidation() {
        if (flag == "Edit Pet") {
            binding.buttonAdd.text = getString(R.string.update)
            binding.MoreAdd.text = getString(R.string.update_more_detail)

            viewModel.petDescriptionApi(token, petId)




            binding.addButton.setSafeOnClickListener {

                if (!flagForBack) {  // TODO That Mean We Can Add Pet
                    FormValidations.addPetDetails(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtDateOfBirth,
                        binding.llDateOfBirth,
                        binding.tvDateOfBirth,
                        binding.etOrigin,
                        binding.llOrigin,
                        binding.tvOrigin,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        this@AddPetActivity,
                        imageArray.size,
                        binding.imagesView,
                        binding.llAddImage,
                        binding.llAddress,
                        binding.etAddress,
                        binding.tvAddress
                    )
                    if (binding.etPetName.text.isNotEmpty() && binding.txtDateOfBirth.text.isNotEmpty() && binding.etOrigin.text.isNotEmpty()
                        && binding.genderSpinner.selectedItem != "Select gender" && imageArray.size > 0 && binding.etAddress.text.isNotEmpty()
                    ) {

                        val request = UpdatePetRequest()

                        request.upload_file = imageArray
                        request.petName = binding.etPetName.text.toString()
                        request.dob = binding.txtDateOfBirth.text.toString()
                        request.origin = binding.etOrigin.text.toString()
                        request.petAddress = binding.etAddress.text.toString()
                        request.lat = latRequest
                        request.long = longRequest
                        request.gender = genderRequest
                        request.petCategoryId = petTypeId
                        request.size = binding.etSize.text.toString()
                        request.description = binding.etDescription.text.toString()
                        request.breed = binding.etBreed.text.toString()
                        request.petBreedId = petBreedId
                        request.purchesStore = binding.etPurchasedStore.text.toString()
                        request.petType = binding.etPetType.text.toString()
                        request.celebrate = binding.txtCelebrate.text.toString()
                        request.movie = binding.etmovie.text.toString()
                        request.song = binding.etSong.text.toString()
                        request.toy = binding.etToy.text.toString()
                        request.favColour = binding.etColor.text.toString()
                        request.favFood = binding.etFood.text.toString()
                        request.favPlace = binding.etPlace.text.toString()
                        request.lastVaccinate = binding.txtVaccination.text.toString()
                        request.doctorAppoint = binding.txtAppointment.text.toString()
                        request.travel = binding.etTravel.text.toString()
                        request.language = binding.etLanguage.text.toString()
                        request.dietFreq = binding.etDiet.text.toString()
                        request.veterinary = binding.etVerterinary.text.toString()

//                        Add More Details
                        request.animalShelter = binding.etAnimalShelter.text.toString()
                        request.medicalReport = binding.etMedicalReport.text.toString()
                        request.habits = binding.etHabits.text.toString()
                        request.awards = binding.etAwards.text.toString()
                        request.favoriteClimate = binding.etFavoriteClimate.text.toString()
                        request.placeOfBirth = binding.etPOB.text.toString()
                        request.commonDiseases = binding.etCommonDiseases.text.toString()
                        request.insurance = binding.etInsurance.text.toString()
                        request.bestFriend = binding.etBestFriend.text.toString()
                        request.placeOfTravel = binding.etPOT.text.toString()


                        viewModel.updatePetAPi(token, petId, request)

                    }
                } else { // TODO this Mean we get back on This UI

                    binding.BasicInfoLL.isVisible = true
                    binding.MoreAddDetalsLL.isVisible = false
                    flagForDetails = false
                    flagForBack = false
                    binding.scroll.scrollY = 0
                    binding.buttonAdd.text = getString(R.string.add)
                    binding.MoreAdd.text = getString(R.string.add_more_details)
                }


            }

            binding.addMoreButton.setSafeOnClickListener {
                if (!flagForDetails) {
                    FormValidations.addPetDetails(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtDateOfBirth,
                        binding.llDateOfBirth,
                        binding.tvDateOfBirth,
                        binding.etOrigin,
                        binding.llOrigin,
                        binding.tvOrigin,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        this@AddPetActivity,
                        imageArray.size,
                        binding.imagesView,
                        binding.llAddImage,
                        binding.llAddress,
                        binding.etAddress,
                        binding.tvAddress
                    )
                    if (binding.etPetName.text.isNotEmpty() && binding.txtDateOfBirth.text.isNotEmpty() && binding.etOrigin.text.isNotEmpty()
                        && binding.genderSpinner.selectedItem != "Select gender" && imageArray.isNotEmpty() && binding.etAddress.text.isNotEmpty()
                    ) {

                        binding.BasicInfoLL.isVisible = false
                        binding.MoreAddDetalsLL.isVisible = true
                        flagForDetails = true
                        flagForBack = true
                        binding.scroll.scrollY = 0
                        binding.buttonAdd.text = getString(R.string.previous)
                        binding.MoreAdd.text = getString(R.string.add)


                    }
                } else { // TODO Api Request Create Here with all details


                    val request = UpdatePetRequest()

                    request.petName = binding.etPetName.text.toString()
                    request.dob = binding.txtDateOfBirth.text.toString()
                    request.origin = binding.etOrigin.text.toString()
                    request.gender = genderRequest
                    request.size = binding.etSize.text.toString()
                    request.petAddress = binding.etAddress.text.toString()
                    request.lat = latRequest
                    request.long = longRequest
                    request.petCategoryId = petTypeId
                    request.description = binding.etDescription.text.toString()
                    request.breed = binding.etBreed.text.toString()
                    request.petBreedId = petBreedId
                    request.purchesStore = binding.etPurchasedStore.text.toString()
                    request.petType = binding.etPetType.text.toString()
                    request.celebrate = binding.txtCelebrate.text.toString()
                    request.movie = binding.etmovie.text.toString()
                    request.song = binding.etSong.text.toString()
                    request.toy = binding.etToy.text.toString()
                    request.favColour = binding.etColor.text.toString()
                    request.favFood = binding.etFood.text.toString()
                    request.favPlace = binding.etPlace.text.toString()
                    request.lastVaccinate = binding.txtVaccination.text.toString()
                    request.doctorAppoint = binding.txtAppointment.text.toString()
                    request.travel = binding.etTravel.text.toString()
                    request.language = binding.etLanguage.text.toString()
                    request.dietFreq = binding.etDiet.text.toString()
                    request.veterinary = binding.etVerterinary.text.toString()
                    request.upload_file = imageArray

                    request.animalShelter = binding.etAnimalShelter.text.toString()
                    request.medicalReport = binding.etMedicalReport.text.toString()
                    request.habits = binding.etHabits.text.toString()
                    request.awards = binding.etAwards.text.toString()
                    request.favoriteClimate = binding.etFavoriteClimate.text.toString()
                    request.placeOfBirth = binding.etPOB.text.toString()
                    request.commonDiseases = binding.etCommonDiseases.text.toString()
                    request.insurance = binding.etInsurance.text.toString()
                    request.bestFriend = binding.etBestFriend.text.toString()
                    request.placeOfTravel = binding.etPOT.text.toString()


                    viewModel.updatePetAPi(token, petId, request)

                }

            }


        } else {
            binding.buttonAdd.text = getString(R.string.add)
            binding.MoreAdd.text = getString(R.string.add_more_details)



            binding.addButton.setSafeOnClickListener {


                if (!flagForBack) {  // TODO That Mean We Can Add Pet
                    FormValidations.addPetDetails(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtDateOfBirth,
                        binding.llDateOfBirth,
                        binding.tvDateOfBirth,
                        binding.etOrigin,
                        binding.llOrigin,
                        binding.tvOrigin,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        this@AddPetActivity,
                        imageArray.size,
                        binding.imagesView,
                        binding.llAddImage,
                        binding.llAddress,
                        binding.etAddress,
                        binding.tvAddress

                    )
                    if (binding.etPetName.text.isNotEmpty() && binding.txtDateOfBirth.text.isNotEmpty() && binding.etOrigin.text.isNotEmpty()
                        && binding.genderSpinner.selectedItem != "Select gender" && imageArray.size > 0 && binding.etAddress.text.isNotEmpty()
                    ) {

                        val request = AddPetRequest()



                        request.petName = binding.etPetName.text.toString()
                        request.dob = binding.txtDateOfBirth.text.toString()
                        request.origin = binding.etOrigin.text.toString()
                        request.petAddress = binding.etAddress.text.toString()
                        request.lat = latRequest
                        request.long = longRequest
                        request.petCategoryId = petTypeId
                        request.gender = binding.genderSpinner.selectedItem.toString()
                        request.size = binding.etSize.text.toString()
                        request.description = binding.etDescription.text.toString()
                        request.breed = binding.etBreed.text.toString()
                        request.petBreedId = petBreedId
                        request.purchesStore = binding.etPurchasedStore.text.toString()
                        request.petType = binding.etPetType.text.toString()
                        request.celebrate = binding.txtCelebrate.text.toString()
                        request.upload_file = imageArray


                        viewModel.addPetAPi(token, request)

                    }
                } else { // TODO this Mean we get back on This UI

                    binding.BasicInfoLL.isVisible = true
                    binding.MoreAddDetalsLL.isVisible = false
                    flagForDetails = false
                    flagForBack = false
                    binding.scroll.scrollY = 0
                    binding.buttonAdd.text = getString(R.string.add)
                    binding.MoreAdd.text = getString(R.string.add_more_details)
                }


            }

            binding.addMoreButton.setSafeOnClickListener {
                if (!flagForDetails) {
                    FormValidations.addPetDetails(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtDateOfBirth,
                        binding.llDateOfBirth,
                        binding.tvDateOfBirth,
                        binding.etOrigin,
                        binding.llOrigin,
                        binding.tvOrigin,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        this@AddPetActivity,
                        imageArray.size,
                        binding.imagesView,
                        binding.llAddImage,
                        binding.llAddress,
                        binding.etAddress,
                        binding.tvAddress

                    )
                    if (binding.etPetName.text.isNotEmpty() && binding.txtDateOfBirth.text.isNotEmpty() && binding.etOrigin.text.isNotEmpty()
                        && binding.genderSpinner.selectedItem != "Select gender" && imageArray.isNotEmpty() && binding.etAddress.text.isNotEmpty()
                    ) {

                        binding.BasicInfoLL.isVisible = false
                        binding.MoreAddDetalsLL.isVisible = true
                        flagForDetails = true
                        flagForBack = true
                        binding.scroll.scrollY = 0
                        binding.buttonAdd.text = getString(R.string.previous)
                        binding.MoreAdd.text = getString(R.string.add)


                    }
                } else { // TODO Api Request Create Here with all details


                    val request = AddPetRequest()

                    request.petName = binding.etPetName.text.toString()
                    request.dob = binding.txtDateOfBirth.text.toString()
                    request.origin = binding.etOrigin.text.toString()
                    request.petAddress = binding.etAddress.text.toString()
                    request.lat = latRequest
                    request.long = longRequest
                    request.gender = genderRequest
                    request.petCategoryId = petTypeId
                    request.size = binding.etSize.text.toString()
                    request.description = binding.etDescription.text.toString()
                    request.breed = binding.etBreed.text.toString()
                    request.petBreedId = petBreedId
                    request.purchesStore = binding.etPurchasedStore.text.toString()
                    request.petType = binding.etPetType.text.toString()
                    request.celebrate = binding.txtCelebrate.text.toString()
                    request.movie = binding.etmovie.text.toString()
                    request.song = binding.etSong.text.toString()
                    request.toy = binding.etToy.text.toString()
                    request.favColour = binding.etColor.text.toString()
                    request.favFood = binding.etFood.text.toString()
                    request.favPlace = binding.etPlace.text.toString()
                    request.lastVaccinate = binding.txtVaccination.text.toString()
                    request.doctorAppoint = binding.txtAppointment.text.toString()
                    request.travel = binding.etTravel.text.toString()
                    request.language = binding.etLanguage.text.toString()
                    request.dietFreq = binding.etDiet.text.toString()
                    request.veterinary = binding.etVerterinary.text.toString()
                    request.upload_file = imageArray

                    request.animalShelter = binding.etAnimalShelter.text.toString()
                    request.medicalReport = binding.etMedicalReport.text.toString()
                    request.habits = binding.etHabits.text.toString()
                    request.awards = binding.etAwards.text.toString()
                    request.favoriteClimate = binding.etFavoriteClimate.text.toString()
                    request.placeOfBirth = binding.etPOB.text.toString()
                    request.commonDiseases = binding.etCommonDiseases.text.toString()
                    request.insurance = binding.etInsurance.text.toString()
                    request.bestFriend = binding.etBestFriend.text.toString()
                    request.placeOfTravel = binding.etPOT.text.toString()

                    viewModel.addPetAPi(token, request)

                }

            }


        }
    }


    private fun observePetBreedListResponse() {

        lifecycleScope.launch {
            viewModel._petBreeddata.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.statusCode == 200) {
                            try {

                                filterDataPetBeed = response.data.result
                                setAdapterCategory(response.data.result)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddPetActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }




    private fun setEditFieldValue(field: EditText, value: String?) {
        field.setText(value?.takeIf { it.isNotBlank() } ?: "")
    }

    private fun setTextFieldValue(field: TextView, value: String?) {
        field.text = value?.takeIf { it.isNotBlank() } ?: ""
    }


}
