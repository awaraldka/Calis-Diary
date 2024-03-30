package com.callisdairy.Vendor.Fragmnets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.AdapterVendors.InterestedUserAdapter
import com.callisdairy.Interface.GetLocation
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.InterestedUserDocs
import com.callisdairy.databinding.FragmentIntrestedUserListBinding
import com.callisdairy.viewModel.GoogleLocationApiViewModel
import com.callisdairy.viewModel.VendorCommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InterestedUserListFragment : Fragment(), GetLocation {

    private var _binding: FragmentIntrestedUserListBinding? = null
    private val binding get() = _binding!!

    lateinit var titleVendor: TextView
    lateinit var backVendor: ImageView


    var from = ""
    var productId = ""
    var nameLocation = ""
    var token = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    var data= ArrayList<InterestedUserDocs>()
    private val viewModel: VendorCommonViewModel by viewModels()
    private val viewModelLocation : GoogleLocationApiViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntrestedUserListBinding.inflate(layoutInflater, container, false)

        titleVendor = activity?.findViewById(R.id.titleVendor)!!
        backVendor = activity?.findViewById(R.id.backVendor)!!

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        arguments?.getString("from")?.let { from= it }
        arguments?.getString("productId")?.let { productId = it }

        backVendor.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        if (from.lowercase() == "service" || from == "HomeService"){
            titleVendor.text = getString(R.string.intrested_service)
            data.clear()
            pages = 0
            page = 1
            limit = 10
            viewModel.interestedUserListApi(token,"SERVICE",1,10,"",productId,"")
        }else if(from.lowercase()== "product" || from == "HomeProduct"){
            titleVendor.text = getString(R.string.intrested_products)
            data.clear()
             pages = 0
             page = 1
             limit = 10
            viewModel.interestedUserListApi(token,"PRODUCT",1,10,productId,"","")
        }else{
            titleVendor.text = getString(R.string.intrested_pets)
            data.clear()
            pages = 0
            page = 1
            limit = 10
            viewModel.interestedUserListApi(token,"PET",1,10,"","",productId)
        }




        binding.swipeRefresh.setOnRefreshListener {
            dataLoadFlag =  false
            loaderFlag = true
            binding.ProgressBarScroll.isVisible = false
            if (from.lowercase()== "service"){
                viewModel.interestedUserListApi(token,"SERVICE",1,10,"",productId,"")
            }else if(from.lowercase() == "product"){
                viewModel.interestedUserListApi(token,"PRODUCT",1,10,productId,"","")
            }else{
                viewModel.interestedUserListApi(token,"PET",1,10,"","",productId)
            }

            binding.swipeRefresh.isRefreshing = false
        }


        binding.scrollPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    if (from.lowercase() == "service"){
                        viewModel.interestedUserListApi(token,"SERVICE",page, limit,"",productId,"")
                    }else if(from.lowercase() == "product"){
                        viewModel.interestedUserListApi(token,"PRODUCT",page, limit,productId,"","")
                    }else{
                        viewModel.interestedUserListApi(token,"PET",page, limit,"","",productId)
                    }

                }
            }
        })




        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })

        observeResponseList()
        observeLatLngResponse()
    }


    private fun observeResponseList() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._intrestedUserData.collectLatest{ response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }
                                    if (response.data.result!!.docs!!.size > 0){
                                        binding.InterestedRecycler.isVisible = true
                                        binding.NotFound.isVisible = false
                                        data.addAll(response.data.result.docs!!)
                                        pages = response.data.result.pages!!
                                        page = response.data.result.page!!
                                        setAdapter()
                                    }else{
                                        binding.InterestedRecycler.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.InterestedRecycler.isVisible = false
                            binding.NotFound.isVisible = true
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            if (loaderFlag){
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

    private fun setAdapter() {
        binding.InterestedRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = InterestedUserAdapter(requireContext(),data, from, this)
        binding.InterestedRecycler.adapter = adapter
    }

    override fun getLocation(name: String) {
        nameLocation =name
        viewModelLocation.getLatLng(name,SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.GMKEY).toString())
    }


    private fun observeLatLngResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelLocation._latLngStateFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {
                                val latitude = response.data?.results?.get(0)?.geometry?.location?.lat!!
                                val longitude = response.data.results[0].geometry.location.lng
                                openMap(longitude.toString(),latitude.toString())


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
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

    private fun openMap(desLong: String, desLat: String) {
        val latitude = desLat// Destination latitude
        val longitude = desLong // Destination longitude
        val label = nameLocation // Destination label

        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps") // Use Google Maps app
        startActivity(intent)

    }

}