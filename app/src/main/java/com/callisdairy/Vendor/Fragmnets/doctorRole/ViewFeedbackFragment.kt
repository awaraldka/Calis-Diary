package com.callisdairy.Vendor.Fragmnets.doctorRole

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.callisdairy.ModalClass.feedbackDetails
import com.callisdairy.R
import com.callisdairy.databinding.FragmentViewFeedBackBinding
import com.callisdairy.extension.setSafeOnClickListener

class ViewFeedbackFragment : Fragment() {

    private var _binding: FragmentViewFeedBackBinding? = null
    private val binding get() = _binding!!
    lateinit var backVendor: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentViewFeedBackBinding.inflate(layoutInflater, container, false)
        backVendor = activity?.findViewById(R.id.backVendor)!!

        val myObject = arguments?.getSerializable("myObject") as? feedbackDetails

        backVendor.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

        myObject?.apply {

            binding.firstName.text = name
            binding.ratings.text = Ratings
            binding.date.text = Date
            binding.etMessage.text = feedback


            Glide.with(requireContext()).load(userImage).into(binding.userProfilePicPublic)
            Glide.with(requireContext()).load(petImage).into(binding.petImage)

        }
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }

        })
    }




}