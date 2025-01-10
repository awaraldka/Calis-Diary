package com.callisdairy.UI.Fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.callisdairy.Adapter.MyProfileItemListAdaptor
import com.callisdairy.Adapter.ProfilePostAdaptor
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.Interface.FollowDialogListener
import com.callisdairy.Interface.RestrictDialogListener
import com.callisdairy.Interface.ViewPetFromProfile
import com.callisdairy.Interface.ViewPost
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Activities.EditProfileActivity
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.UI.dialogs.MuteBottomFragmentDialog
import com.callisdairy.UI.dialogs.RestrictBottomFragmentDialog
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.MutualFriends
import com.callisdairy.api.response.MyPetListDocs
import com.callisdairy.api.response.MyPostDocs
import com.callisdairy.databinding.FragmentUserProfileBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.othersProfileViewModel
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : Fragment(), FollowDialogListener, RestrictDialogListener, ViewPost,
    ViewPetFromProfile, CommonDialogInterface {


    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    var userName = ""
    var userId = ""
    var token = ""

    var tabFlag = ""
    var privacyType = ""

    var page = 1
    var limit = 30
    var pages = 0
    var dataLoadFlag = true
    var data = ArrayList<MyPostDocs>()


    var pagePet = 1
    var limitPet = 30
    var pagesPet = 0
    var dataLoadFlagPet = true
    var dataPet = ArrayList<MyPetListDocs>()

    var stackFlag = false
    var isYouFlag = false

    var receiverId = ""
    var petPic = ""
    var profilePic = ""
    var name = ""

    lateinit var backTitle: ImageView
    lateinit var dialogs:BottomSheetDialog
    lateinit var getView: View
    lateinit var adapter : MyProfileItemListAdaptor

    private val viewModel: othersProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        backTitle = activity?.findViewById(R.id.backTitle)!!


        arguments?.getString("userName")?.let {
            userName = it
        }
        arguments?.getString("id")?.let {
            userId = it
        }
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)
            .toString()


        val loginVendor = SavedPrefManager.getBooleanPreferences(requireContext(),SavedPrefManager.isVendor)



//        binding.follow.isEnabled = loginVendor



        println("==================>>>>>>>>>> $userId")
        println("==================>>>>>>>>>> $token")



        lifecycleScope.launch {
            val callApis = listOf(
                async { viewModel.viewOthersProfileApi(token, userId) },
                async { viewModel.othersPostApi(token, userId, page, limit) },
                async { viewModel.othersPetApi(token, userId, pagePet, limitPet) },
            )
            callApis.awaitAll()
        }


        binding.editProfile.setSafeOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }


        setTabs()
        clicks()
        setListeners()

