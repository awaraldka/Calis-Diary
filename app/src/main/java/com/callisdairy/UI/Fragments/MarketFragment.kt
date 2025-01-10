package com.callisdairy.UI.Fragments

import RequestPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.callisdairy.Adapter.LocationFilterAdapter
import com.callisdairy.Adapter.MarketBannerAdapter
import com.callisdairy.Adapter.MarketCategoryAdapter
import com.callisdairy.Adapter.MarketLatestListedPetAdapter
import com.callisdairy.Adapter.ProductListCategory
import com.callisdairy.Adapter.ServiceCategoryAdapter
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Adapter.productListAdapter
import com.callisdairy.Adapter.serviceListAdapter
import com.callisdairy.Interface.BannerSliderListener
import com.callisdairy.Interface.CategoryClick
import com.callisdairy.Interface.CategoryView
import com.callisdairy.Interface.GetFilterData
import com.callisdairy.Interface.InterestedClick
import com.callisdairy.Interface.LikeUnlikePet
import com.callisdairy.Interface.LikeUnlikeProduct
import com.callisdairy.Interface.LikeUnlikeService
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.ServiceClick
import com.callisdairy.Interface.productView
import com.callisdairy.Interface.serviceView
import com.callisdairy.Interface.viewPests
import com.callisdairy.ModalClass.MarketFilter
import com.callisdairy.ModalClass.MarketModalClass
import com.callisdairy.ModalClass.PetListDataModelClass
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Activities.PetDescriptionActivity
import com.callisdairy.UI.Activities.ProductDescriptionActivity
import com.callisdairy.UI.Activities.ServiceDescriptionActivity
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.releaseAllPlayers
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.BannerListDocs
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.ListCategoryDocs
import com.callisdairy.api.response.PetListDocs
import com.callisdairy.api.response.ProductListDocs
import com.callisdairy.api.response.ServiceListDocs
import com.callisdairy.databinding.FragmentMarketFragmenrtBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.MarketViewModel
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MarketFragment : Fragment(), productView, serviceView, viewPests,
    CategoryView, LikeUnlikePet, LikeUnlikeService, LikeUnlikeProduct,
    CategoryClick, ServiceClick, InterestedClick, PopupItemClickListener, BannerSliderListener,
    GetFilterData {

    private var _binding: FragmentMarketFragmenrtBinding? = null
    private val binding get() = _binding!!
    var categoryData: ArrayList<MarketModalClass> = ArrayList()
    private var fusedLocationClient: FusedLocationProviderClient? = null

    lateinit var adapterSearchFilter: LocationFilterAdapter
    val dataFilter = ArrayList<MarketFilter>()

    lateinit var marketBannerAdapter: MarketBannerAdapter


    var fromCome = ""
    var type = ""

    var seacrh = false
    var seacrhKeyword = ""

    var petListData: ArrayList<PetListDataModelClass> = ArrayList()

    lateinit var adapter: MarketCategoryAdapter
    lateinit var listadapter: MarketLatestListedPetAdapter
    lateinit var productListCategoryAdapter: ProductListCategory
    lateinit var productListAdapter: productListAdapter
    lateinit var serviceCategoryAdapter: ServiceCategoryAdapter
    lateinit var ServiceListAdapter: serviceListAdapter

    var dataProduct = ArrayList<ProductListDocs>()
    var dataPet = ArrayList<PetListDocs>()
    private var dataDoc = ArrayList<ServiceListDocs>()
    var latitude = 0.0
    var longitude = 0.0


    var durationPlay = 0L
    var docsBanner = ArrayList<BannerListDocs>()

    private val sliderHandler = Handler(Looper.getMainLooper())

    lateinit var sliderRunnable: Runnable

    //     Bottom Tab
    lateinit var SelectedHome: ImageView
    lateinit var UnSelectedHome: ImageView

    lateinit var UnSelectedMarket: ImageView
    lateinit var SelectedMarket: ImageView

    lateinit var UnSelectedCart: ImageView
    lateinit var SelectedCart: ImageView


    lateinit var UnSelectedProfile: ImageView
    lateinit var SelectedProfile: ImageView


    lateinit var UnSelectedFavorites: ImageView
    lateinit var SelectedFavorites: ImageView
    var sliderList: ArrayList<SlideModel> = ArrayList()


//   Tool Bar

    var token = ""

    lateinit var back: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView


    lateinit var Homeview: View
    lateinit var Marketview: View
    lateinit var Favoritesview: View
    lateinit var ProfileView: View
    lateinit var NotificationView: View
    lateinit var MenuView: View


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


    private val viewModel: MarketViewModel by viewModels()


    companion object {
        fun newInstance(): MarketFragment {
            return MarketFragment()
        }
    }

    fun setData(flag: String) {
        fromCome = flag
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketFragmenrtBinding.inflate(layoutInflater, container, false)
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }

        arguments?.getString("flag")?.let {
            fromCome = it
        }


        allIds()
        toolBarWithBottomTab()
        addData()

        addFilterData(Pets = false, Products = true, Services = false)



        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)
            .toString()

        RequestPermission.requestMultiplePermissions(requireActivity())
        click()
        getLocation()
        setCategoryFilter()

        binding.pullToRefresh.setOnRefreshListener {
            binding.DFsearch.setText("")
            seacrh = false
            radiusRequest = "10"
            binding.pullToRefresh.isRefreshing = false
        }


        back.setSafeOnClickListener {
            val fm: FragmentManager = requireActivity().supportFragmentManager
            if (flag == "MarketFragment") {
                for (i in 0 until fm.backStackEntryCount) {
                    fm.popBackStack()
                }
            } else {
                fm.popBackStack()
            }
        }


        sliderRunnable = Runnable {
            lifecycleScope.launch {
                val currentItem = binding.storeViewpager.currentItem
                val lastItem = binding.storeViewpager.adapter?.itemCount?.minus(1) ?: 0
                val banner = docsBanner.getOrNull(currentItem)?.bannerImage?.getOrNull(0)
                type = banner?.type ?: ""

                if (type == "video") {
                    durationPlay = (docsBanner.getOrNull(currentItem)?.duration?.toDouble()
                        ?.times(1000))?.toLong()!!
                }
                if (currentItem == lastItem) {
                    binding.storeViewpager.currentItem = 0
                } else {
                    binding.storeViewpager.currentItem = currentItem + 1
                }
            }
        }

        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                PlayerViewAdapter.playIndexThenPausePreviousPlayer(position)

                if (type == "video") {
                    sliderHandler.postDelayed(sliderRunnable, durationPlay)

                } else {
                    sliderHandler.postDelayed(sliderRunnable, 3000)
                }
            }
        }

        binding.storeViewpager.registerOnPageChangeCallback(callback)


        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == 0) {
                sliderHandler.postDelayed(sliderRunnable, 3000)
            } else {
                releaseAllPlayers()
                sliderHandler.removeCallbacks(sliderRunnable)
            }

        })





        binding.DFsearch.addTextChangedListener(textWatcherGlobalSearch)



        setCategoryAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListCategoryResponse()
        observeProductListResponse()
        observeLatestListedPetResponse()
        observeServiceListCategoryResponse()
        observeServiceListResponse()
        observeBannerListResponse()
        observeLikeProductResponse()
        observeLikePetResponse()
        observeLikeServiceResponse()
        observeAddToInterestedResponse()
        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()



        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    if (flag == "MarketFragment") {
                        for (i in 0 until fm.backStackEntryCount) {
                            fm.popBackStack()
                        }
                    } else {
                        fm.popBackStack()
                    }


                }
            })


    }


    private fun addData() {
        categoryData.clear()
        petListData.clear()
        categoryData.add(MarketModalClass(R.drawable.pets, getString(R.string.pets)))
        categoryData.add(MarketModalClass(R.drawable.market_product, getString(R.string.products)))
        categoryData.add(MarketModalClass(R.drawable.services, getString(R.string.services)))
        categoryData.add(
            MarketModalClass(
                R.drawable.vet_doctor,
                getString(R.string.vet_doctor_new)
            )
        )


    }

    private fun setCategoryAdapter() {
        binding.CategoryItem.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = MarketCategoryAdapter(requireContext(), categoryData, this)
        binding.CategoryItem.adapter = adapter
    }

    fun click() {

        binding.llFilter.setSafeOnClickListener {
            openFilterPopUp()
        }

        binding.ProductCategoryClick.setSafeOnClickListener {
            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "MarketProducts")
            startActivity(intent)

        }

        binding.serviceCategoryClick.setSafeOnClickListener {
            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "Service")
            startActivity(intent)

        }

        binding.LatestListClick.setSafeOnClickListener {
            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "ListedPet")
            startActivity(intent)

        }


    }


    private fun allIds() {
        SelectedHome = activity?.findViewById(R.id.SelectedHome)!!
        UnSelectedHome = activity?.findViewById(R.id.UnSelectedHome)!!

        UnSelectedMarket = activity?.findViewById(R.id.UnSelectedMarket)!!
        SelectedMarket = activity?.findViewById(R.id.SelectedMarket)!!

        UnSelectedCart = activity?.findViewById(R.id.UnSelectedCart)!!
        SelectedCart = activity?.findViewById(R.id.SelectedCart)!!

        UnSelectedProfile = activity?.findViewById(R.id.UnSelectedProfile)!!
        SelectedProfile = activity?.findViewById(R.id.SelectedProfile)!!

        SelectedFavorites = activity?.findViewById(R.id.SelectedFavorites)!!
        UnSelectedFavorites = activity?.findViewById(R.id.UnSelectedFavorites)!!


        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!

        Homeview = activity?.findViewById(R.id.Homeview)!!
        Marketview = activity?.findViewById(R.id.Marketview)!!
        Favoritesview = activity?.findViewById(R.id.Favoritesview)!!
        ProfileView = activity?.findViewById(R.id.ProfileView)!!
        NotificationView = activity?.findViewById(R.id.NotificationView)!!
        MenuView = activity?.findViewById(R.id.MenuView)!!


    }

    private fun toolBarWithBottomTab() {

        mainTitle.text = ""



        SelectedMarket.visibility = View.GONE
        UnSelectedMarket.visibility = View.VISIBLE

        SelectedHome.visibility = View.GONE
        UnSelectedHome.visibility = View.VISIBLE

        SelectedCart.visibility = View.VISIBLE
        UnSelectedCart.visibility = View.GONE

        UnSelectedProfile.visibility = View.VISIBLE
        SelectedProfile.visibility = View.GONE

        UnSelectedFavorites.visibility = View.VISIBLE
        SelectedFavorites.visibility = View.GONE

        if (fromCome == "MarketFragmentMenu") {
            mainTitle.visibility = View.GONE
            chat.visibility = View.VISIBLE
            back.visibility = View.VISIBLE
            search.visibility = View.GONE
            Username.text = getString(R.string.market)

            Homeview.isVisible = false
            Marketview.isVisible = false
            Favoritesview.isVisible = false
            ProfileView.isVisible = false
            NotificationView.isVisible = false
            MenuView.isVisible = true
        } else {
            mainTitle.visibility = View.VISIBLE
            chat.visibility = View.VISIBLE
            back.visibility = View.GONE
            search.visibility = View.GONE

            Homeview.isVisible = false
            Marketview.isVisible = true
            Favoritesview.isVisible = false
            ProfileView.isVisible = false
            NotificationView.isVisible = false
            MenuView.isVisible = false
            Username.text = getString(R.string.app_name)
        }


    }

    override fun petCategoryView(text: String) {

        when (text) {

            getString(R.string.pets) -> {
                val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "ListedPet")
                startActivity(intent)
            }

            getString(R.string.services) -> {
                val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "Service")
                startActivity(intent)
            }

            getString(R.string.vet_doctor_new) -> {
                val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "Doctor")
                startActivity(intent)
            }

            else -> {
                val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "MarketProducts")
                startActivity(intent)
            }
        }


    }


