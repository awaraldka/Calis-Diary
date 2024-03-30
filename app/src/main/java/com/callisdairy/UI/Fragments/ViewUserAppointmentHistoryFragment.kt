package com.callisdairy.UI.Fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.FragmentViewUserAppoinmentHistoryBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.DoctorAppointmentViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class ViewUserAppointmentHistoryFragment : Fragment(), CommonDialogInterface {

    private val viewModel : DoctorAppointmentViewModel by viewModels()


    private var _binding: FragmentViewUserAppoinmentHistoryBinding? =  null
    private val binding get() = _binding!!

    var appointmentId= ""
    var userIdBlock = ""
    var doctorId = ""
    var from = ""
    var token = ""
    lateinit var backTitle:ImageView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentViewUserAppoinmentHistoryBinding.inflate(layoutInflater, container, false)
        backTitle = activity?.findViewById(R.id.backTitle)!!

        arguments?.getString("id")?.let { appointmentId = it }
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        binding.ownDetailsClick.setOnClickListener {
            binding.ownDetails.isVisible = true
            binding.doctorDetails.isVisible = false

            binding.ownDetailsClick.setBackgroundResource(R.drawable.main_button_background)
            binding.docotorclick.setBackgroundResource(R.drawable.border_background)
            binding.appointmentDetailtext.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.docText.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))

        }

        binding.docotorclick.setOnClickListener {
            binding.doctorDetails.isVisible = true
            binding.ownDetails.isVisible = false

            binding.ownDetailsClick.setBackgroundResource(R.drawable.border_background)
            binding.docotorclick.setBackgroundResource(R.drawable.main_button_background)
            binding.appointmentDetailtext.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
            binding.docText.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

        }

        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        binding.cancelAppointment.setSafeOnClickListener {
            androidExtension.alertBoxCommon(getString(R.string.cancel_appointment_msg),requireContext(),this)
        }



        binding.giveFeedBack.setSafeOnClickListener {
            if (binding.ratingBar.numStars.toString().isEmpty() || binding.etDescription.text.isEmpty()){
                androidExtension.alertBox("Please provide rating",requireContext())
            }else{

                viewModel.addFeedBackApi(token=token,appointmentId =appointmentId,doctorId= doctorId,title=appointmentId,
                    rating = binding.ratingBar.rating.toDouble(), message = binding.etDescription.text.toString())
            }
        }


        viewModel.appointmentDetailsApi(token,appointmentId)



        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewAppointmentResponse()
        observeAddFeedBackResponse()
        observeCancelAppointmentResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }

        })

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewAppointmentResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._appointmentDetailsData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            binding.retailerCustomer.isVisible = true
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    val inputFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
                                    val outputFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

                                    with(response.data.result){
                                        binding.etNameOfPet.text =  petName
                                        binding.etLandline.text =  landLine
                                        binding.etFax.text =  faxNumber
                                        binding.etMail.text =  email
                                        binding.etVaccinationDate.text =  vaccinationDate
                                        binding.etReasonForVisit.text =  reasonForVisit
                                        binding.etmedicalConditions.text =  medicalCondition
                                        binding.etDosage.text =  dose
                                        binding.etDiet.text =  diet
                                        binding.etAppointmentType.text =  consultingType
                                        binding.etStatus.text =  appointmentStatus.uppercase()


                                        binding.feedbackAdd.isVisible = appointmentStatus.lowercase() == "completed"
                                        binding.cancelAppointment.isVisible = appointmentStatus.lowercase() == "confirmed"


                                        if (appointmentStatus.lowercase() == "approved" ||appointmentStatus.lowercase() == "confirmed" ||appointmentStatus.lowercase() == "completed" ){
                                            binding.etStatus.setTextColor(android.graphics.Color.parseColor("#00FF00"))
                                        }else{
                                            binding.etStatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))

                                        }

                                        binding.etDate.text =  DateFormat.formatDate(appointmentDate)
                                        val time = inputFormat.parse(slot)
                                        val time12Hour = time?.let {
                                            outputFormat.format(it)
                                        }
                                        binding.etTime.text =  time12Hour


                                        userId.apply {
                                            userIdBlock =  id
                                            if (name.contains(" ")){
                                                binding.firstName.text = name.split(" ")[0]
                                                binding.lastName.text = name.split(" ")[1]
                                            }else{
                                                binding.firstName.text = name
                                                binding.lastName.text =getString(R.string.na_)
                                            }

                                            binding.etCell.text = mobileNumber
                                            binding.etAddress.text =address
                                            binding.zipCode.text =zipCode
                                            binding.etCountry.text =country
                                            binding.etState.text =state
                                            binding.etCity.text =city

                                            Glide.with(requireContext()).load(petPic).placeholder(R.drawable.placeholder_pet).into(binding.petImage)
                                            Glide.with(requireContext()).load(profilePic).placeholder(
                                                R.drawable.placeholder).into(binding.userProfilePicPublic)


                                        }

                                        if (isRated){
                                            binding.giveFeedBack.isVisible = false

                                            binding.etDescription.setText(feedback.message)

                                            binding.ratingBar.rating = feedback.rating.toFloat()
                                            binding.ratingBar.isEnabled = false
                                            binding.etDescription.isEnabled = false

                                        }

                                    }



                                    with(response.data.result.docId) {




                                        if (name.contains(" ")) {
                                            binding.firstNamedoctorDetails.text = name.split(" ")[0]
                                            binding.lastNamedoctorDetails.text = name.split(" ")[1]
                                        } else {
                                            binding.firstNamedoctorDetails.text = name
                                            binding.lastNamedoctorDetails.text = getString(R.string.na_)
                                        }

                                        doctorId = id

                                        binding.gender.text = gender
                                        binding.etAddressdoctorDetails.text = address
                                        binding.zipCodedoctorDetails.text = zipCode
                                        binding.etCountrydoctorDetails.text = country
                                        binding.etStatedoctorDetails.text = state
                                        binding.etCitydoctorDetails.text = city
                                        binding.middleName.text = docIdForDoctor.middleName
                                        binding.primaryLanguageName.text = docIdForDoctor.primarySpokenLanguage
                                        binding.etSpecialization.text =
                                            docIdForDoctor.specialization
                                        binding.etPractice.text = "${docIdForDoctor.experience} years"



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
                                            val dayObject = docIdForDoctor.clinicHours.find { it.day.equals(dayAbbreviation, ignoreCase = true) }

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


                                        Glide.with(requireContext()).load(profilePic)
                                            .placeholder(R.drawable.placeholder_pet)
                                            .into(binding.showprofileImage)



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



    private fun observeAddFeedBackResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addFeedBackData.collectLatest { response ->

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

    override fun commonWork() {
        viewModel.cancelAppointmentApi(token= token,id =appointmentId)
    }

    private fun observeCancelAppointmentResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._cancelAppointmentData.collectLatest { response ->

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


}