//        CLICK_MANAGE_ACCORDING_TO_ACCOUNT_TYPE()

        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
        }



        binding.message.setSafeOnClickListener {
            val intent = Intent(context, OneToOneChatActivity::class.java)
            intent.putExtra("receiverId", receiverId)
            intent.putExtra("petImage", petPic)
            intent.putExtra("userImage", profilePic)
            intent.putExtra("userName", name)
            startActivity(intent)
        }


        return _binding?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewProfileResponse()
        observeMyPostList()
        observeMyPetList()
        observeResponseFollowUnFollow()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (stackFlag) {
                        stackFlag = false
                        binding.scrollViewPost.scrollY = 0
                        postFunctionality()
                    } else {
                        activity?.finishAfterTransition()
                    }


                }
            })
    }

    private fun setListeners() {
        binding.follow.setOnClickListener {
//            fragmentManager?.let { it1 ->
//                FollowBottomFragmentDialog(this,userName,petPic,profilePic).show(
//                    it1,
//                    "Follow Bottom Sheet Dialog Fragment"
//                )
//            }
            binding.Unfollow.isVisible = true
            binding.follow.isVisible = false
            viewModel.followUnfollowAPi(token, userId)


        }
    }


    private fun setTabs() {
        binding.postButton.setOnClickListener {
            tabFlag = "Post"
            stackFlag = false
            binding.scrollViewPost.scrollY = 0
            binding.ProgressBarScroll.isVisible = false
            postFunctionality()
        }

        binding.PetsClick.setOnClickListener {
            tabFlag = "Pet"
            stackFlag = true
            binding.scrollViewPost.scrollY = 0
            binding.ProgressBarScroll.isVisible = false

            petFunctionality()
        }
    }


    override fun restrictListener() {
        fragmentManager?.let { it1 ->
            RestrictBottomFragmentDialog(this).show(
                it1,
                "Follow Bottom Sheet Dialog Fragment"
            )
        }

    }

    override fun followListener() {
        binding.Unfollow.isVisible = true
        binding.follow.isVisible = false
        viewModel.followUnfollowAPi(token, userId)


    }

    override fun muteListener() {
        fragmentManager?.let { it1 ->
            MuteBottomFragmentDialog(requireContext()).show(
                it1,
                "Follow Bottom Sheet Dialog Fragment"
            )
        }

    }

    override fun restrictAccountListener() {

    }


    private fun clicks() {


        binding.viewDetails.setOnClickListener {
            viewPetDetailsPopUp()
        }


        binding.FollowingClick.setOnClickListener {

            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Following")
            intent.putExtra("userType", "otherUser")
            intent.putExtra("id", userId)
            intent.putExtra("following", binding.FollowingPublic.text.toString())
            intent.putExtra("Followers", binding.followersPublic.text.toString())
            startActivity(intent)


        }

        binding.FollowersClick.setOnClickListener {


            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Followers")
            intent.putExtra("userType", "otherUser")
            intent.putExtra("id", userId)
            intent.putExtra("following", binding.FollowingPublic.text.toString())
            intent.putExtra("Followers", binding.followersPublic.text.toString())
            startActivity(intent)


        }

        binding.Unfollow.setOnClickListener {
            when (privacyType.lowercase()) {
                "public" -> {
                    binding.Unfollow.isVisible = false
                    binding.follow.isVisible = true
                    viewModel.followUnfollowAPi(token, userId)
                }

                "private" -> {
                    androidExtension.alertBoxCommon(
                        "Are you sure you want to unfollow this account.",
                        requireContext(),
                        this
                    )
                }
            }

        }

        binding.privateFollowLl.setOnClickListener {
            println("==========privacyType============ $privacyType")
            when (privacyType.lowercase()) {
                "public" -> {

                }
                "private" -> {

                    if (binding.privateFollowTv.text.toString().lowercase() == "follow") {
                        binding.privateFollowTv.text = "Requested"
                        binding.privateFollowTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                        binding.privateFollowLl.background =
                            resources.getDrawable(R.drawable.border_background)
                        viewModel.followUnfollowAPi(token, userId)

                    } else if (binding.privateFollowTv.text.toString()
                            .lowercase() == "requested"
                    ) {
                        binding.privateFollowTv.text = "follow"
                        binding.privateFollowTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                        binding.privateFollowLl.background =
                            resources.getDrawable(R.drawable.button_background)
                        viewModel.followUnfollowAPi(token, userId)
                    }


                }
            }
        }


    }


