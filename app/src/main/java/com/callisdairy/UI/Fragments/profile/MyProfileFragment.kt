package com.callisdairy.UI.Fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Adapter.MyProfileItemListAdaptor
import com.callisdairy.Adapter.ProfilePostAdaptor
import com.callisdairy.Adapter.StoryAdapter
import com.callisdairy.Interface.ViewPetFromProfile
import com.callisdairy.Interface.ViewPost
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Activities.EditProfileActivity
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.MyPetListDocs
import com.callisdairy.api.response.MyPostDocs
import com.callisdairy.databinding.FragmentMyProfileBinding
import com.callisdairy.viewModel.ProfileViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.extension.setSafeOnClickListener
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProfileFragment : Fragment(), ViewPetFromProfile, ViewPost {


    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    var tabFlag = "PET"
    lateinit var getView: View

    //     Bottom Tab
    lateinit var SelectedHome: ImageView
    lateinit var UnSelectedHome: ImageView

    lateinit var UnSelectedMarket: ImageView
    lateinit var SelectedMarket: ImageView

    lateinit var UnSelectedCart: ImageView
    lateinit var SelectedCart: ImageView

    lateinit var UnSelectedProfile: ImageView
    lateinit var SelectedProfile: ImageView

    lateinit var SelectedFavorites: ImageView
    lateinit var UnSelectedFavorites: ImageView

    //   Tool Bar

    lateinit var back: ImageView
    lateinit var chat: ImageView
    lateinit var search: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView

    lateinit var Homeview: View
    lateinit var Marketview: View
    lateinit var Favoritesview: View
    lateinit var ProfileView: View
    lateinit var NotificationView: View
    lateinit var MenuView: View

    var token = ""
    var page = 1
    var limit = 30
    var pages = 0
    var dataLoadFlag = true
    var data = ArrayList<MyPostDocs>()
    var isYouFlag = true

    var pagePet = 1
    var limitPet = 30
    var pagesPet = 0
    var dataLoadFlagPet = true
    var dataPet = ArrayList<MyPetListDocs>()

    var selectedTab = false


    lateinit var dialogs:BottomSheetDialog



    private val viewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)
        allIds()
        toolBarWithBottomTab()
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)!!



        binding.scrollViewPost.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (tabFlag == "MY_POST") {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                    dataLoadFlag = false
                    page++
                    binding.ProgressBarScroll.visibility = View.VISIBLE

                    if (page > pages) {
                        binding.ProgressBarScroll.visibility = View.GONE
                    } else {
                        viewModel.myPostApi(token, page, limit)
                    }


                }
            } else {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                    dataLoadFlagPet = false
                    pagePet++
                    binding.ProgressBarScroll.visibility = View.VISIBLE

                    if (pagePet > pagesPet) {
                        binding.ProgressBarScroll.visibility = View.GONE
                    } else {
                        viewModel.myPetApi(token= token,search ="",page= pagePet,limit=  limitPet, fromDate = "", toDate = "", publishStatus = "")
                    }


                }
            }

        })

        binding.viewDetails.setSafeOnClickListener{
            viewPetDetailsPopUp()
        }



        clicks()
        setStoryAdaptor()





        setTabs()
        return _binding?.root
    }


    override fun onStart() {
        super.onStart()
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)!!
        lifecycleScope.launch {
            val callApis = listOf(
                async { viewModel.viewProfileApi(token) },
                async { viewModel.myPostApi(token, page, limit) },
                async {viewModel.myPetApi(token= token,search ="",page= pagePet,limit=  limitPet, fromDate = "", toDate = "", publishStatus = "")
                }
            )

            callApis.awaitAll()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewProfileResponse()
        observeMyPostList()
        observeMyPetList()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (selectedTab) {
                        selectedTab = false
                        binding.scrollViewPost.scrollY = 0
                        petTabManage()
                    } else {
                        val fm: FragmentManager = requireActivity().supportFragmentManager
                        for (i in 0 until fm.backStackEntryCount) {
                            fm.popBackStack()
                        }
                    }
                }
            })
    }


    private fun setTabs() {
        binding.petButton.setSafeOnClickListener {
            tabFlag = "PET"
            selectedTab = false
            binding.scrollViewPost.scrollY = 0
            binding.ProgressBarScroll.isVisible = false
            petTabManage()
        }



        binding.myPostButton.setSafeOnClickListener {
            tabFlag = "MY_POST"
            selectedTab = true
            binding.ProgressBarScroll.isVisible = false
            binding.scrollViewPost.scrollY = 0
            myPostTabManage()

        }
    }

    private fun setStoryAdaptor() {
        binding.storyRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val storyAdaptor = StoryAdapter(requireContext())
        binding.storyRv.adapter = storyAdaptor
    }


    private fun clicks() {


        binding.FollowingClick.setSafeOnClickListener {

            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Following")
            intent.putExtra("userType", "")
            intent.putExtra("id", "")
            intent.putExtra("following", binding.followingCount.text.toString())
            intent.putExtra("Followers", binding.followersCount.text.toString())
            startActivity(intent)

        }

        binding.FollowersClick.setSafeOnClickListener {

            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Followers")
            intent.putExtra("userType", "")
            intent.putExtra("id", "")
            intent.putExtra("following", binding.followingCount.text.toString())
            intent.putExtra("Followers", binding.followersCount.text.toString())
            startActivity(intent)

        }


        binding.editProfile.setSafeOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }


    }

    private fun allIds() {
        SelectedHome = activity?.findViewById(R.id.SelectedHome)!!
        UnSelectedHome = activity?.findViewById(R.id.UnSelectedHome)!!

        UnSelectedMarket = activity?.findViewById(R.id.UnSelectedMarket)!!
        SelectedMarket = activity?.findViewById(R.id.SelectedMarket)!!

        UnSelectedCart = activity?.findViewById(R.id.UnSelectedCart)!!
        SelectedCart = activity?.findViewById(R.id.SelectedCart)!!

        UnSelectedProfile = activity?.findViewById(R.id.UnSelectedProfile)!!
        SelectedProfile = activity?.findViewById(R.id.SelectedProfile)!!

        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!


        Homeview = activity?.findViewById(R.id.Homeview)!!
        Marketview = activity?.findViewById(R.id.Marketview)!!
        Favoritesview = activity?.findViewById(R.id.Favoritesview)!!
        ProfileView = activity?.findViewById(R.id.ProfileView)!!
        NotificationView = activity?.findViewById(R.id.NotificationView)!!
        MenuView = activity?.findViewById(R.id.MenuView)!!


        SelectedFavorites = activity?.findViewById(R.id.SelectedFavorites)!!
        UnSelectedFavorites = activity?.findViewById(R.id.UnSelectedFavorites)!!
    }

    private fun toolBarWithBottomTab() {
        mainTitle.visibility = View.VISIBLE
        chat.visibility = View.VISIBLE
        back.visibility = View.GONE
        search.visibility = View.VISIBLE

        mainTitle.text = ""
        Username.text = getString(R.string.app_name)


        SelectedMarket.visibility = View.GONE
        UnSelectedMarket.visibility = View.VISIBLE

        SelectedHome.visibility = View.GONE
        UnSelectedHome.visibility = View.VISIBLE

        SelectedCart.visibility = View.GONE
        UnSelectedCart.visibility = View.VISIBLE

        UnSelectedProfile.visibility = View.GONE
        SelectedProfile.visibility = View.VISIBLE

        UnSelectedFavorites.visibility = View.VISIBLE
        SelectedFavorites.visibility = View.GONE


        Homeview.isVisible = false
        Marketview.isVisible = false
        Favoritesview.isVisible = false
        ProfileView.isVisible = true
        NotificationView.isVisible = false
        MenuView.isVisible = false

    }


