package com.callisdairy.Vendor.Fragmnets

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.AdapterVendors.VendorServiceListAdapter
import com.callisdairy.Interface.VendorFilter
import com.callisdairy.Interface.VendorProductClick
import com.callisdairy.R
import com.callisdairy.UI.Activities.subscription.SubscribePlansUserActivity
import com.callisdairy.UI.dialogs.FilterVendorDialog
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.api.response.ProductsListDocs
import com.callisdairy.databinding.FragmentVendorServiceListBinding
import com.callisdairy.viewModel.VendorCommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorServiceListFragment : Fragment(), VendorProductClick, VendorFilter {


    private var _binding : FragmentVendorServiceListBinding? = null
    private val binding get() = _binding!!
    var serviceType = ""

    var token = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    var data = ArrayList<ProductsListDocs>()


    lateinit var addVendors: ImageView
    lateinit var homeTv: TextView
    lateinit var productTv: TextView
    lateinit var petTv: TextView
    lateinit var serviceTv: TextView
    lateinit var settingsTv: TextView


    lateinit var settingsView:View
    lateinit var serviceView:View
    lateinit var petsView:View
    lateinit var productView:View
    lateinit var Homeview:View

    var totalServiceCount = 0
    var remainingServiceCount = 0
    var isClickedToAdd =  false

    private val viewModel: VendorCommonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentVendorServiceListBinding.inflate(layoutInflater, container, false)
        findIdAndHandleTab()
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()
        arguments?.getString("service")?.let { serviceType = it }

        binding.addServiceClick.setOnClickListener {
            viewModel.checkPlanApi(token = token)
        }


        binding.DfSearch.addTextChangedListener(textWatcherSearch)

        binding.swipeRefresh.setOnRefreshListener {
            dataLoadFlag =  false
            loaderFlag = true
            binding.ProgressBarScroll.isVisible = false
            binding.DfSearch.setText("")
            binding.swipeRefresh.isRefreshing = false
        }


        binding.pageScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                   callApis("")
                }
            }
        })


        binding.llFilter.setOnClickListener {
            parentFragmentManager.let { it1 ->
                FilterVendorDialog(this,"").show(
                    it1, "Filter Bottom Sheet Dialog Fragment"
                )
            }
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        page = 1
        limit = 10
        callApis("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseCheckLimitOfPostingContent()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                }
            })
        observeResponseList()
    }

    private fun observeResponseList() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._vieListServiceData.collectLatest{ response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }
                                    if (response.data.result.docs.size > 0){
                                        binding.serviceListRecycler.isVisible = true
                                        binding.NotFound.isVisible = false
                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.pages!!
                                        page = response.data.result.page!!
                                        setAdapter()
                                    }else{
                                        binding.serviceListRecycler.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.serviceListRecycler.isVisible = false
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
        binding.serviceListRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = VendorServiceListAdapter(requireContext(),data,this)
        binding.serviceListRecycler.adapter = adapter
    }

    override fun viewProduct(id: String) {
        val intent = Intent(requireContext(), CommonContainerActivity::class.java)
        intent.putExtra("flag","viewProduct")
        intent.putExtra("from","Service")
        intent.putExtra("id",id)
        startActivity(intent)
    }


    private fun findIdAndHandleTab() {


        addVendors = activity?.findViewById(R.id.addVendors)!!

        homeTv = activity?.findViewById(R.id.homeTv)!!
        productTv = activity?.findViewById(R.id.productTv)!!
        petTv = activity?.findViewById(R.id.petTv)!!
        serviceTv = activity?.findViewById(R.id.serviceTv)!!
        settingsTv = activity?.findViewById(R.id.settingsTv)!!

        settingsView = activity?.findViewById(R.id.settingsView)!!
        serviceView = activity?.findViewById(R.id.serviceView)!!
        petsView = activity?.findViewById(R.id.petsView)!!
        productView = activity?.findViewById(R.id.productView)!!
        Homeview = activity?.findViewById(R.id.Homeview)!!



        homeTv.setTextColor(Color.parseColor("#757575"))
        productTv.setTextColor(Color.parseColor("#757575"))
        petTv.setTextColor(Color.parseColor("#757575"))
        serviceTv.setTextColor(Color.parseColor("#6FCFB9"))
        settingsTv.setTextColor(Color.parseColor("#757575"))

        addVendors.isVisible = false

        Homeview.isVisible = false
        productView.isVisible = false
        petsView.isVisible = false
        serviceView.isVisible = true
        settingsView.isVisible = false

    }

    private fun callApis(search:String) {
        when (serviceType) {
            "Total Services" -> {
                viewModel.serviceListApi(token, search, "", "", page, limit, "")
            }
            "Pending Services" -> {
                viewModel.serviceListApi(token, search, "", "", page, limit, "PENDING")
            }

            "Rejected Services" -> {
                viewModel.serviceListApi(token, search, "", "", page, limit, "REJECTED")
            }

            else -> {
                viewModel.serviceListApi(token, search, "", "", page, limit, "APPROVED")
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
            data.clear()
            dataLoadFlag = false
            loaderFlag = false
            binding.ProgressBarScroll.isVisible = false
            callApis(s.toString())


        }

    }

    override fun vendorFilter(startDate: String, endDate: String, type: String) {
        val startDateSend = DateFormat.filterDateFormat(startDate)!!
        val endDateSend = DateFormat.filterDateFormat(endDate)!!
        loaderFlag = true
        dataLoadFlag = false
        when (type) {
            "Rejected" -> {
                viewModel.serviceListApi(token, "", startDateSend, endDateSend, 1, 10, "REJECTED")
            }
            "Approved" -> {
                viewModel.serviceListApi(token, "", startDateSend, endDateSend, 1, 10, "APPROVED")
            }
            "Pending" -> {
                viewModel.serviceListApi(token, "", startDateSend, endDateSend, 1, 10, "PENDING")
            }
            else -> {
                viewModel.serviceListApi(token, "", startDateSend, endDateSend, 1, 10, "")
            }
        }
    }


    private fun observeResponseCheckLimitOfPostingContent() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._checkPlanData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {



                                    totalServiceCount = response.data.result.totalServiceCount.toInt()
                                    remainingServiceCount = response.data.result.remainingServiceCount.toInt()
                                    binding.count.text =  "$remainingServiceCount"


                                    if (isClickedToAdd){
                                        if (response.data.result.remainingServiceCount.toInt()> 0){

                                            val intent = Intent(requireContext(),CommonContainerActivity::class.java)
                                            intent.putExtra("flag","addService")
                                            intent.putExtra("from","add")
                                            startActivity(intent)
                                        }

                                        if (response.data.result.remainingServiceCount.toInt() == 0){
                                            val intent =Intent(requireContext(),
                                                SubscribePlansUserActivity::class.java)
                                            intent.putExtra("Screen","Service")
                                            startActivity(intent)
                                        }

                                        isClickedToAdd = false

                                    }








                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
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


}