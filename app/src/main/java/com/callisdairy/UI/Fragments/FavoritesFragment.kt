package com.callisdairy.UI.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
import com.callisdairy.Adapter.FavPetAdapter
import com.callisdairy.Adapter.FavProductAdapter
import com.callisdairy.Adapter.FavServicesAdapter
import com.callisdairy.Interface.InterestedClick
import com.callisdairy.Interface.LikeUnlikePet
import com.callisdairy.Interface.LikeUnlikeProduct
import com.callisdairy.Interface.LikeUnlikeService
import com.callisdairy.Interface.productView
import com.callisdairy.Interface.serviceView
import com.callisdairy.Interface.viewChat
import com.callisdairy.Interface.viewPests
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.UI.Activities.PetDescriptionActivity
import com.callisdairy.UI.Activities.ProductDescriptionActivity
import com.callisdairy.UI.Activities.ServiceDescriptionActivity
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.FavPetDocs
import com.callisdairy.api.response.FavProductDocs
import com.callisdairy.api.response.FavServiceDocs
import com.callisdairy.databinding.FragmentFavoritesBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoritesFragment : Fragment(), viewPests, viewChat, LikeUnlikePet, serviceView,
    LikeUnlikeService, productView, LikeUnlikeProduct, InterestedClick {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    lateinit var listadapter: FavPetAdapter
    lateinit var productListAdapter: FavProductAdapter
    lateinit var servicesAdapter: FavServicesAdapter


    lateinit var Homeview: View
    lateinit var Marketview: View
    lateinit var Favoritesview: View
    lateinit var ProfileView: View
    lateinit var NotificationView: View
    lateinit var MenuView: View

//   Tool Bar

    lateinit var back: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView

    var token = ""
    var flag = ""
    var flag1 = ""

    var petPosition = 0
    var productPosition = 0
    var servicePosition = 0
    var selectedTab = false
    //     Bottom Tab
    lateinit var SelectedHome: ImageView
    lateinit var UnSelectedHome: ImageView

    lateinit var UnSelectedMarket: ImageView
    lateinit var SelectedMarket: ImageView

    lateinit var UnSelectedCart: ImageView
    lateinit var SelectedCart: ImageView


    lateinit var UnSelectedProfile: ImageView
    lateinit var SelectedProfile: ImageView


    lateinit var SelectedFavorites: ImageView
    lateinit var UnSelectedFavorites: ImageView

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true
    var favPetData = ArrayList<FavPetDocs>()


    var pagesService = 0
    var pageService = 1
    var limitService = 10
    var dataLoadFlagService = false
    var loaderFlagService = true
    var favServiceData = ArrayList<FavServiceDocs>()


    var pagesProduct = 0
    var pageProduct = 1
    var limitProduct = 10
    var dataLoadFlagProduct = false
    var loaderFlagProduct = true
    var favProductData = ArrayList<FavProductDocs>()

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    fun setData(passedKey: String) {
        flag = passedKey
    }




    private val viewModel: FavoritesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)

        arguments?.getString("flag")?.let {
            flag = it
        }
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token)
            .toString()
        allIds()
        toolBarWithBottomTab()
        clicks()

        favPetData.clear()
        page = 1
        limit = 10

        favProductData.clear()
        pageProduct = 1
        limitProduct = 10

        favServiceData.clear()
        pageService = 1
        limitService = 10





        viewLifecycleOwner.lifecycleScope.launch {
            val observeAll = listOf(
                async { viewModel.favoritesPetApi(token, page, limit) },
                async { viewModel.favoritesServiceApi(token, pageService, limitService) },
                async { viewModel.favoritesProductApi(token, pageProduct, limitProduct) },
            )
            observeAll.awaitAll()
        }


        binding.favPullToRefresh.setOnRefreshListener {
            binding.progressBar.isVisible = false
            when (flag1) {
                "PRODUCT" -> {
                    favProductData.clear()
                    pageProduct = 1
                    limitProduct = 10
                    dataLoadFlagProduct = false
                    loaderFlagProduct = true


                    viewModel.favoritesProductApi(token, pageProduct, limitProduct)
                    binding.favPullToRefresh.isRefreshing = false
                }
                "SERVICE" -> {
                    favServiceData.clear()
                    pageService = 1
                    limitService = 10
                    dataLoadFlagService = false
                    loaderFlagService = true

                    viewModel.favoritesServiceApi(token, pageService, limitService)
                    binding.favPullToRefresh.isRefreshing = false
                }
                else -> {
                    favPetData.clear()
                    page = 1
                    limit = 10
                    dataLoadFlag = false
                    loaderFlag = true
                    viewModel.favoritesPetApi(token, page, limit)
                    binding.favPullToRefresh.isRefreshing = false
                }
            }

        }






        back.setSafeOnClickListener {
            val fm: FragmentManager = requireActivity().supportFragmentManager
            if (flag == "Favourites") {
                for (i in 0 until fm.backStackEntryCount) {
                    fm.popBackStack()
                }
            } else {
                fm.popBackStack()
            }
        }

        binding.petNestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            when (flag1) {
                "PET" -> {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        dataLoadFlag = true
                        page++
                        binding.progressBar.visibility = View.VISIBLE
                        if (page > pages) {
                            binding.progressBar.visibility = View.GONE
                        } else {
                            viewModel.favoritesPetApi(token, page, limit)
                        }
                    }
                }

                "PRODUCT" -> {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        dataLoadFlagProduct = true
                        pageProduct++
                        binding.progressBar.visibility = View.VISIBLE
                        if (pageProduct > pagesProduct) {
                            binding.progressBar.visibility = View.GONE
                        } else {
                            viewModel.favoritesProductApi(token, pageProduct, limitProduct)
                        }
                    }
                }

                "SERVICE" -> {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        dataLoadFlagService = true
                        pageService++
                        binding.progressBar.visibility = View.VISIBLE
                        if (pageService > pagesService) {
                            binding.progressBar.visibility = View.GONE
                        } else {
                            viewModel.favoritesServiceApi(token, pageService, limitService)
                        }
                    }
                }

                else -> {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        dataLoadFlag = true
                        page++
                        binding.progressBar.visibility = View.VISIBLE
                        if (page > pages) {
                            binding.progressBar.visibility = View.GONE
                        } else {
                            viewModel.favoritesPetApi(token, page, limit)
                        }
                    }
                }


            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavPetResponse()
        observeFavServiceResponse()
        observeFavProductResponse()
        observeAddToInterestedResponse()
        observeLikeProductResponse()
        observeLikePetResponse()
        observeLikeServiceResponse()


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (selectedTab){
                        binding.petNestedScrollView.scrollY = 0
                        selectedTab = false
                        petDataManage()
                    }else{
                        val fm: FragmentManager = requireActivity().supportFragmentManager
                        if (flag == "Favourites") {
                            for (i in 0 until fm.backStackEntryCount) {
                                fm.popBackStack()
                            }
                        } else {
                            fm.popBackStack()
                        }
                    }




                }
            })

    }


    private fun clicks() {
        binding.petButton.setSafeOnClickListener {
            flag1 = "PET"
            selectedTab = false
            binding.petNestedScrollView.scrollY = 0
            binding.progressBar.isVisible = false
            petDataManage()
        }

        binding.productButton.setSafeOnClickListener {
            flag1 = "PRODUCT"
            selectedTab = true
            binding.progressBar.isVisible = false
            binding.petNestedScrollView.scrollY = 0
            productsDataManage()

        }


        binding.serviceButton.setSafeOnClickListener {
            flag1 = "SERVICE"
            selectedTab = true
            binding.progressBar.isVisible = false
            binding.petNestedScrollView.scrollY = 0
            serviceDataManage()
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


        Homeview = activity?.findViewById(R.id.Homeview)!!
        Marketview = activity?.findViewById(R.id.Marketview)!!
        Favoritesview = activity?.findViewById(R.id.Favoritesview)!!
        ProfileView = activity?.findViewById(R.id.ProfileView)!!
        NotificationView = activity?.findViewById(R.id.NotificationView)!!
        MenuView = activity?.findViewById(R.id.MenuView)!!


        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!


    }

    private fun toolBarWithBottomTab() {

        when (flag) {
            "Favourites" -> {
                mainTitle.visibility = View.VISIBLE
                chat.visibility = View.VISIBLE
                back.visibility = View.GONE
                search.visibility = View.VISIBLE
                Username.text = getString(R.string.app_name)

                UnSelectedFavorites.visibility = View.GONE
                SelectedFavorites.visibility = View.VISIBLE
                Favoritesview.isVisible = true

            }
            "FavouritesMenu" -> {
                mainTitle.visibility = View.VISIBLE
                chat.visibility = View.VISIBLE
                back.visibility = View.VISIBLE
                search.visibility = View.VISIBLE
                Username.text = getString(R.string.app_name)

                Favoritesview.isVisible = false
                MenuView.isVisible = true

                UnSelectedFavorites.visibility = View.VISIBLE
                SelectedFavorites.visibility = View.GONE
            }


        }



        SelectedMarket.visibility = View.GONE
        UnSelectedMarket.visibility = View.VISIBLE

        SelectedHome.visibility = View.GONE
        UnSelectedHome.visibility = View.VISIBLE

        SelectedCart.visibility = View.GONE
        UnSelectedCart.visibility = View.VISIBLE

        UnSelectedProfile.visibility = View.VISIBLE
        SelectedProfile.visibility = View.GONE


        Homeview.isVisible = false
        Marketview.isVisible = false
        ProfileView.isVisible = false
        NotificationView.isVisible = false

    }


    //   fav Pet Observer

    private fun observeFavPetResponse() {

        lifecycleScope.launch {
            viewModel._FavoritesPetData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {
                        if (response.data?.responseCode == 200) {
                            try {
                                Progresss.stop()
                                loaderFlag = false
                                if (!dataLoadFlag) {
                                    favPetData.clear()
                                }
                                binding.petButton.isEnabled = true
                                favPetData.addAll(response.data.result.docs)
                                pages = response.data.result.totalPages
                                page = response.data.result.page
                                interestedPetAdapter()
                                setDataAccordingly()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        setDataAccordingly()
                        response.message?.let { message ->
                            androidExtension.alertBox(message, requireContext())
                        }
                    }

                    is Resource.Loading -> {

                        if (loaderFlag) {
                            binding.NotFound.isVisible = false
                            binding.PetListRecycler.isVisible = false
                            Progresss.start(requireContext())
                        }
                    }

                    is Resource.Empty -> {

                    }

                }

            }
        }


    }

    private fun interestedPetAdapter() {
        binding.PetListRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        listadapter = FavPetAdapter(requireContext(), favPetData, this, this, this, this)
        binding.PetListRecycler.adapter = listadapter
    }


//   fav Service Observer

    private fun observeFavServiceResponse() {

        lifecycleScope.launch {
            viewModel._FavoritesServiceData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {
                        if (response.data?.responseCode == 200) {
                            try {

                                loaderFlagService = false
                                if (!dataLoadFlagService) {
                                    favServiceData.clear()
                                }
                                binding.serviceButton.isEnabled = true
                                favServiceData.addAll(response.data.result.docs)
                                pagesService = response.data.result.totalPages
                                pageService = response.data.result.page
                                setServiceListAdapter()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.serviceButton.isEnabled = true
                        response.message?.let { message ->
                        }
                    }

                    is Resource.Loading -> {
                        if (loaderFlagService) {
                        }
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }


    }

    private fun setServiceListAdapter() {
        binding.ServiceListRecycler.layoutManager = LinearLayoutManager(requireContext())
        servicesAdapter =
            FavServicesAdapter(requireContext(), favServiceData, this, this, this, this)
        binding.ServiceListRecycler.adapter = servicesAdapter
    }


//   fav Product observer

    private fun observeFavProductResponse() {

        lifecycleScope.launch {
            viewModel._FavoritesProductData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

                        if (response.data?.responseCode == 200) {
                            try {

                                loaderFlagProduct = false
                                if (!dataLoadFlagProduct) {
                                    favProductData.clear()
                                }
                                binding.productButton.isEnabled = true
                                favProductData.addAll(response.data.result.docs)
                                pagesProduct = response.data.result.totalPages
                                pageProduct = response.data.result.page
                                setProductListAdapter()


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.productButton.isEnabled = true
                        response.message?.let { message ->
                        }
                    }

                    is Resource.Loading -> {
                        binding.NotFound.isVisible = false
                        if (loaderFlagService) {
                        }

                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }


    }

    private fun setProductListAdapter() {
        binding.ProductListRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        productListAdapter =
            FavProductAdapter(requireContext(), favProductData, this, this, this, this)
        binding.ProductListRecycler.adapter = productListAdapter
    }


    override fun viewPet(_id: String, interested: Boolean) {


        val intent = Intent(requireContext(), PetDescriptionActivity::class.java)
        intent.putExtra("id", _id)
        intent.putExtra("flag", "fav")
        intent.putExtra("type", interested)
        startActivity(intent)
    }

    override fun viewChatClick() {
        val intent = Intent(activity, OneToOneChatActivity::class.java)
        startActivity(intent)
    }

    override fun addLikeUnlikePet(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) {
        petPosition = position
        viewModel.likeUnlikePetApi(token, _id)
    }

    override fun viewDes(_id: String, interested: Boolean) { // View Service

        val intent = Intent(requireContext(), ServiceDescriptionActivity::class.java)
        intent.putExtra("id", _id)
        intent.putExtra("flag", "fav")
        intent.putExtra("type", interested)

        startActivity(intent)

    }

    override fun addLikeUnlikeService(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) {
        servicePosition = position
        viewModel.likeUnlikeServicesApi(token, _id)
    }

    override fun view(_id: String, interested: Boolean, position: Int) {  // View Product



        val intent = Intent(requireContext(), ProductDescriptionActivity::class.java)
        intent.putExtra("id", _id)
        intent.putExtra("flag", "Fav")
        intent.putExtra("type", interested)

        startActivity(intent)
    }

    override fun addLikeUnlike(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) { // Product
        productPosition = position
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
                                    if (favProductData.size == 1) {
                                        binding.NotFound.isVisible = true
                                        binding.NotFound.text = "No Products Found."
                                        binding.ProductListRecycler.isVisible = false

                                    }
                                    favProductData.removeAt(productPosition);
                                    productListAdapter.notifyItemRemoved(productPosition);
                                    productListAdapter.notifyItemRangeChanged(
                                        productPosition,
                                        favProductData.size
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                            }
                        }

                        is Resource.Loading -> {}

                        is Resource.Empty -> {
                        }

                    }

                }
            }

        }
    }

    private fun observeLikePetResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likePet.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {
                                    if (favPetData.size == 1) {
                                        binding.NotFound.isVisible = true
                                        binding.NotFound.text = "No Pets Found."
                                        binding.PetListRecycler.isVisible = false
                                    }
                                    favPetData.removeAt(petPosition);
                                    listadapter.notifyItemRemoved(petPosition);
                                    listadapter.notifyItemRangeChanged(petPosition, favPetData.size)
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
//                                Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }

        }
    }

    private fun observeLikeServiceResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likeService.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {
                                    if (favServiceData.size == 1) {
                                        binding.NotFound.isVisible = true
                                        binding.NotFound.text = "No Services Found."
                                        binding.ServiceListRecycler.isVisible = false

                                    }
                                    favServiceData.removeAt(servicePosition);
                                    servicesAdapter.notifyItemRemoved(servicePosition);
                                    servicesAdapter.notifyItemRangeChanged(
                                        servicePosition,
                                        favServiceData.size
                                    )
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
//                                Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }

        }
    }


