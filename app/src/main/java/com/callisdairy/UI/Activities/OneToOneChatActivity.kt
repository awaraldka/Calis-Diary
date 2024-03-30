package com.callisdairy.UI.Activities

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Adapter.OneToOneChatAdaptor
import com.callisdairy.Interface.AudioListeners
import com.callisdairy.Interface.FinishAudioListener
import com.callisdairy.Interface.ViewImages
import com.callisdairy.R
import com.callisdairy.R.*
import com.callisdairy.Socket.*
import com.callisdairy.UI.Activities.AgoraCalling.Calling
import com.callisdairy.UI.Activities.AgoraCalling.VoiceCallActivity
import com.callisdairy.UI.Activities.recordview.AudioRecorder
import com.callisdairy.UI.Activities.recordview.playback.AndroidAudioPlayer
import com.callisdairy.UI.dialogs.ImageShowDialog
import com.callisdairy.Utils.*
import com.callisdairy.Utils.ImageRotation.getBitmap
import com.callisdairy.api.response.MediaResultHome
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.databinding.ActivityOneToOneChatBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.AddEventViewModel
import com.devlomi.record_view.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.callisdairy.extension.androidExtension
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class OneToOneChatActivity : AppCompatActivity() , AudioListeners, ViewImages {


    var receiverId = ""
    var petImage = ""
    var userImage = ""
    var userId = ""
    var userName = ""
    var userTypeChat = ""
    lateinit var chatAdaptor : OneToOneChatAdaptor
    lateinit var socketInstance: SocketManager
    lateinit var showImage: ImageView

    var imageFile: File? = null
    var photoURI: Uri? = null
    private var CAMERA: Int = 2
    var imagePath = ""
    private val GALLERY = 1
    lateinit var image: Uri
    var profilepic = ""
    lateinit var dialogs:Dialog

    var imageArray = ""
    var sendImage = ""
    var from = ""
    var audioLink = ""

    var mimeType = ""

    var chatData =  ArrayList<ChatHistoryResult>()
    var videoThumbNail = ""

    private lateinit var binding: ActivityOneToOneChatBinding
    lateinit var sendMessage: ImageView
    lateinit var visibleLoader: ProgressBar

    private val viewModel: AddEventViewModel by viewModels()
    lateinit var audioRecorder: AudioRecorder
    lateinit var recordFile: File

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOneToOneChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.attributes.windowAnimations = style.Fade

        intent.getStringExtra("receiverId")?.let {
            receiverId = it
        }
        intent.getStringExtra("petImage")?.let {
            petImage = it
        }
        intent.getStringExtra("userImage")?.let {
            userImage = it
        }
        intent.getStringExtra("userName")?.let {
            userName = it
        }

        intent.getStringExtra("sendImage")?.let {
            sendImage = it
        }

        intent.getStringExtra("userTypeChat")?.let {
            userTypeChat = it
        }

        intent.getStringExtra("from")?.let {
            from = it
        }
        socketInstance = SocketManager.getInstance(this)

        userId = SavedPrefManager.getStringPreferences(this,SavedPrefManager.userId).toString()


        if(userTypeChat == "VENDORDOCTORVET"){
            binding.CallingFeature.isVisible = false
            binding.VoiceCall.isVisible = false
            binding.userImageVisiblity.isVisible = false
            Glide.with(this).load(userImage).placeholder(R.drawable.placeholder).into(binding.petImage)
        }else{
            Glide.with(this).load(petImage).placeholder(R.drawable.placeholder_pet).into(binding.petImage)
            Glide.with(this).load(userImage).placeholder(R.drawable.placeholder).into(binding.userImage)
        }



        binding.sendMessage.setOnClickListener {
            if (binding.message.text.isNotEmpty() && sendImage.isEmpty()){
                socketInstance.oneToOneChat(userId,receiverId,binding.message.text.toString(),"text","")
                binding.message.setText("")
                onAddListeners()
            }else{
                socketInstance.oneToOneChat(userId,receiverId,binding.message.text.toString(),"attachment",sendImage)
                binding.message.setText("")
                sendImage = ""
                onAddListeners()
            }

        }




        binding.nameUser.text = userName




        binding.profile.setSafeOnClickListener{
            if (userTypeChat == "VENDORDOCTORVET"){
                val intent = Intent(this, CommonActivityForViewActivity::class.java)
                intent.putExtra("id",receiverId)
                intent.putExtra("flag", "viewDoctors")
                startActivity(intent)
            }else{
                val intent = Intent(this, CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "OtherUsers")
                intent.putExtra("userName", userName)
                intent.putExtra("id", receiverId)
                startActivity(intent)

            }

        }

        binding.name.setSafeOnClickListener{
            val intent = Intent(this, CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "OtherUsers")
            intent.putExtra("userName", userName)
            intent.putExtra("id", receiverId)
            startActivity(intent)
        }






        // recording

        audioRecorder = AudioRecorder()

        val recordView = findViewById<RecordView>(id.recordView)
        val recordButton = findViewById<RecordButton>(id.recordButton)

        recordButton.setRecordView(recordView)

        recordButton.setOnRecordClickListener {
            Toast.makeText(this@OneToOneChatActivity, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show()
            Log.d("RecordButton", "RECORD BUTTON CLICKED")
        }

        recordView.cancelBounds = 8f


        recordView.setSmallMicColor(Color.parseColor("#c2185b"))
        recordView.setLessThanSecondAllowed(false)
        recordView.setSlideToCancelText("Slide To Cancel")
        recordView.setCustomSounds(raw.record_start, raw.record_finished, 0)


        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                AndroidAudioPlayer.releaseMediaPlayer()
                binding.smile.visibility = View.INVISIBLE
                binding.msgLL.visibility = View.INVISIBLE
                binding.recordView.isVisible = true
                recordFile = File(cacheDir, "" + UUID.randomUUID() + "audio.mp3")
                try {
                    audioRecorder.start(recordFile!!.path)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onCancel() {
                stopRecording(true)
                Log.d("RecordView", "onCancel")
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                binding.smile.isVisible = true
                binding.msgLL.isVisible = true
                binding.recordView.isVisible = false
                stopRecording(false)
                val time = getHumanTimeText(recordTime)

                Log.d("RecordView", "onFinish Limit Reached? $limitReached")
                Log.d("path", "onFinish Limit Reached? ${recordFile!!.path}")
                Log.d("RecordTime", time!!)
                var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()
                val surveyBody = RequestBody.create("audio/mp3".toMediaTypeOrNull(), recordFile!!)
                requestMultiImagesAndVideos.add(MultipartBody.Part.createFormData("files", recordFile!!.name, surveyBody))

                viewModel.uploadAudioApi(requestMultiImagesAndVideos)

            }

            override fun onLessThanSecond() {
                binding.smile.isVisible = true
                binding.msgLL.isVisible = true
                binding.recordView.isVisible = false

                stopRecording(true)
                Log.d("RecordView", "onLessThanSecond")
            }

            override fun onLock() {
                Log.d("RecordView", "onLock")
            }
        })


        recordView.setOnBasketAnimationEndListener {
            Log.d(
                "RecordView",
                "Basket Animation Finished"
            )
        }

        recordView.setRecordPermissionHandler(RecordPermissionHandler {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return@RecordPermissionHandler true
            }
            val recordPermissionAvailable = ContextCompat.checkSelfPermission(
                this@OneToOneChatActivity,
                Manifest.permission.RECORD_AUDIO
            ) == PermissionChecker.PERMISSION_GRANTED
            if (recordPermissionAvailable) {
                return@RecordPermissionHandler true
            }
            ActivityCompat.requestPermissions(
                this@OneToOneChatActivity, arrayOf(Manifest.permission.RECORD_AUDIO),
                0
            )
            false
        })

