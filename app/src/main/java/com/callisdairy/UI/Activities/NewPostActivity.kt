package com.callisdairy.UI.Activities

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.callisdairy.Adapter.LocationSelectAdapter
import com.callisdairy.Adapter.PostViewAdapter
import com.callisdairy.Adapter.TagPeopleAdapter
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.LocationClick
import com.callisdairy.Interface.RemoveImage
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.UI.Fragments.autoPlayVideo.toast
import com.callisdairy.Utils.CommonForImages
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Home.flagHome
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.TagPeopleDocs
import com.callisdairy.databinding.ActivityNewPostBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.callisdairy.viewModel.TagPeopleViewModel
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


@AndroidEntryPoint
class NewPostActivity : AppCompatActivity(), RemoveImage,
    LocationClick, Finish {

    private lateinit var binding: ActivityNewPostBinding
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var tick: LinearLayout
    var token = ""
    var imageFile: File? = null
    var docs : List<TagPeopleDocs> = listOf()


    lateinit var taggedAdapter: TagPeopleAdapter
    lateinit var adapterLocation: LocationSelectAdapter
    lateinit var imagesSelected: PostViewAdapter


    var latitude = 0.0
    var longitude = 0.0


    var tagpeople = false


    lateinit var socketInstance: SocketManager

    var requestMetaWords = ArrayList<String>()
    var requestTagPeople = ArrayList<String>()

    var imageArray: ArrayList<String> = ArrayList()

    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()

    private var fusedLocationClient: FusedLocationProviderClient? = null


    private val viewModel: TagPeopleViewModel by viewModels()
    private val viewModelLocation: GoogleLocationApiViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        fusedLocationClient = this.let { LocationServices.getFusedLocationProviderClient(it) }


        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token)!!

        binding.back.setSafeOnClickListener {
            finishAfterTransition()
        }

        getLocation()

        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)



        binding.addPost.setSafeOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                RequestPermission.requestMultiplePermissions(this)
            } else {
//                val pickIntent =
//                   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                pickIntent.type = "image/* video/*"
//                pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                pickIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15)
//                pickIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0) // set the video quality to low
//                startActivityForResult(pickIntent, SELECT_REQUEST_CODE)



                val config = ImagePickerConfig {
                    mode = ImagePickerMode.MULTIPLE
                    language = "EN"
                    returnMode =  ReturnMode.NONE
                    isIncludeAnimation = true
                    isFolderMode = true
                    isIncludeVideo = true
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





        binding.shareButton.setSafeOnClickListener {
            if(binding.etCaption.text.isEmpty() && requestMultiImagesAndVideos.isEmpty()) {

            } else {

                viewModel.addPostApi(
                    token,
                    requestMultiImagesAndVideos,
                    binding.etCaption.text.toString(),
                    requestMetaWords,
                    requestTagPeople,
                    latitude,
                    longitude,
                    binding.txtLocation.text.toString()
                )
            }


        }

        binding.forward.setSafeOnClickListener {
            if (binding.etCaption.text.isEmpty() && requestMultiImagesAndVideos.isEmpty()) {

            } else {
                viewModel.addPostApi(
                    token,
                    requestMultiImagesAndVideos,
                    binding.etCaption.text.toString(),
                    requestMetaWords,
                    requestTagPeople,
                    latitude,
                    longitude,
                    binding.txtLocation.text.toString()
                )
            }
        }

        binding.llTagPeople.setSafeOnClickListener {
            openPopUp()
        }

        binding.llLocation.setSafeOnClickListener {
            locationPopUp()

        }



        observeTagPeopleResponse()

        setAdapterImages()
        observeResponceLocationList()
        observeLatLngResponse()
        observeAddPost()
    }



    private val launcher = registerImagePicker { result: List<Image> ->
        if (imageArray.size >= 4) {
            toast("Limit already have reached.")
        } else {
            val urls = HashSet<String>(imageArray)
            val remainingSlots = 4 - imageArray.size
            val n = minOf(result.size, remainingSlots)
            for (i in 0 until n) {
                val imageUrl = result[i].path
                if (!urls.contains(imageUrl)) {
                    urls.add(imageUrl)
                    val mimeType = CommonForImages.getMimeType(imageUrl)
                    val imageFile = File(imageUrl)
                    val surveyBody = imageFile.asRequestBody(mimeType?.toMediaTypeOrNull())
                    requestMultiImagesAndVideos.add(MultipartBody.Part.createFormData("uploaded_file", imageFile.name.trim(), surveyBody))
                }

            }
            imageArray.clear()
            imageArray.addAll(urls)
            notifyAdapterData()
        }
    }




    @SuppressLint("NotifyDataSetChanged")
    private fun notifyAdapterData() {
        imagesSelected.notifyDataSetChanged()
        binding.addPost.isEnabled = imageArray.size < 4

        binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)
        binding.imagesView.text = ""
        binding.imagesView.isVisible = false
    }


    @SuppressLint("InflateParams", "SetTextI18n", "NotifyDataSetChanged")
    fun openPopUp() {

        try {
            val bindingPopup = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, bindingPopup.rootView, 0)!!
            recyclerView = bindingPopup.findViewById(R.id.popup_recyclerView)
            tick = bindingPopup.findViewById(R.id.tick)
            recyclerView.layoutManager = LinearLayoutManager(this)

            if (docs.isEmpty()){
                viewModel.tagPeopleApi(token, "")
            }else{
                setAdapter()
            }



            tick.isVisible = true


            val title = bindingPopup.findViewById<TextView>(R.id.popupTitle)
            title.text = "Tag People"
            val backButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)
            backButton.setSafeOnClickListener { dialog.dismiss() }


            val searchEditText = bindingPopup.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            searchEditText.addTextChangedListener(textWatcher)


            var taggedCount = 0
            tick.setSafeOnClickListener {

                val selectedItem = docs.filter { it.isSelected }

                for (i in selectedItem.indices) {
                    taggedCount++
                    requestTagPeople.add(docs[i]._id)
                }

                binding.txtTag.text = when {
                    selectedItem.isEmpty() -> getString(R.string.tag_people)
                    selectedItem.size == 1 -> selectedItem[0].userName
                    selectedItem.size == 2 -> "${selectedItem[0].userName} ${selectedItem[1].userName}"
                    else -> "${selectedItem[0].userName} ${selectedItem[1].userName} and ${selectedItem.size - 1} others"
                }




                if (taggedCount > 0) {
                    binding.txtTag.text = "$taggedCount people tagged in this post."
                } else {
                    binding.txtTag.text = getString(R.string.tag_people)
                }


                dialog.dismiss()
            }

            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            filterData(s.toString())
        }

    }

    private fun filterData(searchText: String) {
        val filteredList = docs.filter { item ->
            try {
                item.name.contains(searchText, ignoreCase = true) ||
                        item.userName.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        taggedAdapter.filterData(filteredList)
    }






    private fun observeTagPeopleResponse() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._tagPeopleData.collectLatest { response ->


                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if (response.data?.statusCode == 200) {
                                try {
                                    tagpeople = true
                                    docs = emptyList()
                                    docs = response.data.result.docs
                                    setAdapter()


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@NewPostActivity)
                            }

                        }

                        is Resource.Loading -> {
                            if (!tagpeople){
                                Progresss.start(this@NewPostActivity)
                            }

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }
        }
    }

    private fun setAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        taggedAdapter = TagPeopleAdapter(this, docs)
        recyclerView.adapter = taggedAdapter
    }





    private fun setAdapterImages() {
        binding.PostRecyclerView.layoutManager = GridLayoutManager(this, 2)
        imagesSelected = PostViewAdapter(this, imageArray, this)
        binding.PostRecyclerView.adapter = imagesSelected


    }

    override fun deleteImage(adapterPosition: Int) {
        imageArray.removeAt(adapterPosition)
        requestMultiImagesAndVideos.removeAt(adapterPosition)

        imagesSelected.notifyItemRemoved(adapterPosition)
        imagesSelected.notifyItemRangeChanged(adapterPosition, imageArray.size)

        binding.addPost.isEnabled = imageArray.size < 4

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


    @SuppressLint("InflateParams", "SetTextI18n")
    fun locationPopUp() {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)


            val title = binding.findViewById<TextView>(R.id.popupTitle)
            title.text = "Select Location"
            val backButton = binding.findViewById<ImageView>(R.id.BackButton)
            backButton.setSafeOnClickListener { dialog.dismiss() }


            val searchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            searchEditText.addTextChangedListener(textWatcherLocation)
            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private val textWatcherLocation = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(string: Editable?) {
            if (string.toString() != "") {
                viewModelLocation.getLocationApi(
                    Uri.encode(string.toString()),
                    SavedPrefManager.getStringPreferences(this@NewPostActivity,SavedPrefManager.GMKEY).toString()
                )

            } else {
                viewModelLocation.getLocationApi(
                    Uri.encode(string.toString()),
                    SavedPrefManager.getStringPreferences(this@NewPostActivity,SavedPrefManager.GMKEY).toString()
                )
            }

        }

    }

    private fun observeResponceLocationList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapterLocation = LocationSelectAdapter(this, predictions, this)
        recyclerView.adapter = adapterLocation
    }

    override fun getLocation(locationName: String) {
        binding.txtLocations.isVisible = false
        binding.txtLocation.isVisible = true
        binding.txtLocation.text = locationName
        viewModelLocation.getLatLng(locationName, SavedPrefManager.getStringPreferences(this@NewPostActivity,SavedPrefManager.GMKEY).toString())
        dialog.dismiss()
    }

    private fun observeLatLngResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelLocation._latLngStateFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {

                                latitude = response.data?.results?.get(0)?.geometry?.location?.lat!!
                                longitude =
                                    response.data.results[0].geometry.location.lng

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

    private fun observeAddPost() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addPostData.collect { response ->
                    print("Response message >>>>> $response")
                    when (response) {
                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    flagHome = true

                                    androidExtension.alertBoxFinishActivity(
                                        response.data.responseMessage,
                                        this@NewPostActivity,
                                        this@NewPostActivity
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                        }

                        is Resource.Error -> {
                            Progresss.stop()
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@NewPostActivity)
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }
                    }
                }
            }
        }
    }

    override fun finishActivity() {
        finishAfterTransition()
    }





}