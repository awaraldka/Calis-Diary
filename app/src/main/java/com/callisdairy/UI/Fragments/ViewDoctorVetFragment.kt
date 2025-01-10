package com.callisdairy.UI.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.FragmentViewDoctorVetBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.DoctorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewDoctorVetFragment : Fragment() , CommonDialogInterface {

    private var _binding: FragmentViewDoctorVetBinding? = null
    private val binding get() = _binding!!
    lateinit var backTitle: ImageView

    var blockedUser = false
    var isFollowedUser = false
    private val viewModel: DoctorViewModel by viewModels()

    var id = ""
    var userIdBlock = ""
    var token = ""
    var from = ""


    var profilePicChat = ""
    var userIdChat = ""
    var nameChat = ""
    var userTypeChat = ""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewDoctorVetBinding.inflate(layoutInflater, container, false)
        backTitle = activity?.findViewById(R.id.backTitle)!!

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)
            .toString()
        arguments?.getString("id")?.let { id = it }
        arguments?.getString("from")?.let { from = it }


        viewModel.viewDetailsApi(token, id)


        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


            binding.unBlockUser.setSafeOnClickListener {
                androidExtension.alertBoxCommon("Are you sure you want to unBlock this user?",requireContext(),this)
            }

            binding.blockUser.setSafeOnClickListener {
                androidExtension.alertBoxCommon("Are you sure you want to block this user?",requireContext(),this)

            }

            binding.chatNow.setSafeOnClickListener {
                val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId",userIdChat)
                intent.putExtra("userImage", profilePicChat)
                intent.putExtra("userName", nameChat)
                intent.putExtra("userTypeChat", userTypeChat)
                intent.putExtra("from", "chat")
                startActivity(intent)

            }



        binding.sendRequest.setSafeOnClickListener {
            if (blockedUser){
                androidExtension.alertBox(getString(R.string.user_is_blocked),requireContext())
            }else {
                binding.sendRequest.isVisible = false
                binding.requested.isVisible = true
                viewModel.followUnfollowAPi(token, userIdChat)
            }
        }

        binding.requested.setSafeOnClickListener {
            if (blockedUser){
                androidExtension.alertBox(getString(R.string.user_is_blocked),requireContext())
            }else {

                binding.sendRequest.isVisible = true
                binding.requested.isVisible = false
                viewModel.followUnfollowAPi(token, userIdChat)
            }
        }


        binding.bookAppointment.setOnClickListener {
            if (blockedUser){
                androidExtension.alertBox(getString(R.string.user_is_blocked),requireContext())
            }else {
                val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "Make Appointment")
                intent.putExtra("id", id)
                startActivity(intent)
            }
        }



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewProfileResponse()
        observeBlockUserResponse()
        observeResponseFollowUnFollow()
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                    activity?.finishAfterTransition()
                }

            })


    }


    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._viewDetailsData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    userIdBlock = response.data.result._id

                                    with(response.data.result) {
                                        profilePicChat = doctorVetProfile.userProfileImage.toString()
                                        userIdChat = doctorVetProfile.userId.toString()
                                        nameChat = name
                                        userTypeChat = userType

                                        blockedUser = isBlocked
                                        isFollowedUser = isRequested


                                        if (isRequested){
                                            binding.requested.isVisible = true
                                            binding.sendRequest.isVisible = false
                                        }else{
                                            binding.requested.isVisible = false
                                            binding.sendRequest.isVisible = true
                                        }




                                        binding.fiveStarCount.text = "(${userRatingScores.fiveStars})"
                                        binding.fourStarCount.text = "(${userRatingScores.fourStars})"
                                        binding.threeStarCount.text = "(${userRatingScores.threeStars})"
                                        binding.twoStarCount.text = "(${userRatingScores.twoStars})"
                                        binding.oneStarCount.text = "(${userRatingScores.oneStars})"
                                        binding.ratingAverage.text = averageRating.toString()


                                        binding.firstName.text = doctorVetProfile.firstName?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.middleName.text = doctorVetProfile.middleName?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.lastName.text = doctorVetProfile.lastName?.takeIf { it.isNotBlank() } ?: "NA"

                                        binding.gender.text = gender.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etAddress.text = address.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.zipCode.text = zipCode.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etCountry.text = country.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etState.text = state.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etCity.text = city.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.primaryLanguageName.text = doctorVetProfile.primarySpokenLanguage?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etSpecialization.text = doctorVetProfile.specialization?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etPractice.text = doctorVetProfile.experience?.takeIf { it.isNotBlank() } ?: "NA"



                                        binding.etCollege.text = doctorVetProfile.collegeUniversity?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etLicense.text = doctorVetProfile.license?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etLicenseExpiration.text = doctorVetProfile.licenseExpiry?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etPermit.text = doctorVetProfile.permit?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etDegree.text = doctorVetProfile.degreeType?.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etPermitExpiration.text = doctorVetProfile.permitExpiry?.takeIf { it.isNotBlank() } ?: "NA"


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

                                        Glide.with(requireContext()).load(doctorVetProfile.userProfileImage)
                                            .placeholder(R.drawable.placeholder_pet)
                                            .into(binding.showprofileImage)



                                        if (isBlocked){
                                            binding.blockUser.isVisible = false
                                            binding.unBlockUser.isVisible = true
                                        }else{
                                            binding.blockUser.isVisible = true
                                            binding.unBlockUser.isVisible = false
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
                            Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }
        }
    }

    override fun commonWork() {
        viewModel.blockUserApi(token,userIdBlock)
    }

    private fun observeBlockUserResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._blockUserData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            binding.retailerCustomer.isVisible = true
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBoxFinishFragment(response.data.responseMessage,requireContext(),parentFragmentManager,requireActivity())


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


    private fun observeResponseFollowUnFollow() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._followUnfollowData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    Progresss.stop()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
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