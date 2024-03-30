package com.callisdairy.Vendor.Fragmnets

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.databinding.FragmentVendorProfileBinding
import com.callisdairy.viewModel.VendorCommonViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorProfileFragment : Fragment() {


    private var _binding : FragmentVendorProfileBinding? = null
    private val binding get() = _binding!!

    var token = ""

    private val viewModel: VendorCommonViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentVendorProfileBinding.inflate(layoutInflater, container, false)

        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()


        binding.editProfile.setOnClickListener {
            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag","editProfile")
            startActivity(intent)
        }
        return  binding.root

    }

    override fun onStart() {
        super.onStart()
        viewModel.viewProfileApi(token)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewProfileResponse()
    }



    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    if (response.data.result.name.contains(" ")){
                                        binding.firstName.text = response.data.result.name.split(" ")[0]
                                        binding.lastName.text = response.data.result.name.split(" ")[1]
                                    }else{
                                        binding.firstName.text = response.data.result.name
                                        binding.lastName.text =getString(R.string.na_)
                                    }

                                    binding.etPhone.text = response.data.result.mobileNumber
                                    binding.etMail.text = response.data.result.email
                                    binding.gender.text =response.data.result.gender
                                    binding.dateOfBirth.text =response.data.result.userDob
                                    binding.etAddress.text =response.data.result.address
                                    binding.zipCode.text =response.data.result.zipCode
                                    binding.etCountry.text =response.data.result.country
                                    binding.etState.text =response.data.result.state
                                    binding.etCity.text =response.data.result.city

                                    Glide.with(requireContext()).load(response.data.result.profilePic).placeholder(R.drawable.placeholder_pet).into(binding.showprofileImage)





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



}