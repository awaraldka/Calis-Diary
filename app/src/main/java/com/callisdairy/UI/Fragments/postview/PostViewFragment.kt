package com.callisdairy.UI.Fragments.postview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.callisdairy.Adapter.ImageHomeAdapter
import com.callisdairy.Interface.DeleteClick
import com.callisdairy.Interface.MorOptionsClick
import com.callisdairy.Interface.MoreOptions
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommentsActivity
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter
import com.callisdairy.UI.dialogs.ImageShowDialog
import com.callisdairy.UI.dialogs.MoreOnPostDialog
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.databinding.FragmentPostViewBinding
import com.callisdairy.viewModel.ViewPostViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.ViewPostResult
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostViewFragment : Fragment(), MoreOptions, DeleteClick , MorOptionsClick {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!

    var postId = ""
    lateinit var backTitle: ImageView
    var token = ""
    var likeCounts = 0
    var position = 0


    var loaderFlag =  true
     var userIdValue = ""
     var imageUrl = ""
     var _id = ""




    private val viewModel: ViewPostViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostViewBinding.inflate(layoutInflater, container, false)
        backTitle = activity?.findViewById(R.id.backTitle)!!
        arguments?.getString("id")?.let {
            postId = it
        }

        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        binding.moreOptions.setSafeOnClickListener {
            childFragmentManager.let { it1 ->
                MoreOnPostDialog(this,_id,position,userIdValue,imageUrl).show(
                    it1, "Follow Bottom Sheet Dialog Fragment"
                )
            }
        }


        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                PlayerViewAdapter.playIndexThenPausePreviousPlayer(position)

            }
        }
        binding.storeViewpager.registerOnPageChangeCallback(callback)



        setListeners()
        return _binding?.root
    }

    override fun onStart() {
        super.onStart()
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        viewModel.viewPostApi(token, postId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseViewPost()
        observeResponseAddLike()
        observeResponseDeletePost()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })
    }


    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        binding.heart.setSafeOnClickListener {
            binding.heart.visibility = View.GONE
            binding.solidHeart.visibility = View.VISIBLE


            val count = likeCounts + 1
            likeCounts = count

            if (count == 0 ){
                binding.PostCount.isVisible = false
                binding.PostCount.text = "$count Like"
            }else{
                binding.PostCount.isVisible = true
                binding.PostCount.text = "$count Likes"
            }
            viewModel.addLikeApi(token,"POST",postId,"")

        }

        binding.solidHeart.setSafeOnClickListener {
            binding.heart.visibility = View.VISIBLE
            binding.solidHeart.visibility = View.GONE

            val count = likeCounts - 1
            likeCounts = count
            if (count == 0 ){
                binding.PostCount.isVisible = false
                binding.PostCount.text = "$count Like"
            }else{
                binding.PostCount.isVisible = true
                binding.PostCount.text = "$count Likes"
            }

            viewModel.addLikeApi(token,"POST",postId,"")


        }




        binding.comment.setSafeOnClickListener {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra("_id",postId)
            startActivity(intent)
        }

        binding.commentMore.setSafeOnClickListener {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra("_id",postId)
            startActivity(intent)
        }

        binding.share.setOnClickListener {

        }
    }



