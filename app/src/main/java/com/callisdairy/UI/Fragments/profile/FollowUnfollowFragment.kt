package com.callisdairy.UI.Fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.callisdairy.Interface.UserProfileClick
import com.callisdairy.R
import com.callisdairy.databinding.FragmentFollowUnfollowBinding
import com.callisdairy.Adapter.FollowUnfollowAdapter
import com.callisdairy.Adapter.FollowerAdapter
import com.callisdairy.Adapter.FollowingAdapter
import com.callisdairy.Interface.HomeUserProfileView
import com.callisdairy.Interface.ProfileFollowUnfollowMain
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.FollowingUserListFollowing
import com.callisdairy.viewModel.FollowingListViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.FollowersListDocs
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowUnfollowFragment : Fragment(), UserProfileClick, ProfileFollowUnfollowMain  {
    private var _binding: FragmentFollowUnfollowBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: FollowUnfollowAdapter
    var flag = ""
    var token = ""
    var following = ""
    var Followers = ""
    var userType = ""
    var userId = ""


    lateinit var back: ImageView

    private val viewModel: FollowingListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowUnfollowBinding.inflate(layoutInflater, container, false)

        back = activity?.findViewById(R.id.backTitle)!!

        arguments?.getString("flag")?.let {
            flag = it
        }
        arguments?.getString("following")?.let {
            following = it
        }
        arguments?.getString("Followers")?.let {
            Followers = it
        }
        arguments?.getString("userType")?.let {
            userType = it
        }
        arguments?.getString("userId")?.let {
            userId = it
        }
        setTabs()



        back.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        when (flag) {
            "Following" -> {
                binding.ViewPager.currentItem = 1
            }
            "Followers" -> {
                binding.ViewPager.currentItem = 0
            }
        }

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()


        return _binding?.root
    }


    private fun setTabs() {
        binding.TabLayout.addTab(binding.TabLayout.newTab().setText("$Followers Followers"))
        binding.TabLayout.addTab(binding.TabLayout.newTab().setText("$following Following"))
        binding.TabLayout.tabGravity = TabLayout.GRAVITY_FILL


        binding.ViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.TabLayout))

        binding.TabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                println("onTabSelected================>")
                binding.ViewPager.currentItem = tab.position
                if (tab.position == 0) {

                }else{

                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                println("onTabUnselected================>")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                println("onTabUnselected================>")
            }
        })

        adapter = FollowUnfollowAdapter(
            requireContext(),
            childFragmentManager,
            binding.TabLayout.tabCount,
            this,
            this,
            userType,
            userId
        )
        binding.ViewPager.adapter = adapter
    }

    override fun userProfileListener(_id: String, userName: String) {

        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "OtherUsers")
        intent.putExtra("userName", userName)
        intent.putExtra("id", _id)
        startActivity(intent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseFollowUnFollow()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                }
            })
    }


    private fun observeResponseFollowUnFollow() {


        lifecycleScope.launch {
            viewModel._followUnfollowData.collectLatest { response ->

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
                        Progresss.stop()


                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }

        }


    }

    override fun followUnfollowMain(_id: String, position: Int, followingData: ArrayList<FollowingUserListFollowing>, followingAdapter: FollowingAdapter) {
        followingAdapter.notifyItemChanged(position)
        viewModel.followUnfollowAPi(token, _id)
    }

    override fun followUnfollowMainTwo(
        _id: String,
        position: Int,
        followingData: ArrayList<FollowersListDocs>,
        followerAdapter: FollowerAdapter
    ) {
        followerAdapter.notifyItemChanged(position)
        viewModel.followUnfollowAPi(token, _id)
    }
}