package com.callisdairy.Vendor.Fragmnets.doctorRole

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.AdapterVendors.FeedbackListAdapter
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.UserFeedBackListDocs
import com.callisdairy.databinding.FragmentFeedBackListBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.DoctorAppointmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedBackListFragment : Fragment() {



    private var _binding: FragmentFeedBackListBinding? = null
    private val binding get() = _binding!!

    var token = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true
    lateinit var backVendor:ImageView
    
    var listFeedbackData: List<UserFeedBackListDocs> = listOf()
    

    private val viewModel: DoctorAppointmentViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentFeedBackListBinding.inflate(layoutInflater, container, false)
        backVendor = activity?.findViewById(R.id.backVendor)!!

        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()


        binding.pulltToRefresh.setOnRefreshListener {
            binding.DFsearch.setText("")
            binding.pulltToRefresh.isRefreshing = false

        }


        binding.DFsearch.addTextChangedListener(textWatcher)




        backVendor.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

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
                    viewModel.listFeedBackApi(token = token, search = "", page = page, limit = limit)
                }
            }
        })




        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAppointmentListResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }

        })
    }



    private fun callApi(search:String){
        page = 1
        limit = 10
        dataLoadFlag = false
        loaderFlag = false
        viewModel.listFeedBackApi(token = token, search = search, page = page, limit = limit)

    }


    private fun observeAppointmentListResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._listFeedBackData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        listFeedbackData = emptyList()
                                    }

                                    listFeedbackData = listFeedbackData + response.data.result.docs

                                    if (listFeedbackData.isNotEmpty()) {

                                        binding.NotFound.isVisible = false
                                        binding.feedbackList.isVisible = true

                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setAdapterFeedbackList()
                                    } else {
                                        binding.NotFound.isVisible = true
                                        binding.feedbackList.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.feedbackList.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            if (loaderFlag) {
                                binding.feedbackList.isVisible = false
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


    private fun setAdapterFeedbackList(){
        binding.feedbackList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FeedbackListAdapter(requireContext(), listFeedbackData)
        binding.feedbackList.adapter = adapter
    }






}