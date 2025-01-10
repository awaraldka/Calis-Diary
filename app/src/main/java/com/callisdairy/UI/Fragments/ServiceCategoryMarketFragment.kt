package com.callisdairy.UI.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.ProductListSubCategory
import com.callisdairy.Adapter.ServiceCategoryAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Adapter.serviceListAdapter
import com.callisdairy.Interface.InterestedClick
import com.callisdairy.Interface.LikeUnlikeService
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.ServiceClick
import com.callisdairy.Interface.SubCategoryClick
import com.callisdairy.Interface.serviceView
import com.callisdairy.R
import com.callisdairy.UI.Activities.ServiceDescriptionActivity
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.ListCategoryDocs
import com.callisdairy.api.response.ServiceListDocs
import com.callisdairy.api.response.SubCategoryDocs
import com.callisdairy.databinding.FragmentServiceCategoryMarketBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.MarketServiceAllViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServiceCategoryMarketFragment : Fragment(), serviceView, ServiceClick, SubCategoryClick,
    LikeUnlikeService,InterestedClick,PopupItemClickListener {
    private var _binding: FragmentServiceCategoryMarketBinding? =  null
    private val binding get() = _binding!!
    lateinit var serviceCategoryAdapter: ServiceCategoryAdapter
    lateinit var ServiceListAdapter: serviceListAdapter
    var flag = ""
    var serviceCategory = ""
    var serviceSubCategory = ""
    lateinit var ListSubCategoryAdapter:ProductListSubCategory
    var token = ""
    var latitude = 0.0
    var longitude  = 0.0

    var docs = ArrayList<ServiceListDocs>()
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    private var fusedLocationClient: FusedLocationProviderClient? = null
    lateinit var backTitle: ImageView


    private lateinit var dialog1: Dialog
    private var radiusRequest = ""
    private var countryRequest = ""
    private var stateRequest = ""
    private var cityRequest = ""
    lateinit var adapterDialog: openDialog
    var stateCode = ""
    var cityCode = ""
    var flagFilter = ""
    var countryCode = ""
    var filterData: ArrayList<CountryList> = ArrayList()
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var country: TextView
    lateinit var state: TextView
    lateinit var city: TextView
    var applyFilter = false






    private val viewModel: MarketServiceAllViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentServiceCategoryMarketBinding.inflate(layoutInflater, container, false)
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        backTitle = activity?.findViewById(R.id.backTitle)!!

        arguments?.getString("flag")?.let {
            flag = it
        }
        token =  SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        getLocation()


        viewModel.serviceListCategoryApi("SERVICE")
        backTitle.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        binding.DFsearch.addTextChangedListener(textWatcherGlobalSearch)



        binding.pullToRefresh.setOnRefreshListener {
            pages = 0
            page = 1
            limit = 10
            dataLoadFlag = false
            loaderFlag  = true
            docs.clear()
            serviceCategory = ""
            serviceSubCategory = ""
            binding.serviceSubCategory.isVisible =  false
            binding.ProgressBarScroll.isVisible =  false
            binding.DFsearch.text = Editable.Factory.getInstance().newEditable("")

            viewModel.serviceListCategoryApi("SERVICE")

            binding.pullToRefresh.isRefreshing = false
        }


        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                loaderFlag = false
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.listServiceApi(token,page,limit,serviceCategory,serviceSubCategory,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")
                }
            }
        })



        binding.filterClick.setOnClickListener {
            openFilterPopUp()
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeServiceListCategoryResponse()
        observeServiceListResponse()
        observeSubCategoryResponse()
        observeLikeServiceResponse()
        observeAddToInterestedResponse()

        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()

            }
        })
    }



