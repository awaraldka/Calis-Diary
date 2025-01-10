package com.callisdairy.UI.Activities

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.CategoryTypeProfileAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.PopupItemClickListenerProfile
import com.callisdairy.R
import com.callisdairy.Utils.*
import com.callisdairy.Validations.FormValidations.AddPetProfile
import com.callisdairy.api.response.CountryList
import com.callisdairy.databinding.ActivityAddPetProfileBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.SignUpViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddPetProfileActivity : AppCompatActivity(), PopupItemClickListener , Finish,
    PopupItemClickListenerProfile {
    private lateinit var binding: ActivityAddPetProfileBinding

    private val viewModel: SignUpViewModel by viewModels()
    var flag = ""
    var petTypeId = ""
    var petBreedId = ""
    var token = ""
    var from = ""
    var loginKey = ""
    var isSuggested = false
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: openDialog
    var filterData: ArrayList<CountryList> = ArrayList()
    lateinit var adapterCategory: CategoryTypeProfileAdapter
    var filterDataCategory: ArrayList<String> = ArrayList()


    var imageFile: File? = null
    var photoURI: Uri? = null
    private var CAMERA: Int = 2
    var imagePath = ""
    private val GALLERY = 1
    lateinit var image: Uri
    var profilepic = ""
    var petPic = ""
    var USER_IMAGE_UPLOADED_PET = false
    var USER_IMAGE_UPLOADED_PROFILE = false
    private var base64: String? = null


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPetProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        intent.getStringExtra("token").let {
            token = it.toString()
        }
        intent.getStringExtra("from").let {
            from = it.toString()
        }

        intent.getStringExtra("loginKey").let {
            loginKey = it.toString()
        }

        intent.getBooleanExtra("isSuggested", false).let {
            isSuggested = it
        }


        if (from == "AddPetProfileActivity"){
           token =  SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()
        }



        binding.backTitle.setSafeOnClickListener {
            finishAfterTransition()
        }




        binding.llPetBreed.setOnClickListener {
            if(binding.etPetType.text.isEmpty()){
                binding.tvPetType.visibility = View.VISIBLE
                binding.tvPetType.text = getString(R.string.select_pet_tpe_first)
                binding.tvPetType.setTextColor(Color.parseColor("#C63636"))
                binding.llPetType.setBackgroundResource(R.drawable.errordrawable)

            }else{
                flag = "Pet Breed"
                openPopUp(flag)
            }



        }



        binding.llPetType.setSafeOnClickListener {
            flag = "Pet Type"
            petTypeId = ""
            openPopUp(flag)
        }


        binding.imgPetSelector.setSafeOnClickListener {
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


        binding.llPetCategory.setSafeOnClickListener {
            if (binding.etPetType.text.isEmpty()){
                binding.tvPetType.visibility = View.VISIBLE
                binding.tvPetType.text = getString(R.string.select_pet_tpe_first)
                binding.tvPetType.setTextColor(Color.parseColor("#C63636"))
                binding.llPetType.setBackgroundResource(R.drawable.errordrawable)
            }else{
                flag = "Pet Category"
                openPopUp(flag)
            }



        }



        binding.AddProfileButton.setSafeOnClickListener {
            AddPetProfile(
                binding.tvPetType,
                binding.llPetType,
                binding.etPetType,
                binding.llPetCategory,
                binding.tvPetCategory,
                binding.PetCategorySpinner,
                this,
                binding.imgPetProfile,
                petPic
            )

            if (binding.PetCategorySpinner.text.isNotEmpty() && binding.etPetType.text.isNotEmpty() && petPic.isNotEmpty()) {

                viewModel.addPetProfileApi(token,binding.PetCategorySpinner.text.toString(),binding.etPetType.text.toString()
                    ,"data:image/png;base64,${petPic}",petTypeId,petBreedId)
            }

        }

        observePetCategoryListResponse()
        observePetProfileResponse()
        observePetCategoryResponse()
        observePetBreedListResponse()
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

                "Pet Category" -> {
                    dialougTitle.text = flag

                    viewModel.petCategoryApi(petTypeId)

                }
                "Pet Breed" -> {
                    dialougTitle.text = flag

                    viewModel.petBreedListApi(petCategoryId = petTypeId)

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

            "Pet Type" -> {
                binding.etPetType.text = data
                binding.PetCategorySpinner.text = ""
                binding.etPetBreed.text = ""
                petTypeId = code
                dialog.dismiss()
            }
            "Pet Breed" -> {
                binding.etPetBreed.text = data
                binding.PetCategorySpinner.text = ""
                petBreedId = code
                dialog.dismiss()
            }
        }
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
                            androidExtension.alertBox(message, this@AddPetProfileActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetProfileActivity)
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
            if(flag == "Pet Category"){
                filterDataCategory(s.toString())
            }
            else{
                filterData(s.toString())
            }



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

    private fun filterDataCategory(searchText: String) {
        val filteredList: java.util.ArrayList<String> = java.util.ArrayList()


        for (item in filterDataCategory) {
            try {
                if (item.lowercase().contains(searchText.lowercase())) {
                    filteredList.add(item)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            adapterCategory.filterListProfile(filteredList)

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    @SuppressLint("IntentReset")
    private fun selectImage() {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val cameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        cameraButton.setSafeOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                try {
                    imageFile = createImageFile()!!
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(this, "com.callisdairy.fileprovider", imageFile!!)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    pickImagesCamera.launch(takePictureIntent)

                    dialog.dismiss()
                }
            }
        }

        val galleryButton = view.findViewById<ImageView>(R.id.choose_from_gallery)
        galleryButton.setSafeOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            pickImagesGallery.launch(intent)
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



    private val pickImagesCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    imageFile = File(imagePath)

                    image = Uri.fromFile(imageFile)
//                    val intent = CropImage.activity(image).setInitialCropWindowPaddingRatio(0f).getIntent(this)
//                    cropImage.launch(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }


    private val pickImagesGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    image = result.data?.data!!
//                    val intent = CropImage.activity(image).setInitialCropWindowPaddingRatio(0f).getIntent(this)
//                    cropImage.launch(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }


//    private val cropImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                try {
//                    val data: Intent? = result.data
//                    val resultImage = CropImage.getActivityResult(data)
//                    val newUri = resultImage.uri
//
//                    val getRealPath = ImageRotation.getRealPathFromURI2(this, newUri!!)
//                    val finalBitmap = ImageRotation.modifyOrientation(getBitmap(getRealPath)!!, getRealPath)
//                    imageFile = getRealPath?.let { File(it) }
//
//                    petPic = finalBitmap?.let { bitmapToString(it) }.toString()
//                    Glide.with(this).load(imageFile).into(binding.imgPetProfile)
//                    USER_IMAGE_UPLOADED_PET = true
//
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//
//            }
//        }








    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && (grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            selectImage()
        } else {
            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitmap(path: String?): Bitmap? {
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

//   Add Pet Profile Api


    private fun observePetProfileResponse() {

        lifecycleScope.launchWhenCreated {

            viewModel._addPetProfileData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.responseCode == 200) {
                            try {
                                if(loginKey == "social" && !isSuggested) {
                                    showDialogAddedPet()
                                } else {
                                    androidExtension.alertBoxFinishActivity(response.data.responseMessage, this@AddPetProfileActivity,this@AddPetProfileActivity)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddPetProfileActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetProfileActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }

    override fun finishActivity() {
        finishAfterTransition()
    }

    //    Pet Category List Observer

    private fun observePetCategoryResponse() {

        lifecycleScope.launchWhenCreated {

            viewModel._petCategoryListData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.statusCode == 200) {
                            try {
                                filterDataCategory = response.data.result.petCategoryType
                                setAdapterForCategory(response.data.result.petCategoryType)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@AddPetProfileActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetProfileActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }


    fun setAdapterForCategory(result: ArrayList<String>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapterCategory = CategoryTypeProfileAdapter(this, result, flag, this)
        recyclerView.adapter = adapterCategory
    }

    override fun getDataForProfile(data: String, flag: String, code: String) {
        if (flag == "Pet Category"){
            binding.PetCategorySpinner.text = data
            dialog.dismiss()
        }

    }

    fun showDialogAddedPet() {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage("Please add pet profile.")
        builder.setPositiveButton("ok") { dialogInterface, which ->
            val intent = Intent(this@AddPetProfileActivity,SuggestionListActivity::class.java)
            intent.putExtra("from","")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }


    //    Pet Breed List Observer
    private fun observePetBreedListResponse() {

        lifecycleScope.launch {
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
                            androidExtension.alertBox(message, this@AddPetProfileActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddPetProfileActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }
        }
    }




}