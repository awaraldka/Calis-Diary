package com.callisdairy.Vendor.Fragmnets

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.LocationSelectAdapter
import com.callisdairy.Adapter.PostViewAdapter
import com.callisdairy.Adapter.openDialogCatgeory
import com.callisdairy.AdapterVendors.petTypeSelectAdapter
import com.callisdairy.Interface.*
import com.callisdairy.R
import com.callisdairy.Utils.*
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.request.AddServiceRequest
import com.callisdairy.api.request.UpdateServiceRequest
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.PetCategoryListDocs
import com.callisdairy.databinding.FragmentAddServiceBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.callisdairy.viewModel.VendorCommonViewModel
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kanabix.api.LocationPrediction
import com.callisdairy.extension.androidExtension
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
class AddServiceFragment : Fragment(), PopupItemClickListener, RemoveImage, LocationClick, Finish,
    FinishBack {

    private var _binding: FragmentAddServiceBinding? =  null
    private val binding get() = _binding!!


    var requestPetType = ArrayList<String>()
    lateinit var adapterPetType: petTypeSelectAdapter
    var categoryId = ""
    var subCategoryId = ""
    var token = ""
    var latitude = 0.0
    var longitude = 0.0

    var name = ""
    var from = ""
    var serviceId = ""
    var currency = ""

    lateinit var backTitle: ImageView
    lateinit var titleVendor: TextView
    lateinit var AdapterLocation: LocationSelectAdapter

    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var tick: LinearLayout
    lateinit var docs: List<CountryList>


    lateinit var adapter: openDialogCatgeory
    var filterData: ArrayList<PetCategoryListDocs> = ArrayList()

    var imageArray: ArrayList<String> = ArrayList()
    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()
    var imageFile: File? = null
    lateinit var imagesSelected: PostViewAdapter

    lateinit var locale: Locale
    private var fusedLocationClient: FusedLocationProviderClient? = null



    private val viewModel: VendorCommonViewModel by viewModels()
    private val viewModelLocation: GoogleLocationApiViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentAddServiceBinding.inflate(layoutInflater, container, false)
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }

        arguments?.getString("from")?.let { from = it }
        arguments?.getString("serviceId")?.let { serviceId = it }


        backTitle = activity?.findViewById(R.id.backVendor)!!
        titleVendor = activity?.findViewById(R.id.titleVendor)!!

        token  = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()




        lifecycleScope.launch {
            currency = LocationClass.getLocationBaseCurrency(fusedLocationClient,requireActivity())
            binding.price.text = "${getString(R.string.product_price)}(${currency})"
        }





        binding.etServiceAddress.addTextChangedListener(textWatcherLocation)

        viewModel.getPetCategoryApi()

        backTitle.setSafeOnClickListener{
            if (from == "editService"){
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }else{
                androidExtension.alertBoxFinish(getString(R.string.back),requireContext(),this)
            }
        }


        binding.llPetType.setSafeOnClickListener{
            openPopUp()
        }


        if (from == "editService"){
            viewModel.serviceDescriptionApi(token,serviceId)
            titleVendor.text = getString(R.string.update_service)
            binding.buttonName.text = getString(R.string.update)
        }else{
            titleVendor.text = getString(R.string.add_service)
        }





        binding.llServiceCategory.setSafeOnClickListener{
            openPopUpCategory("Category")
        }


        binding.etServiceName.addTextChangedListener(textWatcherValidation)
        binding.etPetType.addTextChangedListener(textWatcherValidation)
        binding.etServiceCategory.addTextChangedListener(textWatcherValidation)
        binding.etSubCategory.addTextChangedListener(textWatcherValidation)
        binding.etServicePrice.addTextChangedListener(textWatcherValidation)
        binding.etServiceAddress.addTextChangedListener(textWatcherValidation)
        binding.etCaption.addTextChangedListener(textWatcherValidation)



        binding.addButton.setOnClickListener {

            FormValidations.addServiceValidation(binding.etServiceName,binding.llProductName,binding.tvServiceName,binding.llAddImage,
                binding.imagesView,imageArray,binding.etPetType,binding.llPetType,binding.tvPetType,
                binding.etServiceCategory,binding.llServiceCategory,binding.tvServiceCategory
                ,binding.subCategoryLL,binding.etSubCategory,binding.tvSubCategory,
                binding.llServiceExperience,binding.etServiceExperience,binding.tvServiceExperience,binding.llServicePrice,
                binding.etServicePrice,binding.tvServicePrice,binding.llServiceAddress,binding.etServiceAddress,
                binding.tvServiceAddress,binding.llDescription,binding.etCaption,binding.tvServiceDescription,requireContext())

            if (binding.etServiceName.text.isNotEmpty() && imageArray.isNotEmpty() &&  binding.etPetType.text.isNotEmpty()
                &&  binding.etServiceCategory.text.isNotEmpty() &&  binding.etSubCategory.text.isNotEmpty()
                &&  binding.etServicePrice.text.isNotEmpty()
                &&  binding.etServiceAddress.text.isNotEmpty()&&  binding.etCaption.text.isNotEmpty()){

                currency = getBaseCurrency(currency)

                if (from == "editService"){
                    val request = UpdateServiceRequest()

                    request.serviceName = binding.etServiceName.text.toString()
                    request.serviceId = serviceId
                    request.serviceImage = imageArray
                    request.categoryId = categoryId
                    request.subCategoryId = subCategoryId
                    request.experience = binding.etServiceExperience.text.toString()
                    request.price = binding.etServicePrice.text.toString()
                    request.description = binding.etCaption.text.toString()
                    request.experience_month = binding.etServiceExperienceMonth.text.toString()
                    request.currency = currency
                    viewModel.updateServiceApi(token,request)

                }else{
                    val request = AddServiceRequest()

                    request.serviceName = binding.etServiceName.text.toString()
                    request.productImage = imageArray
                    request.petCategoryId = requestPetType
                    request.categoryId = categoryId
                    request.subCategoryId = subCategoryId
                    request.experience = binding.etServiceExperience.text.toString()
                    request.price = binding.etServicePrice.text.toString()
                    request.lat = latitude
                    request.long = longitude
                    request.currency = currency
                    request.experience_month = binding.etServiceExperienceMonth.text.toString()
                    request.description = binding.etCaption.text.toString()

                    viewModel.addServiceApi(token,request)
                }






            }


        }


        binding.subCategoryLL.setSafeOnClickListener{
            openPopUpCategory("SubCategory")
        }

        binding.llAddImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                RequestPermission.requestMultiplePermissions(requireContext())
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






        setAdapterImages()

        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })

        observePetCategoryListResponse()
        observeSubCategoryListResponse()
        observeCategoryListResponse()
        observeUploadedImagesResponse()
        observeLatLngResponse()
        observeResponceLocationList()
        observeAddServiceObserver()
        observeResponseServiceDetails()
        observeUpdateServiceObserver()
    }

