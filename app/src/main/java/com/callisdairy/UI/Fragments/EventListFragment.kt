package com.callisdairy.UI.Fragments

import android.Manifest
import android.app.Dialog
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
import com.callisdairy.Adapter.EventPostAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.R
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.ListEventDocs
import com.callisdairy.databinding.FragmentEventListBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.EventListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventListFragment : Fragment(), PopupItemClickListener {

    private  var _binding: FragmentEventListBinding?= null
    private val binding get() = _binding!!
    private lateinit var eventPostAdapter: EventPostAdapter


    var data = ArrayList<ListEventDocs>()
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true
    var latitude = 0.0
    var longitude = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null

    var token =""


    //   Tool Bar

    lateinit var back: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView
    lateinit var userName: TextView
    lateinit var addPost: ImageView


    private lateinit var dialog1: Dialog
    private var radiusRequest = ""
    private var countryRequest = ""
    private var stateRequest = ""
    private var cityRequest = ""
    lateinit var adapterDialog: openDialog
    var stateCode = ""
    var cityCode = ""
    var flag = ""
    var countryCode = ""
    var filterData: ArrayList<CountryList> = ArrayList()
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var country: TextView
    lateinit var state: TextView
    lateinit var city: TextView
    lateinit var etPetBreed: TextView
    var petBreedId = ""

    var applyFilter = false






    private val viewModel: EventListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(layoutInflater, container, false)
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }

        allIds()
        toolBarWithBottomTab()

        back.setSafeOnClickListener {
            parentFragmentManager.popBackStack()
        }


        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()

        getLocation()




        binding.MyEventsList.setSafeOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_container, MyEventListFragment()).addToBackStack(null)
                .commit()
        }




        binding.filterClick.setSafeOnClickListener {
            openFilterPopUp()
        }



        binding.DFsearch.addTextChangedListener(textWatcher)


        binding.pullToRefresh.setOnRefreshListener {
            page = 1
            limit = 1
            data.clear()
            dataLoadFlag = false
            loaderFlag = true
            binding.DFsearch.setText("")
            radiusRequest = "10"
            countryRequest = ""
            stateRequest = ""
            cityRequest = ""

            binding.pullToRefresh.isRefreshing = false

        }




        binding.scrollViewEvent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.listEventApi(token=token,search = "",page= page,limit=limit, radius = radiusRequest, country = countryRequest
                        ,state=stateRequest, city = cityRequest, lat = latitude,lng= longitude,petBreedId = petBreedId)
                }

            }


        })



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseProductList()
        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()
        observePetBreedListResponse()
    }







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
                        viewModel.listEventApi(token=token,search = "",page= page,limit=limit, radius = radiusRequest, country = countryRequest
                            ,state=stateRequest, city = cityRequest, lat = latitude,lng= longitude,petBreedId = petBreedId)
                    }
                    catch(e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        }
        catch(e:Exception){
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


    private fun allIds() {
        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        userName = activity?.findViewById(R.id.Username)!!
        addPost = activity?.findViewById(R.id.addPost)!!

    }


    private fun toolBarWithBottomTab() {


        mainTitle.visibility = View.GONE
        addPost.visibility = View.GONE
        chat.visibility = View.VISIBLE
        back.visibility = View.VISIBLE
        search.visibility = View.GONE


        userName.text = getString(R.string.Events)


    }


    val textWatcher = object : TextWatcher {
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
            viewModel.listEventApi(token=token,search = s.toString(),page= page,limit=limit, radius = radiusRequest, country = countryRequest
                ,state=stateRequest, city = cityRequest, lat = latitude,lng= longitude,petBreedId = petBreedId)
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
            val llPetBreed = bindingPopup.findViewById<LinearLayout>(R.id.llPetBreed)
            etPetBreed = bindingPopup.findViewById(R.id.etPetBreed)

            val petBreed = bindingPopup.findViewById<TextView>(R.id.petBreed)
            val tvPetBreed = bindingPopup.findViewById<TextView>(R.id.tvPetBreed)


            petBreed.isVisible = false
            tvPetBreed.isVisible = false
            llPetBreed.isVisible = false

            sixMiles.text = getString(R.string.all_events)
            popupTitle.text = getString(R.string.all_events_globaly)
            clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)

            radiusRequest = "10"


            dialougbackButton.setSafeOnClickListener { dialog1.dismiss() }
            llCountry.setSafeOnClickListener {
                flag = "Country"
                selectionPopUp(flag)
            }

            llState.setSafeOnClickListener {
                if (country.text.isNotEmpty()) {
                    flag = "State"
                    selectionPopUp(flag)
                } else {
                    tvCountry.visibility = View.VISIBLE
                    tvCountry.text = getString(R.string.select_city)
                    tvCountry.setTextColor(Color.parseColor("#C63636"))
                    llCountry.setBackgroundResource(R.drawable.errordrawable)
                }


            }

            llCity.setSafeOnClickListener {
                if (state.text.isNotEmpty()) {
                    flag = "City"
                    selectionPopUp(flag)
                } else {
                    tvState.visibility = View.VISIBLE
                    tvState.text = getString(R.string.select_state)
                    tvState.setTextColor(Color.parseColor("#C63636"))
                    llState.setBackgroundResource(R.drawable.errordrawable)
                }
            }

            llPetBreed.setSafeOnClickListener {
                flag = "Pet Breed"
                selectionPopUp(flag)

            }


            applyButton.setSafeOnClickListener {
                page = 1
                limit = 10
                data.clear()
                dataLoadFlag = false
                viewModel.listEventApi(token,"", page, limit,radiusRequest,countryRequest,stateRequest,cityRequest,latitude,longitude,petBreedId=petBreedId )
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

            threeMiles.setSafeOnClickListener {
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

            fourMiles.setSafeOnClickListener {
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
                radiusRequest = "All Events"
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
                "Pet Breed" -> {
                    dialougTitle.text = flag
                    val petTypeId = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.profileId).toString()
                    viewModel.petBreedListApi(petCategoryId = petTypeId)

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
                if (item.name.lowercase().contains(searchText.lowercase()) ||
                    item.petBreedName.lowercase().contains(searchText.lowercase()) ) {
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
        adapterDialog = openDialog(requireContext(), result, flag, this)
        recyclerView.adapter = adapterDialog
    }


//    Country List Observer

    private fun observeCountryListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
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

                        }

                    }

                }
            }

        }
    }

//    State List Observer

    private fun observeStateListResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._stateData.collect { response ->

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
    }

//    State List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
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
    }


    //    Pet Breed List Observer
    private fun observePetBreedListResponse() {

        lifecycleScope.launch {
            viewModel._petBreeddata.collect { response ->

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



    private fun clearFilterData(oneMiles:TextView,twoMiles:TextView,threeMiles:TextView,fourMiles:TextView,fiveMiles:TextView,sixMiles:TextView) {
        radiusRequest = "10"
        countryRequest = ""
        stateRequest = ""
        cityRequest = ""
        country.text = ""
        city.text = ""
        state.text = ""
        petBreedId =""
        etPetBreed.text = ""

        resetButtonColors(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)




    }

    private fun resetButtonColors(oneMiles:TextView,twoMiles:TextView,threeMiles:TextView,fourMiles:TextView,fiveMiles:TextView,sixMiles:TextView){
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
            "Pet Breed" -> {
                etPetBreed.text = data
                petBreedId = code
                dialog.dismiss()
            }

        }
    }







}