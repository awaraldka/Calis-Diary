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
import androidx.fragment.app.Fragment
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.*
import com.callisdairy.Interface.*
import com.callisdairy.R
import com.callisdairy.UI.Activities.ProductDescriptionActivity
import com.callisdairy.Utils.*
import com.callisdairy.api.response.ListCategoryDocs
import com.callisdairy.api.response.ProductListDocs
import com.callisdairy.api.response.SubCategoryDocs
import com.callisdairy.databinding.FragmentMarketProductCategoryBinding
import com.callisdairy.viewModel.MarketProductViewAllViewModel
import com.callisdairy.api.response.CountryList
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketProductCategoryFragment : Fragment(), productView, CategoryClick, LikeUnlikeProduct,
    SubCategoryClick , InterestedClick,PopupItemClickListener {


    private var _binding: FragmentMarketProductCategoryBinding? =  null
    private val binding get() = _binding!!
    lateinit var productListCategoryAdapter:ProductListCategory
    lateinit var productListSubCategoryAdapter:ProductListSubCategory
    lateinit var productListAdapter: productListAdapter
    var flag = ""
    var categoryId = ""
    var subCategoryId = ""
    var token = ""
    var latitude  = 0.0
    var longitude = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var data = ArrayList<ProductListDocs>()
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
    var flagFilter = ""
    var countryCode = ""
    var filterData: ArrayList<CountryList> = ArrayList()
    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var country: TextView
    lateinit var state: TextView
    lateinit var city: TextView
    var applyFilter = false



    private val viewModel: MarketProductViewAllViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentMarketProductCategoryBinding.inflate(layoutInflater, container, false)
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        backTitle = activity?.findViewById(R.id.backTitle)!!
        arguments?.getString("flag")?.let {
            flag = it
        }
        token  = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        data.clear()
        getLocation()

        viewModel.listCategoryApi("PRODUCT")


        binding.pullToRefresh.setOnRefreshListener {
            loaderFlag = true
            categoryId = ""
            subCategoryId = ""
            page = 1
            limit = 10
            data.clear()
            binding.ProductSubCategory.isVisible = false
            binding.ProgressBarScroll.isVisible = false

            viewModel.listCategoryApi("PRODUCT")
            binding.DFsearch.text = Editable.Factory.getInstance().newEditable("")



            binding.pullToRefresh.isRefreshing = false
        }


        binding.DFsearch.addTextChangedListener(textWatcherGlobalSearch)



        binding.filterClick.setOnClickListener {
            openFilterPopUp()
        }


        backTitle.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.productListApi(token,latitude,longitude,20,categoryId, subCategoryId,page, limit,radiusRequest,countryRequest,stateRequest,cityRequest,"")
                }
            }
        })


        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListCategoryResponse()
        observeSubCategoryResponse()
        observeResponseProductList()
        observeLikeProductResponse()
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



//    Category Observer

    private fun observeListCategoryResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._listCategoryData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if(response.data?.statusCode == 200) {
                                try {

                                    binding.ProductCategoryName.isVisible = response.data.result.docs.size  > 0

                                    setProductCategoryAdapter(response.data.result.docs)



                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.ProductCategoryName.isVisible = false
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.ProductCategoryName.isVisible = false
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }

        }
    }

    private fun setProductCategoryAdapter(docs: ArrayList<ListCategoryDocs>) {
        binding.ProductCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productListCategoryAdapter = ProductListCategory(requireContext(), docs,this)
        binding.ProductCategory.adapter = productListCategoryAdapter
    }

    override fun getCategoryValue(_id: String) {
        categoryId = _id
        subCategoryId = ""
        dataLoadFlag = false
        binding.ProductSubCategory.isVisible = true
        viewModel.productListApi(token,latitude,longitude,20,_id, "",1, 10,radiusRequest,countryRequest,stateRequest,cityRequest,"")
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
                                    setProductSubCategoryAdapter(response.data.result.docs)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
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
        binding.ProductSubCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productListSubCategoryAdapter = ProductListSubCategory(requireContext(), docs,this)
        binding.ProductSubCategory.adapter = productListSubCategoryAdapter
    }



    override fun getSubCategoryValue(_id: String, subCategoryName: String) {
        subCategoryId = _id
        dataLoadFlag = false
        viewModel.productListApi(token,latitude,longitude,20,categoryId, _id,1, 10,radiusRequest,countryRequest,stateRequest,cityRequest,"")
        binding.categoryName.text = subCategoryName
    }


