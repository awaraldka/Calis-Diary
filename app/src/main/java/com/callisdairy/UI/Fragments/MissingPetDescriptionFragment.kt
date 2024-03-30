package com.callisdairy.UI.Fragments

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
import com.callisdairy.Adapter.ImageSliderAdaptor
import com.callisdairy.ModalClass.descriptionImage
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.databinding.FragmentMissingPetDescriptionBinding
import com.callisdairy.viewModel.ViewMissingPetViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MissingPetDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentMissingPetDescriptionBinding

    lateinit var imageAdaptor: ImageSliderAdaptor
    var data: ArrayList<descriptionImage> = ArrayList()

    var flag = ""
    var petId = ""
    var imageUrl = ""
    var from = ""

    var receiverId = ""
    var petImage = ""
    var userImage = ""
    var userName = ""





    lateinit var backTitle: ImageView


    private val viewModel: ViewMissingPetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentMissingPetDescriptionBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        backTitle = activity?.findViewById(R.id.backTitle)!!

        arguments?.getString("flag")?.let {
            flag = it
        }
        arguments?.getString("petId")?.let {
            petId = it
        }
        arguments?.getString("from")?.let {
            from = it
        }

        val token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)!!


        viewModel.viewMissingPetApi(token, petId)



        when (from) {
            "myMissingPet" -> {

                binding.messageButton.isVisible = false

            }
            else -> {
                binding.messageButton.isVisible = true
            }
        }


        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            fragmentManager?.popBackStack()
        }

        binding.shareButton.setSafeOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, imageUrl)
                putExtra(Intent.EXTRA_TEXT, "Cali's Diary")
                putExtra(Intent.EXTRA_TEXT, "https://calisdiary.com/")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }


        binding.messageButton.setSafeOnClickListener {
            val intent = Intent(requireContext(), OneToOneChatActivity::class.java)
            intent.putExtra("receiverId", receiverId)
            intent.putExtra("petImage",petImage)
            intent.putExtra("userImage", userImage)
            intent.putExtra("userName",userName)
            intent.putExtra("from", "chat")
            startActivity(intent)
        }

        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseViewMissingPet()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    activity?.finishAfterTransition()
                    fragmentManager?.popBackStack()

                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeResponseViewMissingPet() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._viewMissingPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    binding.indicator.isVisible = response.data.result.petImage.size > 1

                                    setImageAdapter(response.data.result.petImage)

                                    if (response.data.result.petImage.size > 0){
                                        imageUrl = response.data.result.petImage[0]
                                    }
                                     receiverId = response.data.result.userId._id
                                    petImage = response.data.result.userId.petPic
                                     userImage = response.data.result.userId.profilePic
                                     userName = response.data.result.userId.userName


                                    if (response.data.result.lastSeen.isNotEmpty()){
                                        binding.lastSeen.text =  DateFormat.covertTimeOtherFormat(response.data.result.lastSeen)
                                    }else{
                                        binding.lastSeen.text = "NA"
                                    }

                                    if (response.data.result.type.isNotEmpty()){
                                        binding.type.text =  response.data.result.type

                                    }else{
                                        binding.type.text = "NA"
                                    }

                                    if (response.data.result.gender.isNotEmpty()){
                                        binding.gender.text =  response.data.result.gender

                                    }else{
                                        binding.gender.text = "NA"
                                    }

                                    if (response.data.result.color.isNotEmpty()){
                                        binding.color.text =  response.data.result.color

                                    }else{
                                        binding.color.text = "NA"
                                    }

                                    if (response.data.result.breed.isNotEmpty()){
                                        binding.breed.text =  response.data.result.breed

                                    }else{
                                        binding.breed.text = "NA"
                                    }

                                    if (response.data.result.peculiarity.isNotEmpty()){
                                        binding.pecuiarity.text =  response.data.result.peculiarity

                                    }else{
                                        binding.pecuiarity.text = "NA"
                                    }

                                    if (response.data.result.userDetails.name.isNotEmpty()){
                                        binding.name.text =  response.data.result.userDetails.name

                                    }else{
                                        binding.name.text = "NA"
                                    }

                                    if (response.data.result.userDetails.address.isNotEmpty()){
                                        binding.address.text =  response.data.result.userDetails.address

                                    }else{
                                        binding.address.text = "NA"
                                    }


                                    if (response.data.result.userDetails.email.isNotEmpty()){
                                        binding.mail.text =  response.data.result.userDetails.email

                                    }else{
                                        binding.mail.text = "NA"
                                    }


                                    if (response.data.result.userDetails.mobileNumber.isNotEmpty()){
                                        binding.mobileNumber.text =  response.data.result.userDetails.mobileNumber

                                    }else{
                                        binding.mobileNumber.text = "NA"
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



    private fun setImageAdapter(petImage: ArrayList<String>) {
        imageAdaptor = ImageSliderAdaptor(requireContext(), petImage)
        binding.storeViewpager.adapter = imageAdaptor
        binding.indicator.setViewPager(binding.storeViewpager)

    }

}