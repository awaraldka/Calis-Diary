package com.callisdairy.Vendor.Fragmnets.doctorRole

import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.R
import com.callisdairy.UI.Activities.AgoraCalling.Calling
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.FragmentViewAppointmentDetailsBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.DoctorAppointmentViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewAppointmentDetailsFragment : Fragment(), CommonDialogInterface {

    private var _binding: FragmentViewAppointmentDetailsBinding? =  null
    private val binding get() = _binding!!

    lateinit var backVendor:ImageView
    var appointmentId= ""
    var userIdBlock = ""
    var from = ""
    var isAppointment = ""
    var token = ""
    var receiverId = ""
    var callingUserName = ""


    var profilePicChat = ""
    var petPicChat = ""
    var userIdChat = ""
    var nameChat = ""
    var userTypeChat = ""


    private val viewModel : DoctorAppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentViewAppointmentDetailsBinding.inflate(layoutInflater, container, false)
        backVendor = activity?.findViewById(R.id.backVendor)!!

        arguments?.getString("id")?.let { appointmentId = it }
        arguments?.getString("from")?.let { isAppointment = it }
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()


        viewModel.appointmentDetailsApi(token,appointmentId)



        binding.blockUser.setSafeOnClickListener {
            from = "blockUser"
            androidExtension.alertBoxCommon(getString(R.string.block_this_user),requireContext(),this)

        }

        binding.MarkAsDone.setSafeOnClickListener {
            from = "MarkAsDone"
            androidExtension.alertBoxCommon(getString(R.string.complete_appointment_msg),requireContext(),this)

        }

        binding.cancelAppointment.setSafeOnClickListener {
            from = "cancelAppointment"
            androidExtension.alertBoxCommon(getString(R.string.cancel_appointment_msg),requireContext(),this)

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


        binding.stratCall.setSafeOnClickListener {
            val intent = Intent(requireContext(), Calling::class.java)
            intent.putExtra("receiverId",receiverId)
            intent.putExtra("from","sender")
            intent.putExtra("userName",callingUserName)
            startActivity(intent)
        }



        backVendor.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewAppointmentResponse()
        observeBlockUserResponse()
        observeMarkAsDoneResponse()
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


                                    with(response.data.result){

                                        profilePicChat = userId.profilePic
                                        petPicChat = userId.petPic
                                        userIdChat = userId.id
                                        nameChat = userId.userName
                                        userTypeChat = userId.userType



                                        binding.etNameOfPet.text =  petName.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etLandline.text =  landLine.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etFax.text =  faxNumber.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etMail.text =  email.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etVaccinationDate.text =  vaccinationDate.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etReasonForVisit.text =  reasonForVisit.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etmedicalConditions.text =  medicalCondition.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etDosage.text =  dose.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etDiet.text =  diet.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etGender.text =  gender.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etAboutGender.text =  aboutGender.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etHowHere.text =  howHere.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etAppointmentType.text =  consultingType.takeIf { it.isNotBlank() } ?: "NA"
                                        binding.etStatus.text =  appointmentStatus.uppercase().takeIf { it.isNotBlank() } ?: "NA"
                                        receiverId = userId.id
                                        if (appointmentStatus.lowercase() == "approved" ||appointmentStatus.lowercase() == "confirmed" ||appointmentStatus.lowercase() == "completed" ){
                                            binding.etStatus.setTextColor(android.graphics.Color.parseColor("#00FF00"))
                                        }else{
                                            binding.etStatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))

                                        }

                                        if (appointmentStatus.lowercase() == "completed" ){
                                            binding.buttonCancel.isVisible = false
                                        }

                                        binding.etDate.text =  DateFormat.formatDate(appointmentDate)

                                        binding.etTime.text =  DateFormat.convertTo12HourFormat(slot)


                                        userId.apply {
                                            userIdBlock =  id
                                            if (name.contains(" ")){
                                                binding.firstName.text = name.split(" ")[0]
                                                binding.lastName.text = name.split(" ")[1]
                                            }else{
                                                binding.firstName.text = name
                                                binding.lastName.text =getString(R.string.na_)
                                            }
                                            callingUserName = name
                                            binding.etCell.text = mobileNumber
                                            binding.etAddress.text =address
                                            binding.zipCode.text =zipCode
                                            binding.etCountry.text =country
                                            binding.etState.text =state
                                            binding.etCity.text =city

                                            Glide.with(requireContext()).load(petPic).placeholder(R.drawable.placeholder_pet).into(binding.petImage)
                                            Glide.with(requireContext()).load(profilePic).placeholder(R.drawable.placeholder).into(binding.userProfilePicPublic)


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

    override fun commonWork() {
        when (from) {
            "blockUser" -> {
                viewModel.blockUserApi(token,userIdBlock)
            }
            "cancelAppointment" -> {
                viewModel.cancelAppointmentApi(token,appointmentId)
            }
            else -> {
                viewModel.markAsDoneApi(token,appointmentId)
            }
        }
    }


    private fun observeMarkAsDoneResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._markAsDoneData.collectLatest { response ->

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


    private fun observeCancelAppointmentResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._cancelAppointmentData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
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