//    Product List Observer

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
                        viewModel.productListApi(token,latitude,longitude,20,categoryId, subCategoryId,page, limit,radiusRequest,countryRequest,stateRequest,cityRequest,"")


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

    private fun observeResponseProductList() {

       lifecycleScope.launch {
           lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
               viewModel._productListData.collectLatest{ response ->

                   when (response) {

                       is Resource.Success -> {

                           Progresss.stop()
                           if(response.data?.statusCode == 200) {
                               try {
                                   loaderFlag = false
                                   if (!dataLoadFlag) {
                                       data.clear()
                                   }
                                   if (response.data.result.docs.isNotEmpty()){
                                       binding.ProductListRecycler.isVisible = true
                                       binding.NotFound.isVisible = false
                                       data.addAll(response.data.result.docs)
                                       pages = response.data.result.totalPages
                                       page = response.data.result.page
                                       setProductListAdapter()
                                   }else{
                                       binding.ProductListRecycler.isVisible = false
                                       binding.categoryName.isVisible = false
                                       binding.NotFound.isVisible = true

                                   }



                               }catch (e:Exception){
                                   e.printStackTrace()
                               }
                           }
                       }

                       is Resource.Error -> {
                           Progresss.stop()
                           binding.ProductListRecycler.isVisible = false
                           binding.NotFound.isVisible = true
                           binding.categoryName.isVisible = false
                           response.message?.let { message ->
                               if (!message.lowercase().contains("data not found")){
                                   androidExtension.alertBox(message, requireContext())
                               }
                           }

                       }

                       is Resource.Loading -> {
                           binding.NotFound.isVisible = false

                           if (loaderFlag){
                               Progresss.start(requireContext())
                               binding.categoryName.isVisible = false
                               binding.ProductListRecycler.isVisible = false
                           }

                       }

                       is Resource.Empty -> {

                       }

                   }

               }
           }


        }

    }

    private fun setProductListAdapter(){
        binding.ProductListRecycler.layoutManager = GridLayoutManager(requireContext(),2)
        productListAdapter = productListAdapter(requireContext(), data,this,this, this)
        binding.ProductListRecycler.adapter = productListAdapter
    }

    override fun view(
        _id: String,
        interested: Boolean,
        position: Int
    ) {
        val intent = Intent(requireContext(), ProductDescriptionActivity::class.java)
        intent.putExtra("id",_id)
        intent.putExtra("flag","Market")
        intent.putExtra("type",interested)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlike(_id: String, position: Int, heart: ImageView, heartfill: ImageView) {
        if (heart.isVisible) {
            data[position].isLike = true
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, data.size)
            productListAdapter.notifyDataSetChanged()

        } else {
            data[position].isLike = false
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, data.size)
            productListAdapter.notifyDataSetChanged()
        }

            viewModel.addLikeProductApi(token,_id)
    }

    private fun observeLikeProductResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likeData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    }catch (e: Exception) {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun productInterest(_id: String, position: Int) {
        if (!data[position].isInterested) {
            data[position].isInterested = true
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, data.size)
            productListAdapter.notifyDataSetChanged()

        }

        viewModel.addToInterestedApi(token,"PRODUCT","",_id,"")
    }

    override fun petInterest(_id: String, position: Int) {

    }

    override fun serviceInterest(_id: String, position: Int) {

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

            sixMiles.text = getString(R.string.all_produtcs)
            popupTitle.text = getString(R.string.all_produtcs_globlay)
            clearFilterData(oneMiles,twoMiles,threeMiles,fourMiles,fiveMiles,sixMiles)

            dialougbackButton.setOnClickListener { dialog1.dismiss() }
            llCountry.setOnClickListener {
                flagFilter = "Country"
                selectionPopUp(flagFilter)
            }

            llState.setOnClickListener {
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

            llCity.setOnClickListener {
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

            applyButton.setOnClickListener {
                page = 1
                limit =10
                subCategoryId = ""
                categoryId  = ""
                dataLoadFlag = false
                loaderFlag = true
                binding.ProgressBarScroll.isVisible = false
                viewModel.productListApi(token,latitude,longitude,20,categoryId, subCategoryId,page, limit,radiusRequest,countryRequest,stateRequest,cityRequest,"")

                dialog1.dismiss()
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
                radiusRequest = "All Products"
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
            dialougbackButton.setOnClickListener { dialog.dismiss() }


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
            subCategoryId = ""
            categoryId  = ""
            dataLoadFlag = false
            radiusRequest = "10"
            countryRequest = ""
            stateRequest = ""
            cityRequest = ""
            viewModel.productListApi(token,latitude,longitude,20,categoryId, subCategoryId,page, limit,radiusRequest,countryRequest,stateRequest,cityRequest,s.toString())

        }
    }





}