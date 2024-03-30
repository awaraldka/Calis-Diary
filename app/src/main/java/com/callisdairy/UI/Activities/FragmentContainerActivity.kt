package com.callisdairy.UI.Activities

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.callisdairy.Interface.AddPostListener
import com.callisdairy.ModalClass.FileParsingClass
import com.callisdairy.R
import com.callisdairy.Socket.*
import com.callisdairy.UI.Fragments.*
import com.callisdairy.UI.Fragments.profile.MyProfileFragment
import com.callisdairy.UI.dialogs.AddPostFragmentDialog
import com.callisdairy.Utils.CommonConverter
import com.callisdairy.Utils.ImageRotation
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.Constants
import com.callisdairy.api.response.KeysData
import com.callisdairy.viewModel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class FragmentContainerActivity : AppCompatActivity(), OnClickListener, AddPostListener {


    lateinit var back: ImageView
    lateinit var SearchClick: ImageView
    lateinit var addPost: ImageView

    private var mLastClickTime: Long = 0

//     Bottom Tab

    lateinit var llHome: LinearLayout
    lateinit var SelectedHome: ImageView
    lateinit var UnSelectedHome: ImageView


    lateinit var LLCart: LinearLayout
    lateinit var UnSelectedCart: ImageView
    lateinit var SelectedCart: ImageView


    lateinit var FavoritesTab: RelativeLayout
    lateinit var UnSelectedFavorites: ImageView
    lateinit var SelectedFavorites: ImageView


    lateinit var profile: LinearLayout
    lateinit var UnSelectedProfile: ImageView
    lateinit var SelectedProfile: ImageView


    lateinit var UnSelectedNotification: ImageView
    lateinit var SelectedNotification: ImageView
    lateinit var NotificationClick: RelativeLayout


    lateinit var MenuLL: LinearLayout
    lateinit var UnSelectedMarket: ImageView
    lateinit var SelectedMarket: ImageView
    lateinit var filterClick: ImageView

//   Tool Bar


    lateinit var ChantClick: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView


    lateinit var Homeview: View
    lateinit var Marketview: View
    lateinit var Favoritesview: View
    lateinit var ProfileView: View
    lateinit var NotificationView: View
    lateinit var MenuView: View

    private val viewModel: LoginViewModel by viewModels()

    var imageFile: File? = null
    var photoURI: Uri? = null
    private var CAMERA: Int = 2
    var imagePath = ""
    private val GALLERY = 1
    lateinit var image: Uri
    var profilepic = ""
    var USER_IMAGE_UPLOADED = false
    private var base64: String? = null
    lateinit var socketInstance: SocketManager
    var userId = ""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        socketInstance = SocketManager.getInstance(this)
        window.attributes.windowAnimations = R.style.Fade
        findIdS()
        clicks()
        viewModel.appConfig()
        getAllApiKeys()

        llHome.isEnabled = false
        supportFragmentManager.beginTransaction().replace(R.id.home_container, HomeFragment())
            .commit()


        userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()

        if (!socketInstance.isConnected) {
            socketInstance.connect()
        }

        socketInstance.onlineUser(userId)

    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(v: View?) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()


        when (v?.id) {

            R.id.logout -> {
//                androidExtension.showDialogLogout(this,getString(R.string.are_you_sure_you_want_to_logout),getString(R.string.logout))
            }


//            Bottom Tab Clicks
            R.id.llHome -> {


                llHome.isEnabled = false
                filterClick.isVisible = true

                supportFragmentManager.beginTransaction()
                    .replace(R.id.home_container, HomeFragment())
                    .addToBackStack(null).commit()


                SelectedMarket.visibility = View.GONE
                UnSelectedMarket.visibility = View.VISIBLE

                SelectedHome.visibility = View.VISIBLE
                UnSelectedHome.visibility = View.GONE

                SelectedCart.visibility = View.GONE
                UnSelectedCart.visibility = View.VISIBLE

                UnSelectedProfile.visibility = View.VISIBLE
                SelectedProfile.visibility = View.GONE

                UnSelectedNotification.visibility = View.VISIBLE
                SelectedNotification.visibility = View.GONE


                UnSelectedFavorites.visibility = View.VISIBLE
                SelectedFavorites.visibility = View.GONE


                Homeview.isVisible = true
                Marketview.isVisible = false
                Favoritesview.isVisible = false
                ProfileView.isVisible = false
                NotificationView.isVisible = false
                MenuView.isVisible = false


            }


            R.id.FavoritesTab -> {
                filterClick.isVisible = false

                llHome.isEnabled = true
                val bundle = Bundle()
                bundle.putString("flag", "Favourites")
                val obj = FavoritesFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.home_container, obj)
                    .addToBackStack(null).commit()


                SelectedMarket.visibility = View.GONE
                UnSelectedMarket.visibility = View.VISIBLE

                SelectedHome.visibility = View.GONE
                UnSelectedHome.visibility = View.VISIBLE

                SelectedCart.visibility = View.GONE
                UnSelectedCart.visibility = View.VISIBLE

                UnSelectedProfile.visibility = View.VISIBLE
                SelectedProfile.visibility = View.GONE

                UnSelectedNotification.visibility = View.VISIBLE
                SelectedNotification.visibility = View.GONE

                UnSelectedFavorites.visibility = View.GONE
                SelectedFavorites.visibility = View.VISIBLE


                Homeview.isVisible = false
                Marketview.isVisible = false
                Favoritesview.isVisible = true
                ProfileView.isVisible = false
                NotificationView.isVisible = false
                MenuView.isVisible = false

            }


            R.id.NotificationClick -> {
                llHome.isEnabled = true
                filterClick.isVisible = false
                supportFragmentManager.beginTransaction()
                    .replace(R.id.home_container, NotificationFragment()).addToBackStack(null)
                    .commit()

                SelectedMarket.visibility = View.GONE
                UnSelectedMarket.visibility = View.VISIBLE

                SelectedHome.visibility = View.GONE
                UnSelectedHome.visibility = View.VISIBLE

                UnSelectedNotification.visibility = View.GONE
                SelectedNotification.visibility = View.VISIBLE

                SelectedCart.visibility = View.GONE
                UnSelectedCart.visibility = View.VISIBLE

                UnSelectedProfile.visibility = View.VISIBLE
                SelectedProfile.visibility = View.GONE

                UnSelectedFavorites.visibility = View.VISIBLE
                SelectedFavorites.visibility = View.GONE


                Homeview.isVisible = false
                Marketview.isVisible = false
                Favoritesview.isVisible = false
                ProfileView.isVisible = false
                NotificationView.isVisible = true
                MenuView.isVisible = false

            }


            R.id.LLCart -> {
                llHome.isEnabled = true
                filterClick.isVisible = false


                val bundle = Bundle()
                bundle.putString("flag", "MarketFragment")
                val obj = MarketFragment()
                obj.arguments = bundle


                supportFragmentManager.beginTransaction()
                    .replace(R.id.home_container, obj, "MarketFragment")
                    .addToBackStack("MarketFragment").commit()


                SelectedMarket.visibility = View.GONE
                UnSelectedMarket.visibility = View.VISIBLE

                SelectedHome.visibility = View.GONE
                UnSelectedHome.visibility = View.VISIBLE

                SelectedCart.visibility = View.VISIBLE
                UnSelectedCart.visibility = View.GONE

                UnSelectedProfile.visibility = View.VISIBLE
                SelectedProfile.visibility = View.GONE

                UnSelectedNotification.visibility = View.VISIBLE
                SelectedNotification.visibility = View.GONE

                UnSelectedFavorites.visibility = View.VISIBLE
                SelectedFavorites.visibility = View.GONE


                Homeview.isVisible = false
                Marketview.isVisible = true
                Favoritesview.isVisible = false
                ProfileView.isVisible = false
                NotificationView.isVisible = false
                MenuView.isVisible = false

            }


            R.id.profile -> {
                llHome.isEnabled = true
                filterClick.isVisible = false

                supportFragmentManager.beginTransaction()
                    .replace(R.id.home_container, MyProfileFragment()).addToBackStack(null).commit()

                SelectedMarket.visibility = View.GONE
                UnSelectedMarket.visibility = View.VISIBLE

                SelectedHome.visibility = View.GONE
                UnSelectedHome.visibility = View.VISIBLE

                SelectedCart.visibility = View.GONE
                UnSelectedCart.visibility = View.VISIBLE

                UnSelectedProfile.visibility = View.GONE
                SelectedProfile.visibility = View.VISIBLE

                UnSelectedNotification.visibility = View.VISIBLE
                SelectedNotification.visibility = View.GONE

                UnSelectedFavorites.visibility = View.VISIBLE
                SelectedFavorites.visibility = View.GONE

                Homeview.isVisible = false
                Marketview.isVisible = false
                Favoritesview.isVisible = false
                ProfileView.isVisible = true
                NotificationView.isVisible = false
                MenuView.isVisible = false

            }


            R.id.MenuLL -> {
                llHome.isEnabled = true
                filterClick.isVisible = false

                val bundle = Bundle()
                bundle.putString("flag", "")
                val obj = MenuFragment()
                obj.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.home_container, obj)
                    .addToBackStack(null).commit()


                SelectedMarket.visibility = View.VISIBLE
                UnSelectedMarket.visibility = View.GONE

                SelectedHome.visibility = View.GONE
                UnSelectedHome.visibility = View.VISIBLE

                SelectedCart.visibility = View.GONE
                UnSelectedCart.visibility = View.VISIBLE

                UnSelectedProfile.visibility = View.VISIBLE
                SelectedProfile.visibility = View.GONE

                UnSelectedNotification.visibility = View.VISIBLE
                SelectedNotification.visibility = View.GONE


                UnSelectedFavorites.visibility = View.VISIBLE
                SelectedFavorites.visibility = View.GONE

                Homeview.isVisible = false
                Marketview.isVisible = false
                Favoritesview.isVisible = false
                ProfileView.isVisible = false
                NotificationView.isVisible = false
                MenuView.isVisible = true


            }