//         Service category observer
    //   Service Category Observer

    private fun observeServiceListCategoryResponse() {


        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._servicelistCategoryData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {


                            if(response.data?.statusCode == 200) {
                                try {

                                    binding.serviceCategoryNames.isVisible = response.data.result.docs.size > 0
                                    setServiceCategoryAdapter(response.data.result.docs)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.serviceCategoryNames.isVisible = false
                            response.message?.let { message ->
                            }

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

    private fun setServiceCategoryAdapter(docs: ArrayList<ListCategoryDocs>) {
        binding.serviceCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        serviceCategoryAdapter = ServiceCategoryAdapter(requireContext(), docs,this)
        binding.serviceCategory.adapter = serviceCategoryAdapter
    }

    override fun viewDes(_id: String, interested: Boolean) {
        val intent = Intent(requireContext(), ServiceDescriptionActivity::class.java)
        intent.putExtra("id",_id)
        intent.putExtra("flag","Market")
        intent.putExtra("type",interested)
        startActivity(intent)
    }

    override fun getServiceValue(_id: String) {
        serviceCategory  =_id
        serviceSubCategory = ""
        dataLoadFlag = false
        viewModel.listServiceApi(token,1,10,_id,serviceSubCategory,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")
        viewModel.listSubCategoryApi(_id)

    }



//    SubCategory Observer

    private fun observeSubCategoryResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._listSubCategoryData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if(response.data?.responseCode == 200) {
                                try {
                                    binding.serviceSubCategory.isVisible =  response.data.result.docs.size > 0
                                    setProductSubCategoryAdapter(response.data.result.docs)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                            }

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

    private fun setProductSubCategoryAdapter(docs: ArrayList<SubCategoryDocs>) {
        binding.serviceSubCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        ListSubCategoryAdapter = ProductListSubCategory(requireContext(), docs,this)
        binding.serviceSubCategory.adapter = ListSubCategoryAdapter
    }

    override fun getSubCategoryValue(_id: String, subCategoryName: String) {
        serviceSubCategory = _id
        dataLoadFlag = false
        viewModel.listServiceApi(token,1,10,serviceCategory,_id,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")
        binding.Name.text = subCategoryName
    }


//     Service list Category Api

    private fun getLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            fusedLocationClient?.lastLocation!!.addOnCompleteListener(requireActivity()) { task ->

                if (task.isSuccessful && task.result != null) {
                    try {
                        val lastLocation = task.result
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation).longitude
                        viewModel.listServiceApi(token,page,limit,serviceCategory,serviceSubCategory,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")


                    }
                    catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }



    private fun observeServiceListResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._ServiceListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data?.responseCode == 200) {

                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        docs.clear()
                                    }

                                    if (response.data.result.docs.size > 0){
                                        binding.NotFound.isVisible = false
                                        binding.serviceList.isVisible = true
                                        docs.addAll(response.data.result.docs)
                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setServiceListAdapter()
                                    }else{
                                        binding.NotFound.isVisible = true
                                        binding.Name.isVisible = false
                                        binding.serviceList.isVisible = false
                                    }



                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.Name.isVisible = false
                            binding.serviceList.isVisible = false
                            response.message?.let { message ->
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            if (loaderFlag){
                                Progresss.start(requireContext())
                                binding.Name.isVisible = false
                                binding.serviceList.isVisible = false
                            }
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }
            }


    }
    private fun setServiceListAdapter() {
        binding.serviceList.layoutManager = LinearLayoutManager(requireContext())
        ServiceListAdapter = serviceListAdapter(requireContext(),docs,this,this, this)
        binding.serviceList.adapter = ServiceListAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlikeService(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) {

        if (heart.isVisible) {
            docs[position].isLike = true
            ServiceListAdapter.notifyItemChanged(position)
            ServiceListAdapter.notifyItemRangeChanged(position, docs.size)
            ServiceListAdapter.notifyDataSetChanged()

        } else {
            docs[position].isLike = false
            ServiceListAdapter.notifyItemChanged(position)
            ServiceListAdapter.notifyItemRangeChanged(position, docs.size)
            ServiceListAdapter.notifyDataSetChanged()

        }
        viewModel.likeUnlikeServicesApi(token,_id)
    }
    private fun observeLikeServiceResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likeService.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                            }
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

    override fun productInterest(_id: String, position: Int) {

    }

    override fun petInterest(_id: String, position: Int) {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun serviceInterest(_id: String, position: Int) {
        if (!docs[position].isInterested) {
            docs[position].isInterested = true
            ServiceListAdapter.notifyItemChanged(position)
            ServiceListAdapter.notifyItemRangeChanged(position, docs.size)
            ServiceListAdapter.notifyDataSetChanged()

        }
        viewModel.addToInterestedApi(token,"SERVICE","","",_id)
    }
    private fun observeAddToInterestedResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addToInterestedData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                            }
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

    // Post Filter


    private fun openFilterPopUp() {
        try {
            val bindingPopup = LayoutInflater.from(requireActivity()).inflate(R.layout.missing_pet_filter_popup, null)
            dialog1 = DialogUtils().createDialog(requireActivity(), bindingPopup.rootView, 0)!!
            val applyButton = bindingPopup.findViewById<LinearLayout>(R.id.applyButton)
            val clearAllButton = bindingPopup.findViewById<LinearLayout>(R.id.clearAllButton)
            val oneMiles = bindingPopup.findViewById<TextView>(R.id.oneMiles)
            val twoMiles = bindingPopup.findViewById<TextView>(R.id.twoMiles)
            val threeMiles = bindingPopup.findViewById<TextView>(R.id.threeMiles)
            val fourMiles = bindingPopup.findViewById<TextView>(R.id.fourMiles)
            val fiveMiles = bindingPopup.findViewById<TextView>(R.id.fiveMiles)
            val sixMiles = bindingPopup.findViewById<TextView>(R.id.sixMiles)
            val tvCountry = bindingPopup.findViewById<TextView>(R.id.tvCountry)
            val tvState = bindingPopup.findViewById<TextView>(R.id.tvState)
            val popupTitle = bindingPopup.findViewById<TextView>(R.id.popupTitle)
            val llCountry = bindingPopup.findViewById<LinearLayout>(R.id.llCountry)
            val llState = bindingPopup.findViewById<LinearLayout>(R.id.llState)
            val llCity = bindingPopup.findViewById<LinearLayout>(R.id.llCity)
            val dialougbackButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)
            country = bindingPopup.findViewById(R.id.etCountry)
            state = bindingPopup.findViewById(R.id.etState)
            city = bindingPopup.findViewById(R.id.etCity)

            val petBreed = bindingPopup.findViewById<TextView>(R.id.petBreed)
            val tvPetBreed = bindingPopup.findViewById<TextView>(R.id.tvPetBreed)
            val llPetBreed = bindingPopup.findViewById<LinearLayout>(R.id.llPetBreed)
            radiusRequest = "10"

            petBreed.isVisible = false
            tvPetBreed.isVisible = false
            llPetBreed.isVisible = false

            sixMiles.text = getString(R.string.all_services)
            popupTitle.text = getString(R.string.all_services_globaly)
            clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)

            dialougbackButton.setSafeOnClickListener { dialog1.dismiss() }


            llCountry.setSafeOnClickListener {
                flagFilter = "Country"
                selectionPopUp(flagFilter)
             }

            llState.setSafeOnClickListener {
                if (country.text.isNotEmpty()) {
                    flagFilter = "State"
                    selectionPopUp(flagFilter)
                } else {
                    tvCountry.visibility = View.VISIBLE
                    tvCountry.text = getString(R.string.select_city)
                    tvCountry.setTextColor(Color.parseColor("#C63636"))
                    llCountry.setBackgroundResource(R.drawable.errordrawable)
                }


            }

            llCity.setSafeOnClickListener {
                if (state.text.isNotEmpty()) {
                    flagFilter = "City"
                    selectionPopUp(flagFilter)
                } else {
                    tvState.visibility = View.VISIBLE
                    tvState.text = getString(R.string.select_state)
                    tvState.setTextColor(Color.parseColor("#C63636"))
                    llState.setBackgroundResource(R.drawable.errordrawable)
                }
            }

            applyButton.setSafeOnClickListener {
                page = 1
                limit = 10
                serviceCategory = ""
                serviceSubCategory = ""
                dataLoadFlag = false
                loaderFlag = true
                binding.ProgressBarScroll.isVisible = false
                viewModel.listServiceApi(token,page,limit,serviceCategory,serviceSubCategory,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")
                dialog1.dismiss()
            }

            clearAllButton.setSafeOnClickListener {
                clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)
            }

            oneMiles.setSafeOnClickListener {
                radiusRequest = "10"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.button_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            twoMiles.setSafeOnClickListener {
                radiusRequest = "20"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.button_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            threeMiles.setOnClickListener {
                radiusRequest = "30"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.button_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            fourMiles.setOnClickListener {
                radiusRequest = "40"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.button_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            fiveMiles.setSafeOnClickListener {
                radiusRequest = "50"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.button_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            sixMiles.setSafeOnClickListener {
                radiusRequest = "All Services"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.button_background)
            }

            dialog1.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun selectionPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireActivity()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireActivity(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialougbackButton.setSafeOnClickListener { dialog.dismiss() }


            val SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            when (flag) {
                "State" -> {
                    dialougTitle.text = flag
                    viewModel.getStateListApi(countryCode)

                }
                "City" -> {
                    dialougTitle.text = flag
                    viewModel.getCityListApi(countryCode, stateCode)

                }
                "Country" -> {
                    dialougTitle.text = flag
                    viewModel.getCountryApi()

                }
            }


            SearchEditText.addTextChangedListener(textWatchers)



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FilterData(s.toString())
        }
    }

    private fun FilterData(searchText: String) {
        val filteredList: java.util.ArrayList<CountryList> = java.util.ArrayList()


        for (item in filterData) {
            try {
                if (item.name.lowercase().contains(searchText.lowercase())) {
                    filteredList.add(item)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            adapterDialog.filterList(filteredList)

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    fun setAdapter(result: ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterDialog = openDialog(requireContext(), result, flagFilter, this)
        recyclerView.adapter = adapterDialog
    }


//    Country List Observer

    private fun observeCountryListResponse() {

        lifecycleScope.launchWhenCreated {
            viewModel._countryStateFlow.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.statusCode == 200) {
                            try {
                                filterData = response.data.result
                                setAdapter(response.data.result)
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

//    State List Observer

    private fun observeStateListResponse() {

        lifecycleScope.launch {
            viewModel._stateData.collect { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.statusCode == 200) {
                            Progresss.stop()
                            try {
                                filterData = response.data.result
                                setAdapter(response.data.result)
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

//    State List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launch {
            viewModel._citydata.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if (response.data?.statusCode == 200) {
                            try {
                                filterData = response.data.result
                                setAdapter(response.data.result)
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
                    }

                }

            }
        }
    }



    private fun clearFilterData(oneMiles: TextView, twoMiles: TextView, threeMiles: TextView, fourMiles: TextView, fiveMiles: TextView, sixMiles: TextView) {
        radiusRequest = "10"
        countryRequest = ""
        stateRequest = ""
        cityRequest = ""
        country.text = ""
        city.text = ""
        state.text = ""

        resetButtonColors(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)




    }

    private fun resetButtonColors(oneMiles: TextView, twoMiles: TextView, threeMiles: TextView, fourMiles: TextView, fiveMiles: TextView, sixMiles: TextView){
        oneMiles.setBackgroundResource(R.drawable.button_background)
        twoMiles.setBackgroundResource(R.drawable.border_background)
        threeMiles.setBackgroundResource(R.drawable.border_background)
        fourMiles.setBackgroundResource(R.drawable.border_background)
        fiveMiles.setBackgroundResource(R.drawable.border_background)
        sixMiles.setBackgroundResource(R.drawable.border_background)


        oneMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        twoMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        threeMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        fourMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        fiveMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        sixMiles.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))

    }

    override fun getData(data: String, flag: String, code: String) {
        when (flag) {
            "City" -> {
                cityRequest = data
                city.text = data
                cityCode = data
                dialog.dismiss()

            }
            "State" -> {
                stateRequest = data
                state.text = data
                stateCode = code
                city.text = ""
                dialog.dismiss()
            }
            "Country" -> {
                countryRequest = data
                country.text = data
                countryCode = code
                state.text = ""
                city.text = ""
                dialog.dismiss()
            }
        }
    }


    private val textWatcherGlobalSearch = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            page = 1
            limit = 10
            serviceCategory = ""
            serviceSubCategory = ""
            dataLoadFlag = false
            radiusRequest = "10"
            stateRequest = ""
            countryRequest = ""
            cityRequest = ""
            viewModel.listServiceApi(token,page,limit,serviceCategory,serviceSubCategory,latitude,longitude,80,radiusRequest,countryRequest,stateRequest,cityRequest,s.toString())

        }
    }



}