//        ------------------


        var popup = EmojiPopup.Builder
            .fromRootView(binding.activityRootView).build(binding.message);

        binding.emojis.setOnClickListener {
            popup.toggle();
        }

        binding.message.setOnClickListener {
            Home.showKeyboard(this)
        }

        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }


        binding.CallingFeature.setOnClickListener {
            val intent = Intent(this,Calling::class.java)
            intent.putExtra("receiverId",receiverId)
            intent.putExtra("from","sender")
            intent.putExtra("userName",userName)
            startActivity(intent)
        }


        binding.VoiceCall.setOnClickListener {
            val intent = Intent(this,VoiceCallActivity::class.java)
            intent.putExtra("receiverId",receiverId)
            intent.putExtra("userImage",userImage)
            intent.putExtra("userName",userName)
            intent.putExtra("from","sender")
            startActivity(intent)
        }

        binding.message.addTextChangedListener(textWatchers)







        binding.selectImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                RequestPermission.requestMultiplePermissions(this)
            } else {
                selectImage()
            }
        }


        setChatAdaptor()




        binding.chatRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    binding.godown.visibility = View.GONE
                }
            }
        })

        binding.godown.setOnClickListener {
            binding.chatRecycler.smoothScrollToPosition(chatAdaptor.itemCount)
            binding.godown.visibility = View.GONE
        }
        binding.chatRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                print("$dy+$dx")
                if (dy < 0) {
                    binding.chatRecycler.visibility = View.VISIBLE
                }
            }
        })


        observeUploadedImagesResponse()
        observeUploadedAudioResponse()

    }


    fun getRealPathFromUri(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(
            MediaStore.Audio.Media.DATA
        )
        val cursor = contentUri?.let {
            applicationContext.contentResolver
                .query(it, proj, null, null, null)
        } as Cursor
        if (cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }


    fun playAudio() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val data = Uri.parse(getRealPathFromUri(Uri.parse(recordFile.path)))
            // Intent intent = new Intent(Intent.ACTION_VIEW, data);
            intent.setDataAndType(data, "audio/3gp")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            // TODO: handle exception
        }
    }


    private fun SocketInitalize() {
        socketInstance.onlineUser(userId)
        socketInstance.checkOnlineUser(receiverId)
        socketInstance.chatHistory(userId,receiverId)
        onAddListeners()
    }

    override fun onResume() {
        super.onResume()

        SocketInitalize()
    }


    private fun onAddListeners() {
        socketInstance.initialize(object : SocketManager.SocketListener {
            override fun onConnected() {
                Log.e("browse_page_err", "omd " + "onConnected")

                // onlineStatus()
            }
            override fun onDisConnected() {
                socketInstance.connect()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun chatHistroy(listdat: ArrayList<ChatHistoryResult>) {
                chatData.clear()
                chatData.addAll(listdat)
                chatAdaptor.notifyDataSetChanged()
                binding.chatRecycler.scrollToPosition(chatAdaptor.itemCount -1)

            }

            override fun chatListData(listdat: ArrayList<chatDataResult>) {
            }

            override fun viewchat(listdat: ArrayList<MessagesChat>) {

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun oneToOneChat(listdat: ChatHistoryResult) {
                if (listdat != null) {
                    chatData.addAll(listOf(listdat))
                    chatAdaptor.notifyDataSetChanged()
                    binding.chatRecycler.smoothScrollToPosition(chatAdaptor.itemCount)

                }


            }

            override fun onlineUser(listdat: ArrayList<OnlineUserResult>) {
                for (i in listdat.indices){
                    if (receiverId == listdat[i].userId){
                        binding.onlineUser.isVisible = true
                        binding.activeUser.isVisible = true
                        binding.offlineUser.isVisible = false
                    }else{
                        binding.onlineUser.isVisible = false
                        binding.activeUser.isVisible = false
                        binding.offlineUser.isVisible = true
                    }
                }



            }

            override fun offlineUser(listdat: CheckOnlineUserResult) {
                if (listdat._id != userId){
                    if (listdat.isOnline){
                        binding.onlineUser.isVisible = true
                        binding.activeUser.isVisible = true
                        binding.offlineUser.isVisible = false
                    }else{
                        binding.onlineUser.isVisible = false
                        binding.activeUser.isVisible = false
                        binding.offlineUser.isVisible = true
                    }
                }

            }

            override fun checkOnlineUser(listdat: CheckOnlineUserResult) {
                if (listdat.isOnline){
                    binding.onlineUser.isVisible = true
                    binding.activeUser.isVisible = true
                    binding.offlineUser.isVisible = false
                }else{
                    binding.onlineUser.isVisible = false
                    binding.activeUser.isVisible = false
                    binding.offlineUser.isVisible = true
                }


            }

            override fun typing(listdat: JsonObject) {

            }

            override fun typingUser(listdat: UserTypingResult) {
                binding.typing.isVisible =listdat.status
                binding.activeUser.isVisible = listdat.status // == false

            }


        })}


    private fun setChatAdaptor() {
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        chatAdaptor = OneToOneChatAdaptor(this, chatData, this,this)
        binding.chatRecycler.adapter = chatAdaptor


    }

    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty()){
                AndroidAudioPlayer.releaseMediaPlayer()
                binding.sendMessage.visibility = View.VISIBLE
                binding.recordButton.visibility = View.GONE
                binding.recordView.visibility = View.GONE
                socketInstance.typeUser(userId,receiverId,true)


            }else{
                AndroidAudioPlayer.releaseMediaPlayer()
                binding.sendMessage.visibility = View.GONE
                binding.recordButton.visibility = View.VISIBLE
                socketInstance.typeUser(userId,receiverId,false)

            }
        }
    }