//          Tool Bar

            R.id.ChantClick -> {
                filterClick.isVisible = true
                llHome.isEnabled = true
                val intent = Intent(this, ChatsActivity::class.java)
//                val intent = Intent(this, CheckSocketConectivity::class.java)
                startActivity(intent)
            }


            R.id.addPost -> {

                supportFragmentManager.let { it1 ->
                    AddPostFragmentDialog(this).show(
                        it1, "Follow Bottom Sheet Dialog Fragment"
                    )
                }
            }


            R.id.SearchClick -> {

                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }

            R.id.GamesClick -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.home_container, GamesFragment()).addToBackStack(null).commit()

            }


        }
    }


    fun findIdS() {
        profile = findViewById(R.id.profile)
        llHome = findViewById(R.id.llHome)

        back = findViewById(R.id.back)
        filterClick = findViewById(R.id.filterClick)



        llHome = findViewById(R.id.llHome)
        MenuLL = findViewById(R.id.MenuLL)
        LLCart = findViewById(R.id.LLCart)

        SelectedHome = findViewById(R.id.SelectedHome)
        UnSelectedHome = findViewById(R.id.UnSelectedHome)

        UnSelectedMarket = findViewById(R.id.UnSelectedMarket)
        SelectedMarket = findViewById(R.id.SelectedMarket)

        UnSelectedCart = findViewById(R.id.UnSelectedCart)
        SelectedCart = findViewById(R.id.SelectedCart)
        UnSelectedProfile = findViewById(R.id.UnSelectedProfile)
        SelectedProfile = findViewById(R.id.SelectedProfile)

        UnSelectedProfile = findViewById(R.id.UnSelectedProfile)
        SelectedProfile = findViewById(R.id.SelectedProfile)

        UnSelectedNotification = findViewById(R.id.UnSelectedNotification)
        SelectedNotification = findViewById(R.id.SelectedNotification)








        ChantClick = findViewById(R.id.ChantClick)
        mainTitle = findViewById(R.id.MainTitle)
        SearchClick = findViewById(R.id.SearchClick)
        Username = findViewById(R.id.Username)

        FavoritesTab = findViewById(R.id.FavoritesTab)
        UnSelectedFavorites = findViewById(R.id.UnSelectedFavorites)
        SelectedFavorites = findViewById(R.id.SelectedFavorites)







        NotificationClick = findViewById(R.id.NotificationClick)
        addPost = findViewById(R.id.addPost)



        Homeview = findViewById(R.id.Homeview)
        Marketview = findViewById(R.id.Marketview)
        Favoritesview = findViewById(R.id.Favoritesview)
        ProfileView = findViewById(R.id.ProfileView)
        NotificationView = findViewById(R.id.NotificationView)
        MenuView = findViewById(R.id.MenuView)


    }


    private fun clicks() {


        back.setOnClickListener(this)
        filterClick.setOnClickListener(this)


        llHome.setOnClickListener(this)
        MenuLL.setOnClickListener(this)
        LLCart.setOnClickListener(this)
        profile.setOnClickListener(this)
        FavoritesTab.setOnClickListener(this)



        ChantClick.setOnClickListener(this)

        NotificationClick.setOnClickListener(this)
        SearchClick.setOnClickListener(this)
        addPost.setOnClickListener(this)

    }

    override fun post() {

        val intent = Intent(this, NewPostActivity::class.java)
        startActivity(intent)

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun story() {

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            RequestPermission.requestMultiplePermissions(this)
        } else {
            selectImage()
        }


    }

    override fun pet() {
        val intent = Intent(this, AddPetActivity::class.java)
        intent.putExtra("flag", "Add Pet")
        startActivity(intent)
    }

    override fun live() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun selectImage() {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val CameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        CameraButton.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                try {
                    imageFile = createImageFile()!!
                } catch (ex: IOException) {
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
            intent.type = "image/*video/*"
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
                        val cr = this.contentResolver
                        val mimeType = cr.getType(image)
                        var splitData = mimeType?.split("/")
                        val path = ImageRotation.getRealPathFromURI2(this, image)

                        if (path != null) {
                            imageFile = File(path)
                            val fileParsingClass =
                                FileParsingClass(imageFile!!, splitData?.get(0)!!)
                            val intent = Intent(this, AddStoryActivity::class.java)
                            intent.putExtra("fileParsingClass", fileParsingClass)
                            startActivity(intent)


                        }

                        USER_IMAGE_UPLOADED = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        } else if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {

                try {

                    imageFile = File(imagePath)
                    var imageData = Uri.fromFile(imageFile)
                    val cr = this.contentResolver
                    val mimeType = cr.getType(imageData)
                    var splitData = mimeType?.split("/")
                    var type = ""
                    if(imageData.toString().contains(".jpg") || imageData.toString().contains(".jpeg") || imageData.toString().contains(".png")) {
                        type = "image"
                    }
                    var fileParsingClass = FileParsingClass(imageFile!!, type)
                    val intent = Intent(this, AddStoryActivity::class.java)
                    intent.putExtra("fileParsingClass", fileParsingClass)
                    startActivity(intent)
//                    Glide.with(this).load(imageFile).into(binding.profileImage)

                    var finalBitmap = ImageRotation.modifyOrientation(
                        ImageRotation.getBitmap(imagePath)!!, imagePath
                    )


                    profilepic = finalBitmap?.let { bitmapToString(it) }.toString()

                    USER_IMAGE_UPLOADED = true
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


            }
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


    override fun onDestroy() {
        super.onDestroy()
        socketInstance.disConnect()

    }



    override fun onResume() {
        super.onResume()
        socketInstance.onlineUser(userId)
        onAddListeners()
    }


    private fun onAddListeners() {
        socketInstance.initialize(object : SocketManager.SocketListener {
            override fun onConnected() {
                Log.e("browse_page_err", "omd " + "onConnected")

            }

            override fun onDisConnected() {
                socketInstance.connect()
            }

            override fun chatHistroy(listdat: ArrayList<ChatHistoryResult>) {

            }


            override fun chatListData(listdat: ArrayList<chatDataResult>) {

            }

            override fun viewchat(listdat: ArrayList<MessagesChat>) {

            }

            override fun oneToOneChat(listdat: ChatHistoryResult) {

            }

            override fun onlineUser(listdat: ArrayList<OnlineUserResult>) {

            }

            override fun offlineUser(listdat: CheckOnlineUserResult) {

            }

            override fun checkOnlineUser(listdat: CheckOnlineUserResult) {

            }

            override fun typing(listdat: JsonObject) {

            }

            override fun typingUser(listdat: UserTypingResult) {

            }


        })
    }


    override fun onPause() {
        super.onPause()
        socketInstance.offlineUser(userId)
    }


    private fun getAllApiKeys() {

        lifecycleScope.launch {
            viewModel._appConfigData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        if(response.data!!.responseCode == 200) {
                            try{
                                val decodeKeyJsonString  = CommonConverter.dynamicSaltResponseConverter(this@FragmentContainerActivity, response.data.result)
                                val decodeKey = JSONObject(decodeKeyJsonString!!)

                                val keysData = KeysData(
                                    gMapKey = decodeKey.getString("G_MAP_KEY"),
                                    ipApiKey = decodeKey.getString("IP_API_Key"),
                                    appId = decodeKey.getString("appId"),
                                    appCertificate = decodeKey.getString("appCertificate"),
                                    customerKey = decodeKey.getString("customerKey"),
                                    customerSecret = decodeKey.getString("customerSecret")
                                )


                                Constants.initializeKeys(keysData)

                                SavedPrefManager.saveStringPreferences(this@FragmentContainerActivity,SavedPrefManager.GMKEY,
                                    Constants.getKeysData()!!.gMapKey)
                                SavedPrefManager.saveStringPreferences(this@FragmentContainerActivity,SavedPrefManager.AgoraAppId,
                                    Constants.getKeysData()!!.appId)





                            }catch (e:Exception){
                                e.printStackTrace()
                            }
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