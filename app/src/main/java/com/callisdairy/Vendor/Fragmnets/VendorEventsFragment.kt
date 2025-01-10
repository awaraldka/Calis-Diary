package com.callisdairy.Vendor.Fragmnets

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.EventPostAdapter
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.ListEventDocs
import com.callisdairy.databinding.FragmentVendorEventsBinding
import com.callisdairy.viewModel.EventListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorEventsFragment : Fragment() {

    private var _binding: FragmentVendorEventsBinding? = null
    private val binding get() = _binding!!


    private lateinit var eventPostAdapter: EventPostAdapter


    var data = ArrayList<ListEventDocs>()
    var pages = 0
    var page = 1
    var limit = 20
    var dataLoadFlag = false
    var loaderFlag = true
    var latitude = 0.0
    var longitude = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null

    var token = ""


    private val viewModel: EventListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVendorEventsBinding.inflate(layoutInflater, container, false)

        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }


        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        getLocation()




        binding.MyEventsList.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.vendorContainer, VendorMyEventsFragment()).addToBackStack(null)
                .commit()
        }





        binding.DFsearch.addTextChangedListener(textWatcher)


        binding.pullToRefresh.setOnRefreshListener {
            page = 1
            limit = 20
            data.clear()
            dataLoadFlag = false
            loaderFlag = true
            binding.DFsearch.setText("")

            binding.pullToRefresh.isRefreshing = false

        }




        binding.scrollViewEvent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.listEventApi(
                        token,
                        "",
                        page,
                        limit,
                        "",
                        "",
                        "",
                        "",
                        latitude,
                        longitude,
                        ""
                    )
                }

            }


        })



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseProductList()

    }


    private fun getLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->

                if (task.isSuccessful && task.result != null) {
                    try {
                        val lastLocation = task.result
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation).longitude

                        if (data.isEmpty()){
                            viewModel.listEventApi(
                                token,
                                "",
                                1,
                                10,
                                "",
                                "",
                                "",
                                "",
                                latitude,
                                longitude,
                            ""
                            )
                        }


                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun observeResponseProductList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._listEventData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }



                                    if (response.data.result.docs.size > 0) {
                                        binding.postRecycler.isVisible = true
                                        binding.NotFound.isVisible = false
                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        PostAdapter()
                                    } else {
                                        binding.postRecycler.isVisible = false
                                        binding.NotFound.isVisible = true
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.postRecycler.isVisible = false
                            binding.NotFound.isVisible = true
                            response.message?.let { message ->
//                            androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            if (loaderFlag) {
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


    private fun PostAdapter() {
        binding.postRecycler.layoutManager = LinearLayoutManager(requireContext())
        eventPostAdapter = EventPostAdapter(requireContext(), data)
        binding.postRecycler.adapter = eventPostAdapter
    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            page = 1
            limit = 20
            data.clear()
            dataLoadFlag = false
            loaderFlag = false
            viewModel.listEventApi(
                token,
                s.toString(),
                page,
                limit,
                "",
                "",
                "",
                "",
                latitude,
                longitude,
                ""
            )

        }

    }


}