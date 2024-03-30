package com.callisdairy.UI.Fragments.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.FollowerAdapter
import com.callisdairy.Interface.ProfileFollowUnfollow
import com.callisdairy.Interface.ProfileFollowUnfollowMain
import com.callisdairy.Interface.UserProfileClick
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.FollowersListDocs
import com.callisdairy.api.response.FollowingUserListFollowing
import com.callisdairy.databinding.FragmentFollowerListBinding
import com.callisdairy.viewModel.FollowingListViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowerListFragment(
    val userProfileClick: UserProfileClick,
    val userType: String,
    val userId: String,
    val followUnfollow: ProfileFollowUnfollowMain
    ) : Fragment(), ProfileFollowUnfollow {


    private var _binding: FragmentFollowerListBinding? = null
    private val binding get() = _binding!!
    var token = ""
    lateinit var followerAdapter : FollowerAdapter


    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true
    var data = ArrayList<FollowersListDocs>()





    private val viewModel: FollowingListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowerListBinding.inflate(layoutInflater, container, false)



        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()


        if (userType == "otherUser") {
            viewModel.othersFollowersListApi(token, userId,"",page, limit)
        } else {
            viewModel.followersListApi(token,"",page, limit)
        }
        binding.DFsearch.addTextChangedListener(textWatcher)
        binding.scrollViewEvent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    if (userType == "otherUser") {
                        viewModel.othersFollowersListApi(token, userId,"",page, limit)
                    } else {
                        viewModel.followersListApi(token,"",page, limit)
                    }
                }

            }


        })

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(userType == "otherUser"){
            observeOtherUserFollowersResponse()
        }else{
            observeFollowersResponse()
        }


    }


//    Followers List API

    private fun observeFollowersResponse() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._followersListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {


                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }

                                    if (response.data.result.docs.size > 0){
                                        binding.rvFollowing.isVisible = true
                                        binding.NotFound.isVisible = false

                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.totalPages
                                        page = response.data.result.page
                                        setFollowerAdaptor()

                                    }else{
                                        binding.NotFound.isVisible = true
                                        binding.rvFollowing.isVisible = false
                                        binding.ProgressBarScroll.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.rvFollowing.isVisible = false
                            binding.ProgressBarScroll.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                           if (loaderFlag){
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


    //    Other User Following List

    private fun observeOtherUserFollowersResponse() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._othersFollowersListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }

                                    if (response.data.result.docs.size > 0){
                                        binding.rvFollowing.isVisible = true
                                        binding.NotFound.isVisible = false

                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.totalPages
                                        page = response.data.result.page
                                        setFollowerAdaptor()

                                    }else{
                                        binding.NotFound.isVisible = true
                                        binding.rvFollowing.isVisible = false
                                        binding.ProgressBarScroll.isVisible = false
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.rvFollowing.isVisible = false
                            binding.ProgressBarScroll.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            if (loaderFlag){
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


    private fun setFollowerAdaptor() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        followerAdapter = FollowerAdapter(requireContext(), data, userProfileClick,this)
        binding.rvFollowing.adapter = followerAdapter
    }

    override fun followUnfollow(
        _id: String,
        position: Int,
        followingData: ArrayList<FollowingUserListFollowing>
    ) {

    }

    override fun followUnfollowTwo(
        _id: String,
        position: Int,
        followingData: ArrayList<FollowersListDocs>
    ) {
        followUnfollow.
        followUnfollowMainTwo(_id,position,followingData,followerAdapter)
    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            page= 1
            limit =10
            dataLoadFlag = false
            if(userType == "otherUser"){
                viewModel.othersFollowersListApi(token,userId,s.toString(),page, limit)
            }else{
                viewModel.followersListApi(token,s.toString(),page, limit)
            }
        }

    }



}