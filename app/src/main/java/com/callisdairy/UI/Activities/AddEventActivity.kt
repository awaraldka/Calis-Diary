package com.callisdairy.UI.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.PostViewAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.RemoveImage
import com.callisdairy.ModalClass.DialogData
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.UI.Fragments.autoPlayVideo.toast
import com.callisdairy.Utils.*
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.request.AddEventRequest
import com.callisdairy.api.response.CountryList
import com.callisdairy.databinding.ActivityAddEventBinding
import com.callisdairy.viewModel.AddEventViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.request.EditEventRequest
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.extension.androidExtension
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddEventActivity : AppCompatActivity(), PopupItemClickListener, RemoveImage, Finish {

    private lateinit var binding: ActivityAddEventBinding
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    var flag = ""
    lateinit var adapter: openDialog

    var data = ArrayList<DialogData>()
    val c = Calendar.getInstance()
    var countryCode = ""
    var stateCode = ""
    var cityCode = ""
    var token = ""
    var filterData: ArrayList<CountryList> = ArrayList()
    val SELECT_REQUEST_CODE = 1
    lateinit var imagesSelected: PostViewAdapter
    lateinit var image: Uri

    var imageFile: File? = null
    var intentFlag = ""
    var eventId = ""
    var finalDate = StringBuffer()
    lateinit var socketInstance: SocketManager


    //    Uploaded Images Ur
    var imageArray: ArrayList<String> = ArrayList()
    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()

    private val viewModel: AddEventViewModel by viewModels()


    @SuppressLint("IntentReset")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)


        intent.getStringExtra("flag")?.let {
            intentFlag = it
        }

        intent.getStringExtra("eventId")?.let {
            eventId = it
        }


        if (intentFlag == "EditEvent") {
            binding.title.text = "Edit Event"
            viewModel.viewEventApi(eventId)

        } else {
            binding.title.text = getString(R.string.add_event_)
        }

        imageArray.clear()


        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }

        binding.llCity.setSafeOnClickListener {

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

        binding.StateLL.setSafeOnClickListener {
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

        binding.llCountry.setSafeOnClickListener {
            flag = "Country"
            openPopUp(flag)
        }

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()

        binding.etEventName.addTextChangedListener(textWatcher)
        binding.etAdress.addTextChangedListener(textWatcher)
        binding.etCountry.addTextChangedListener(textWatcher)
        binding.etState.addTextChangedListener(textWatcher)
        binding.etCity.addTextChangedListener(textWatcher)
        binding.txtDateOfBirth.addTextChangedListener(textWatcher)
        binding.etCaption.addTextChangedListener(textWatcher)






        binding.ChatClick.setSafeOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }


        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        c.set(year, month , date)

        binding.llDateOfBirth.setSafeOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                DatePickerDialog.OnDateSetListener
                { view, year, monthOfYear, dayOfMonth ->
                    val dateFormat = "$dayOfMonth-${monthOfYear + 1}-$year"
                    binding.txtDateOfBirth.text = DateFormat.dateFormatPicker(dateFormat)
                    finalDate.delete(0, finalDate.length
                    )
                    finalDate.append(dateFormat)
                    getNewStartTime()

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.minDate = c.timeInMillis

            datePickerDialog.show()
        }


        binding.addPost.setSafeOnClickListener {
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






        binding.shareButton.setSafeOnClickListener {
                FormValidations.addEvent(
                    binding.etEventName,
                    binding.llEventName,
                    binding.tvEventName,
                    binding.etAdress,
                    binding.addressLL,
                    binding.tvAddress,
                    binding.etCountry,
                    binding.llCountry,
                    binding.tvCountry,
                    binding.etState,
                    binding.StateLL,
                    binding.tvState,
                    binding.etCity,
                    binding.llCity,
                    binding.tvCity,
                    binding.txtDateOfBirth,
                    binding.llDateOfBirth,
                    binding.tvDateOfBirth,
                    binding.txtTime,
                    binding.llTime,
                    binding.tvTimeError,
                    binding.etCaption,
                    binding.llDescription,
                    binding.tvDescription,
                    this@AddEventActivity,
                    imageArray.size,
                    binding.imagesView,
                    binding.llAddImage
                )

                if (binding.etEventName.text.isNotEmpty() && binding.etAdress.text.isNotEmpty() && binding.etAdress.text.length > 2
                    && binding.etCountry.text.isNotEmpty() && binding.etState.text.isNotEmpty() && binding.etCity.text.isNotEmpty()
                    && binding.txtDateOfBirth.text.isNotEmpty() && binding.txtTime.text.isNotEmpty() && binding.etCaption.text.isNotEmpty() && imageArray.size != 0
                ) {
                    if (intentFlag == "EditEvent") {
                        val request = EditEventRequest()

                        request.eventId = eventId
                        request.eventName = binding.etEventName.text.toString()
                        request.address = binding.etAdress.text.toString()
                        request.country = binding.etCountry.text.toString()
                        request.state = binding.etState.text.toString()
                        request.city = binding.etCity.text.toString()
                        var date = "${binding.txtDateOfBirth.text} ${binding.txtTime.text}"
                        request.date = DateFormat.dateFormat(date).toString()
                        request.description = binding.etCaption.text.toString()
                        request.image = imageArray
                        viewModel.updateEventApi(token, request)
                    }else{
                        val request = AddEventRequest()
                        request.eventName = binding.etEventName.text.toString()
                        request.address = binding.etAdress.text.toString()
                        request.country = binding.etCountry.text.toString()
                        request.state = binding.etState.text.toString()
                        request.city = binding.etCity.text.toString()
                        request.date = DateFormat.dateFormat(finalDate.toString()).toString()
                        request.description = binding.etCaption.text.toString()
                        request.image = imageArray
                        viewModel.addEventApi(token, request)
                    }



                }

        }


        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()
        setAdapterImages()
        observeAddEventResponse()
        observeUpdateEventResponse()
        observeViewEventResponse()
        observeUploadedImagesResponse()
    }


    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)


            val title = binding.findViewById<TextView>(R.id.popupTitle)
            val backButton = binding.findViewById<ImageView>(R.id.BackButton)
            backButton.setSafeOnClickListener { dialog.dismiss() }


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
                    title.text = getString(R.string.country)
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


