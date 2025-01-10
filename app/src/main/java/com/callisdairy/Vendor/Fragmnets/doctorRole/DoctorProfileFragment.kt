package com.callisdairy.Vendor.Fragmnets.doctorRole

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.callisdairy.databinding.FragmentDoctorProfileBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.VendorCommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DoctorProfileFragment : Fragment() {

    private var _binding: FragmentDoctorProfileBinding? =  null
    private val binding get() = _binding!!

    var profile = false

    private val viewModel: VendorCommonViewModel by viewModels()

    var token = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentDoctorProfileBinding.inflate(layoutInflater, container, false)


        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()



        binding.editProfile.setSafeOnClickListener {
            val intent = Intent(requireContext(), CommonContainerActivity::class.java)
            intent.putExtra("flag","EditDoctorProfileFragment")
            startActivity(intent)

        }



        return binding.root

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
                            binding.retailerCustomer.isVisible = true
                            Progresss.stop()
                            profile  = true
                            if (response.data?.responseCode == 200) {
                                try {


                                    with(response.data.result){

                                        binding.firstName.text = doctorVetProfile.firstName
                                        binding.lastName.text = doctorVetProfile.lastName



                                        binding.etPhone.text = doctorVetProfile.phoneNumber
                                        binding.gender.text =gender
                                        binding.dob.text =userDob
                                        binding.etAddress.text =doctorVetProfile.clinicAddress
                                        binding.zipCode.text =zipCode
                                        binding.etCountry.text =country
                                        binding.etState.text =state
                                        binding.etCity.text =city
                                        binding.middleName.text =doctorVetProfile.middleName
                                        binding.primaryLanguageName.text =doctorVetProfile.primarySpokenLanguage
                                        binding.etSpecialization.text =doctorVetProfile.specialization
                                        binding.etPractice.text =doctorVetProfile.experience
                                        binding.etCollege.text = doctorVetProfile.collegeUniversity
                                        binding.etLicense.text = doctorVetProfile.license
                                        binding.etLicenseExpiration.text = doctorVetProfile.licenseExpiry
                                        binding.etPermit.text = doctorVetProfile.permit
                                        binding.etPermitExpiration.text = doctorVetProfile.permitExpiry
                                        binding.etDegree.text = doctorVetProfile.degreeType
                                        Glide.with(requireContext()).load(doctorVetProfile.userProfileImage).placeholder(R.drawable.placeholder).into(binding.showprofileImage)

                                        val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

                                        val dayAbbreviationMap = mapOf(
                                            "Mon" to "Monday",
                                            "Tue" to "Tuesday",
                                            "Wed" to "Wednesday",
                                            "Thu" to "Thursday",
                                            "Fri" to "Friday",
                                            "Sat" to "Saturday",
                                            "Sun" to "Sunday"
                                        )

                                        val textViewMap = mapOf(
                                            "Monday" to binding.etMonday,
                                            "Tuesday" to binding.etTuesday,
                                            "Wednesday" to binding.etWednesday,
                                            "Thursday" to binding.etThursday,
                                            "Friday" to binding.etFriday,
                                            "Saturday" to binding.etSaturday,
                                            "Sunday" to binding.etSunday
                                        )

                                        for (day in daysOfWeek) {
                                            val dayAbbreviation = dayAbbreviationMap.entries.find { it.value.equals(day, ignoreCase = true) }?.key
                                            val dayObject = doctorVetProfile.clinicHours.find { it.day.equals(dayAbbreviation, ignoreCase = true) }

                                            val textView = textViewMap[day]
                                            if (dayObject != null) {
                                                if (dayObject.isSelected) {
                                                    textView?.text = "${dayObject.openTime} To ${dayObject.closeTime}"
                                                } else {
                                                    textView?.text = "Not Available"
                                                }
                                            }
                                        }

                                        for ((day, textView) in textViewMap) {
                                            if (textView.text.isNullOrEmpty() && textView.text != "Not Available") {
                                                textView.text = "Not Available"
                                            }
                                        }
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

                            if (!profile){
                                binding.retailerCustomer.isVisible
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








}