//    Profile Observer


    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    binding.Name.text = response.data.result.name
                                    binding.userName.text = response.data.result.userName
                                    binding.AddressUser.text = response.data.result.address
                                    binding.followingCount.text =
                                        response.data.result.followingCount.toString()
                                    binding.followersCount.text =
                                        response.data.result.followersCount.toString()

                                    if (response.data.result.postLikesCount.toInt() == 1){
                                        binding.LikesCount.text = "${response.data.result.postLikesCount} Like"

                                    }else{
                                        binding.LikesCount.text = "${response.data.result.postLikesCount} Likes"

                                    }
                                    Glide.with(requireContext()).load(response.data.result.petPic).placeholder(R.drawable.placeholder_pet).into(binding.petPicture)
                                    Glide.with(requireContext()).load(response.data.result.profilePic).placeholder(R.drawable.placeholder).into(binding.userProfilePic)

                                    binding.PostCount.text  = response.data.result.totalPosts.toString()




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


//    My Post Observer and View Post

    private fun observeMyPostList() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._myPostData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {

                                    if (dataLoadFlag) {
                                        data.clear()
                                    }
                                    data.addAll(response.data.result.docs)
                                    pages = response.data.result.totalPages
                                    page = response.data.result.page

                                    setPostAdaptor()

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

    private fun setPostAdaptor() {
        binding.myPostRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = ProfilePostAdaptor(requireContext(), data, this)
        binding.myPostRecycler.adapter = adapter
    }

    override fun viewPost(_id: String) {

        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "ViewMyPost")
        intent.putExtra("id", _id)
        startActivity(intent)


    }


