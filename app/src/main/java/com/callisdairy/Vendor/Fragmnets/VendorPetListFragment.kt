package com.callisdairy.Vendor.Fragmnets

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.AdapterVendors.VendorPetAdapter
import com.callisdairy.Interface.VendorFilter
import com.callisdairy.Interface.VendorProductClick
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddPetActivity
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Activities.subscription.SubscribePlansUserActivity
import com.callisdairy.UI.dialogs.FilterVendorDialog
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.api.response.MyPetListDocs
import com.callisdairy.databinding.FragmentVendorPetListBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorPetListFragment : Fragment(), VendorProductClick, VendorFilter {



    private var _binding : FragmentVendorPetListBinding? = null
    private val binding get() = _binding!!

    lateinit var homeTv: TextView
    lateinit var productTv: TextView
    lateinit var petTv: TextView
    lateinit var serviceTv: TextView
    lateinit var settingsTv: TextView
    lateinit var addVendors: ImageView


    lateinit var settingsView:View
    lateinit var serviceView:View
    lateinit var petsView:View
    lateinit var productView:View
    lateinit var Homeview:View



    var token = ""
    var publishStatus = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    var dataPet = ArrayList<MyPetListDocs>()



    var totalPetCount = 0
    var remainingPetCount = 0
    var isClickedToAdd =  false

    private val viewModel : ProfileViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVendorPetListBinding.inflate(layoutInflater, container, false)
        findIdAndHandleTab()
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()

        arguments?.getString("petType")?.let { publishStatus = it }


        binding.addPetClick.setSafeOnClickListener {
            isClickedToAdd = true
            viewModel.checkPlanApi(token = token)
        }


        binding.pullToRefresh.setOnRefreshListener {
            dataLoadFlag =  false
            loaderFlag = true
            viewModel.checkPlanApi(token = token)
            binding.ProgressBarScroll.isVisible = false
            binding.DfSearch.setText("")
            binding.pullToRefresh.isRefreshing = false
        }


        binding.DfSearch.addTextChangedListener(textWatcherSearch)


        binding.llFilter.setSafeOnClickListener {
            parentFragmentManager.let { it1 ->
                FilterVendorDialog(this,"Pet").show(
                    it1, "Filter Bottom Sheet Dialog Fragment"
                )
            }
        }



        binding.pageScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.myPetApi(token =token,search="",page=page,limit=limit, fromDate = "", toDate = "", publishStatus = publishStatus)
                }
            }
        })


        return  binding.root
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


    override fun onStart() {
        super.onStart()
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()
        viewModel.checkPlanApi(token = token)
        viewModel.myPetApi(token =token,search="",page=1,limit=10, fromDate = "", toDate = "", publishStatus = publishStatus)

    }

    private fun observeResponseList() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._myPetData.collectLatest{ response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        dataPet.clear()
                                    }
                                    if (response.data.result.docs.size > 0){
                                        binding.petListRecycler.isVisible = true
                                        binding.NotFound.isVisible = false
                                        dataPet.addAll(response.data.result.docs)
                                        pages = response.data.result.pages!!
                                        page = response.data.result.page!!
                                        setAdapter()
                                    }else{
                                        binding.petListRecycler.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.petListRecycler.isVisible = false
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
        binding.petListRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = VendorPetAdapter(requireContext(),dataPet,this)
        binding.petListRecycler.adapter = adapter
    }

    override fun viewProduct(id: String) {
        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "ViewMyPet")
        intent.putExtra("typeUser", "vendor")
        intent.putExtra("id", id)
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
        petTv.setTextColor(Color.parseColor("#6FCFB9"))
        serviceTv.setTextColor(Color.parseColor("#757575"))
        settingsTv.setTextColor(Color.parseColor("#757575"))

        addVendors.isVisible = false

        Homeview.isVisible = false
        productView.isVisible = false
        petsView.isVisible = true
        serviceView.isVisible = false
        settingsView.isVisible = false

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
                viewModel.myPetApi(token =token,search="",page=page,limit=limit, fromDate = "", toDate = "", publishStatus = publishStatus)

            }else{
                viewModel.myPetApi(token =token,search=s.toString(),page=page,limit=limit, fromDate = "", toDate = "", publishStatus = publishStatus)

            }


        }

    }


    override fun vendorFilter(startDate: String, endDate: String, type: String) {
        val startDateSend = DateFormat.filterDateFormat(startDate)!!
        val endDateSend = DateFormat.filterDateFormat(endDate)!!

        loaderFlag = true
        dataLoadFlag = false
        viewModel.myPetApi(token =token,search="",page=page,limit=limit, fromDate = startDateSend, toDate = endDateSend, publishStatus = publishStatus)
    }


    private fun observeResponseCheckLimitOfPostingContent() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._checkPlanData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {




                                    totalPetCount = response.data.result.totalPetCount.toInt()
                                    remainingPetCount = response.data.result.remainingPetCount.toInt()
                                    binding.count.text =  "$remainingPetCount"

                                    if(isClickedToAdd){
                                        if (response.data.result.remainingPetCount.toInt() > 0){
                                            val intent = Intent(context, AddPetActivity::class.java)
                                            intent.putExtra("flag", "Add Pet")
                                            startActivity(intent)
                                        }

                                        if (response.data.result.remainingPetCount.toInt() == 0){
                                            val intent =Intent(requireContext(),
                                                SubscribePlansUserActivity::class.java)
                                            intent.putExtra("Screen","Pet")
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