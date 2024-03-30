package com.callisdairy.Vendor.Fragmnets.doctorRole

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.AppointmentHistoryListAdapter
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.Appointment
import com.callisdairy.databinding.FragmentAppointmentHistoryBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.DoctorAppointmentViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppointmentHistoryFragment : Fragment() {
    private var _binding: FragmentAppointmentHistoryBinding? =  null
    private val binding get() = _binding!!

    var appointmentData: List<Appointment> = listOf()
    lateinit var backTitle:ImageView
    private val viewModel: DoctorAppointmentViewModel by viewModels()

    var token = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentAppointmentHistoryBinding.inflate(layoutInflater, container, false)
        backTitle = activity?.findViewById(R.id.backTitle)!!
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()
        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }
        binding.pullToRefresh.setOnRefreshListener {
            binding.pullToRefresh.isRefreshing = false
            binding.DFsearch.setText("")
        }

        binding.DFsearch.addTextChangedListener(textWatcher)

        callApi("")

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                loaderFlag = false
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.appointmentListVirtualApi(token, "", page, limit, "", "")
                }
            }
        })




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAppointmentListResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })
    }



    private fun observeAppointmentListResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._appointmentListVirtualData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        appointmentData = emptyList()
                                    }

                                    appointmentData = appointmentData + response.data.result.docs

                                    if (appointmentData.isNotEmpty()) {

                                        binding.NotFound.isVisible = false
                                        binding.appointmentHistory.isVisible = true

                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setAdapterClinic()
                                    } else {
                                        binding.NotFound.isVisible = true
                                        binding.appointmentHistory.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.appointmentHistory.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            if (loaderFlag) {
                                binding.appointmentHistory.isVisible = false
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






    private fun setAdapterClinic() {
        binding.appointmentHistory.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AppointmentHistoryListAdapter(requireContext(), appointmentData)
        binding.appointmentHistory.adapter = adapter
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().isNotEmpty()){
                callApi(s.toString())
            }else{
                callApi("")
            }
        }
    }


    fun callApi(search:String){

         page = 1
         limit = 10
         dataLoadFlag = false
        loaderFlag = false
        viewModel.appointmentListVirtualApi(token, search, page, limit, "", "")

    }

}