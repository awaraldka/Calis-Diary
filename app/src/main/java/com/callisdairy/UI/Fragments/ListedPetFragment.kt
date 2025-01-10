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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.MarketLatestListedPetAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.InterestedClick
import com.callisdairy.Interface.LikeUnlikePet
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.viewPests
import com.callisdairy.R
import com.callisdairy.UI.Activities.PetDescriptionActivity
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.PetListDocs
import com.callisdairy.databinding.FragmentListedPetBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.ListedPetViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListedPetFragment : Fragment(), LikeUnlikePet, viewPests, InterestedClick,
    PopupItemClickListener {

    private var _binding: FragmentListedPetBinding? =  null
    private val binding get() = _binding!!


    var dataPet = ArrayList<PetListDocs>()
    lateinit var listadapter: MarketLatestListedPetAdapter
    var token = ""
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var latitude = 0.0
    var longitude = 0.0

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    lateinit var backTitle: ImageView


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
    var applyFilter = false



    private val viewModel: ListedPetViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentListedPetBinding.inflate(layoutInflater, container, false)
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        backTitle = activity?.findViewById(R.id.backTitle)!!


        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        getLocation()

        binding.scrollViewPet.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.listPetApi(token,page,limit,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")
                }
            }
        })
        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        binding.pullToRefresh.setOnRefreshListener {
            pages = 0
            dataPet.clear()
            page = 1
            limit = 10
            dataLoadFlag =  false
            loaderFlag = true
            binding.DFsearch.text  = Editable.Factory.getInstance().newEditable("")
            binding.pullToRefresh.isRefreshing = false
        }

        binding.filterClick.setSafeOnClickListener {
            openFilterPopUp()
        }



        binding.DFsearch.addTextChangedListener(textWatcherGlobalSearch)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLatestListedPetResponse()
        observeLikePetResponse()
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
                        viewModel.listPetApi(token,page,limit,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")
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



    //    Latest listed Pet observer

    private fun observeLatestListedPetResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._latestListedPetData.collectLatest { response ->

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
                                        binding.LatestListPets.isVisible = true
                                        binding.NotFound.isVisible = false
                                        dataPet.addAll(response.data.result.docs)
                                        pages = response.data.result.totalPages
                                        page = response.data.result.page
                                        setPetListAdapter()
                                    }else{
                                        binding.LatestListPets.isVisible = false
                                        binding.NotFound.isVisible = true
                                    }


                                }catch (e:Exception){
                                    e.printStackTrace()
                                }

                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.LatestListPets.isVisible = false
                            binding.NotFound.isVisible = true
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            if(loaderFlag){
                                binding.LatestListPets.isVisible = false
                                Progresss.start(requireContext())
                            }

                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }

        }
    }
    private fun setPetListAdapter() {
        binding.LatestListPets.layoutManager = GridLayoutManager(requireContext(),2)
        listadapter = MarketLatestListedPetAdapter(requireContext(), dataPet, this, this,this)
        binding.LatestListPets.adapter = listadapter
    }
    override fun viewPet(_id: String, interested: Boolean) {
        val intent = Intent(requireContext(), PetDescriptionActivity::class.java)
        intent.putExtra("id",_id)
        intent.putExtra("flag","Market")
        intent.putExtra("type",interested)
        startActivity(intent)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlikePet(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) {

        if (heart.isVisible) {
            dataPet[position].isLike = true
            listadapter.notifyItemChanged(position)
            listadapter.notifyItemRangeChanged(position, dataPet.size)
            listadapter.notifyDataSetChanged()

        } else {
            dataPet[position].isLike = false
            listadapter.notifyItemChanged(position)
            listadapter.notifyItemRangeChanged(position, dataPet.size)
            listadapter.notifyDataSetChanged()

        }


        viewModel.likeUnlikePetApi(token,_id)
    }
    private fun observeLikePetResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likePet.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
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
//                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
//                                Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }
            }

        }
    }



    //    Add To Interested Apis and Observer

    override fun productInterest(_id: String, position: Int) {


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun petInterest(_id: String, position: Int) {

        if (!dataPet[position].isInterested) {
            dataPet[position].isInterested = true
            listadapter.notifyItemChanged(position)
            listadapter.notifyItemRangeChanged(position, dataPet.size)
            listadapter.notifyDataSetChanged()

        }


        viewModel.addToInterestedApi(token,"PET",_id,"","")

    }
    override fun serviceInterest(_id: String, position: Int) {


    }
    private fun observeAddToInterestedResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addToInterestedData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
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
//                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
//                                Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                            Progresss.stop()
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

            sixMiles.text = getString(R.string.all_pets)
            popupTitle.text = getString(R.string.all_pet_globaly)
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

            applyButton.setSafeOnClickListener {
                page = 1
                limit = 10
                dataLoadFlag = false
                loaderFlag = true
                binding.ProgressBarScroll.isVisible = false
                viewModel.listPetApi(token,page,limit,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,"")

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
                radiusRequest = ""
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
        adapterDialog = openDialog(requireContext(), result, flag, this)
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
            dataLoadFlag = false
            radiusRequest = ""
            stateRequest = ""
            countryRequest = ""
            cityRequest = ""
            viewModel.listPetApi(token,page,limit,latitude,longitude,20,radiusRequest,countryRequest,stateRequest,cityRequest,s.toString())

        }
    }




}