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
import com.callisdairy.Adapter.OpenDialogForPetName
import com.callisdairy.Adapter.PostViewAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.PetNameClickListener
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.RemoveImage
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.UI.Fragments.autoPlayVideo.toast
import com.callisdairy.Utils.CommonForImages
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.request.AddMissingPetRequest
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.api.response.MyPetListDocs
import com.callisdairy.databinding.ActivityAddMissingPetBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.AddMissingPetViewModel
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class AddMissingPetActivity : AppCompatActivity(), RemoveImage, Finish , PopupItemClickListener,PetNameClickListener {

    private lateinit var binding: ActivityAddMissingPetBinding

    var flag = ""
    var genderValue = ""
    var token = ""
    var petId = ""
    val c = Calendar.getInstance()

    lateinit var image: Uri

    private lateinit var imagesSelected: PostViewAdapter
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var latitude = 0.0
    private var longitude = 0.0
    val request = AddMissingPetRequest()
    lateinit var socketInstance: SocketManager


    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: openDialog
    lateinit var adapterForPetName: OpenDialogForPetName
    var filterData: ArrayList<CountryList> = ArrayList()

    //    Uploaded Images Url
    var imageFile: File? = null
    var imageArray: ArrayList<String> = ArrayList()
    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()

    var petTypeId = ""
    var petIdRequest = ""
    var petBreedId = ""
    var dialogFlag = ""
    var filterDataPetName: ArrayList<MyPetListDocs> = ArrayList()


    private val viewModel: AddMissingPetViewModel by viewModels()



    @SuppressLint("IntentReset")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMissingPetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        fusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }


        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)
        petTypeId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.profileId).toString()


        binding.llPetType.isEnabled = false


        intent?.getStringExtra("flag")?.let {
            flag = it
        }

        intent?.getStringExtra("petId")?.let {
            petId = it
        }

        intent?.getStringExtra("petIdRequest")?.let {
            petIdRequest = it
        }

        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()



        binding.switchClick.setOnCheckedChangeListener { _, isChecked ->
            binding.petIdText.isVisible = isChecked
            binding.llTrackerId.isVisible = isChecked

            if (!isChecked){
                binding.tvTrackerId.text = ""
                binding.llTrackerId.setBackgroundResource(R.drawable.white_border_background)
            }

        }



        binding.llAddImage.setSafeOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    RequestPermission.requestMultiplePermissions(this)
                }
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


        binding.llPetType.setSafeOnClickListener {
            dialogFlag = "Pet Type"
            openPopUp(dialogFlag)
        }


        binding.etBreed.setOnClickListener {
            if(binding.etPetType.text.isEmpty()){
                toast("Please select pet type.")
                return@setOnClickListener
            }
            dialogFlag = "Pet Breed"
            openPopUp(dialogFlag)
        }



        binding.llPetNameFilled.setOnClickListener {
            dialogFlag = "Pet Name"
            openPopUp(dialogFlag)
        }


        binding.etPetName.addTextChangedListener(textWatcher)
        binding.txtLastSeen.addTextChangedListener(textWatcher)
        binding.etColor.addTextChangedListener(textWatcher)
        binding.etBreed.addTextChangedListener(textWatcher)
        binding.etPeculiarity.addTextChangedListener(textWatcher)
        binding.etName.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etContact.addTextChangedListener(textWatcher)
        binding.etTrackerId.addTextChangedListener(textWatcher)
        val petType = SavedPrefManager.getStringPreferences(this,SavedPrefManager.profileType)
        binding.etPetType.text = petType






        if (flag == "Edit") {
            binding.txtAdd.setText(R.string.update)
            binding.tvTitle.setText(R.string.edit_missing_pet)
            viewModel.viewMissingPetApi(token, petId)




            binding.addButton.setSafeOnClickListener {

                if (!binding.switchClick.isChecked){
                    binding.petIdText.isVisible = false
                    binding.llTrackerId.isVisible = false
                    FormValidations.addPet(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtLastSeen,
                        binding.llLastSeen,
                        binding.tvDateOfBirth,
                        binding.etPetType,
                        binding.llPetType,
                        binding.tvType,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        binding.etColor,
                        binding.llColor,
                        binding.tvColor,
                        binding.etBreed,
                        binding.llBreed,
                        binding.tvBreed,
                        binding.etPeculiarity,
                        binding.llPeculiarity,
                        binding.tvPeculiarity,
                        binding.etName,
                        binding.llName,
                        binding.tvName,
                        binding.etAddress,
                        binding.llAddress,
                        binding.tvAddress,
                        binding.etEmail,
                        binding.llEmail,
                        binding.tvEmail,
                        binding.etContact,
                        binding.llContact,
                        binding.tvContact,
                        this@AddMissingPetActivity,
                        imageArray,
                        binding.llAddImage,
                        binding.tvImage
                    )
                    if (binding.etPetName.text.isNotEmpty() && binding.txtLastSeen.text.isNotEmpty() && binding.etPetType.text.isNotEmpty()
                        && binding.genderSpinner.selectedItem != "Select gender" && binding.etColor.text.isNotEmpty()
                        && binding.etBreed.text.isNotEmpty() && binding.etPeculiarity.text.isNotEmpty()
                        && binding.etName.text.isNotEmpty() && binding.etAddress.text.isNotEmpty()
                        && binding.etEmail.text.isNotEmpty() && binding.etEmail.text.matches(Regex(FormValidations.emailPattern))
                        && binding.etContact.text.isNotEmpty() && binding.etContact.text.length > 9 && imageArray.size > 0
                    ) {
                        genderValue = binding.genderSpinner.selectedItem.toString()

                        request.petId = petIdRequest
                        request.petName = binding.etPetName.text.toString()
                        request.lastSeen = binding.txtLastSeen.text.toString()
                        request.type = binding.etPetType.text.toString()
                        request.gender = genderValue
                        request.trackerID = binding.etTrackerId.text.toString()
                        request.color = binding.etColor.text.toString()
                        request.breed = binding.etBreed.text.toString()
                        request.peculiarity = binding.etPeculiarity.text.toString()
                        request.userDetails.name = binding.etName.text.toString()
                        request.userDetails.address = binding.etAddress.text.toString()
                        request.userDetails.email = binding.etEmail.text.toString()
                        request.userDetails.mobileNumber = binding.etContact.text.toString()
                        request.petImage = imageArray


                        getLocation()
                    }
                }else{
                    binding.petIdText.isVisible = true
                    binding.llTrackerId.isVisible = true

                    FormValidations.addPetCheck(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtLastSeen,
                        binding.llLastSeen,
                        binding.tvDateOfBirth,
                        binding.etPetType,
                        binding.llPetType,
                        binding.tvType,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        binding.etColor,
                        binding.llColor,
                        binding.tvColor,
                        binding.etBreed,
                        binding.llBreed,
                        binding.tvBreed,
                        binding.etPeculiarity,
                        binding.llPeculiarity,
                        binding.tvPeculiarity,
                        binding.etName,
                        binding.llName,
                        binding.tvName,
                        binding.etAddress,
                        binding.llAddress,
                        binding.tvAddress,
                        binding.etEmail,
                        binding.llEmail,
                        binding.tvEmail,
                        binding.etContact,
                        binding.llContact,
                        binding.tvContact,
                        this@AddMissingPetActivity,
                        imageArray,
                        binding.llAddImage,
                        binding.tvImage,
                        binding.etTrackerId,
                        binding.llTrackerId,
                        binding.tvTrackerId
                    )
                    if (binding.etPetName.text.isNotEmpty() && binding.txtLastSeen.text.isNotEmpty() && binding.etPetType.text.isNotEmpty() && binding.genderSpinner.selectedItem != "Select gender" && binding.etColor.text.isNotEmpty() && binding.etBreed.text.isNotEmpty() && binding.etPeculiarity.text.isNotEmpty() && binding.etName.text.isNotEmpty() && binding.etAddress.text.isNotEmpty() && binding.etEmail.text.isNotEmpty() && binding.etEmail.text.matches(
                            Regex(FormValidations.emailPattern)
                        ) && binding.etContact.text.isNotEmpty() && binding.etContact.text.length > 9  && binding.switchClick.isChecked && binding.etTrackerId.text.isNotEmpty()
                    ) {
                        genderValue = binding.genderSpinner.selectedItem.toString()
                        request.petId = petIdRequest
                        request.petName = binding.etPetName.text.toString()
                        request.lastSeen = binding.txtLastSeen.text.toString()
                        request.type = binding.etPetType.text.toString()
                        request.gender = genderValue
                        request.trackerID = binding.etTrackerId.text.toString()
                        request.color = binding.etColor.text.toString()
                        request.breed = binding.etBreed.text.toString()
                        request.peculiarity = binding.etPeculiarity.text.toString()
                        request.userDetails.name = binding.etName.text.toString()
                        request.userDetails.address = binding.etAddress.text.toString()
                        request.userDetails.email = binding.etEmail.text.toString()
                        request.userDetails.mobileNumber = binding.etContact.text.toString()
                        request.petImage = imageArray


                        getLocation()
                    }
                }




            }
        } else {
            binding.txtAdd.setText(R.string.add)

            binding.addButton.setSafeOnClickListener {
                if (!binding.switchClick.isChecked){
                    binding.petIdText.isVisible = false
                    binding.llTrackerId.isVisible = false

                    FormValidations.addPet(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtLastSeen,
                        binding.llLastSeen,
                        binding.tvDateOfBirth,
                        binding.etPetType,
                        binding.llPetType,
                        binding.tvType,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        binding.etColor,
                        binding.llColor,
                        binding.tvColor,
                        binding.etBreed,
                        binding.llBreed,
                        binding.tvBreed,
                        binding.etPeculiarity,
                        binding.llPeculiarity,
                        binding.tvPeculiarity,
                        binding.etName,
                        binding.llName,
                        binding.tvName,
                        binding.etAddress,
                        binding.llAddress,
                        binding.tvAddress,
                        binding.etEmail,
                        binding.llEmail,
                        binding.tvEmail,
                        binding.etContact,
                        binding.llContact,
                        binding.tvContact,
                        this@AddMissingPetActivity,
                        imageArray,
                        binding.llAddImage,
                        binding.tvImage
                    )
                    if (binding.etPetName.text.isNotEmpty() && binding.txtLastSeen.text.isNotEmpty() && binding.etPetType.text.isNotEmpty() && binding.genderSpinner.selectedItem != "Select gender" && binding.etColor.text.isNotEmpty() && binding.etBreed.text.isNotEmpty() && binding.etPeculiarity.text.isNotEmpty() && binding.etName.text.isNotEmpty() && binding.etAddress.text.isNotEmpty() && binding.etEmail.text.isNotEmpty() && binding.etEmail.text.matches(
                            Regex(FormValidations.emailPattern)
                        ) && binding.etContact.text.isNotEmpty() && binding.etContact.text.length > 9
                    ) {


                        genderValue = binding.genderSpinner.selectedItem.toString()
                        request.petId = petIdRequest
                        request.petName = binding.etPetName.text.toString()
                        request.lastSeen = binding.txtLastSeen.text.toString()
                        request.type = binding.etPetType.text.toString()
                        request.gender = genderValue
                        request.trackerID = binding.etTrackerId.text.toString()
                        request.color = binding.etColor.text.toString()
                        request.breed = binding.etBreed.text.toString()
                        request.peculiarity = binding.etPeculiarity.text.toString()
                        request.userDetails.name = binding.etName.text.toString()
                        request.userDetails.address = binding.etAddress.text.toString()
                        request.userDetails.email = binding.etEmail.text.toString()
                        request.userDetails.mobileNumber = binding.etContact.text.toString()
                        request.petImage = imageArray
                        request.lat = latitude
                        request.long = longitude


                        getLocation()

                    }




                }else{
                    binding.petIdText.isVisible = true
                    binding.llTrackerId.isVisible = true

                    FormValidations.addPetCheck(
                        binding.etPetName,
                        binding.llPetName,
                        binding.tvPetName,
                        binding.txtLastSeen,
                        binding.llLastSeen,
                        binding.tvDateOfBirth,
                        binding.etPetType,
                        binding.llPetType,
                        binding.tvType,
                        binding.genderSpinner,
                        binding.llGender,
                        binding.tvGender,
                        binding.etColor,
                        binding.llColor,
                        binding.tvColor,
                        binding.etBreed,
                        binding.llBreed,
                        binding.tvBreed,
                        binding.etPeculiarity,
                        binding.llPeculiarity,
                        binding.tvPeculiarity,
                        binding.etName,
                        binding.llName,
                        binding.tvName,
                        binding.etAddress,
                        binding.llAddress,
                        binding.tvAddress,
                        binding.etEmail,
                        binding.llEmail,
                        binding.tvEmail,
                        binding.etContact,
                        binding.llContact,
                        binding.tvContact,
                        this@AddMissingPetActivity,
                        imageArray,
                        binding.llAddImage,
                        binding.tvImage,
                        binding.etTrackerId,
                        binding.llTrackerId,
                        binding.tvTrackerId
                    )

                    if (binding.etPetName.text.isNotEmpty() && binding.txtLastSeen.text.isNotEmpty() && binding.etPetType.text.isNotEmpty() && binding.genderSpinner.selectedItem != "Select gender" && binding.etColor.text.isNotEmpty() && binding.etBreed.text.isNotEmpty() && binding.etPeculiarity.text.isNotEmpty() && binding.etName.text.isNotEmpty() && binding.etAddress.text.isNotEmpty() && binding.etEmail.text.isNotEmpty() && binding.etEmail.text.matches(
                            Regex(FormValidations.emailPattern)
                        ) && binding.etContact.text.isNotEmpty() && binding.etContact.text.length > 9 && binding.switchClick.isChecked && binding.etTrackerId.text.isNotEmpty()
                    ) {


                        genderValue = binding.genderSpinner.selectedItem.toString()
                        request.petId = petIdRequest
                        request.petName = binding.etPetName.text.toString()
                        request.lastSeen = binding.txtLastSeen.text.toString()
                        request.type = binding.etPetType.text.toString()
                        request.gender = genderValue
                        request.trackerID = binding.etTrackerId.text.toString()
                        request.color = binding.etColor.text.toString()
                        request.breed = binding.etBreed.text.toString()
                        request.peculiarity = binding.etPeculiarity.text.toString()
                        request.userDetails.name = binding.etName.text.toString()
                        request.userDetails.address = binding.etAddress.text.toString()
                        request.userDetails.email = binding.etEmail.text.toString()
                        request.userDetails.mobileNumber = binding.etContact.text.toString()
                        request.petImage = imageArray
                        request.lat = latitude
                        request.long = longitude


                        getLocation()

                    }


                    }



            }


        }


        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        c.set(year, month , date)
        binding.llLastSeenCalender.setSafeOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->
                    val dateFormat = DateFormat.dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")
                    binding.txtLastSeen.text = dateFormat

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }


        setAdapterImages()
        observeResponseViewMissingPet()
        observeAddMissingPetResponse()
        observeEdtMissingPetResponse()
        observeUploadedImagesResponse()
        observePetCategoryListResponse()
        observePetBreedListResponse()
        observeMyPetList()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!binding.switchClick.isChecked){
                FormValidations.addPet(
                    binding.etPetName,
                    binding.llPetName,
                    binding.tvPetName,
                    binding.txtLastSeen,
                    binding.llLastSeen,
                    binding.tvDateOfBirth,
                    binding.etPetType,
                    binding.llPetType,
                    binding.tvType,
                    binding.genderSpinner,
                    binding.llGender,
                    binding.tvGender,
                    binding.etColor,
                    binding.llColor,
                    binding.tvColor,
                    binding.etBreed,
                    binding.llBreed,
                    binding.tvBreed,
                    binding.etPeculiarity,
                    binding.llPeculiarity,
                    binding.tvPeculiarity,
                    binding.etName,
                    binding.llName,
                    binding.tvName,
                    binding.etAddress,
                    binding.llAddress,
                    binding.tvAddress,
                    binding.etEmail,
                    binding.llEmail,
                    binding.tvEmail,
                    binding.etContact,
                    binding.llContact,
                    binding.tvContact,
                    this@AddMissingPetActivity,
                    imageArray,
                    binding.llAddImage,
                    binding.tvImage
                )
            }else{
                FormValidations.addPetCheck(
                    binding.etPetName,
                    binding.llPetName,
                    binding.tvPetName,
                    binding.txtLastSeen,
                    binding.llLastSeen,
                    binding.tvDateOfBirth,
                    binding.etPetType,
                    binding.llPetType,
                    binding.tvType,
                    binding.genderSpinner,
                    binding.llGender,
                    binding.tvGender,
                    binding.etColor,
                    binding.llColor,
                    binding.tvColor,
                    binding.etBreed,
                    binding.llBreed,
                    binding.tvBreed,
                    binding.etPeculiarity,
                    binding.llPeculiarity,
                    binding.tvPeculiarity,
                    binding.etName,
                    binding.llName,
                    binding.tvName,
                    binding.etAddress,
                    binding.llAddress,
                    binding.tvAddress,
                    binding.etEmail,
                    binding.llEmail,
                    binding.tvEmail,
                    binding.etContact,
                    binding.llContact,
                    binding.tvContact,
                    this@AddMissingPetActivity,
                    imageArray,
                    binding.llAddImage,
                    binding.tvImage,
                    binding.etTrackerId,
                    binding.llTrackerId,
                    binding.tvTrackerId
                )
            }




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



    private fun setAdapterImages() {
        binding.PostRecyclerView.layoutManager = GridLayoutManager(this, 2)
        imagesSelected = PostViewAdapter(this, imageArray, this)
        binding.PostRecyclerView.adapter = imagesSelected

    }

    override fun deleteImage(adapterPosition: Int) {
        imageArray.removeAt(adapterPosition)
        imagesSelected.notifyItemRemoved(adapterPosition)
        imagesSelected.notifyItemRangeChanged(adapterPosition, imageArray.size)

        binding.llAddImage.isVisible = imageArray.size < 4


    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeResponseViewMissingPet() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._viewMissingPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    for (i in response.data.result.petImage.indices){
                                        imageArray.add(response.data.result.petImage[i])
                                    }
                                    binding.etPetName.setText(response.data.result.petName)
                                    binding.etPetNameFilled.text = response.data.result.petName
//                                    binding.etPetType.text = response.data.result.type
                                    binding.txtLastSeen.text = response.data.result.lastSeen
                                    binding.etColor.setText(response.data.result.color)
                                    binding.etBreed.text = response.data.result.breed
                                    binding.etPeculiarity.setText(response.data.result.peculiarity)
                                    binding.etName.setText(response.data.result.userDetails.name)
                                    binding.etAddress.setText(response.data.result.userDetails.address)
                                    binding.etEmail.setText(response.data.result.userDetails.email)
                                    binding.etContact.setText(response.data.result.userDetails.mobileNumber)
                                    binding.etTrackerId.setText(response.data.result.trackerID)



                                    if(response.data.result.trackerID.isNotEmpty()){
                                        binding.switchClick.isChecked = true
                                        binding.tvTrackerId.isVisible = true
                                        binding.llTrackerId.isVisible = true
                                    }


                                    binding.llAddImage.isVisible = imageArray.size < 4

                                    binding.tvImage.visibility = View.GONE
                                    binding.tvImage.text = ""
                                    binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)

                                    setAdapterImages()
                                    genderValue = response.data.result.gender

                                    when(genderValue.lowercase()){

                                        "male" -> {
                                            binding.genderSpinner.setSelection(1)
                                            genderValue = getString(R.string.male)
                                            binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                            binding.tvGender.visibility = View.GONE
                                            binding.tvGender.text = ""
                                        }


                                        "female" -> {
                                            binding.genderSpinner.setSelection(2)
                                            genderValue = getString(R.string.female)
                                            binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                                            binding.tvGender.visibility = View.GONE
                                            binding.tvGender.text = ""
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
                                androidExtension.alertBox(message, this@AddMissingPetActivity)
                            }

                        }

                        is Resource.Loading -> {

                            Progresss.start(this@AddMissingPetActivity)


                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }

            }

        }


    }