//    Add To Interested Apis and Observer


    @SuppressLint("NotifyDataSetChanged")
    override fun productInterest(_id: String, position: Int) {

        if (!favProductData[position].isInterested) {
            favProductData[position].isInterested = true
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, favProductData.size)
            productListAdapter.notifyDataSetChanged()

        }

        viewModel.addToInterestedApi(token, "PRODUCT", "", _id, "")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun petInterest(_id: String, position: Int) {

        if (!favPetData[position].isInterested) {
            favPetData[position].isInterested = true
            listadapter.notifyItemChanged(position)
            listadapter.notifyItemRangeChanged(position, favPetData.size)
            listadapter.notifyDataSetChanged()

        }


        viewModel.addToInterestedApi(token, "PET", _id, "", "")

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun serviceInterest(_id: String, position: Int) {
        if (!favServiceData[position].isInterested) {
            favServiceData[position].isInterested = true
            servicesAdapter.notifyItemChanged(position)
            servicesAdapter.notifyItemRangeChanged(position, favServiceData.size)
            servicesAdapter.notifyDataSetChanged()

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
//                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
//                                Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }

        }
    }


//    Tab Manage


    private fun productsDataManage() {
        binding.PetListRecycler.isVisible = false
        binding.ServiceListRecycler.isVisible = false

        if (favProductData.size > 0) {
            binding.NotFound.isVisible = false
            binding.ProductListRecycler.isVisible = true
        } else {
            binding.NotFound.isVisible = true
            binding.NotFound.text = "No Products Found."
            binding.ProductListRecycler.isVisible = false
        }




        binding.petButton.setBackgroundResource(R.drawable.border_background)
        binding.productButton.setBackgroundResource(R.drawable.main_button_background)
        binding.serviceButton.setBackgroundResource(R.drawable.border_background)
        binding.txtPet.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtProduct.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.txtServices.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
    }


    private fun petDataManage() {

        binding.ProductListRecycler.isVisible = false
        binding.ServiceListRecycler.isVisible = false

        if (favPetData.size > 0) {
            binding.NotFound.isVisible = false
            binding.PetListRecycler.isVisible = true
        } else {
            binding.NotFound.isVisible = true
            binding.NotFound.text = "No Pets Found."
            binding.PetListRecycler.isVisible = false
        }



        binding.petButton.setBackgroundResource(R.drawable.main_button_background)
        binding.productButton.setBackgroundResource(R.drawable.border_background)
        binding.serviceButton.setBackgroundResource(R.drawable.border_background)
        binding.txtPet.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.txtProduct.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtServices.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
    }


    private fun serviceDataManage() {
        binding.PetListRecycler.isVisible = false
        binding.ProductListRecycler.isVisible = false


        if (favServiceData.size > 0) {
            binding.NotFound.isVisible = false
            binding.ServiceListRecycler.isVisible = true
        } else {
            binding.NotFound.isVisible = true
            binding.NotFound.text = "No Services Found."
            binding.ServiceListRecycler.isVisible = false
        }


        binding.petButton.setBackgroundResource(R.drawable.border_background)
        binding.productButton.setBackgroundResource(R.drawable.border_background)
        binding.serviceButton.setBackgroundResource(R.drawable.main_button_background)
        binding.txtPet.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtProduct.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        binding.txtServices.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
    }


    private fun setDataAccordingly() {
        when (flag1) {
            "PRODUCT" -> {
                productsDataManage()
            }
            "SERVICE" -> {
                serviceDataManage()
            }
            else -> {
                petDataManage()
            }
        }
    }




}