//    Country List Observer

    private fun observeCountryListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
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
                                androidExtension.alertBox(message, this@AddEventActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddEventActivity)
                        }

                        is Resource.Empty -> {

                        }

                    }

                }
            }

        }
    }

//    State List Observer

    private fun observeStateListResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
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
                                androidExtension.alertBox(message, this@AddEventActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddEventActivity)
                        }

                        is Resource.Empty -> {

                        }

                    }

                }
            }

        }
    }

//    State List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
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
                                androidExtension.alertBox(message, this@AddEventActivity)
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(this@AddEventActivity)
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }
        }
    }


//   add Event Observer

    private fun observeAddEventResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._addEventData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {
                                androidExtension.alertBoxFinishActivity(
                                    response.data.responseMessage,
                                    this@AddEventActivity,
                                    this@AddEventActivity
                                )

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddEventActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddEventActivity)
                    }

                    is Resource.Empty -> {

                    }

                }

            }
        }
    }


//   update Event Observer

    private fun observeUpdateEventResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._updateData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {
                                androidExtension.alertBoxFinishActivity(
                                    response.data.responseMessage,
                                    this@AddEventActivity,
                                    this@AddEventActivity
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddEventActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddEventActivity)
                    }

                    is Resource.Empty -> {

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


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FormValidations.addEvent(
                binding.etEventName,
                binding.llEventName,
                binding.tvEventName,
                binding.etAdress,
                binding.addressLL,
                binding.tvAddress,
                binding.etCountry,
                binding.llCountry,
                binding.tvCountry,
                binding.etState,
                binding.StateLL,
                binding.tvState,
                binding.etCity,
                binding.llCity,
                binding.tvCity,
                binding.txtDateOfBirth,
                binding.llDateOfBirth,
                binding.tvDateOfBirth,
                binding.txtTime,
                binding.llTime,
                binding.tvTimeError,
                binding.etCaption,
                binding.llDescription,
                binding.tvDescription,
                this@AddEventActivity,
                imageArray.size,
                binding.imagesView,
                binding.llAddImage
            )

        }
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
//            val images = ImagePicker.getImages(data)
//
//            if (imageArray.size >= 4) {
//                toast("Limit already have reached.")
//                // do nothing, `imageArray` is already full
//            } else {
//                val remainingSlots = 4 - imageArray.size
//                val n = minOf(images.size, remainingSlots)
//                for (i in 0 until n) {
//                    val imageUrl = images[i].path
//                        val mimeType = CommonForImages.getMimeType(imageUrl.toString())
//                        val imageFile = File(imageUrl)
//                        val surveyBody = RequestBody.create(mimeType?.toMediaTypeOrNull(), imageFile)
//                        requestMultiImagesAndVideos.add(MultipartBody.Part.createFormData("files", imageFile.name, surveyBody))
//                }
//                viewModel.uploadMultipleImagesApi(requestMultiImagesAndVideos)
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data)
//    }


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


    //   add Event Observer

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewEventResponse() {

        lifecycleScope.launch {
            viewModel._viewEventData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {

                                binding.etEventName.setText(response.data.result.eventName)
                                binding.etAdress.setText(response.data.result.address)
                                binding.etCountry.text = response.data.result.country
                                binding.etState.text = response.data.result.state
                                binding.etCity.text = response.data.result.city
                                binding.txtDateOfBirth.text = DateFormat.eventViewDateFormat(response.data.result.date)
                                binding.txtTime.text = DateFormat.getTimeOnly2(response.data.result.date)
                                binding.etCaption.setText(response.data.result.description)
                                imageArray = response.data.result.image
//                                imagesAdapter = response.data.result.image
                                binding.llAddImage.isVisible = imageArray.size < 4
                                setAdapterImages()
                                finalDate.append(binding.txtDateOfBirth.text)

                                if (imageArray.size != 0) {
                                    binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)
                                    binding.imagesView.text = ""
                                    binding.imagesView.isVisible = false
                                }
                                getNewStartTime()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddEventActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddEventActivity)
                    }

                    is Resource.Empty -> {

                    }

                }

            }
        }
    }


