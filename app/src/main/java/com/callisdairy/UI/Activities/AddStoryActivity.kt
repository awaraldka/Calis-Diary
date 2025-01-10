package com.callisdairy.UI.Activities


import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.callisdairy.ModalClass.FileParsingClass
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.request.AddStoryRequest
import com.callisdairy.api.request.story
import com.callisdairy.databinding.ActivityAddStoryBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.AddStoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody


@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    lateinit var fileParsingClass: FileParsingClass
    var imageparts = ArrayList<MultipartBody.Part>()



    var imageUrl = ""
    var mimeType = ""
    lateinit var socketInstance: SocketManager

    private val viewModel: AddStoryViewModel by viewModels()
    private var fileSize = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        GET_PARSING_DATA()
        Glide.with(this).load(fileParsingClass.imageFile).into(binding.profileImage)
        mimeType = fileParsingClass.mimeType


        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)


        if(mimeType == "image") {
            val requestGalleryImageFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), fileParsingClass.imageFile)
            imageparts.add(MultipartBody.Part.createFormData("uploaded_file", fileParsingClass.imageFile.name, requestGalleryImageFile))

        } else if(mimeType == "video") {
            val requestGalleryImageFile: RequestBody =
                RequestBody.create("video/*".toMediaTypeOrNull(), fileParsingClass.imageFile)
            imageparts.add(MultipartBody.Part.createFormData("uploaded_file", fileParsingClass.imageFile.name, requestGalleryImageFile)
            )

        }



        binding.doneButton.setSafeOnClickListener {
            val token= SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token)!!
            val request = AddStoryRequest()
            val newRequest = story()
            newRequest.caption =  binding.etCaption.text.toString()
            if(mimeType == "image") {
                newRequest.image = imageparts
            } else if(mimeType == "video") {
                newRequest.videos = imageparts

            }
            request.storyArray.add(newRequest)
          viewModel.addStoryApi(token,imageparts, binding.etCaption.text.toString())
        }

        observeAddStoryResponse()

        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }


        binding.cameraClick.visibility = View.GONE


    }








    private fun GET_PARSING_DATA() {
        intent?.getSerializableExtra("fileParsingClass")?.let {
            fileParsingClass = it as FileParsingClass
        }
    }



    private fun observeAddStoryResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._addStoryData.collect { response ->

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
                            androidExtension.alertBox(message, this@AddStoryActivity)
                        }
                    }

                    is Resource.Loading -> {
                        Progresss.start(this@AddStoryActivity)
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }
                }

            }
        }
    }





}