//    Pick Image

    @SuppressLint("IntentReset")
    private fun selectImage() {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val CameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        CameraButton.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                try {
                    imageFile = createImageFile()!!
                }catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(
                        this, "com.callisdairy.fileprovider", imageFile!!
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
            intent.type = "image/* video/*"
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
            imageFileName, ".jpg", storageDir
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
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        image = data.data!!
                        val path = getPathFromURI(image)
                        mimeType = getMimeType(path)!!
                        var getRealPath = ImageRotation.getRealPathFromURI2(this, image)

                        if(mimeType.contains("video")) {
                            val projection = arrayOf(MediaStore.Video.Media.DATA)
                            val cursor = contentResolver.query(image, projection, null, null, null)
                            if (cursor != null) {
                                // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                                // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
                                val column_index = cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                                cursor.moveToFirst()
                                getRealPath = cursor.getString(column_index)
                            }
                        }

                        println("image: $image")
                        println("path: $path")
                        println("getRealPath: $getRealPath")
                        if (path != null) {
                            imageFile = File(getRealPath)
                            println("mimeType: $mimeType")
                            showImagePopUp(imageFile!!)
                            val requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()


                            val surveyBody = RequestBody.create(mimeType?.toMediaTypeOrNull(), imageFile!!)
                            requestMultiImagesAndVideos.add(MultipartBody.Part.createFormData("files", path.substring(path.lastIndexOf("/")+1), surveyBody))
                            viewModel.uploadMultipleImagesApi(requestMultiImagesAndVideos)
                            Glide.with(this).load(imageFile).into(showImage)
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        } else if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                try {

                    val finalBitmap = ImageRotation.modifyOrientation(getBitmap(imagePath)!!, imagePath)
                    val getUri = ImageRotation.getImageUri(this,finalBitmap!!)
                    val path = getPathFromURI(getUri)
                    val imageFile = File(path)
                    mimeType = getMimeType(imagePath)!!
                    showImagePopUp(imageFile!!)
                    val requestMultiImagesAndVideos = ArrayList<MultipartBody.Part>()


                    val surveyBody = RequestBody.create(mimeType?.toMediaTypeOrNull(), imageFile!!)
                    requestMultiImagesAndVideos.add(MultipartBody.Part.createFormData("files", imagePath.substring(imagePath.lastIndexOf("/")+1), surveyBody))
                    viewModel.uploadMultipleImagesApi(requestMultiImagesAndVideos)
                    Glide.with(this).load(imageFile).into(showImage)


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


            }
        }
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun showImagePopUp(imageFile: File) {
        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.show_image, null)
            dialogs = DialogUtils().createDialog(this, binding.rootView, 0)!!

            val cancel: ImageView = binding.findViewById(R.id.cancel_action)
            showImage = binding.findViewById(R.id.showImage)
            visibleLoader = binding.findViewById(R.id.visibleLoader)
            sendMessage = binding.findViewById(R.id.sendMessage)

            cancel.setOnClickListener {
                dialogs.dismiss()
            }

            sendMessage.setOnClickListener {

                lifecycleScope.launch(Dispatchers.IO){
                    imageArray = if (mimeType.contains("image")){
                        socketInstance.oneToOneChat(userId,receiverId,imageArray,"image","")
                        ""
                    }else{
                        socketInstance.oneToOneChat(userId,receiverId,imageArray,"mp4",videoThumbNail)
                        ""
                    }
                    dialogs.dismiss()
                }


            }






            dialogs.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    fun getPathFromURI(contentUri: Uri?): String? {
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





    @SuppressLint("NotifyDataSetChanged")
    private fun observeUploadedImagesResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._uploadImagesData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    visibleLoader.isVisible = false
                                    for (i in 0 until response.data.result.size) {
                                        imageArray = response.data.result[i].mediaUrl
                                        videoThumbNail = response.data.result[i].thumbnail
                                        sendMessage.isVisible = true

                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            sendMessage.isVisible = false
                            visibleLoader.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@OneToOneChatActivity)
                            }
                        }

                        is Resource.Loading -> {
                            sendMessage.isVisible = false
                            visibleLoader.isVisible = true
                        }

                        is Resource.Empty -> {

                        }

                        else -> {}
                    }

                }
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeUploadedAudioResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._uploadImagesDataAudio.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    for (i in 0 until response.data.result.size) {
                                        audioLink = response.data.result[i].mediaUrl
                                        socketInstance.oneToOneChat(userId,receiverId,audioLink,"mp3",sendImage)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@OneToOneChatActivity)
                            }
                        }

                        is Resource.Loading -> {
                        }

                        is Resource.Empty -> {
                        }

                        else -> {}
                    }

                }
            }

        }
    }



    private fun stopRecording(deleteFile: Boolean) {
        audioRecorder.stop()
        if (deleteFile) {
            recordFile.delete()
        }
    }


    private fun getHumanTimeText(milliseconds: Long): String? {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(milliseconds),
            TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        )
    }



    override fun playAudioClick(
        url: String,
        ownUserPlay: ImageView,
        ownUserPause: ImageView,
        otherUserPlay: ImageView,
        otherUserPause: ImageView,
        s: String,
        index: Int,
        finishAudioListener: FinishAudioListener
    ) {
        player.playFile(url,ownUserPlay, ownUserPause,otherUserPlay,otherUserPause, s,index,finishAudioListener)
    }

    override fun pauseAudioClick() {
        player.stop()
    }


    override fun onDestroy() {
        super.onDestroy()
        AndroidAudioPlayer.releaseMediaPlayer()
    }

    override fun onPause() {
        super.onPause()
        AndroidAudioPlayer.releaseMediaPlayer()
    }

    private fun getMimeType(url: String?): String? {
        var type: String? = null
        Log.d(ContentValues.TAG, "url: $url")

        val extension = MimeTypeMap.getFileExtensionFromUrl(url?.trim())
        Log.d(ContentValues.TAG, "extension: $extension")

        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
//        Toast.makeText(this, type, Toast.LENGTH_SHORT).show()
        return type
    }

    override fun viewImage(message: String) {
        val media = ArrayList<MediaUrls>()
        media.add(MediaUrls(MediaResultHome(message,message,message),"image"))
        ImageShowDialog(media).show(supportFragmentManager, "ShowImage")
    }


}