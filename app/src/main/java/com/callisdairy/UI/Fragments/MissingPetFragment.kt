package com.callisdairy.UI.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.callisdairy.Adapter.MissingPetAdapter
import com.callisdairy.Adapter.MyMissingPetAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.DeleteClick
import com.callisdairy.Interface.MissingPest
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddMissingPetActivity
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Activities.subscription.SubscribePlansUserActivity
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.LocationClass
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.MissingPetDocs
import com.callisdairy.databinding.FragmentMissingPetBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.MissingPetViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class MissingPetFragment : Fragment(), MissingPest, DeleteClick, PopupItemClickListener {

    private lateinit var dialog1: Dialog
    private lateinit var dialog: Dialog

    private lateinit var binding: FragmentMissingPetBinding

    private var fusedLocationClient: FusedLocationProviderClient? = null

    lateinit var missingPetAdapter: MissingPetAdapter
    lateinit var myMissingPetAdapter: MyMissingPetAdapter

    var flag = ""
    var flags = ""
    var token = ""
    var missingPetId = ""
    var adapterPosition = 0

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = true
    var loaderFlag = true
    var data = ArrayList<MissingPetDocs>()

    var myMissingpages = 0
    var myMissingpage = 1
    var myMissinglimit = 10
    var myMissingdataLoadFlag = true
    var myMissingloaderFlag = true
    var myMissingdata = ArrayList<MissingPetDocs>()

    //   Tool Bar

    lateinit var back: ImageView
    lateinit var chat: ImageView
    lateinit var search: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView


    private val viewModel: MissingPetViewModel by viewModels()


    var startLat = 0.0
    var startLong = 0.0
    private var queue: RequestQueue? = null
    private var radiusRequest = ""
    private var countryRequest = ""
    private var stateRequest = ""
    private var cityRequest = ""
    lateinit var adapter: openDialog
    var stateCode = ""
    var cityCode = ""
    var countryCode = ""
    var filterData: ArrayList<CountryList> = ArrayList()

    private lateinit var recyclerView: RecyclerView
    lateinit var country: TextView
    lateinit var state: TextView
    lateinit var city: TextView
    var applyFilter = false

    lateinit var etPetBreed: TextView
    var petBreedId = ""


    var remainingMissedPet = 0
    var totalMissedPet = 0
    var isClickedToAdd =  false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMissingPetBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }





        arguments?.getString("flag")?.let {
            flags = it
        }
        binding.missingPetFilter.visibility = View.VISIBLE
        binding.missingButton.isEnabled = false
        binding.myMissingPetsButton.isEnabled = true
        allIds()
        toolBarWithBottomTab()
        getLocation()
        LocationClass.displayLocationSettingsRequest(requireActivity())

        queue = Volley.newRequestQueue(activity?.applicationContext)

        back.setSafeOnClickListener {
            fragmentManager?.popBackStack()
        }

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)
            .toString()




        binding.dfSearch.addTextChangedListener(textWatcher)


        binding.swipeRefresh.setOnRefreshListener {
            viewModel.checkPlanApi(token = token)
            when (flag) {
                "missingPet" -> {
                    data.clear()
                    loaderFlag = true
                    dataLoadFlag = true
                    flag = "missingPet"
                    page = 1
                    limit = 10

                    viewModel.missingPetApi(token, "", "", page, limit, "", "", "", "",startLat,startLong,petBreedId)

                    binding.swipeRefresh.isRefreshing = false
                }

                "myMissingPet" -> {
                    myMissingdata.clear()
                    myMissingloaderFlag = true
                    myMissingdataLoadFlag = true
                    myMissingpage = 1
                    myMissinglimit = 10
                    flag = "myMissingPet"
                    viewModel.myMissingPetApi(
                        token,
                        "",
                        "MYPET",
                        myMissingpage,
                        myMissinglimit,
                        "",
                        "",
                        "",
                        "",startLat,startLong
                    )
                    binding.swipeRefresh.isRefreshing = false
                }

                else -> {
                    data.clear()
                    loaderFlag = true
                    dataLoadFlag = true
                    page = 1
                    limit = 10
                    flag = ""
                    viewModel.missingPetApi(token, "", "", page, limit, "", "", "", "",startLat,startLong,petBreedId)
                    binding.swipeRefresh.isRefreshing = false
                }

            }
        }

        binding.scrollPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                when (flag) {

                    "missingPet" -> {
                        dataLoadFlag = false
                        page++
                        binding.ProgressBarScroll.visibility = View.VISIBLE
                        if (page > pages) {
                            binding.ProgressBarScroll.visibility = View.GONE
                        } else {
                            viewModel.missingPetApi(token, "", "", page, limit, "", "", "", "",startLat,startLong,petBreedId)
                        }
                    }

                    "myMissingPet" -> {
                        myMissingdataLoadFlag = false
                        myMissingpage++
                        binding.ProgressBarScroll.visibility = View.VISIBLE
                        if (myMissingpage > myMissingpages) {
                            binding.ProgressBarScroll.visibility = View.GONE
                        } else {

                            viewModel.myMissingPetApi(
                                token,
                                "",
                                "MYPET",
                                myMissingpage,
                                myMissinglimit, "", "", "", "",startLat,startLong
                            )
                        }
                    }

                    else -> {
                        dataLoadFlag = false
                        page++
                        binding.ProgressBarScroll.visibility = View.VISIBLE
                        if (page > pages) {
                            binding.ProgressBarScroll.visibility = View.GONE
                        } else {
                            viewModel.missingPetApi(token, "", "", page, limit, "", "", "", "",startLat,startLong,petBreedId)
                        }
                    }

                }


            }
        })


        binding.missingPetFilter.setSafeOnClickListener {
            openFilterPopUp()
        }





        binding.addImage.setOnClickListener {
            isClickedToAdd = true
            viewModel.checkPlanApi(token = token)

        }


        binding.missingButton.setSafeOnClickListener {
            binding.missingButton.isEnabled = false
            binding.myMissingPetsButton.isEnabled = true
            binding.missingButton.setBackgroundResource(R.drawable.main_button_background)
            binding.myMissingPetsButton.setBackgroundResource(R.drawable.border_background)
            binding.txtMissing.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.txtMyMissingPets.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
            binding.addImage.visibility = View.GONE
            binding.missingPetFilter.visibility = View.VISIBLE
            flag = "missingPet"
            binding.dfSearch.setText("")
            binding.countLL.isVisible = false
            data.clear()
            dataLoadFlag = true
            loaderFlag = true
            page = 1
            limit = 10
            viewModel.missingPetApi(token, "", "", page, limit, "", "", "", "",startLat,startLong,petBreedId)
        }

        binding.myMissingPetsButton.setSafeOnClickListener {
            binding.missingButton.isEnabled = true
            binding.myMissingPetsButton.isEnabled = false
            binding.missingPetFilter.visibility = View.GONE

            binding.countLL.isVisible = true

            binding.missingButton.setBackgroundResource(R.drawable.border_background)
            binding.myMissingPetsButton.setBackgroundResource(R.drawable.main_button_background)
            binding.txtMissing.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
            binding.txtMyMissingPets.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.addImage.visibility = View.VISIBLE
            flag = "myMissingPet"
            binding.dfSearch.setText("")
            viewModel.myMissingPetApi(token, "", "MYPET", myMissingpage, myMissinglimit, "", "", "", "",startLat,startLong)

        }


        return view
    }

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
            val popupTitle = bindingPopup.findViewById<TextView>(R.id.popupTitle)
            val tvCountry = bindingPopup.findViewById<TextView>(R.id.tvCountry)
            val tvState = bindingPopup.findViewById<TextView>(R.id.tvState)
            val llCountry = bindingPopup.findViewById<LinearLayout>(R.id.llCountry)
            val llState = bindingPopup.findViewById<LinearLayout>(R.id.llState)
            val llCity = bindingPopup.findViewById<LinearLayout>(R.id.llCity)
            val dialougbackButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)
            country = bindingPopup.findViewById(R.id.etCountry)
            state = bindingPopup.findViewById(R.id.etState)
            city = bindingPopup.findViewById(R.id.etCity)
            val llPetBreed = bindingPopup.findViewById<LinearLayout>(R.id.llPetBreed)
            etPetBreed = bindingPopup.findViewById(R.id.etPetBreed)
            radiusRequest = "10"

            sixMiles.text = getString(R.string.all_followers)
            popupTitle.text = getString(R.string.all_missing_pet_globaly)
            clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)

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
                data.clear()
                loaderFlag = true
                dataLoadFlag = true
                flag = "missingPet"
                page = 1
                limit = 10
                applyFilter = true

                viewModel.missingPetApi(
                    token,
                    "",
                    "",
                    page,
                    limit,
                    radiusRequest,
                    countryRequest,
                    stateRequest,
                    cityRequest
                    ,startLat,startLong,petBreedId)


            }

            clearAllButton.setOnClickListener {
                clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)
            }

            oneMiles.setOnClickListener {
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

            twoMiles.setOnClickListener {
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

            fiveMiles.setOnClickListener {
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

            sixMiles.setOnClickListener {
                radiusRequest = "All Pets"
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

    private fun clearFilterData(oneMiles:TextView,twoMiles:TextView,threeMiles:TextView,fourMiles:TextView,fiveMiles:TextView,sixMiles:TextView) {
        radiusRequest = "10"
        countryRequest = ""
        stateRequest = ""
        cityRequest = ""
        country.text = ""
        city.text = ""
        state.text = ""
        etPetBreed.text = ""
        petBreedId= ""

        resetButtonColors(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)




    }


    fun selectionPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireActivity()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireActivity(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialougbackButton.setOnClickListener { dialog.dismiss() }


            val SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            when (flag) {
                "State" -> {
                    dialougTitle.text = "State"
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
                if (item.name.lowercase().contains(searchText.lowercase())) {
                    filteredList.add(item)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            adapter.filterList(filteredList)

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    fun setAdapter(result: java.util.ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = openDialog(requireContext(), result, flag, this)
        recyclerView.adapter = adapter
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

//    city List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
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






    override fun onStart() {
        super.onStart()
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        viewModel.checkPlanApi(token = token)
        if (flag =="myMissingPet"){

            myMissingloaderFlag = true
            myMissingdataLoadFlag =  true
            myMissingdata.clear()
            viewModel.myMissingPetApi(token, "", "MYPET", 1, 10, "", "", "", "",startLat,startLong)
        }

    }


    override fun viewMissingPet(_id: String) {
        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "ViewMissingPet")
        intent.putExtra("from", flag)
        intent.putExtra("petId", _id)
        startActivity(intent)
    }

    override fun deleteMissingPet(_id: String, position: Int) {
        missingPetId = _id
        adapterPosition = position
        androidExtension.alertBoxDelete(
            "Are you sure you want to delete this?",
            requireContext(),
            this
        )
    }


    private fun allIds() {

        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!

    }

    private fun toolBarWithBottomTab() {

        when (flags) {

            "MissingPetsMenu" -> {
                mainTitle.visibility = View.VISIBLE
                chat.visibility = View.VISIBLE
                search.visibility = View.VISIBLE
                back.visibility = View.GONE
            }

        }


        Username.text = getString(R.string.app_name)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseMissingPetList()
        observeResponseMyMissingPetList()

        observeResponseCheckLimitOfPostingContent()


        observeResponseDelete()
        //        Observers
        observeCountryListResponse()
        observeCityListResponse()
        observePetBreedListResponse()
        observeStateListResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    fm.popBackStack()

                }
            })
    }


    private fun observeResponseMissingPetList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._missingPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    if(applyFilter) {
                                        applyFilter = false
                                        dialog1.dismiss()
                                    }
                                    loaderFlag = false
                                    if (dataLoadFlag) {
                                        data.clear()
                                    }

                                    if (response.data.result.docs.size > 0) {
                                        binding.NotFound.isVisible = false
                                        binding.missingRecycler.isVisible = true

                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.pages
                                        page = response.data.result.page

                                        setMissingPetAdapter(data)

                                    } else {
                                        binding.NotFound.isVisible = true
                                        binding.NotFound.text = "No Missing Pets Found."
                                        binding.missingRecycler.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.NotFound.text = "No Missing Pets Found."
                            binding.missingRecycler.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            binding.missingRecycler.isVisible = false
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


    private fun observeResponseMyMissingPetList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._myMmissingPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    myMissingloaderFlag = false
                                    if (myMissingdataLoadFlag) {
                                        myMissingdata.clear()
                                    }

                                    if (response.data.result.docs.size > 0) {
                                        binding.NotFound.isVisible = false
                                        binding.missingRecycler.isVisible = true
                                        val responseItems = response.data.result.docs

                                        for (item in responseItems) {
                                            if (myMissingdata.none { it._id == item._id }) {
                                                myMissingdata.add(item)
                                            }
                                        }

                                        myMissingpages = response.data.result.pages
                                        myMissingpage = response.data.result.page

                                        setMyMissingPetAdapter(myMissingdata)


                                    } else {
                                        binding.NotFound.isVisible = true
                                        binding.NotFound.text = "You Have No Missing Pets."
                                        binding.missingRecycler.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.NotFound.text = "You Have No Missing Pets."
                            binding.missingRecycler.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            binding.missingRecycler.isVisible = false
                            if (myMissingloaderFlag) {
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

    private fun setMissingPetAdapter(missingData: ArrayList<MissingPetDocs>) {
        binding.missingRecycler.layoutManager = LinearLayoutManager(requireContext())
        missingPetAdapter = MissingPetAdapter(requireContext(), missingData, this)
        binding.missingRecycler.adapter = missingPetAdapter
    }

    private fun setMyMissingPetAdapter(missingData: ArrayList<MissingPetDocs>) {
        binding.missingRecycler.layoutManager = LinearLayoutManager(requireContext())
        myMissingPetAdapter = MyMissingPetAdapter(requireContext(), missingData, this)
        binding.missingRecycler.adapter = myMissingPetAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteItem() {
        myMissingdata.removeAt(adapterPosition)
        myMissingPetAdapter.notifyItemChanged(adapterPosition)
        myMissingPetAdapter.notifyDataSetChanged()


        viewModel.deleteEventApi(token, missingPetId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseDelete() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._myListDeleteData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    if (data.size == 0) {
                                        binding.NotFound.isVisible = true
                                        binding.NotFound.text = "You Have No Missing Pets."
                                        binding.missingRecycler.isVisible = false
                                    }


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


    override fun trackPet(trackingId: String) {
        petLocationApi(trackingId)
    }

    private fun openMap(desLong: String, desLat: String) {

        val srcAdd = "&origin=" + startLat + "," + startLong
//
        val desAdd = "&destination=" + desLat + "," + desLong

        val link =
            "https://www.google.com/maps/dir/?api=1&travelmode=driving$srcAdd$desAdd"
        Log.e("Url Polyline?", link)


        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            //                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" +stringLatitude+ "," + stringLongitude + "&daddr=" +  Double.parseDouble(workOrderDetailsModelClass.getCoords_lat()) + "," + Double.parseDouble(workOrderDetailsModelClass.getCoords_lng())));
            startActivity(intent)
        } catch (ane: ActivityNotFoundException) {
            Toast.makeText(context, "Please Install Google Maps ", Toast.LENGTH_LONG).show()
        } catch (ex: java.lang.Exception) {
            ex.message
        }

    }


    private fun petLocationApi(trackingId: String) {
        val url =
            "https://vahantrack.com/api/api.php?api=user&ver=1.0&key=873FB0B40E045A0&cmd=OBJECT_GET_LOCATIONS,$trackingId"

        Progresss.start(requireContext())
        val jsonObject = JSONObject()
        val req: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url,
            null, Response.Listener { response ->
                val jsonObject = JSONObject(response.toString())
                val key = jsonObject.getJSONObject(trackingId)
                try {
                    Progresss.stop()
                    println(response)
                    val lat = key.getString("lat")
                    val long = key.getString("lng")
                    openMap(long, lat)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }, Response.ErrorListener { error ->
                Progresss.stop()
                androidExtension.alertBox(
                    "Sorry, you can't track this pet. Please ensure you have a tracking device.",
                    requireContext()
                )

            }) {

        }
        queue!!.add(req)
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

                        startLat = (lastLocation)!!.latitude
                        startLong = (lastLocation).longitude
                        viewModel.missingPetApi(token, "", "", page, limit, "", "", "", "",startLat,startLong,petBreedId)
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (flag == "myMissingPet") {
                data.clear()
                loaderFlag = false
                dataLoadFlag = true
                page = 1
                limit = 10

                viewModel.missingPetApi(token, s.toString(), "MYPET", page, limit, "", "", "", "",startLat,startLong,petBreedId)
            } else {
                data.clear()
                loaderFlag = false
                dataLoadFlag = true
                page = 1
                limit = 10
                viewModel.missingPetApi(token, s.toString(), "", page, limit, "", "", "", "",startLat,startLong,petBreedId)
            }

        }

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


    override fun onPause() {
        super.onPause()
        myMissingdataLoadFlag = true
        dataLoadFlag = true
    }



    private fun observeResponseCheckLimitOfPostingContent() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._checkPlanData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    remainingMissedPet = response.data.result.remainingMissedPet.toInt()
                                    totalMissedPet = response.data.result.totalMissedPet.toInt()
                                    binding.count.text =  "$remainingMissedPet"
                                    if (isClickedToAdd){
                                        if (response.data.result.remainingMissedPet.toInt() > 0){
                                            val intent = Intent(requireContext(), AddMissingPetActivity::class.java)
                                            intent.putExtra("flag", "Add")
                                            startActivity(intent)
                                        }

                                        if (response.data.result.remainingMissedPet.toInt() == 0){
                                            val intent =Intent(requireContext(),
                                                SubscribePlansUserActivity::class.java)
                                            intent.putExtra("Screen","Missing")
                                            startActivity(intent)
                                        }
                                        isClickedToAdd =false

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