//    Category Observer

    private fun observeListCategoryResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._listCategoryData.collectLatest { response ->

                    when (response) {
                        is Resource.Success -> {

                            if (response.data?.statusCode == 200) {
                                try {
                                    setProductCategoryAdapter(response.data.result.docs)
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

    private fun setProductCategoryAdapter(docs: ArrayList<ListCategoryDocs>) {
        binding.ProductCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        productListCategoryAdapter = ProductListCategory(requireContext(), docs, this)
        binding.ProductCategory.adapter = productListCategoryAdapter
    }


//   Service Category Observer

    private fun observeServiceListCategoryResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._servicelistCategoryData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.statusCode == 200) {
                                try {
                                    setServiceCategoryAdapter(response.data.result.docs)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
//                            androidExtension.alertBox(message, requireContext())
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
        binding.serviceCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        serviceCategoryAdapter = ServiceCategoryAdapter(requireContext(), docs, this)
        binding.serviceCategory.adapter = serviceCategoryAdapter
    }


//    Product List Observer

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
                        latitude = (lastLocation)!!.latitude
                        longitude = (lastLocation).longitude

                        callApis(latitude, longitude)
                    } catch (e: IndexOutOfBoundsException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeProductListResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._productListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.statusCode == 200) {
                                Progresss.stop()
                                binding.progressBarSearch.isVisible = false
                                seacrh = false
                                try {

                                    binding.MainLayout.isVisible = true
                                    if (response.data.result.docs.size > 0) {
                                        binding.ProductListRecycler.isVisible = true
                                        binding.ProductListRecyclerNotFound.isVisible = false
                                        dataProduct = response.data.result.docs
                                        setProductListAdapter(response.data.result.docs)
                                    } else {
                                        binding.ProductListRecycler.isVisible = false
                                        binding.ProductListRecyclerNotFound.isVisible = true
                                    }


                                    if (dataPet.isEmpty() && dataDoc.isEmpty() && dataProduct.isEmpty()) {
                                        binding.notShow.isVisible = false
                                        binding.NoDataFound.isVisible = true
                                    } else {
                                        binding.notShow.isVisible = true
                                        binding.NoDataFound.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.progressBarSearch.isVisible = false
                            if (dataPet.size < 1 && dataDoc.size < 1 && dataProduct.size < 1) {
                                binding.notShow.isVisible = false
                                binding.NoDataFound.isVisible = true
                            } else {
                                binding.notShow.isVisible = true
                                binding.NoDataFound.isVisible = false
                                binding.ProductListRecycler.isVisible = dataProduct.size > 1
                                binding.ProductListRecyclerNotFound.isVisible = dataProduct.size < 1
                            }
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            if (!seacrh) {
                                Progresss.start(requireContext())
                            } else {
                                binding.progressBarSearch.isVisible = true
                            }
                        }

                        is Resource.Empty -> {

                        }

                    }

                }
            }

        }
    }

    private fun setProductListAdapter(docs: ArrayList<ProductListDocs>) {
        binding.ProductListRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        productListAdapter = productListAdapter(requireContext(), docs, this, this, this)
        binding.ProductListRecycler.adapter = productListAdapter
    }

    override fun view(
        _id: String,
        interested: Boolean,
        position: Int
    ) {
        val intent = Intent(requireContext(), ProductDescriptionActivity::class.java)
        intent.putExtra("id", _id)
        intent.putExtra("flag", "Market")
        intent.putExtra("type", interested)
        startActivity(intent)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlike(_id: String, position: Int, heart: ImageView, heartfill: ImageView) {
        if (heart.isVisible) {
            dataProduct[position].isLike = true
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, dataProduct.size)
            productListAdapter.notifyDataSetChanged()

        } else {
            dataProduct[position].isLike = false
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, dataProduct.size)
            productListAdapter.notifyDataSetChanged()

        }
        viewModel.addLikeProductApi(token, _id)
    }

    private fun observeLikeProductResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likeData.collectLatest { response ->

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

    override fun getCategoryValue(_id: String) {
        viewModel.productListApi(
            token,
            latitude,
            longitude,
            20,
            _id,
            "",
            1,
            10,
            radiusRequest,
            countryRequest,
            stateRequest,
            cityRequest,
            ""
        )
    }


//    Latest listed Pet observer

    private fun observeLatestListedPetResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._latestListedPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

                                    if (response.data.result.docs.size > 0) {
                                        binding.LatestListPets.isVisible = true
                                        binding.LatestListPetsNotFound.isVisible = false

                                        dataPet = response.data.result.docs
                                        setPetListAdapter(response.data.result.docs)
                                    } else {
                                        binding.LatestListPets.isVisible = false
                                        binding.LatestListPetsNotFound.isVisible = true
                                    }

                                    if (dataPet.isEmpty() && dataDoc.isEmpty() && dataProduct.isEmpty()) {
                                        binding.notShow.isVisible = false
                                        binding.NoDataFound.isVisible = true
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            if (dataPet.size < 1 && dataDoc.size < 1 && dataProduct.size < 1) {
                                binding.notShow.isVisible = false
                                binding.NoDataFound.isVisible = true
                            } else {
                                binding.notShow.isVisible = true
                                binding.NoDataFound.isVisible = false
                                binding.LatestListPets.isVisible = dataPet.size > 1
                                binding.LatestListPetsNotFound.isVisible = dataPet.size < 1
                            }
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.LatestListPets.isVisible = false
                            binding.LatestListPetsNotFound.isVisible = false
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }

        }
    }

    private fun setPetListAdapter(docs: ArrayList<PetListDocs>) {
        binding.LatestListPets.layoutManager = GridLayoutManager(requireContext(), 2)
        listadapter = MarketLatestListedPetAdapter(requireContext(), docs, this, this, this)
        binding.LatestListPets.adapter = listadapter
    }

    override fun viewPet(_id: String, interested: Boolean) {
        val intent = Intent(requireContext(), PetDescriptionActivity::class.java)
        intent.putExtra("id", _id)
        intent.putExtra("flag", "Market")
        intent.putExtra("type", interested)
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


        viewModel.likeUnlikePetApi(token, _id)
    }

    private fun observeLikePetResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likePet.collectLatest { response ->

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
//                                androidExtension.alertBox(message, requireContext())
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


// Service List API

    private fun observeServiceListResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._ServiceListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

                                    if (response.data.result.docs.size > 0) {
                                        binding.serviceList.isVisible = true
                                        binding.serviceListNotFound.isVisible = false
                                        dataDoc = response.data.result.docs
                                        setServiceListAdapter(response.data.result.docs)
                                    } else {
                                        binding.serviceList.isVisible = false
                                        binding.serviceListNotFound.isVisible = true
                                    }

                                    if (dataPet.isEmpty() && dataDoc.isEmpty() && dataProduct.isEmpty()) {
                                        binding.notShow.isVisible = false
                                        binding.NoDataFound.isVisible = true
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {

                            if (dataPet.size < 1 && dataDoc.size < 1 && dataProduct.size < 1) {
                                binding.notShow.isVisible = false
                                binding.NoDataFound.isVisible = true
                            } else {
                                binding.notShow.isVisible = true
                                binding.NoDataFound.isVisible = false
                                binding.serviceList.isVisible = dataDoc.size > 1
                                binding.serviceListNotFound.isVisible = dataDoc.size < 1
                            }

                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.serviceList.isVisible = false
                            binding.serviceListNotFound.isVisible = false
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }

        }
    }

    private fun setServiceListAdapter(docs: ArrayList<ServiceListDocs>) {
        binding.serviceList.layoutManager = LinearLayoutManager(requireContext())
        ServiceListAdapter = serviceListAdapter(requireContext(), docs, this, this, this)
        binding.serviceList.adapter = ServiceListAdapter
    }

    override fun viewDes(_id: String, interested: Boolean) {
        val intent = Intent(requireContext(), ServiceDescriptionActivity::class.java)
        intent.putExtra("id", _id)
        intent.putExtra("flag", "Market")
        intent.putExtra("type", interested)

        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlikeService(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) {
        if (heart.isVisible) {
            dataDoc[position].isLike = true
            ServiceListAdapter.notifyItemChanged(position)
            ServiceListAdapter.notifyItemRangeChanged(position, dataDoc.size)
            ServiceListAdapter.notifyDataSetChanged()

        } else {
            dataDoc[position].isLike = false
            ServiceListAdapter.notifyItemChanged(position)
            ServiceListAdapter.notifyItemRangeChanged(position, dataDoc.size)
            ServiceListAdapter.notifyDataSetChanged()

        }
        viewModel.likeUnlikeServicesApi(token, _id)
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

    override fun getServiceValue(_id: String) {
        viewModel.listServiceApi(
            token,
            1,
            10,
            _id,
            "",
            latitude,
            longitude,
            20,
            radiusRequest,
            countryRequest,
            stateRequest,
            cityRequest,
            ""
        )
    }


//    Add To Interested Apis and Observer


    @SuppressLint("NotifyDataSetChanged")
    override fun productInterest(_id: String, position: Int) {

        if (!dataProduct[position].isInterested) {
            dataProduct[position].isInterested = true
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, dataProduct.size)
            productListAdapter.notifyDataSetChanged()

        }

        viewModel.addToInterestedApi(token, "PRODUCT", "", _id, "")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun petInterest(_id: String, position: Int) {

        if (!dataPet[position].isInterested) {
            dataPet[position].isInterested = true
            listadapter.notifyItemChanged(position)
            listadapter.notifyItemRangeChanged(position, dataPet.size)
            listadapter.notifyDataSetChanged()

        }


        viewModel.addToInterestedApi(token, "PET", _id, "", "")

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun serviceInterest(_id: String, position: Int) {
        if (!dataDoc[position].isInterested) {
            dataDoc[position].isInterested = true
            ServiceListAdapter.notifyItemChanged(position)
            ServiceListAdapter.notifyItemRangeChanged(position, dataDoc.size)
            ServiceListAdapter.notifyDataSetChanged()

        }
        viewModel.addToInterestedApi(token, "SERVICE", "", "", _id)

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


    private fun callApis(latitude: Double, longitude: Double) {


        Log.d("GEt LAt Long", "latitude longitude: $latitude $longitude")



        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

            val callAllApis = listOf(
                async { viewModel.listCategoryApi("PRODUCT") },
                async { viewModel.bannerListApi() },
                async {
                    viewModel.productListApi(
                        token,
                        latitude,
                        longitude,
                        80,
                        "",
                        "",
                        1,
                        4,
                        radiusRequest,
                        countryRequest,
                        stateRequest,
                        cityRequest,
                        ""
                    )
                },
                async {
                    viewModel.listPetApi(
                        token,
                        1,
                        4,
                        latitude,
                        longitude,
                        80,
                        radiusRequest,
                        countryRequest,
                        stateRequest,
                        cityRequest,
                        ""
                    )
                },
                async { viewModel.serviceListCategoryApi("SERVICE") },
                async {
                    viewModel.listServiceApi(
                        token,
                        1,
                        4,
                        "",
                        "",
                        latitude,
                        longitude,
                        80,
                        radiusRequest,
                        countryRequest,
                        stateRequest,
                        cityRequest,
                        ""
                    )
                },

                )
            callAllApis.awaitAll()        // use awaitAll to wait for all network requests
        }

    }


//     Banner List Api observer


    private fun observeBannerListResponse() {


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._bannerListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {

                                try {
                                    docsBanner.clear()
                                    docsBanner = response.data.result.docs



                                    binding.storeViewpager.isVisible =
                                        response.data.result.docs.isNotEmpty()
                                    binding.indicator.isVisible =
                                        response.data.result.docs.isNotEmpty()


                                    setImageAdaptor()

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
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


    private fun setImageAdaptor() {

        marketBannerAdapter = MarketBannerAdapter(requireContext(), docsBanner, this)
        binding.storeViewpager.adapter = marketBannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    override fun slider(s: String, videoUrl: String, duration: Long) {
    }


    override fun onPause() {
        super.onPause()
        releaseAllPlayers()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }


    // Post Filter


    private fun openFilterPopUp() {
        try {
            val bindingPopup = LayoutInflater.from(requireActivity())
                .inflate(R.layout.missing_pet_filter_popup, null)!!
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




            sixMiles.text = getString(R.string.all_vendors)
            popupTitle.text = getString(R.string.all_pps)
            clearFilterData(oneMiles, twoMiles, threeMiles, fourMiles, fiveMiles, sixMiles)

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
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val callAllApis = listOf(
                        async {
                            viewModel.productListApi(
                                token,
                                latitude,
                                longitude,
                                20,
                                "",
                                "",
                                1,
                                4,
                                radiusRequest,
                                countryRequest,
                                stateRequest,
                                cityRequest,
                                ""
                            )
                        },
                        async {
                            viewModel.listPetApi(
                                token,
                                1,
                                4,
                                latitude,
                                longitude,
                                20,
                                radiusRequest,
                                countryRequest,
                                stateRequest,
                                cityRequest,
                                ""
                            )
                        },
                        async {
                            viewModel.listServiceApi(
                                token,
                                1,
                                4,
                                "",
                                "",
                                latitude,
                                longitude,
                                20,
                                radiusRequest,
                                countryRequest,
                                stateRequest,
                                cityRequest,
                                ""
                            )
                        },
                    )
                    callAllApis.awaitAll()
                }

                dialog1.dismiss()
            }

            clearAllButton.setSafeOnClickListener {
                clearFilterData(oneMiles, twoMiles, threeMiles, fourMiles, fiveMiles, sixMiles)
            }

            oneMiles.setSafeOnClickListener {
                radiusRequest = "10"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                oneMiles.setBackgroundResource(R.drawable.button_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            twoMiles.setSafeOnClickListener {
                radiusRequest = "20"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.button_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            threeMiles.setSafeOnClickListener {
                radiusRequest = "30"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.button_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            fourMiles.setSafeOnClickListener {
                radiusRequest = "40"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.button_background)
                fiveMiles.setBackgroundResource(R.drawable.border_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            fiveMiles.setSafeOnClickListener {
                radiusRequest = "50"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                oneMiles.setBackgroundResource(R.drawable.border_background)
                twoMiles.setBackgroundResource(R.drawable.border_background)
                threeMiles.setBackgroundResource(R.drawable.border_background)
                fourMiles.setBackgroundResource(R.drawable.border_background)
                fiveMiles.setBackgroundResource(R.drawable.button_background)
                sixMiles.setBackgroundResource(R.drawable.border_background)
            }

            sixMiles.setSafeOnClickListener {
                radiusRequest = "All Vendor"
                oneMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                twoMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                threeMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fourMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fiveMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                sixMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
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
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
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


    private fun clearFilterData(
        oneMiles: TextView,
        twoMiles: TextView,
        threeMiles: TextView,
        fourMiles: TextView,
        fiveMiles: TextView,
        sixMiles: TextView
    ) {
        radiusRequest = "10"
        countryRequest = ""
        stateRequest = ""
        cityRequest = ""
        country.text = ""
        city.text = ""
        state.text = ""

        resetButtonColors(oneMiles, twoMiles, threeMiles, fourMiles, fiveMiles, sixMiles)


    }

    private fun resetButtonColors(
        oneMiles: TextView,
        twoMiles: TextView,
        threeMiles: TextView,
        fourMiles: TextView,
        fiveMiles: TextView,
        sixMiles: TextView
    ) {
        oneMiles.setBackgroundResource(R.drawable.button_background)
        twoMiles.setBackgroundResource(R.drawable.border_background)
        threeMiles.setBackgroundResource(R.drawable.border_background)
        fourMiles.setBackgroundResource(R.drawable.border_background)
        fiveMiles.setBackgroundResource(R.drawable.border_background)
        sixMiles.setBackgroundResource(R.drawable.border_background)


        oneMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        twoMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        threeMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        fourMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        fiveMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        sixMiles.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

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

            if (s.toString().isEmpty()) {
                seacrh = false
                binding.SearchBasedOn.isVisible = false
                callApisFilters("", "", "")
            } else {
                seacrh = true
                seacrhKeyword = s.toString()
                binding.SearchBasedOn.isVisible = true
                viewModel.productListApi(
                    token,
                    latitude,
                    longitude,
                    80,
                    "",
                    "",
                    1,
                    4,
                    radiusRequest,
                    countryRequest,
                    stateRequest,
                    cityRequest,
                    s.toString()
                )

            }


        }
    }

//  Based On Category Filter Data

    private fun setCategoryFilter() {
        binding.SearchBasedOn.layoutManager = LinearLayoutManager(requireContext())
        adapterSearchFilter = LocationFilterAdapter(requireContext(), dataFilter, this)
        binding.SearchBasedOn.adapter = adapterSearchFilter
    }

    override fun getFilterData(Name: String) {
        binding.SearchBasedOn.isVisible = false
        seacrh = true
        when (Name) {
            "Pets" -> {
                addFilterData(Pets = true, Products = false, Services = false)
                callApisFilters("", seacrhKeyword, "")
            }

            "Products" -> {
                addFilterData(Pets = false, Products = true, Services = false)
                callApisFilters(seacrhKeyword, "", "")
            }

            "Services" -> {
                addFilterData(Pets = false, Products = false, Services = true)
                callApisFilters("", "", seacrhKeyword)

            }
        }

    }


    private fun addFilterData(Pets: Boolean, Products: Boolean, Services: Boolean) {
        dataFilter.clear()
        dataFilter.add(MarketFilter("Pets", Pets))
        dataFilter.add(MarketFilter("Products", Products))
        dataFilter.add(MarketFilter("Services", Services))
    }

    private fun callApisFilters(product: String, pet: String, service: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val callAllApis = listOf(
                async {
                    viewModel.productListApi(
                        token,
                        latitude,
                        longitude,
                        80,
                        "",
                        "",
                        1,
                        4,
                        radiusRequest,
                        countryRequest,
                        stateRequest,
                        cityRequest,
                        product
                    )
                },
                async {
                    viewModel.listPetApi(
                        token,
                        1,
                        4,
                        latitude,
                        longitude,
                        80,
                        radiusRequest,
                        countryRequest,
                        stateRequest,
                        cityRequest,
                        pet
                    )
                },
                async {
                    viewModel.listServiceApi(
                        token,
                        1,
                        4,
                        "",
                        "",
                        latitude,
                        longitude,
                        80,
                        radiusRequest,
                        countryRequest,
                        stateRequest,
                        cityRequest,
                        service
                    )
                },
            )
            callAllApis.awaitAll()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        releaseAllPlayers()
    }


}