//    View Post Observer

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeResponseViewPost() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._viewPostData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false

                                    _id = response.data.result._id
                                    userIdValue = response.data.result.userId._id
                                    binding.userName.text =   formatUserTag(response.data.result)
                                    binding.postDate.text = DateFormat.covertTimeOtherFormat(response.data.result.createdAt)
                                    Glide.with(requireContext()).load(response.data.result.userId.profilePic).placeholder(R.drawable.placeholder).into(binding.userProfile)
                                    Glide.with(requireContext()).load(response.data.result.userId.petPic).placeholder(R.drawable.placeholder_pet).into(binding.petImage)

                                    binding.PostDescription.text = response.data.result.title.replace("\"","")
                                    if (response.data.result.mediaUrls.isNotEmpty() && response.data.result.mediaUrls[0].media.mediaUrlMobile.isNotEmpty()){
                                        binding.imageShow.isVisible = true
                                        binding.PostDescription.isVisible = true
                                        binding.PostText.isVisible = false
                                    }else{
                                        binding.imageShow.isVisible = false
                                        binding.PostDescription.isVisible = false
                                        binding.PostText.isVisible = true
                                    }

                                    binding.PostText.text =  response.data.result.title.replace("\"","")


                                    likeCounts = response.data.result.likeCount

                                    if (response.data.result.likeCount == 0) {

                                        binding.PostCount.isVisible = false
                                    } else {
                                        binding.PostCount.isVisible = true
                                        when (response.data.result.likeCount) {
                                            in 0..2 -> {
                                                binding.PostCount.text =
                                                    "${response.data.result.likeCount} Like"
                                            }
                                            in 3..999 -> {
                                                binding.PostCount.text =
                                                    "${response.data.result.likeCount} Likes"
                                            }
                                            else -> {
                                                binding.PostCount.text =
                                                    "${response.data.result.likeCount}K Likes"
                                            }
                                        }
                                    }
                                    if (response.data.result.commentCount == 0) {
                                        binding.commentMore.isVisible = false
                                    } else {
                                        binding.commentMore.isVisible = true
                                        when (response.data.result.commentCount) {
                                            in 0..2 -> {
                                                binding.commentMore.text =
                                                    "View ${response.data.result.commentCount} Comment"
                                            }

                                            in 2..999 -> {
                                                binding.commentMore.text =
                                                    "View All ${response.data.result.commentCount} Comments"
                                            }

                                            else -> {
                                                binding.commentMore.text =
                                                    "View All ${response.data.result.commentCount}k Comments"
                                            }
                                        }
                                    }

                                    imageUrl = response.data.result.mediaUrls.getOrNull(0).toString()
                                    setImageAdaptor(response.data.result.mediaUrls)
                                    binding.indicator.isVisible = response.data.result.mediaUrls.size > 1

                                    if (response.data.result.isLiked) {
                                        binding.solidHeart.isVisible = true
                                        binding.heart.isVisible = false
                                    } else {
                                        binding.solidHeart.isVisible = false
                                        binding.heart.isVisible = true
                                    }


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
                            if (loaderFlag){
                                Progresss.start(requireContext())
                            }


                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }

            }

        }


    }

    private fun setImageAdaptor(mediaUrls: ArrayList<MediaUrls>) {
//        val imageAdaptor = ImagePostViewAdaptor(requireContext(), mediaUrls)
        val imageAdaptor = ImageHomeAdapter(requireContext(), mediaUrls,this)
        binding.storeViewpager.adapter = imageAdaptor
        binding.indicator.setViewPager(binding.storeViewpager)

    }

//     Like Unlike observer

    private fun observeResponseAddLike() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addLikeData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

//                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let {
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
//                        Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
//                            Progresss.stop()
                        }

                    }

                }
            }
        }


    }


    private fun formatUserTag(listData: ViewPostResult): CharSequence {
        val userName = "<font color=\"black\">${listData.userId.userName}</font>"
        val address = if (listData.address.isNotBlank()) {
            "in <font color=\"black\">${listData.address}</font>"
        } else {
            ""
        }
        val tagCount = listData.tagPeople.size
        val secondName = if (tagCount > 0) {
            "tagged <font color=\"black\">${listData.tagPeople[0].name}</font>"
        } else {
            ""
        }
        val others = if (tagCount > 1) {
            "<font color=\"black\">${tagCount - 1} others</font>"
        } else {
            ""
        }

        return Html.fromHtml(when {
            tagCount == 1 && address.isNotBlank() -> "$userName $secondName $address"
            tagCount > 1 && address.isNotBlank() -> "$userName $secondName and $others $address"
            tagCount == 1 && address.isBlank() -> "$userName tagged $secondName"
            address.isNotBlank() -> "$userName $address"
            else -> userName
        })
    }

    override fun share(imageUrl: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, imageUrl)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun hidePost(_id: String, position: Int) {

    }

    override fun report(_id: String, position: Int) {

    }

    override fun deletePost(_id: String, position: Int) {
        postId =  _id
        androidExtension.alertBoxDelete("Are you sure you want to delete this post?", requireContext(),this)
    }

    override fun deleteItem() {
        viewModel.deletePostApi(token,postId)
    }

    private fun observeResponseDeletePost() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deletePostData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    activity?.finishAfterTransition()
                                    parentFragmentManager.popBackStack()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
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

    override fun openPopUpMore(_id: String, position: Int, _id1: String, imageUrl: String) {

    }

    override fun sharePost(imageUrl: String) {

    }

    override fun viewImages(media: ArrayList<MediaUrls>) {
        ImageShowDialog(media).show(parentFragmentManager, "ShowImage")
    }


    override fun onDestroy() {
        super.onDestroy()
        PlayerViewAdapter.releaseAllPlayers()
    }


    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }

}