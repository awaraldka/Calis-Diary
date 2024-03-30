package com.callisdairy.UI.Fragments

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
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
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.HomePostAdapter
import com.callisdairy.Adapter.StoryHomeAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.*
import com.callisdairy.ModalClass.FileParsingClass
import com.callisdairy.ModalClass.storymodel.Story
import com.callisdairy.ModalClass.storymodel.StoryUser
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddStoryActivity
import com.callisdairy.UI.Activities.CommentsActivity
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.playIndexThenPausePreviousPlayer
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.releaseAllPlayers
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.releaseRecycledPlayers
import com.callisdairy.UI.Fragments.autoPlayVideo.RecyclerViewScrollListener
import com.callisdairy.UI.dialogs.ImageShowDialog
import com.callisdairy.UI.dialogs.MoreOnPostDialog
import com.callisdairy.Utils.*
import com.callisdairy.Utils.Home.data
import com.callisdairy.Utils.Home.dataLoadFlag
import com.callisdairy.Utils.Home.limit
import com.callisdairy.Utils.Home.loaderFlag
import com.callisdairy.Utils.Home.page
import com.callisdairy.Utils.Home.pages
import com.callisdairy.Utils.Home.suggestedUserData
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.api.response.UserStoriesDoc
import com.callisdairy.api.response.suggestionListDocs
import com.callisdairy.databinding.FragmentHomeBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.HomeViewModel
import com.callisdairy.viewModel.SuggestionListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class HomeFragment : Fragment(), HomeUserProfileView, HomeLikePost , MorOptionsClick , MoreOptions,
    HomeStoryClick , DeleteClick,PopupItemClickListener,ReportPost ,SuggestionListClick {

    private var _binding: FragmentHomeBinding? =  null
    private val binding get() = _binding!!
    lateinit var adapterStory: StoryHomeAdapter
    lateinit var homePostAdapter: HomePostAdapter


    var postId = ""
    var postAdapterPosition = 0
    var indexValue = 0
    var positionPost = 0

    private lateinit var scrollListener: RecyclerViewScrollListener


    var isScrollingUp = false
    private var previousPosition = RecyclerView.NO_POSITION




    //     Bottom Tab

    lateinit var SelectedHome : ImageView
    lateinit var UnSelectedHome : ImageView

    lateinit var UnSelectedMarket : ImageView
    lateinit var SelectedMarket : ImageView

    lateinit var UnSelectedCart : ImageView
    lateinit var SelectedCart : ImageView

    lateinit var UnSelectedProfile : ImageView
    lateinit var SelectedProfile : ImageView

    lateinit var unSelectedNotification : ImageView
    lateinit var selectedNotification : ImageView

    lateinit var UnSelectedFavorites : ImageView
    lateinit var SelectedFavorites : ImageView


    lateinit var filterClick : ImageView


    lateinit var Homeview:View
    lateinit var Marketview:View
    lateinit var Favoritesview:View
    lateinit var ProfileView:View
    lateinit var NotificationView:View
    lateinit var MenuView:View


//   Tool Bar

    lateinit var back: ImageView
    lateinit var addPost: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView

    var token = ""
    private var fusedLocationClient: FusedLocationProviderClient? = null


    var postPosition = 0
    var latitude = 0.0
    var longitude = 0.0

    private val viewModel: HomeViewModel by viewModels()
    private val viewModelSuggestionList: SuggestionListViewModel by viewModels()

    lateinit var unlikePostView:ImageView
    lateinit var likePostView:ImageView
    private lateinit var dialog1: Dialog
    private var radiusRequest = ""
    private var countryRequest = ""
    private var stateRequest = ""
    private var cityRequest = ""
    lateinit var adapter: openDialog
    var stateCode = ""
    var cityCode = ""
    var flag = ""
    var countryCode = ""
    var petBreedId = ""
    var filterData: ArrayList<CountryList> = ArrayList()
    private lateinit var dialog: Dialog



    private lateinit var recyclerView: RecyclerView
    lateinit var country: TextView
    lateinit var state: TextView
    lateinit var city: TextView
    lateinit var etPetBreed: TextView




    var imageFile: File? = null
    var photoURI: Uri? = null
    private var CAMERA: Int = 2
    var imagePath = ""
    private val GALLERY = 1
    lateinit var image: Uri
    var profilepic = ""
    var USER_IMAGE_UPLOADED = false
    private var base64: String? = null



    private var isStoryViewVisible = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        allIds()
        toolBarWithBottomTab()
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        filterClick.isVisible = true


        getLocation()

        PostAdapter()



        binding.pullToRefresh.setOnRefreshListener {

                pages = 0
                page = 1
                limit = 15
                dataLoadFlag = false
                Home.flagHome = false
                loaderFlag = true
                releaseAllPlayers()
                binding.ProgressBarScroll.isVisible = false
                binding.postRecycler.scrollToPosition(0)
                viewModelSuggestionList.suggestionListAPi(token,"",1, 20)
                viewModel.homeListApi(token = token, page =1, limit =15,radius="",country= "",city="",state="",lat =latitude,long=longitude,petBreedId= petBreedId)

                viewModel.storyListApi(token)

            binding.pullToRefresh.isRefreshing = false

        }



        filterClick.setSafeOnClickListener {
            openFilterPopUp()
            releaseAllPlayers()
        }


//        binding.postRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                // Check if scrolling has stopped
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    // Check the scrolling direction and adjust visibility accordingly
//                    if (isScrollingUp) {
//                        // Scroll Up: Show the view if it was hidden
//                        if (!isStoryViewVisible) {
//                            binding.StoryView.visibility = View.VISIBLE
//                            isStoryViewVisible = true
//                        }
//                    } else {
//                        // Scroll Down: Hide the view
//                        binding.StoryView.visibility = View.GONE
//                        isStoryViewVisible = false
//                    }
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                isScrollingUp = dy < 0
//                val newPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
//                if (newPosition != previousPosition) {
//                    binding.StoryView.visibility = View.GONE
//                    isStoryViewVisible = false
//                }
//                if (newPosition != null) {
//                    previousPosition = newPosition
//                }
//            }
//        })


        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewModel.storyListApi(token)


        if (Home.flagHome){
            pages = 0
            page = 1
            limit = 15
            dataLoadFlag = false
            loaderFlag = false
            Home.flagHome = false
            viewModel.homeListApi(token = token, page =page, limit =limit,radius="",country= "",city="",state="",lat =latitude,long=longitude,petBreedId= petBreedId)
            viewModelSuggestionList.suggestionListAPi(token,"",1, 20)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseHomeSuggestionList()
        observeResponseHomeList()
        observeResponseStoryList()
        observeResponseAddLike()
        observeResponseHidePost()
        observeResponseDeletePost()
        observeResponseReportPost()
        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()
        observePetBreedListResponse()
        observeResponseFollowUnFollow()
    }










    private fun setAdapter(docs: List<UserStoriesDoc>, storyUser: ArrayList<StoryUser>?) {
        binding.StoryView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterStory = StoryHomeAdapter(requireContext(), docs,storyUser,this)
        binding.StoryView.adapter = adapterStory

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseHomeList() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._homeListData.collect{ response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data?.responseCode == 200) {
                                try {
                                    binding.ProgressBarScroll.isVisible = false
                                    loaderFlag = false

                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }


                                    if (response.data.result.docs.size == 1 ){
                                        if (response.data.result.docs[0].mediaUrls.getOrNull(0)?.type.equals("video")){
                                                playIndexThenPausePreviousPlayer(0)
                                        }

                                    }

//                                    if (response.data.result.docs.size > 0){
                                        binding.postRecycler.isVisible = true
                                        binding.NoDataFound.isVisible = false
                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.totalPages
                                        page = response.data.result.page
                                        homePostAdapter.notifyDataSetChanged()

                                    binding.NoDataFound.isVisible = suggestedUserData.isEmpty() && data.isEmpty()


//                                    }else{
//                                        binding.postRecycler.isVisible = true
//                                        if (suggestedUserData.isEmpty()){
//                                            binding.NoDataFound.isVisible = true
//                                        }
//                                    }



                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.postRecycler.isVisible = data.size > 1
                            binding.NoDataFound.isVisible = data.size < 1
                            response.message?.let { message ->
                            }

                        }

                        is Resource.Loading -> {
                            binding.NoDataFound.isVisible = false

                            if (page == 1){
                                Progresss.start(requireContext())
                            }

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }


        }



    }

    private fun observeResponseStoryList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._storyListData.collectLatest{ response ->

                    when (response) {

                        is Resource.Success -> {

                            if(response.data?.responseCode == 200) {
                                try {
                                    val storyUser = ArrayList<StoryUser>()
                                    for(i in 0 until response.data.result.docs.size) {
                                        val story = ArrayList<Story>()
                                        if(response.data.result.docs[i].stories.isNotEmpty()){
                                            for(j in 0 until response.data.result.docs[i].stories.size){
                                                if(response.data.result.docs[i].stories[j].image.media != "") {
                                                    story.add(Story(
                                                        response.data.result.docs[i].stories[j].image.media,DateFormat.storyDateToTimestamp(response.data.result.docs[i].stories[j].createdAt), response.data.result.docs[i].stories[j].caption,response.data.result.docs[i].stories[j].id,response.data.result.docs[i].stories[j].videos.mediaType ))
                                                }else  if(response.data.result.docs[i].stories[j].videos.media!= "") {
                                                    story.add(Story(
                                                        response.data.result.docs[i].stories[j].videos.media,DateFormat.storyDateToTimestamp(response.data.result.docs[i].stories[j].createdAt), response.data.result.docs[i].stories[j].caption,response.data.result.docs[i].stories[j].id,response.data.result.docs[i].stories[j].videos.mediaType))
                                                }
                                            }
                                            storyUser.add(StoryUser(response.data.result.docs[i].userName,response.data.result.docs[i].profilePic,story))

                                        }
                                    }
                                    response.data.result.let { setAdapter(it.docs,storyUser) }
//
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                androidExtension.alertBox(message,requireContext())
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

    private fun PostAdapter() {


        homePostAdapter = HomePostAdapter(requireContext(),data,this,this,this,this, suggestedUserData)
        binding.postRecycler.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(requireContext())
        binding.postRecycler.layoutManager = layoutManager
        binding.postRecycler.adapter = homePostAdapter

        scrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                Log.d("visible item index", index.toString())

                if (index != indexValue) {
                    releaseRecycledPlayers(indexValue)
                }

                // Play the video of the current visible item
                indexValue = index
                playIndexThenPausePreviousPlayer(index)



                if (index + 1 == data.size ) {
                    page++

                    loaderFlag = false

                    val param = binding.postRecycler.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0,0,0,30)
                    binding.postRecycler.layoutParams = param

                    binding.ProgressBarScroll.isVisible = true
                    if (page > pages) {
                        binding.ProgressBarScroll.isVisible = false
                    } else {
                        dataLoadFlag = true
                        viewModelSuggestionList.suggestionListAPi(token,"",1, 20)
                        viewModel.homeListApi(token = token, page =page, limit =limit,radius="",country= "",city="",state="",lat =latitude,long=longitude,petBreedId= petBreedId)
                    }
                }else{
                    val param = binding.postRecycler.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0,0,0,0)
                    binding.postRecycler.layoutParams = param
                    binding.ProgressBarScroll.isVisible = false
                }


            }

        }
        binding.postRecycler.addOnScrollListener(scrollListener)
        }


    private fun allIds(){
        SelectedHome = activity?.findViewById(R.id.SelectedHome)!!
        UnSelectedHome = activity?.findViewById(R.id.UnSelectedHome)!!

        UnSelectedMarket = activity?.findViewById(R.id.UnSelectedMarket)!!
        SelectedMarket = activity?.findViewById(R.id.SelectedMarket)!!

        UnSelectedCart = activity?.findViewById(R.id.UnSelectedCart)!!
        SelectedCart = activity?.findViewById(R.id.SelectedCart)!!

        UnSelectedProfile = activity?.findViewById(R.id.UnSelectedProfile)!!
        SelectedProfile = activity?.findViewById(R.id.SelectedProfile)!!


        unSelectedNotification = activity?.findViewById(R.id.UnSelectedNotification)!!
        selectedNotification = activity?.findViewById(R.id.SelectedNotification)!!


        UnSelectedFavorites = activity?.findViewById(R.id.UnSelectedFavorites)!!
        SelectedFavorites = activity?.findViewById(R.id.SelectedFavorites)!!

        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!
        addPost = activity?.findViewById(R.id.addPost)!!


        Homeview = activity?.findViewById(R.id.Homeview)!!
        Marketview =activity?.findViewById(R.id.Marketview)!!
        Favoritesview = activity?.findViewById(R.id.Favoritesview)!!
        ProfileView = activity?.findViewById(R.id.ProfileView)!!
        NotificationView = activity?.findViewById(R.id.NotificationView)!!
        MenuView = activity?.findViewById(R.id.MenuView)!!

        filterClick = activity?.findViewById(R.id.filterClick)!!



    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        playIndexThenPausePreviousPlayer(indexValue)

        homePostAdapter.notifyItemChanged(positionPost)
        homePostAdapter.notifyItemRangeChanged(positionPost, data.size)
        homePostAdapter.notifyDataSetChanged()
    }


    private fun toolBarWithBottomTab(){
        mainTitle.visibility = View.VISIBLE
        chat.visibility = View.VISIBLE
        back.visibility = View.GONE
        search.visibility = View.VISIBLE
        addPost.visibility = View.VISIBLE


        mainTitle.text = ""
        Username.text = getString(R.string.app_name)


        SelectedMarket.visibility = View.GONE
        UnSelectedMarket.visibility = View.VISIBLE

        SelectedHome.visibility = View.VISIBLE
        UnSelectedHome.visibility = View.GONE

        SelectedCart.visibility = View.GONE
        UnSelectedCart.visibility = View.VISIBLE

        UnSelectedProfile.visibility  = View.VISIBLE
        SelectedProfile.visibility  = View.GONE

        unSelectedNotification.visibility = View.VISIBLE
        selectedNotification.visibility = View.GONE

        UnSelectedFavorites.visibility  = View.VISIBLE
        SelectedFavorites.visibility  = View.GONE


        Homeview.isVisible = true
        Marketview.isVisible = false
        Favoritesview.isVisible = false
        ProfileView.isVisible = false
        NotificationView.isVisible = false
        MenuView.isVisible = false



    }

    override fun viewProfile(_id: String, userName: String) {

        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "OtherUsers")
        intent.putExtra("userName", userName)
        intent.putExtra("id", _id)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun likePost(
        _id: String,
        likedValue: Boolean,
        unlikePost: ImageView,
        likePost: ImageView,
        position: Int
    ) {
        unlikePostView = unlikePost
        likePostView = likePost
        postPosition = position
        if (unlikePostView.isVisible) {
            data[postPosition].isLiked = true
            data[position].likeCount++


        } else {
            data[postPosition].isLiked = false
            data[postPosition].likeCount --

        }
        homePostAdapter.notifyItemChanged(position, "liked")
        viewModel.addLikeApi(token,"POST",_id,"")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun likePostText(
        _id: String,
        likedValue: Boolean,
        unlikePost: ImageView,
        likePost: ImageView,
        position: Int
    ) {
        if (unlikePost.isVisible) {
            data[position].isLiked = true
            data[position].likeCount = data[position].likeCount + 1
            homePostAdapter.notifyItemChanged(position)
            homePostAdapter.notifyItemRangeChanged(position, data.size)
            homePostAdapter.notifyDataSetChanged()

        } else {
            data[position].isLiked = false
            data[position].likeCount = data[position].likeCount - 1
            homePostAdapter.notifyItemChanged(position)
            homePostAdapter.notifyItemRangeChanged(position, data.size)
            homePostAdapter.notifyDataSetChanged()

        }

        viewModel.addLikeApi(token,"POST",_id,"")
    }

    override fun commentView(position: Int, id: String) {
        positionPost = position


        val intent = Intent(requireContext(), CommentsActivity::class.java)
        intent.putExtra("_id", id)
        intent.putExtra("position", position)
        startActivity(intent)
    }


    private fun observeResponseAddLike() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addLikeData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

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

//    Show More Options Of post


    override fun openPopUpMore(_id: String, position: Int, userIdValue: String,imageUrl:String) {
        childFragmentManager.let { it1 ->
            MoreOnPostDialog(this,_id,position,userIdValue,imageUrl).show(
                it1, "Follow Bottom Sheet Dialog Fragment"
            )
        }
    }

    override fun sharePost(imageUrl:String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, imageUrl)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun viewImages(media: ArrayList<MediaUrls>) {
        ImageShowDialog(media).show(parentFragmentManager, "ShowImage")
    }


    override fun share(imageUrl:String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, imageUrl)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun hidePost(_id: String, position: Int) {
        data.removeAt(position)
        homePostAdapter.notifyItemRemoved(position)
        homePostAdapter.notifyItemRangeChanged(position, data.size)
        homePostAdapter.notifyDataSetChanged()
        viewModel.hidePostApi(token,_id)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun report(_id: String, position: Int) {

        androidExtension.postReportDialog(requireContext(),position,_id,this)



    }

    override fun deletePost(_id: String, position: Int) {
        postId =  _id
        postAdapterPosition  =  position
        androidExtension.alertBoxDelete("Are you sure you want to delete this post?", requireContext(),this)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun addStory() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            RequestPermission.requestMultiplePermissions(requireActivity())
        } else {
            selectImage()
        }
    }



    @SuppressLint("InflateParams", "IntentReset")
    private fun selectImage() {
        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val CameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        CameraButton.setSafeOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                try {
                    imageFile = createImageFile()!!
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(
                        requireContext(), "com.callisdairy.fileprovider", imageFile!!
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA)
                    dialog.dismiss()
                }
            }
        }

        val GalleryButton = view.findViewById<ImageView>(R.id.choose_from_gallery)
        GalleryButton.setSafeOnClickListener {

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
            if (resultCode == AppCompatActivity.RESULT_OK) {
                if (data != null) {
                    try {
                        image = data.data!!
                        val cr = requireActivity().contentResolver
                        val mimeType = cr.getType(image)
                        val splitData = mimeType?.split("/")
                        val path = getPathFromURI(image)

                        if (path != null) {
                            imageFile = File(path)
                            val fileParsingClass = FileParsingClass(imageFile!!, splitData?.get(0)!!)
                            val intent = Intent(requireContext(), AddStoryActivity::class.java)
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
            if (resultCode == AppCompatActivity.RESULT_OK) {

                try {

                    imageFile = File(imagePath)
                    val imageData = Uri.fromFile(imageFile)
                    val cr = requireActivity().contentResolver
                    val mimeType = cr.getType(imageData)
                    var splitData = mimeType?.split("/")
                    var type = ""
                    if(imageData.toString().contains(".jpg") || imageData.toString().contains(".jpeg") || imageData.toString().contains(".png")) {
                        type = "image"
                    }
                    val fileParsingClass = FileParsingClass(imageFile!!, type)
                    val intent = Intent(requireContext(), AddStoryActivity::class.java)
                    intent.putExtra("fileParsingClass", fileParsingClass)
                    startActivity(intent)

                    val finalBitmap = ImageRotation.modifyOrientation(
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


    private fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
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


//    Hide Post Observer


    private fun observeResponseHidePost() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._hidePostData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

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


    // TODO Delete Own Post

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteItem() {
        data.removeAt(postAdapterPosition)
        homePostAdapter.notifyItemRemoved(postAdapterPosition)
        homePostAdapter.notifyItemRangeChanged(postAdapterPosition, data.size)
        homePostAdapter.notifyDataSetChanged()

        viewModel.deletePostApi(token,postId)
    }



    private fun observeResponseDeletePost() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deletePostData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
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




//    Report Post Api

    private fun observeResponseReportPost() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._reportPostData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBox(response.data.responseMessage,requireContext())

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


    override fun onPause() {
        super.onPause()
        releaseAllPlayers()
    }


// Post Filter


    @SuppressLint("InflateParams")
    private fun openFilterPopUp() {
        try {
            val bindingPopup = LayoutInflater.from(requireActivity()).inflate(R.layout.missing_pet_filter_popup, null)!!
            dialog1 = DialogUtils().createDialog(requireActivity(), bindingPopup.rootView, 0)!!
            
            val applyButton = bindingPopup.findViewById<LinearLayout>(R.id.applyButton)
            val clearAllButton = bindingPopup.findViewById<LinearLayout>(R.id.clearAllButton)
            val oneMiles = bindingPopup.findViewById<TextView>(R.id.oneMiles)
            val twoMiles = bindingPopup.findViewById<TextView>(R.id.twoMiles)
            val threeMiles = bindingPopup.findViewById<TextView>(R.id.threeMiles)
            val fourMiles = bindingPopup.findViewById<TextView>(R.id.fourMiles)
            val fiveMiles = bindingPopup.findViewById<TextView>(R.id.fiveMiles)
            val sixMiles = bindingPopup.findViewById<TextView>(R.id.sixMiles)
            val popupTitle = bindingPopup.findViewById<TextView>(R.id.popupTitle)
            val tvCountry = bindingPopup.findViewById<TextView>(R.id.tvCountry)
            val tvState = bindingPopup.findViewById<TextView>(R.id.tvState)
            val llCountry = bindingPopup.findViewById<LinearLayout>(R.id.llCountry)
            val llState = bindingPopup.findViewById<LinearLayout>(R.id.llState)
            val llCity = bindingPopup.findViewById<LinearLayout>(R.id.llCity)
            val llPetBreed = bindingPopup.findViewById<LinearLayout>(R.id.llPetBreed)
            val dialougbackButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)



            country = bindingPopup.findViewById(R.id.etCountry)
            state = bindingPopup.findViewById(R.id.etState)
            city = bindingPopup.findViewById(R.id.etCity)
            etPetBreed = bindingPopup.findViewById(R.id.etPetBreed)


            sixMiles.text = getString(R.string.all_followers)
            popupTitle.text =  getString(R.string.all_feeds)
            clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)
            radiusRequest = "10"


            dialougbackButton.setSafeOnClickListener { dialog1.dismiss() }
            llCountry.setSafeOnClickListener {
                flag = "Country"
                selectionPopUp(flag)
            }

            llState.setSafeOnClickListener {
                if (country.text.isNotEmpty()) {
                    flag = "State"
                    selectionPopUp(flag)
                } else {
                    tvCountry.visibility = View.VISIBLE
                    tvCountry.text = getString(R.string.select_city)
                    tvCountry.setTextColor(Color.parseColor("#C63636"))
                    llCountry.setBackgroundResource(R.drawable.errordrawable)
                }


            }

            llCity.setSafeOnClickListener {
                if (state.text.isNotEmpty()) {
                    flag = "City"
                    selectionPopUp(flag)
                } else {
                    tvState.visibility = View.VISIBLE
                    tvState.text = getString(R.string.select_state)
                    tvState.setTextColor(Color.parseColor("#C63636"))
                    llState.setBackgroundResource(R.drawable.errordrawable)
                }
            }

            llPetBreed.setSafeOnClickListener {
                    flag = "Pet Breed"
                selectionPopUp(flag)

            }


            applyButton.setSafeOnClickListener {
                data.clear()
//                loaderFlag = true
                dataLoadFlag = false
                page = 1
                limit = 15


                viewModel.homeListApi(token, page, limit,radiusRequest,countryRequest,cityRequest,stateRequest,latitude,longitude,petBreedId)
                viewModelSuggestionList.suggestionListAPi(token,"",1, 20)
                dialog1.dismiss()
            }

            clearAllButton.setSafeOnClickListener {
                clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)
            }

            oneMiles.setSafeOnClickListener {
                radiusRequest = "10"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.button_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            twoMiles.setSafeOnClickListener {
                radiusRequest = "20"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.button_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            threeMiles.setOnClickListener {
                radiusRequest = "30"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.button_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            fourMiles.setSafeOnClickListener {
                radiusRequest = "40"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.button_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            fiveMiles.setSafeOnClickListener {
                radiusRequest = "50"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.button_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            sixMiles.setSafeOnClickListener {
                radiusRequest = "All Followers"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.button_background)
            }

            dialog1.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun selectionPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireActivity()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireActivity(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialougbackButton.setSafeOnClickListener { dialog.dismiss() }


            val SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            when (flag) {
                "State" -> {
                    dialougTitle.text = "State"
                    viewModel.getStateListApi(countryCode)

                }
                "City" -> {
                    dialougTitle.text = flag
                    viewModel.getCityListApi(countryCode, stateCode)

                }
                "Country" -> {
                    dialougTitle.text = flag
                    viewModel.getCountryApi()

                }
                "Pet Breed" -> {
                    dialougTitle.text = flag
                    val petTypeId = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.profileId).toString()
                    viewModel.petBreedListApi(petCategoryId = petTypeId)

                }
            }


            SearchEditText.addTextChangedListener(textWatchers)



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FilterData(s.toString())
        }
    }

    private fun FilterData(searchText: String) {
        val filteredList: java.util.ArrayList<CountryList> = java.util.ArrayList()


        for (item in filterData) {
            try {
                if (item.name.lowercase().contains(searchText.lowercase())||
                    item.petBreedName.lowercase().contains(searchText.lowercase())) {
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


    fun setAdapter(result: ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = openDialog(requireContext(), result, flag, this)
        recyclerView.adapter = adapter
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

//    State List Observer

    private fun observeStateListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._stateData.collect { response ->

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
    }

//    city List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
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



    private fun clearFilterData(oneMiles:TextView,twoMiles:TextView,threeMiles:TextView,fourMiles:TextView,fiveMiles:TextView,sixMiles:TextView) {
        radiusRequest = "10"
        countryRequest = ""
        stateRequest = ""
        cityRequest = ""
        country.text = ""
        city.text = ""
        state.text = ""
        petBreedId = ""
        etPetBreed.text = ""
        resetButtonColors(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)

    }

    private fun resetButtonColors(oneMiles:TextView,twoMiles:TextView,threeMiles:TextView,fourMiles:TextView,fiveMiles:TextView,sixMiles:TextView){
        oneMiles.setBackgroundResource(R.drawable.button_background)
        twoMiles.setBackgroundResource(R.drawable.border_background)
        threeMiles.setBackgroundResource(R.drawable.border_background)
        fourMiles.setBackgroundResource(R.drawable.border_background)
        fiveMiles.setBackgroundResource(R.drawable.border_background)
        sixMiles.setBackgroundResource(R.drawable.border_background)


        oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))

    }

    override fun getData(data: String, flag: String, code: String) {
        when (flag) {
            "City" -> {
                cityRequest = data
                city.text = data
                cityCode = data
                dialog.dismiss()

            }
            "State" -> {
                stateRequest = data
                state.text = data
                stateCode = code
                city.text = ""
                dialog.dismiss()
            }
            "Country" -> {
                countryRequest = data
                country.text = data
                countryCode = code
                state.text = ""
                city.text = ""
                dialog.dismiss()
            }
            "Pet Breed" -> {
                etPetBreed.text = data
                petBreedId = code
                dialog.dismiss()
            }
        }
    }


    //    Product List Observer

    private fun getLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->

                if (task.isSuccessful && task.result != null) {
                    try {
                        val lastLocation = task.result
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation).longitude

                        if (data.size == 0){
                            pages = 0
                            page = 1
                            limit = 15
                            dataLoadFlag = false
//                            loaderFlag = true
                            viewModelSuggestionList.suggestionListAPi(token,"",1, 20)
                            viewModel.homeListApi(token = token, page =page, limit =limit,radius="",country= "",city="",state="",lat =latitude,long=longitude,petBreedId= petBreedId)

                        }else{
                            loaderFlag = false
                            Progresss.stop()
                        }

                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun reportPost(etCaption: String, position:Int, id:String) {
        data.removeAt(position)
        homePostAdapter.notifyItemRemoved(position)
        homePostAdapter.notifyItemRangeChanged(position, data.size)
        homePostAdapter.notifyDataSetChanged()

        viewModel.reportPostApi(token,id,etCaption)
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseHomeSuggestionList() {


        lifecycleScope.launch {
            viewModelSuggestionList._suggestionListData.collectLatest{ response ->
                when (response) {
                    is Resource.Success -> {
                        if(response.data?.responseCode == 200) {
                            try {
                                val result = response.data.result.docs
                                suggestedUserData = result.filter { !it.isFollow  && !it.isRequested} as ArrayList<suggestionListDocs>
                                homePostAdapter.notifyDataSetChanged()

                                binding.NoDataFound.isVisible = suggestedUserData.isEmpty() && data.isEmpty()


                            }catch (e:Exception){
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


    @SuppressLint("NotifyDataSetChanged")
    override fun follow(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String
    ) {
        if(followButton.isVisible && privacyType.lowercase() == "public"){
            suggestedUserData[position].isFollow = true
            suggestedUserData[position].isRequested = false
            homePostAdapter.suggestionUsersAdapterNotify(position)
        }else if (followButton.isVisible && privacyType.lowercase() == "private"){
            suggestedUserData[position].isFollow = false
            suggestedUserData[position].isRequested = true
            homePostAdapter.suggestionUsersAdapterNotify(position)
        }

        viewModelSuggestionList.followUnfollowAPi(token,_id)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun unFollow(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String,
        requestedButton: LinearLayout
    ) {
        if(unFollowButton.isVisible && privacyType.lowercase() == "public"){
            suggestedUserData[position].isFollow = false
            homePostAdapter.suggestionUsersAdapterNotify(position)
        }else if (unFollowButton.isVisible && privacyType.lowercase() == "private"){
            suggestedUserData[position].isFollow = false
            suggestedUserData[position].isRequested = false
            homePostAdapter.suggestionUsersAdapterNotify(position)
        }



        viewModelSuggestionList.followUnfollowAPi(token,_id)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun requested(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String,
        requestedButton: LinearLayout
    ) {
        if (requestedButton.isVisible && privacyType.lowercase() == "private"){
            suggestedUserData[position].isFollow = false
            suggestedUserData[position].isRequested = false
            homePostAdapter.suggestionUsersAdapterNotify(position)
        }



        viewModelSuggestionList.followUnfollowAPi(token,_id)

    }


    private fun observeResponseFollowUnFollow() {


        lifecycleScope.launch {
            viewModelSuggestionList._followUnfollowData.collectLatest{ response ->

                when (response) {

                    is Resource.Success -> {
                        if(response.data?.responseCode == 200) {
                            try {
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