//   add Missing Pet Observer

    private fun observeAddMissingPetResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._addMissingPetData.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    finishAfterTransition()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@AddMissingPetActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddMissingPetActivity)
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }

        }
    }

    private fun getLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->

                if (task.isSuccessful && task.result != null) {
                    try {
                        val lastLocation = task.result
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation).longitude

                        if (flag == "Edit"){
                            viewModel.editMissingPetApi(token,petId, request)
                        }else{
                            viewModel.addMissingPetApi(token, request)
                        }

                    }
                    catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }


//    Edit Missing Pet Observer

    private fun observeEdtMissingPetResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._editMissingPetData.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishActivity(response.data.responseMessage,this@AddMissingPetActivity,this@AddMissingPetActivity)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@AddMissingPetActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddMissingPetActivity)
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }

        }
    }



    //    Upload Multiple Images Obserber


    @SuppressLint("NotifyDataSetChanged")
    private fun observeUploadedImagesResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._uploadImagesData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            binding.ProgressBarScroll.isVisible = false
                            binding.addPost.isVisible = true
                            if (response.data?.responseCode == 200) {
                                try {
                                    requestMultiImagesAndVideos.clear()
                                    for(i in 0 until response.data.result.size) {

                                        imageArray.add(response.data.result[i].mediaUrl)
                                    }

                                    imagesSelected.notifyDataSetChanged()
                                    binding.llAddImage.isVisible = imageArray.size < 4

                                    binding.tvImage.visibility = View.GONE
                                    binding.tvImage.text = ""
                                    binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)



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
                                androidExtension.alertBox(message, this@AddMissingPetActivity)
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
    }

    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    override fun finishActivity() {
        finishAfterTransition()
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url?.trim())
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
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
            dialougbackButton.setSafeOnClickListener { dialog.dismiss() }


            val SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            when (flag) {

                "Pet Type" -> {
                    dialougTitle.text = flag
                    viewModel.getPetCategoryApi()

                }
                "Pet Breed" -> {
                    dialougTitle.text = flag
                    viewModel.petBreedListApi(petCategoryId = petTypeId)

                }
                "Pet Name" -> {
                    dialougTitle.text = flag
                    viewModel.myPetApi(token= token,search ="",page= 1,limit=  80, fromDate = "", toDate = "", publishStatus = "")

                }

            }

            if (flag== "Pet Name"){
                SearchEditText.addTextChangedListener(textWatcherPetName)
            }else{
                SearchEditText.addTextChangedListener(textWatchers)
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
                dialog.dismiss()
            }

            "Pet Breed" -> {
                binding.etBreed.text = data
                petBreedId = code
                dialog.dismiss()
            }

        }
    }

    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterDataPetBreed(s.toString())

        }
    }

    private val textWatcherPetName = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterDataPetName(s.toString())

        }
    }



    //    Pet Category List Observer

    private fun observePetCategoryListResponse() {

        lifecycleScope.launchWhenCreated {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._petCategoryData.collect { response ->

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
                                androidExtension.alertBox(message, this@AddMissingPetActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddMissingPetActivity)
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }

        }
    }

    fun setAdapter(result: ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = openDialog(this, result, dialogFlag, this)
        recyclerView.adapter = adapter
    }

    private fun observePetBreedListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._petBreeddata.collect { response ->

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
                                androidExtension.alertBox(message, this@AddMissingPetActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddMissingPetActivity)
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }

        }
    }

    private fun filterDataPetBreed(searchText: String) {
        val filteredList = filterData.filter { item ->
            try {
                item.name.contains(searchText, ignoreCase = true) ||
                item.petCategoryName.contains(searchText, ignoreCase = true) ||
                        item.petBreedName.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        adapter.filterList(filteredList as ArrayList<CountryList>)
    }


    private fun observeMyPetList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._myPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {
                                    filterDataPetName = response.data.result.docs
                                    setAdapterPetName(response.data.result.docs)
                                } catch (e: Exception) {
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

    private fun setAdapterPetName(result: ArrayList<MyPetListDocs>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapterForPetName = OpenDialogForPetName(this, result, this)
        recyclerView.adapter = adapterForPetName
    }



    private fun filterDataPetName(searchText: String) {
        val filteredList = filterDataPetName.filter { item ->
            try {
                item.petName!!.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        adapterForPetName.filterList(filteredList as ArrayList<MyPetListDocs>)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun selectedData(
        petName: String?,
        mediaUrls: ArrayList<MediaUrls>,
        gender: String?,
        breed: String?,
        name: String,
        address: String,
        email: String,
        mobileNumber: String,
        petIdBreed:String,
        petId:String
    ) {
        imageArray.clear()
        petBreedId = petIdBreed
        petIdRequest = petId
        binding.etPetNameFilled.text = petName
        binding.etPetName.setText(petName)
        binding.etBreed.text = breed
        binding.etName.setText(name)
        binding.etAddress.setText(address)
        binding.etEmail.setText(email)
        binding.etContact.setText(mobileNumber)

        when (gender!!.lowercase()) {
            "male" -> {
                binding.genderSpinner.setSelection(1)
                genderValue = "Male"
                binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                binding.tvGender.visibility = View.GONE
                binding.tvGender.text = ""
            }


            "female" -> {
                binding.genderSpinner.setSelection(2)
                genderValue = "Female"
                binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                binding.tvGender.visibility = View.GONE
                binding.tvGender.text = ""
            }
            else -> {
                binding.genderSpinner.setSelection(0)
                genderValue = "Others"
                binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                binding.tvGender.visibility = View.GONE
                binding.tvGender.text = ""
            }


        }


        for (image in mediaUrls.indices){
            imageArray.add(mediaUrls[image].media.mediaUrlMobile)
        }
        imagesSelected.notifyDataSetChanged()
        binding.llAddImage.isVisible = imageArray.size < 4
        if (imageArray.size != 0) {
            binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)
            binding.tvImage.text = ""
            binding.tvImage.isVisible = false
        }

        dialog.dismiss()
    }



}