//    Profile observer

    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {


                                    receiverId = response.data.result._id
                                    petPic = response.data.result.petPic
                                    profilePic = response.data.result.profilePic
                                    name = response.data.result.name


                                    privacyType = response.data.result.privacyType

                                    println("==========privacyType============ $privacyType")


                                    binding.NamePrivate.text = response.data.result.name
                                    binding.locationPrivate.text = response.data.result.address
                                    binding.followingPrivate.text =
                                        response.data.result.followingCount.toString()
                                    binding.followersPrivate.text = response.data.result.followersCount.toString()
                                    Glide.with(requireContext()).load(response.data.result.petPic).placeholder(R.drawable.placeholder_pet).into(binding.petPicturePicture)
                                    Glide.with(requireContext()).load(response.data.result.profilePic).placeholder(R.drawable.placeholder).into(binding.userProfilePicPrivate)

                                    binding.postPublic.text = response.data.result.totalPosts.toString()
                                    binding.postPrivate.text = response.data.result.totalPosts.toString()

                                    binding.NamePublic.text = response.data.result.name
                                    binding.locationPublic.text = response.data.result.address
                                    binding.FollowingPublic.text =
                                        response.data.result.followingCount.toString()
                                    binding.followersPublic.text =
                                        response.data.result.followersCount.toString()
                                    Glide.with(requireContext()).load(response.data.result.petPic).placeholder(R.drawable.placeholder_pet).into(binding.petPicturePublic)
                                    Glide.with(requireContext()).load(response.data.result.profilePic).placeholder(R.drawable.placeholder).into(binding.userProfilePicPublic)
//isYouFlag = response.data.result.isYou
                                    //layouts handle according to privacyType
                                    if (response.data.result.isYou) {
                                        binding.accountPublic.isVisible = true
                                        binding.PrivateAccount.isVisible = false
                                        binding.message.isVisible = false
                                        binding.follow.isVisible = false
                                        binding.mutualFriends.isVisible = false
                                        binding.editProfile.isVisible = true

                                    } else {
                                        if (response.data.result.privacyType.lowercase() == "public") {
                                            binding.accountPublic.isVisible = true
                                            binding.PrivateAccount.isVisible = false
                                            binding.message.isVisible = true
//                                            binding.follow.isVisible = true
                                            binding.editProfile.isVisible = false

                                            if (response.data.result.isFollowed) {
                                                binding.Unfollow.isVisible = true
                                                binding.follow.isVisible = false
                                            } else {
                                                binding.Unfollow.isVisible = false
                                                binding.follow.isVisible = true
                                            }
                                        } else {
                                            if (response.data.result.isRequested && !response.data.result.isFollowed) {
                                                binding.privateFollowTv.text = "Requested"
                                                binding.privateFollowTv.setTextColor(
                                                    resources.getColor(
                                                        R.color.black
                                                    )
                                                )
                                                binding.privateFollowLl.background =
                                                    resources.getDrawable(R.drawable.border_background)
                                                binding.accountPublic.isVisible = false
                                                binding.PrivateAccount.isVisible = true
                                            } else if (!response.data.result.isRequested && response.data.result.isFollowed) {
                                                binding.accountPublic.isVisible = true
                                                binding.PrivateAccount.isVisible = false
                                                binding.Unfollow.isVisible = true
                                                binding.follow.isVisible = false
                                            } else {
                                                binding.accountPublic.isVisible = false
                                                binding.PrivateAccount.isVisible = true
                                            }
                                        }
                                    }

                                    setMutualFriendsList(response.data.result.mutualFriends)

                                    if (response.data.result.postLikesCount.toInt() == 1){
                                        binding.LikesCount.text = "${response.data.result.postLikesCount} Like"

                                    }else{
                                        binding.LikesCount.text = "${response.data.result.postLikesCount} Likes"

                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            binding.accountPublic.isVisible = false
                            binding.PrivateAccount.isVisible = false

                            Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMutualFriendsList(mutualFriends: ArrayList<MutualFriends>) {
        val mutualFriendNames = StringBuffer()
        mutualFriendNames.append("Followed by ")
        if (mutualFriends.size > 4) {
            //public
            Glide.with(requireContext()).load(mutualFriends[0].petPic).placeholder(R.drawable.notfound_image).into(binding.userImg1)
            Glide.with(requireContext()).load(mutualFriends[1].petPic).placeholder(R.drawable.notfound_image).into(binding.userImg2)
            Glide.with(requireContext()).load(mutualFriends[2].petPic).placeholder(R.drawable.notfound_image).into(binding.userImg3)
            Glide.with(requireContext()).load(mutualFriends[3].petPic).placeholder(R.drawable.notfound_image).into(binding.userImg4)

            //private
            Glide.with(requireContext()).load(mutualFriends[0].petPic).placeholder(R.drawable.notfound_image).into(binding.priUserImg1)
            Glide.with(requireContext()).load(mutualFriends[1].petPic).placeholder(R.drawable.notfound_image).into(binding.priUserImg2)
            Glide.with(requireContext()).load(mutualFriends[2].petPic).placeholder(R.drawable.notfound_image).into(binding.priUserImg3)
            Glide.with(requireContext()).load(mutualFriends[3].petPic).placeholder(R.drawable.notfound_image).into(binding.priUserImg4)

            mutualFriendNames.append("${mutualFriends[0].userName}, ")
            mutualFriendNames.append("${mutualFriends[1].userName}")
            //public
            binding.mutualFriendsTv.text = "$mutualFriendNames and ${mutualFriends.size} others"

            //private
            binding.priUserNameTv.text = "$mutualFriendNames and ${mutualFriends.size} others"
        } else if (mutualFriends.size > 3) {
            //public
            Glide.with(requireContext()).load(mutualFriends[0].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.userImg1)
            Glide.with(requireContext()).load(mutualFriends[1].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.userImg2)
            Glide.with(requireContext()).load(mutualFriends[2].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.userImg3)
            binding.userImg4.isVisible = false

            //private
            Glide.with(requireContext()).load(mutualFriends[0].petPic)
                .placeholder(R.drawable.image_profile).into(binding.priUserImg1)
            Glide.with(requireContext()).load(mutualFriends[1].petPic)
                .placeholder(R.drawable.image_profile).into(binding.priUserImg2)
            Glide.with(requireContext()).load(mutualFriends[2].petPic)
                .placeholder(R.drawable.image_profile).into(binding.priUserImg3)
            binding.priUserImg4.isVisible = false
            mutualFriendNames.append("${mutualFriends[0].userName}, ")
            mutualFriendNames.append("${mutualFriends[1].userName}")

            //public
            binding.mutualFriendsTv.text = "$mutualFriendNames and ${mutualFriends.size} others"

            //private
            binding.priUserNameTv.text = "$mutualFriendNames and ${mutualFriends.size} others"

        } else if (mutualFriends.size > 2) {
            //public
            Glide.with(requireContext()).load(mutualFriends[0].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.userImg1)
            Glide.with(requireContext()).load(mutualFriends[1].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.userImg2)
            binding.userImg3.isVisible = false
            binding.userImg4.isVisible = false

            //private
            Glide.with(requireContext()).load(mutualFriends[0].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.priUserImg1)
            Glide.with(requireContext()).load(mutualFriends[1].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.priUserImg2)
            binding.priUserImg3.isVisible = false
            binding.priUserImg4.isVisible = false
            mutualFriendNames.append("${mutualFriends[0].userName}, ")
            mutualFriendNames.append("${mutualFriends[1].userName}")

            //public
            binding.mutualFriendsTv.text = "$mutualFriendNames and ${mutualFriends.size} others"

            //private
            binding.priUserNameTv.text = "$mutualFriendNames and ${mutualFriends.size} others"

        } else if (mutualFriends.size > 1) {
            //public
            Glide.with(requireContext()).load(mutualFriends[0].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.userImg1)
            binding.userImg2.isVisible = false
            binding.userImg3.isVisible = false
            binding.userImg4.isVisible = false

            //private
            Glide.with(requireContext()).load(mutualFriends[0].petPic)
                .placeholder(R.drawable.notfound_image).into(binding.priUserImg1)
            binding.priUserImg2.isVisible = false
            binding.priUserImg3.isVisible = false
            binding.priUserImg4.isVisible = false
            mutualFriendNames.append(mutualFriends[0].userName)

            //public
            binding.mutualFriendsTv.text = "$mutualFriendNames and ${mutualFriends.size} others"

            //private
            binding.priUserNameTv.text = "$mutualFriendNames and ${mutualFriends.size} others"

        } else {
            //public
            binding.mutualFriends.isInvisible = true

            //private
            binding.priMutualFriends.isInvisible = true
        }
    }


    //     Post Observer and View Post

    private fun observeMyPostList() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._postData.collectLatest { response ->

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

    private fun setPostAdaptor() {
        binding.postRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter = ProfilePostAdaptor(requireContext(), data, this)
        binding.postRecycler.adapter = adapter
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
                viewModel._petData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {
                                    if (dataLoadFlagPet) {
                                        dataPet.clear()
                                    }

                                    dataPet.addAll(response.data.result.docs)
                                    setListItemAdaptor()
                                    pagesPet = response.data.result.pages!!
                                    pagePet = response.data.result.page!!



                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {

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
        binding.petRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = MyProfileItemListAdaptor(requireContext(), dataPet, this, isYouFlag)
        binding.petRecycler.adapter = adapter
    }

    override fun viewPetDetails(_id: String) {
        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "ViewMyPet")
        intent.putExtra("typeUser", "otherUser")
        intent.putExtra("id", _id)
        startActivity(intent)

    }


//     TODO tab manage


    private fun petFunctionality() {
        binding.postRecycler.isVisible = false

        if (dataPet.size > 0) {
            binding.NotFound.isVisible = false
            binding.petRecycler.isVisible = true

        } else {
            binding.NotFound.isVisible = true
            binding.notFoundText.text = "No Pets Yet."
            binding.petRecycler.isVisible = false
        }



        binding.postButton.setBackgroundResource(R.drawable.border_background)
        binding.PetsClick.setBackgroundResource(R.drawable.main_button_background)
        binding.txtPost.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtOnSale.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
    }

    private fun postFunctionality() {
        binding.petRecycler.isVisible = false

        if (data.size > 0) {
            binding.NotFound.isVisible = false
            binding.postRecycler.isVisible = true

        } else {
            binding.NotFound.isVisible = true
            binding.notFoundText.text = "No Posts Yet."
            binding.postRecycler.isVisible = false
        }


        binding.postButton.setBackgroundResource(R.drawable.main_button_background)
        binding.PetsClick.setBackgroundResource(R.drawable.border_background)
        binding.txtPost.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.txtOnSale.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
    }

    private fun setDataAccordingly() {
        when (tabFlag) {
            "Pet" -> {
                petFunctionality()
                setListItemAdaptor()
            }
            else -> {
                postFunctionality()
            }
        }
    }


//    Follow Unfollow


    private fun observeResponseFollowUnFollow() {


        lifecycleScope.launch {
            viewModel._followUnfollowData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {
                        if (response.data?.responseCode == 200) {
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun commonWork() {
        binding.accountPublic.isVisible = false
        binding.PrivateAccount.isVisible = true
        binding.privateFollowTv.text = "follow"
        binding.privateFollowTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.privateFollowLl.background = resources.getDrawable(R.drawable.button_background)
        viewModel.followUnfollowAPi(token, userId)
    }


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
                etFirstName.text= "N/A"
            }else{
                etFirstName.text= petInfo.userId.name
            }

            if (petInfo.userId.email == null || petInfo.userId.email == ""){
                etMail.text= "N/A"
            }else{
//                etMail.text= petInfo.userId.email
                val p = """^([^@]{2})([^@]+)""".toRegex()
                etMail.text = petInfo.userId.email.replace(p){
                    it.groupValues[1] + "*".repeat(it.groupValues[2].length)
                }


            }

            if (petInfo.userId.mobileNumber == null || petInfo.userId.mobileNumber == ""){
                etPhone.text= "N/A"
            }else{
//                etPhone.text= "${petInfo.userId.mobileNumber.substring(0,4)} xxxx ${petInfo.userId.mobileNumber.substring(6,9)}"
                etPhone.text= "******${petInfo.userId.mobileNumber.substring(petInfo.userId.mobileNumber.length-4,petInfo.userId.mobileNumber.length)}"
            }

            if (petInfo.userId.gender == null || petInfo.userId.gender == ""){
                etGender.text= "N/A"
            }else{
                etGender.text= petInfo.userId.gender
            }

            if (petInfo.userId.address == null || petInfo.userId.address == ""){
                etAddress.text= "N/A"
            }else{
                etAddress.text= petInfo.userId.address
            }

            if (petInfo.userId.country == null || petInfo.userId.address == ""){
                etCountry.text= "N/A"
            }else{
                etCountry.text= petInfo.userId.country
            }

            if (petInfo.userId.state == null || petInfo.userId.state == ""){
                etState.text= "N/A"
            }else{
                etState.text= petInfo.userId.state
            }

            if (petInfo.userId.city == null || petInfo.userId.city == ""){
                etCity.text= "N/A"
            }else{
                etCity.text= petInfo.userId.state
            }

            if (petInfo.userId.zipCode == null || petInfo.userId.zipCode == ""){
                etZipCode.text= "N/A"
            }else{
                etZipCode.text= petInfo.userId.zipCode
            }

            if (petInfo.petType == null || petInfo.petType == ""){
                etPetType.text= "N/A"
            }else{
                etPetType.text= petInfo.petType
            }

            if (petInfo.petName == null || petInfo.petName == ""){
                etPetName.text= "N/A"
            }else{
                etPetName.text= petInfo.petName
            }

            if (petInfo.dob == null || petInfo.dob == ""){
                etDateOfBirth.text= "N/A"
            }else{
                etDateOfBirth.text= petInfo.dob
            }

            if (petInfo.origin == null || petInfo.origin == ""){
                etOrigin.text= "N/A"
            }else{
                etOrigin.text= petInfo.origin
            }

            if (petInfo.gender == null || petInfo.gender == ""){
                etGenderPet.text= "N/A"
            }else{
                etGenderPet.text= petInfo.gender
            }

            if (petInfo.description == null || petInfo.description == ""){
                etDescription.text= "N/A"
            }else{
                etDescription.text= petInfo.description
            }

            if (petInfo.petDescription.lastVaccinate == null || petInfo.petDescription.lastVaccinate == ""){
                etVaccinatedDate.text= "N/A"
            }else{
                etVaccinatedDate.text= petInfo.petDescription.lastVaccinate
            }

            if (petInfo.petDescription.size == null || petInfo.petDescription.size == ""){
                etSize.text= "N/A"
            }else{
                etSize.text= petInfo.petDescription.size
            }

            if (petInfo.breed == null || petInfo.breed == ""){
                etBreed.text= "N/A"
            }else{
                etBreed.text= petInfo.breed
            }

            if (petInfo.language == null || petInfo.language == ""){
                etLanguage.text= "N/A"
            }else{
                etLanguage.text= petInfo.language
            }

            if (petInfo.purchesStore == null || petInfo.purchesStore == ""){
                etPurchasedStore.text= "N/A"
            }else{
                etPurchasedStore.text= petInfo.purchesStore
            }

            if (petInfo.animalShelter == null || petInfo.animalShelter == ""){
                etAnimalShelter.text= "N/A"
            }else{
                etAnimalShelter.text= petInfo.animalShelter
            }


            if (petInfo.medicalReport == null || petInfo.medicalReport == ""){
                etMedicalReport.text= "N/A"
            }else{
                etMedicalReport.text= petInfo.medicalReport
            }

            if (petInfo.habit == null || petInfo.habit == ""){
                etHabits.text= "N/A"
            }else{
                etHabits.text= petInfo.habit
            }

            if (petInfo.awards == null || petInfo.awards == ""){
                etAwards.text= "N/A"
            }else{
                etAwards.text= petInfo.awards
            }

            if (petInfo.favoriteClimate == null || petInfo.favoriteClimate == ""){
                etFavoriteClimate.text= "N/A"
            }else{
                etFavoriteClimate.text= petInfo.favoriteClimate
            }

            if (petInfo.placeOfBirth == null || petInfo.placeOfBirth == ""){
                etPlaceOfBirth.text= "N/A"
            }else{
                etPlaceOfBirth.text= petInfo.placeOfBirth
            }

            if (petInfo.commonDiseases == null || petInfo.commonDiseases == ""){
                etCommonDiseases.text= "N/A"
            }else{
                etCommonDiseases.text= petInfo.commonDiseases
            }

            if (petInfo.insurance == null || petInfo.insurance == ""){
                etInsurance.text= "N/A"
            }else{
                etInsurance.text= petInfo.insurance
            }

            if (petInfo.bestFriend == null || petInfo.bestFriend == ""){
                etBF.text= "N/A"
            }else{
                etBF.text= petInfo.bestFriend
            }


            if (petInfo.toy == null || petInfo.toy == ""){
                etToy.text= "N/A"
            }else{
                etToy.text= petInfo.bestFriend
            }


            if (petInfo.dietFreq == null || petInfo.dietFreq == ""){
                etPetDiet.text= "N/A"
            }else{
                etPetDiet.text= petInfo.dietFreq
            }


            if (petInfo.movie == null || petInfo.movie == ""){
                etFavMovies.text= "N/A"
            }else{
                etFavMovies.text= petInfo.movie
            }

            if (petInfo.song == null || petInfo.song == ""){
                etFavSong.text= "N/A"
            }else{
                etFavSong.text= petInfo.song
            }

            if (petInfo.celebrate == null || petInfo.celebrate == ""){
                etCelebrate.text= "N/A"
            }else{
                etCelebrate.text= petInfo.celebrate
            }

            if (petInfo.veterinary == null || petInfo.veterinary == ""){
                etPetVeterinary.text= "N/A"
            }else{
                etPetVeterinary.text= petInfo.veterinary
            }

            if (petInfo.doctorAppoint == null || petInfo.doctorAppoint == ""){
                etAppointDoctor.text= "N/A"
            }else{
                etAppointDoctor.text= petInfo.doctorAppoint
            }

            if (petInfo.favColour == null || petInfo.favColour == ""){
                etFavouriteColor.text= "N/A"
            }else{
                etFavouriteColor.text= petInfo.favColour
            }

            if (petInfo.habits == null || petInfo.habits == ""){
                etPetHabits.text= "N/A"
            }else{
                etPetHabits.text= petInfo.habits
            }

            if (petInfo.favFood == null || petInfo.favFood == ""){
                etFavouriteFood.text= "N/A"
            }else{
                etFavouriteFood.text= petInfo.favFood
            }

            if (petInfo.favPlace == null || petInfo.favPlace == ""){
                etFavouritePlace.text= "N/A"
            }else{
                etFavouritePlace.text= petInfo.favPlace
            }



        }else{
            etFirstName.text= "N/A"
            etMail.text= "N/A"
            etPhone.text= "N/A"
            etGender.text= "N/A"
            etAddress.text= "N/A"
            etState.text= "N/A"
            etCity.text= "N/A"
            etZipCode.text= "N/A"
            etPetType.text= "N/A"
            etPetName.text= "N/A"
            etDateOfBirth.text= "N/A"
            etOrigin.text= "N/A"
            etGenderPet.text= "N/A"
            etDescription.text= "N/A"
            etVaccinatedDate.text= "N/A"
            etSize.text= "N/A"
            etBreed.text= "N/A"
            etLanguage.text= "N/A"
            etPurchasedStore.text= "N/A"
            etAnimalShelter.text= "N/A"
            etMedicalReport.text= "N/A"
            etHabits.text= "N/A"
            etAwards.text= "N/A"
            etFavoriteClimate.text= "N/A"
            etPlaceOfBirth.text= "N/A"
            etCommonDiseases.text= "N/A"
            etInsurance.text= "N/A"
            etBF.text= "N/A"
            etToy.text= "N/A"
            etPetDiet.text= "N/A"
            etFavMovies.text= "N/A"
            etFavSong.text= "N/A"
            etCelebrate.text= "N/A"
            etPetVeterinary.text= "N/A"
            etAppointDoctor.text= "N/A"
            etFavouriteColor.text= "N/A"
            etPetHabits.text= "N/A"
            etFavouriteFood.text= "N/A"
            etFavouritePlace.text= "N/A"

        }


        cancelLayout.setOnClickListener {
            dialogs.dismiss()
        }

        dialogs.setContentView(getView)
        dialogs.show()
    }



}