//    Pet Observer


    private fun observeMyPetList() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._myPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {

                                    if (dataLoadFlagPet) {
                                        dataPet.clear()
                                    }

                                    dataPet.addAll(response.data.result.docs)
                                    pagesPet = response.data.result.pages!!
                                    pagePet = response.data.result.page!!

                                    setListItemAdaptor()
                                    setDataAccordingly()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            setDataAccordingly()
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
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

    private fun setListItemAdaptor() {
        binding.postRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = MyProfileItemListAdaptor(requireContext(), dataPet, this, isYouFlag)
        binding.postRecycler.adapter = adapter
    }

    override fun viewPetDetails(_id: String) {
        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "ViewMyPet")
        intent.putExtra("typeUser", "ownUser")
        intent.putExtra("id", _id)
        startActivity(intent)

    }


// TODO Manage Tabs


    private fun petTabManage() {
        binding.myPostRecycler.isVisible = false


        if (dataPet.size > 0) {
            binding.NotFound.isVisible = false
            binding.postRecycler.isVisible = true


        } else {
            binding.NotFound.isVisible = true
            binding.notFoundText.text = "No Pets Yet."
            binding.postRecycler.isVisible = false
        }


        binding.petButton.setBackgroundResource(R.drawable.main_button_background)
        binding.myPostButton.setBackgroundResource(R.drawable.border_background)
        binding.txtPet.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.txtOnSale.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtMyPost.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
    }


    private fun myPostTabManage() {
        binding.postRecycler.isVisible = false
        if (data.size > 0) {
            binding.NotFound.isVisible = false
            binding.myPostRecycler.isVisible = true

        } else {
            binding.NotFound.isVisible = true
            binding.notFoundText.text = "No Posts Yet."
            binding.myPostRecycler.isVisible = false
        }


        binding.petButton.setBackgroundResource(R.drawable.border_background)
        binding.myPostButton.setBackgroundResource(R.drawable.main_button_background)
        binding.txtPet.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtOnSale.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtMyPost.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun setDataAccordingly() {
        when (tabFlag) {
            "MY_POST" -> {
                myPostTabManage()
            }
            else -> {
                petTabManage()
            }
        }
    }


//    Info View


    @SuppressLint("InflateParams")
    private fun viewPetDetailsPopUp() {
        dialogs = BottomSheetDialog(requireContext())
        getView = layoutInflater.inflate(R.layout.view_pet_info, null)!!
        val metrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(metrics)
        dialogs.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialogs.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        dialogs.behavior.peekHeight = metrics.heightPixels


        val cancelLayout: ImageView = getView.findViewById(R.id.cancelLayout)!!
        val imageSlide: ImageSlider = getView.findViewById(R.id.image_slider)!!
        val etFirstName: TextView = getView.findViewById(R.id.etFirstName)!!
        val etMail: TextView = getView.findViewById(R.id.etMail)!!
        val etPhone: TextView = getView.findViewById(R.id.etPhone)!!
        val etGender: TextView = getView.findViewById(R.id.etGender)!!
        val etAddress: TextView = getView.findViewById(R.id.etAddress)!!
        val etCountry: TextView = getView.findViewById(R.id.etCountry)!!
        val etState: TextView = getView.findViewById(R.id.etState)!!
        val etCity: TextView = getView.findViewById(R.id.etCity)!!
        val etZipCode: TextView = getView.findViewById(R.id.etZipCode)!!
        val etPetType: TextView = getView.findViewById(R.id.etPetType)!!
        val etPetName: TextView = getView.findViewById(R.id.etPetName)!!
        val etDateOfBirth: TextView = getView.findViewById(R.id.etDateOfBirth)!!
        val etOrigin: TextView = getView.findViewById(R.id.etOrigin)!!
        val etGenderPet: TextView = getView.findViewById(R.id.etGenderPet)!!
        val etDescription: TextView = getView.findViewById(R.id.etDescription)!!
        val etVaccinatedDate: TextView = getView.findViewById(R.id.etVaccinatedDate)!!
        val etSize: TextView = getView.findViewById(R.id.etSize)!!
        val etBreed: TextView = getView.findViewById(R.id.etBreed)!!
        val etLanguage: TextView = getView.findViewById(R.id.etLanguage)!!
        val etPurchasedStore: TextView = getView.findViewById(R.id.etPurchasedStore)!!
        val etAnimalShelter: TextView = getView.findViewById(R.id.etAnimalShelter)!!
        val etMedicalReport: TextView = getView.findViewById(R.id.etMedicalReport)!!
        val etHabits: TextView = getView.findViewById(R.id.etHabits)!!
        val etAwards: TextView = getView.findViewById(R.id.etAwards)!!
        val etFavoriteClimate: TextView = getView.findViewById(R.id.etFavoriteClimate)!!
        val etPlaceOfBirth: TextView = getView.findViewById(R.id.etPlaceOfBirth)!!
        val etCommonDiseases: TextView = getView.findViewById(R.id.etCommonDiseases)!!
        val etInsurance: TextView = getView.findViewById(R.id.etInsurance)!!
        val etBF: TextView = getView.findViewById(R.id.etBF)!!
        val etToy: TextView = getView.findViewById(R.id.etToy)!!
        val etPetDiet: TextView = getView.findViewById(R.id.etPetDiet)!!
        val etFavMovies: TextView = getView.findViewById(R.id.etFavMovies)!!
        val etFavSong: TextView = getView.findViewById(R.id.etFavSong)!!
        val etCelebrate: TextView = getView.findViewById(R.id.etCelebrate)!!
        val etPetVeterinary: TextView = getView.findViewById(R.id.etPetVeterinary)!!
        val etAppointDoctor: TextView = getView.findViewById(R.id.etAppointDoctor)!!
        val etFavouriteColor: TextView = getView.findViewById(R.id.etFavouriteColor)!!
        val etPetHabits: TextView = getView.findViewById(R.id.etPetHabits)!!
        val etFavouriteFood: TextView = getView.findViewById(R.id.etFavouriteFood)!!
        val etFavouritePlace: TextView = getView.findViewById(R.id.etFavouritePlace)!!

        val sliderList: ArrayList<SlideModel> = ArrayList()


        if (dataPet.size > 0){
            val petInfo = dataPet[0]

            for (i in petInfo.mediaUrls.indices){
                sliderList.add(SlideModel(petInfo.mediaUrls[i].media.mediaUrlMobile))
            }
            imageSlide.setImageList(sliderList)


            if (petInfo.userId.name == null || petInfo.userId.name == ""){
                etFirstName.text= getString(R.string.na_)
            }else{
                etFirstName.text= petInfo.userId.name
            }

            if (petInfo.userId.email == null || petInfo.userId.email == ""){
                etMail.text= getString(R.string.na_)
            }else{
                etMail.text= petInfo.userId.email
            }

            if (petInfo.userId.mobileNumber == null || petInfo.userId.mobileNumber == ""){
                etPhone.text= getString(R.string.na_)
            }else{
                etPhone.text= petInfo.userId.mobileNumber
            }

            if (petInfo.userId.gender == null || petInfo.userId.gender == ""){
                etGender.text= getString(R.string.na_)
            }else{
                etGender.text= petInfo.userId.gender
            }

            if (petInfo.userId.address == null || petInfo.userId.address == ""){
                etAddress.text= getString(R.string.na_)
            }else{
                etAddress.text= petInfo.userId.address
            }

            if (petInfo.userId.country == null || petInfo.userId.address == ""){
                etCountry.text= getString(R.string.na_)
            }else{
                etCountry.text= petInfo.userId.country
            }

            if (petInfo.userId.state == null || petInfo.userId.state == ""){
                etState.text= getString(R.string.na_)
            }else{
                etState.text= petInfo.userId.state
            }

            if (petInfo.userId.city == null || petInfo.userId.city == ""){
                etCity.text= getString(R.string.na_)
            }else{
                etCity.text= petInfo.userId.state
            }

            if (petInfo.userId.zipCode == null || petInfo.userId.zipCode == ""){
                etZipCode.text= getString(R.string.na_)
            }else{
                etZipCode.text= petInfo.userId.zipCode
            }

            if (petInfo.petType == null || petInfo.petType == ""){
                etPetType.text= getString(R.string.na_)
            }else{
                etPetType.text= petInfo.petType
            }

            if (petInfo.petName == null || petInfo.petName == ""){
                etPetName.text= getString(R.string.na_)
            }else{
                etPetName.text= petInfo.petName
            }

            if (petInfo.dob == null || petInfo.dob == ""){
                etDateOfBirth.text= getString(R.string.na_)
            }else{
                etDateOfBirth.text= petInfo.dob
            }

            if (petInfo.origin == null || petInfo.origin == ""){
                etOrigin.text= getString(R.string.na_)
            }else{
                etOrigin.text= petInfo.origin
            }

            if (petInfo.gender == null || petInfo.gender == ""){
                etGenderPet.text= getString(R.string.na_)
            }else{
                etGenderPet.text= petInfo.gender
            }

            if (petInfo.description == null || petInfo.description == ""){
                etDescription.text= getString(R.string.na_)
            }else{
                etDescription.text= petInfo.description
            }

            if (petInfo.petDescription.lastVaccinate == null || petInfo.petDescription.lastVaccinate == ""){
                etVaccinatedDate.text= getString(R.string.na_)
            }else{
                etVaccinatedDate.text= petInfo.petDescription.lastVaccinate
            }

            if (petInfo.petDescription.size == null || petInfo.petDescription.size == ""){
                etSize.text= getString(R.string.na_)
            }else{
                etSize.text= petInfo.petDescription.size
            }

            if (petInfo.breed == null || petInfo.breed == ""){
                etBreed.text= getString(R.string.na_)
            }else{
                etBreed.text= petInfo.breed
            }

            if (petInfo.language == null || petInfo.language == ""){
                etLanguage.text= getString(R.string.na_)
            }else{
                etLanguage.text= petInfo.language
            }

            if (petInfo.purchesStore == null || petInfo.purchesStore == ""){
                etPurchasedStore.text= getString(R.string.na_)
            }else{
                etPurchasedStore.text= petInfo.purchesStore
            }

            if (petInfo.animalShelter == null || petInfo.animalShelter == ""){
                etAnimalShelter.text= getString(R.string.na_)
            }else{
                etAnimalShelter.text= petInfo.animalShelter
            }


            if (petInfo.medicalReport == null || petInfo.medicalReport == ""){
                etMedicalReport.text= getString(R.string.na_)
            }else{
                etMedicalReport.text= petInfo.medicalReport
            }

            if (petInfo.habit == null || petInfo.habit == ""){
                etHabits.text= getString(R.string.na_)
            }else{
                etHabits.text= petInfo.habit
            }

            if (petInfo.awards == null || petInfo.awards == ""){
                etAwards.text= getString(R.string.na_)
            }else{
                etAwards.text= petInfo.awards
            }

            if (petInfo.favoriteClimate == null || petInfo.favoriteClimate == ""){
                etFavoriteClimate.text= getString(R.string.na_)
            }else{
                etFavoriteClimate.text= petInfo.favoriteClimate
            }

            if (petInfo.placeOfBirth == null || petInfo.placeOfBirth == ""){
                etPlaceOfBirth.text= getString(R.string.na_)
            }else{
                etPlaceOfBirth.text= petInfo.placeOfBirth
            }

            if (petInfo.commonDiseases == null || petInfo.commonDiseases == ""){
                etCommonDiseases.text= getString(R.string.na_)
            }else{
                etCommonDiseases.text= petInfo.commonDiseases
            }

            if (petInfo.insurance == null || petInfo.insurance == ""){
                etInsurance.text= getString(R.string.na_)
            }else{
                etInsurance.text= petInfo.insurance
            }

            if (petInfo.bestFriend == null || petInfo.bestFriend == ""){
                etBF.text= getString(R.string.na_)
            }else{
                etBF.text= petInfo.bestFriend
            }


            if (petInfo.toy == null || petInfo.toy == ""){
                etToy.text= getString(R.string.na_)
            }else{
                etToy.text= petInfo.bestFriend
            }


            if (petInfo.dietFreq == null || petInfo.dietFreq == ""){
                etPetDiet.text= getString(R.string.na_)
            }else{
                etPetDiet.text= petInfo.dietFreq
            }


            if (petInfo.movie == null || petInfo.movie == ""){
                etFavMovies.text= getString(R.string.na_)
            }else{
                etFavMovies.text= petInfo.movie
            }

            if (petInfo.song == null || petInfo.song == ""){
                etFavSong.text= getString(R.string.na_)
            }else{
                etFavSong.text= petInfo.song
            }

            if (petInfo.celebrate == null || petInfo.celebrate == ""){
                etCelebrate.text= getString(R.string.na_)
            }else{
                etCelebrate.text= petInfo.celebrate
            }

            if (petInfo.veterinary == null || petInfo.veterinary == ""){
                etPetVeterinary.text= getString(R.string.na_)
            }else{
                etPetVeterinary.text= petInfo.veterinary
            }

            if (petInfo.doctorAppoint == null || petInfo.doctorAppoint == ""){
                etAppointDoctor.text= getString(R.string.na_)
            }else{
                etAppointDoctor.text= petInfo.doctorAppoint
            }

            if (petInfo.favColour == null || petInfo.favColour == ""){
                etFavouriteColor.text= getString(R.string.na_)
            }else{
                etFavouriteColor.text= petInfo.favColour
            }

            if (petInfo.habits == null || petInfo.habits == ""){
                etPetHabits.text= getString(R.string.na_)
            }else{
                etPetHabits.text= petInfo.habits
            }

            if (petInfo.favFood == null || petInfo.favFood == ""){
                etFavouriteFood.text= getString(R.string.na_)
            }else{
                etFavouriteFood.text= petInfo.favFood
            }

            if (petInfo.favPlace == null || petInfo.favPlace == ""){
                etFavouritePlace.text= getString(R.string.na_)
            }else{
                etFavouritePlace.text= petInfo.favPlace
            }



        }else{
            etFirstName.text= getString(R.string.na_)
            etMail.text= getString(R.string.na_)
            etPhone.text= getString(R.string.na_)
            etGender.text= getString(R.string.na_)
            etAddress.text= getString(R.string.na_)
            etState.text= getString(R.string.na_)
            etCity.text= getString(R.string.na_)
            etZipCode.text= getString(R.string.na_)
            etPetType.text= getString(R.string.na_)
            etPetName.text= getString(R.string.na_)
            etDateOfBirth.text= getString(R.string.na_)
            etOrigin.text= getString(R.string.na_)
            etGenderPet.text= getString(R.string.na_)
            etDescription.text= getString(R.string.na_)
            etVaccinatedDate.text= getString(R.string.na_)
            etSize.text= getString(R.string.na_)
            etBreed.text= getString(R.string.na_)
                etLanguage.text= getString(R.string.na_)
                etPurchasedStore.text= getString(R.string.na_)
                etAnimalShelter.text= getString(R.string.na_)
                etMedicalReport.text= getString(R.string.na_)
                etHabits.text= getString(R.string.na_)
                etAwards.text= getString(R.string.na_)
                etFavoriteClimate.text= getString(R.string.na_)
                etPlaceOfBirth.text= getString(R.string.na_)
                etCommonDiseases.text= getString(R.string.na_)
                etInsurance.text= getString(R.string.na_)
                etBF.text= getString(R.string.na_)
                etToy.text= getString(R.string.na_)
                etPetDiet.text= getString(R.string.na_)
                etFavMovies.text= getString(R.string.na_)
                etFavSong.text= getString(R.string.na_)
                etCelebrate.text= getString(R.string.na_)
                etPetVeterinary.text= getString(R.string.na_)
                etAppointDoctor.text= getString(R.string.na_)
                etFavouriteColor.text= getString(R.string.na_)
                etPetHabits.text= getString(R.string.na_)
                etFavouriteFood.text= getString(R.string.na_)
                etFavouritePlace.text= getString(R.string.na_)

            }







        cancelLayout.setSafeOnClickListener {
            dialogs.dismiss()
        }

        dialogs.setContentView(getView)
        dialogs.show()
    }









}