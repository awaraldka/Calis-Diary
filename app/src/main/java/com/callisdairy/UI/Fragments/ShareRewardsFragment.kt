package com.callisdairy.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.RewardAdapter
import com.callisdairy.ModalClass.TagPeopleModelClass
import com.callisdairy.databinding.FragmentShareRewardsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ShareRewardsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShareRewardsBinding? = null
    private val binding get() = _binding!!

    var data : ArrayList<TagPeopleModelClass> = ArrayList()
    lateinit var Adapter : RewardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentShareRewardsBinding.inflate(layoutInflater, container, false)
        val view = binding.root



//        data.add(TagPeopleModelClass(R.drawable.img11,R.drawable.img22,"Umair John Cena "))
//        data.add(TagPeopleModelClass(R.drawable.img11,R.drawable.img22,"Umair Siddiqui"))
//        data.add(TagPeopleModelClass(R.drawable.img11,R.drawable.img22,"Umair Siddiqui"))

        binding.shareRecycler.layoutManager = LinearLayoutManager(requireContext())
        Adapter = RewardAdapter(requireContext(),data)
        binding.shareRecycler.adapter = Adapter

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val fm: FragmentManager = requireActivity().supportFragmentManager
                fm.popBackStack()

            }
        })
    }

}