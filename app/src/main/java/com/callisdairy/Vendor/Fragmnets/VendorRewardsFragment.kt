package com.callisdairy.Vendor.Fragmnets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.AdapterVendors.VendorRewardsAdapter
import com.callisdairy.Interface.VendorFilter
import com.callisdairy.UI.dialogs.FilterVendorDialog
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.RewardsResponseDocs
import com.callisdairy.databinding.FragmentVendorRewardsBinding
import com.callisdairy.viewModel.RewardsViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorRewardsFragment : Fragment(), VendorFilter {

    private var _binding: FragmentVendorRewardsBinding? = null
    private val binding get() = _binding!!

    var token= ""
    var data = ArrayList<RewardsResponseDocs>()
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true



    private val viewModel: RewardsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentVendorRewardsBinding.inflate(layoutInflater, container, false)


        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()

        viewModel.rewardsApiVendor(token, "","","",1,10)

        viewModel.viewProfileApi(token)


        binding.pullToRefresh.setOnRefreshListener {
            page = 1
            limit = 20
            data.clear()
            dataLoadFlag = false
            loaderFlag = true

            binding.pullToRefresh.isRefreshing = false

        }

        binding.DfSearch.addTextChangedListener(textWatcherSearch)


        binding.llFilter.setOnClickListener {
            parentFragmentManager.let { it1 ->
                FilterVendorDialog(this,"Pet").show(
                    it1, "Filter Bottom Sheet Dialog Fragment"
                )
            }
        }



        binding.scrollViewEvent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.rewardsApiVendor(token, "","","",page,limit)
                }

            }


        })




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseList()
        observeViewProfileResponse()
    }

    private fun observeResponseList() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._vendorRewardsData.collectLatest { response ->

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
                                        binding.mainLayout.isVisible = true
                                        binding.NotFound.isVisible = false


                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setAdapter()
                                    } else {
                                        binding.mainLayout.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.mainLayout.isVisible = false
                            binding.NotFound.isVisible = true
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false

                            if (loaderFlag) {
                                binding.mainLayout.isVisible = false
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
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        val adapter = VendorRewardsAdapter(requireContext(), data)
        binding.list.adapter = adapter
    }

    private fun observeViewProfileResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._profileViewData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            binding.progressBar.isVisible = false
                            if (response.data?.responseCode == 200) {
                                try {
                                    binding.tvTotalReward.isVisible = true
                                    if (response.data.result.points.toInt() > 1) {

                                        binding.tvTotalReward.text =
                                            "${response.data.result.points} POINTS"
                                    } else {
                                        binding.tvTotalReward.text =
                                            "${response.data.result.points} Point"

                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            binding.progressBar.isVisible = true
                        }

                        is Resource.Empty -> {

                        }

                    }

                }
            }
        }
    }


    val textWatcherSearch = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            page = 1
            limit = 10

            dataLoadFlag = false
            loaderFlag = false
            binding.ProgressBarScroll.isVisible = false

            if (s.toString().isEmpty()){
                viewModel.rewardsApiVendor(token, "","","",page,limit)
            }else{
                viewModel.rewardsApiVendor(token, s.toString(),"","",page,limit)
            }


        }

    }

    override fun vendorFilter(startDate: String, endDate: String, type: String) {
        val startDateSend = DateFormat.filterDateFormat(startDate)!!
        val endDateSend = DateFormat.filterDateFormat(endDate)!!
        loaderFlag = true
        dataLoadFlag = false
        viewModel.rewardsApiVendor(token, "",startDateSend,endDateSend,1,10)
    }

}