//    Upload Multiple Images Obserber


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
                                    imageArray.add(response.data.result[i].mediaUrl)
                                }

                                imagesSelected.notifyDataSetChanged()

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
                            androidExtension.alertBox(message, this@AddEventActivity)
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


    private fun getNewStartTime() {
        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                    val formattedTime: String = when {
                        hourOfDay == 0 -> {
                            if (minute < 10) {
                                "${hourOfDay + 12}:0${minute} AM"
                            } else {
                                "${hourOfDay + 12}:${minute} AM"
                            }
                        }
                        hourOfDay > 12 -> {
                            if (minute < 10) {
                                "${hourOfDay - 12}:0${minute} PM"
                            } else {
                                "${hourOfDay - 12}:${minute} PM"
                            }
                        }
                        hourOfDay == 12 -> {
                            if (minute < 10) {
                                "${hourOfDay}:0${minute} PM"
                            } else {
                                "${hourOfDay}:${minute} PM"
                            }
                        }
                        else -> {
                            if (minute < 10) {
                                "${hourOfDay}:${minute} AM"
                            } else {
                                "${hourOfDay}:${minute} AM"
                            }
                        }
                    }


                    finalDate.append(" $formattedTime")
                    binding.txtTime.text = formattedTime
                }
            }

        binding.llTime.setSafeOnClickListener {
            val timePicker: TimePickerDialog = TimePickerDialog(
                this,
                R.style.DatePickerTheme,
                timePickerDialogListener,
                12,
                10,
                false
            )
            timePicker.setCancelable(false)
            timePicker.show()
        }


    }








}
