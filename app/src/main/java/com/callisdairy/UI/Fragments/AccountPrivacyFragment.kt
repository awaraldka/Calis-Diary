package com.callisdairy.UI.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.FragmentAccountPrivacyBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.RewardsViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountPrivacyFragment : Fragment() {

    private var _binding: FragmentAccountPrivacyBinding? =  null
    private val binding get() = _binding!!
    lateinit var backTitle: ImageView



    private val viewModel : RewardsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentAccountPrivacyBinding.inflate(layoutInflater, container, false)
        backTitle = activity?.findViewById(R.id.backTitle)!!


        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        val token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()


        viewModel.viewProfileApi(token)



            binding.switchClick.setSafeOnClickListener{
            viewModel.makePrivatePublicAccountApi(token)
        }





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseAccountType()
        observeCheckAccountTypeResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAfterTransition()
                fragmentManager?.popBackStack()
            }
        })
    }





    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseAccountType() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._privacyTypeData.collectLatest { response ->

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
                            Progresss.stop()
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

    private fun observeCheckAccountTypeResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    binding.switchClick.isChecked = response.data.result.privacyType.lowercase() == "private"
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




}