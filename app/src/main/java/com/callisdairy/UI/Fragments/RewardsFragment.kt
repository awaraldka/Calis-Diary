package com.callisdairy.UI.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.databinding.FragmentRewardsBinding
import com.callisdairy.viewModel.RewardsViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RewardsFragment : Fragment() {

    private lateinit var binding: FragmentRewardsBinding
    var flags = ""

    //   Tool Bar

    lateinit var back: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView

    private val viewModel: RewardsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRewardsBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        arguments?.getString("flag")?.let {
            flags = it
        }
        allIds()
        toolBarWithBottomTab()

        back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.shareButton.setOnClickListener {
            activity?.supportFragmentManager?.let {
                ShareRewardsFragment().show(
                    it, "Bottom Sheet"
                )
            }
        }


        val token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        viewModel.rewardsApi(token)
        viewModel.viewProfileApi(token)


        return view
    }


    private fun allIds() {
        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!

    }

    private fun toolBarWithBottomTab() {

        when (flags) {

            "RewardSideMenu" -> {
                mainTitle.visibility = View.VISIBLE
                chat.visibility = View.VISIBLE
                back.visibility = View.VISIBLE
                search.visibility = View.VISIBLE
            }

        }


        Username.text = getString(R.string.rewards)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseRewards()
        observeViewProfileResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    fm.popBackStack()

                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun observeResponseRewards() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._rewardsData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    if (response.data.result.signUp > 1) {
                                        binding.SignUp.text =
                                            "${response.data.result.signUp} POINTS"
                                    } else {
                                        binding.SignUp.text = "${response.data.result.signUp} POINT"
                                    }

                                    if (response.data.result.login > 1) {
                                        binding.Login.text = "${response.data.result.login} POINTS"
                                    } else {
                                        binding.Login.text = "${response.data.result.login} POINT"
                                    }

                                    if (response.data.result.like > 1) {
                                        binding.Like.text = "${response.data.result.like} POINTS"
                                    } else {
                                        binding.Like.text = "${response.data.result.like} POINT"
                                    }

                                    if (response.data.result.like > 1) {
                                        binding.Share.text = "${response.data.result.share} POINTS"
                                    } else {
                                        binding.Share.text = "${response.data.result.share} POINT"
                                    }

                                    if (response.data.result.post > 1) {
                                        binding.post.text = "${response.data.result.post} POINTS"
                                    } else {
                                        binding.post.text = "${response.data.result.post} POINT"
                                    }

                                    if (response.data.result.post > 1) {
                                        binding.stories.text =
                                            "${response.data.result.story} POINTS"
                                    } else {
                                        binding.stories.text = "${response.data.result.story} POINT"
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

    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            binding.progressBar.isVisible = false
                            if (response.data?.responseCode == 200) {
                                try {
                                    binding.tvTotalReward.isVisible = true
                                    if (response.data.result.points.toInt() > 1) {

                                        binding.tvTotalReward.text =
                                            "${response.data.result.points} POINTS"
                                    } else {
                                        binding.tvTotalReward.text =
                                            "${response.data.result.points} Point"

                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            binding.progressBar.isVisible = true
                        }

                        is Resource.Empty -> {

                        }

                    }

                }
            }
        }
    }


}