//    Pet Type


    fun openPopUp() {

        try {
            val bindingPopup = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireContext(), bindingPopup.rootView, 0)!!
            recyclerView = bindingPopup.findViewById(R.id.popup_recyclerView)
            tick = bindingPopup.findViewById(R.id.tick)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            setAdapter()


            tick.isVisible = true


            val title = bindingPopup.findViewById<TextView>(R.id.popupTitle)
            title.text = getString(R.string.pet_type)
            val backButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)
            backButton.setOnClickListener { dialog.dismiss() }


            val searchEditText = bindingPopup.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            searchEditText.addTextChangedListener(textWatcher)


            tick.setOnClickListener {
                requestPetType.clear()
                val allSelectedItem = docs.filter { it.isSelected && it.productName.isNotEmpty() }
                for (i in allSelectedItem.indices) {
                    requestPetType.add(allSelectedItem[i]._id)

                }
                binding.etPetType.text = if (allSelectedItem.size == 1) {
                    allSelectedItem[0].productName
                } else if (allSelectedItem.size > 1) {
                    "${allSelectedItem[0].productName} and ${allSelectedItem.size - 1} others"
                } else {
                    ""
                }
                dialog.dismiss()
            }

            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }




    private fun setAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterPetType = petTypeSelectAdapter(requireContext(), docs)
        recyclerView.adapter = adapterPetType
    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            filterProductData(s.toString())
        }

    }

    private fun filterProductData(searchText: String) {
        val filteredList = docs.filter { item ->
            try {
                item.petCategoryName.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        adapterPetType.filterData(filteredList)
    }


    //    Pet Category List Observer

    private fun observePetCategoryListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._petCategoryData.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.statusCode == 200) {
                                try {
                                    docs = emptyList()
                                    docs = response.data.result
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
                            Progresss.stop()
                        }

                        is Resource.Empty -> {}

                    }

                }

            }
        }
    }



    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUpCategory(flag: String) {

        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireContext(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialougbackButton.setOnClickListener { dialog.dismiss() }


            val searchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            dialougTitle.text = flag

            if (flag == "Category") {
                viewModel.categoryListApi("SERVICE")

            } else {
                viewModel.listSubCategoryApi(categoryId)
            }

            searchEditText.addTextChangedListener(textWatchers)



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }



//        category List Observer

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
                                setAdapter("Category")
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
                                setAdapter("Sub Category")
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

    private fun setAdapter( flag1: String) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = openDialogCatgeory(requireContext(), filterData, flag1, this)
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
        val filteredList: ArrayList<PetCategoryListDocs> = ArrayList()


        for (item in filterData) {
            try {

                if (item.categoryName != null){
                    if (item.categoryName.lowercase().contains(searchText.lowercase())) {
                        filteredList.add(item)
                    }
                }

                if(item.subCategoryName.lowercase().contains(searchText.lowercase())){
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

    override fun getData(data: String, flag: String, code: String) {
        if (flag == "Category"){
            binding.etServiceCategory.text = data
            categoryId = code
            binding.etSubCategory.text = ""
            subCategoryId =""
            dialog.dismiss()
        }else{
            binding.etSubCategory.text = data
            subCategoryId  = code
            dialog.dismiss()
        }
    }


    private val launcher = registerImagePicker { result: List<Image> ->
        if (imageArray.size >= 4) {
            Toast.makeText(requireContext(),"Limit already have reached.",Toast.LENGTH_SHORT)
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
        binding.PostRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        imagesSelected = PostViewAdapter(requireContext(), imageArray, this)
        binding.PostRecyclerView.adapter = imagesSelected


    }

    override fun deleteImage(adapterPosition: Int) {
        imageArray.removeAt(adapterPosition)
        imagesSelected.notifyItemRemoved(adapterPosition)
        imagesSelected.notifyItemRangeChanged(adapterPosition, imageArray.size)
        binding.llAddImage.isVisible = imageArray.size < 4

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observeUploadedImagesResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._uploadImagesData.collectLatest { response ->

                    when (response) {
                        is Resource.Success -> {
                            binding.ProgressBarScroll.isVisible = false

                            if (response.data?.responseCode == 200) {
                                try {

                                    requestMultiImagesAndVideos.clear()
                                    for (i in 0 until response.data.result.size) {
                                        imageArray.add(response.data.result[i].mediaUrl)
                                    }

                                    imagesSelected.notifyDataSetChanged()

                                    binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)
                                    binding.imagesView.text = ""

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.ProgressBarScroll.isVisible = false

                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            binding.ProgressBarScroll.isVisible = true

                        }

                        is Resource.Empty -> {

                        }

                    }

                }
            }

        }
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
                viewModelLocation.getLocationApi(Uri.encode(string.toString()), SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.GMKEY).toString())

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
        binding.etServiceAddress.setText(locationName)
        binding.etServiceAddress.requestFocus()
        binding.etServiceAddress.isFocusableInTouchMode = true
        Home.showKeyboard(requireActivity())
        binding.etServiceAddress.setSelection(binding.etServiceAddress.text.length)
        binding.getLocationBySearch.isVisible = false
        viewModelLocation.getLatLng(locationName, SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.GMKEY).toString())
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



    val textWatcherValidation = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(string: Editable?) {
            FormValidations.addServiceValidation(binding.etServiceName,binding.llProductName,binding.tvServiceName,binding.llAddImage,
                binding.imagesView,imageArray,binding.etPetType,binding.llPetType,binding.tvPetType,
                binding.etServiceCategory,binding.llServiceCategory,binding.tvServiceCategory
                ,binding.subCategoryLL,binding.etSubCategory,binding.tvSubCategory,
                binding.llServiceExperience,binding.etServiceExperience,binding.tvServiceExperience,binding.llServicePrice,
                binding.etServicePrice,binding.tvServicePrice,binding.llServiceAddress,binding.etServiceAddress,
                binding.tvServiceAddress,binding.llDescription,binding.etCaption,binding.tvServiceDescription,requireContext())
        }

    }

    private fun observeAddServiceObserver() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._addServiceData.collectLatest { response ->

                    when (response) {
                        is Resource.Success -> {
                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {

                                    androidExtension.alertBoxFinishActivity(response.data.responseMessage,requireContext(),this@AddServiceFragment)
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


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeResponseServiceDetails() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._serviceDescriptionData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if(response.data?.responseCode == 200) {
                                try {
                                    binding.etServiceName.setText(response.data.result.serviceName)
                                    binding.etServiceCategory.text = response.data.result.categoryId.categoryName
                                    binding.etSubCategory.text = response.data.result.subCategoryId.subCategoryName
                                    binding.etServiceExperience.setText(response.data.result.experience.toString())
                                    binding.etServicePrice.setText(response.data.result.price.toString())
                                    binding.etServiceAddress.setText(response.data.result.userId.address)
                                    binding.etCaption.setText(response.data.result.description)
                                    binding.etServiceExperienceMonth.setText(response.data.result.experience_month)
                                    categoryId = response.data.result.categoryId._id.toString()
                                    subCategoryId= response.data.result.subCategoryId._id.toString()



                                    imageArray.clear()
                                    for (i in  response.data.result.serviceImage.indices) {
                                        imageArray.add(response.data.result.serviceImage[i])
                                    }
                                    binding.getLocationBySearch.isVisible = false
                                    imagesSelected.notifyDataSetChanged()

                                    binding.llAddImage.setBackgroundResource(R.drawable.white_border_background)
                                    binding.imagesView.text = ""

                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->

                                androidExtension.alertBox(message,requireContext())
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

    private fun observeUpdateServiceObserver() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._updateServiceData.collectLatest { response ->

                    when (response) {
                        is Resource.Success -> {
                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishActivity(response.data.responseMessage,requireContext(),this@AddServiceFragment)

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


    override fun finishActivity() {
        activity?.finishAfterTransition()
        parentFragmentManager.popBackStack()
    }

    override fun finishBackActivity() {
        activity?.finishAfterTransition()
        parentFragmentManager.popBackStack()
    }


    private fun getBaseCurrency(currencyCode: String): String {
        val countryBaseCurrency = hashSetOf("GBP", "USD", "EUR", "CNY", "RUB", "EUR", "INR", "VND", "TRY", "PLN", "IDR", "RON", "PHP")

        return if (countryBaseCurrency.contains(currencyCode)) {
            currencyCode
        } else {
            "USD"
        }
    }



}