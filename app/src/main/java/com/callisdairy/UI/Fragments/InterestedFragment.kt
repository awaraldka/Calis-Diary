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
import com.callisdairy.Adapter.*
import com.callisdairy.Interface.*
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.UI.Activities.PetDescriptionActivity
import com.callisdairy.UI.Activities.ProductDescriptionActivity
import com.callisdairy.UI.Activities.ServiceDescriptionActivity
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.IntrestedPetDocs
import com.callisdairy.databinding.FragmentInterestedBinding
import com.callisdairy.viewModel.IntrestedViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InterestedFragment : Fragment(), viewPests, productView, serviceView, viewChat,
    LikeUnlikeProduct, LikeUnlikePet, LikeUnlikeService ,UnIntrestedPetProductService{

    private var _binding: FragmentInterestedBinding? = null
    private val binding get() = _binding!!

    lateinit var listadapter: InterestedPetAdapter

    lateinit var productListAdapter: InterestedProductAdapter

    lateinit var servicesAdapter: InterestedServicesAdapter


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
    var flag = ""


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
    var userId = ""
    var flag1 = ""

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true

    var pagesProduct = 0
    var pageProduct = 1
    var limitProduct = 10
    var dataLoadFlagProduct = false
    var loaderFlagProduct = true

    var pagesService = 0
    var pageService = 1
    var limitService = 10
    var dataLoadFlagService = false
    var loaderFlagService = true

    var data = ArrayList<IntrestedPetDocs>()
    var dataProducts = ArrayList<IntrestedPetDocs>()
    var dataService = ArrayList<IntrestedPetDocs>()


    var selectedTab = false

    private val viewModel: IntrestedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInterestedBinding.inflate(layoutInflater, container, false)

        arguments?.getString("flag")?.let {
            flag = it
        }

        allIds()
        toolBarWithBottomTab()
        back.setSafeOnClickListener {
            parentFragmentManager.popBackStack()
        }

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        userId = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.userId).toString()


//        TODO Call All APIS

        lifecycleScope.launch {
            val callApis = listOf(
                async { viewModel.intrestedPetApi(token, "PET", page, limit) },
                async {
                    viewModel.intrestedProductApi(
                        token,
                        "PRODUCT",
                        pageProduct,
                        limitProduct
                    )
                },
                async {
                    viewModel.intrestedServiceApi(
                        token,
                        "SERVICE",
                        pageService,
                        limitService
                    )
                },
            )
            callApis.awaitAll()
        }


//      TODO Pull To Refresh

        binding.pullToRefresh.setOnRefreshListener {
            binding.progressBar.isVisible = false
            when (flag1) {
                "PRODUCT" -> {
                    pageProduct = 1
                    limitProduct = 10
                    dataProducts.clear()
                    dataLoadFlagProduct = false
                    loaderFlagProduct = true

                    viewModel.intrestedProductApi(token, "PRODUCT", pageProduct, limitProduct)
                    binding.pullToRefresh.isRefreshing = false
                }
                "SERVICE" -> {
                    pageService = 1
                    limitService = 10
                    dataService.clear()
                    dataLoadFlagService = false
                    loaderFlagProduct = true
                    viewModel.intrestedServiceApi(token, "SERVICE", pageService, limitService)
                    binding.pullToRefresh.isRefreshing = false
                }
                else -> {
                    page = 1
                    limit = 10
                    data.clear()
                    dataLoadFlag = false
                    loaderFlag = true
                    viewModel.intrestedPetApi(token, "PET", page, limit)
                    binding.pullToRefresh.isRefreshing = false
                }
            }


        }


//        TODO Pagination

        binding.petNestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            when (flag1) {

                "PRODUCT" -> {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        dataLoadFlagProduct = true
                        pageProduct++
                        binding.progressBar.visibility = View.VISIBLE
                        if (pageProduct > pagesProduct) {
                            binding.progressBar.visibility = View.GONE
                        } else {
                            viewModel.intrestedProductApi(token, "PRODUCT", pageProduct, limitProduct)
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
                            viewModel.intrestedServiceApi(token, "SERVICE", pageService, limitService)
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
                            viewModel.intrestedPetApi(token, "PET", page, limit)
                        }
                    }
                }
            }


        })


//        TODO Tab Clicks

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




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeIntrestedPetResponse()
        observeIntrestedServiceResponse()
        observeIntrestedProductResponse()

        observeLikeProductResponse()
        observeLikePetResponse()
        observeLikeServiceResponse()
        observeAddToInterestedResponse()


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (selectedTab) {
                        selectedTab = false
                        binding.petNestedScrollView.scrollY = 0
                        petDataManage()
                    } else {
                        val fm: FragmentManager = requireActivity().supportFragmentManager
                        for (i in 1 until fm.backStackEntryCount) {
                            fm.popBackStack()
                        }
                    }


                }
            })

    }


    override fun viewPet(_id: String, interested: Boolean) {
        val intent = Intent(requireContext(), PetDescriptionActivity::class.java)
        intent.putExtra("id", _id)
        intent.putExtra("flag", "Interested")
        intent.putExtra("type", interested)
        startActivity(intent)
    }


    override fun view(
        _id: String,
        interested: Boolean,
        position: Int
    ) {
        val intent = Intent(requireContext(), ProductDescriptionActivity::class.java)
        intent.putExtra("flag", "Interested")
        intent.putExtra("id", _id)
        intent.putExtra("type", interested)
        startActivity(intent)
    }

    override fun viewChatClick() {
        val intent = Intent(activity, OneToOneChatActivity::class.java)
        startActivity(intent)
    }

    override fun viewDes(_id: String, interested: Boolean) {
        val intent = Intent(requireContext(), ServiceDescriptionActivity::class.java)
        intent.putExtra("flag", "Interested")
        intent.putExtra("id", _id)
        intent.putExtra("type", interested)
        startActivity(intent)
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
        Username.text = getString(R.string.interested)

        when (flag) {
            "Favourites" -> {
                mainTitle.visibility = View.VISIBLE
                chat.visibility = View.VISIBLE
                back.visibility = View.GONE
                search.visibility = View.VISIBLE

                UnSelectedFavorites.visibility = View.GONE
                SelectedFavorites.visibility = View.VISIBLE
                Favoritesview.isVisible = true

            }
            "FavouritesMenu" -> {
                mainTitle.visibility = View.VISIBLE
                chat.visibility = View.VISIBLE
                back.visibility = View.GONE
                search.visibility = View.VISIBLE

                Favoritesview.isVisible = false
                MenuView.isVisible = true

                UnSelectedFavorites.visibility = View.VISIBLE
                SelectedFavorites.visibility = View.GONE
            }

            "Interested" -> {
                mainTitle.visibility = View.VISIBLE
                chat.visibility = View.VISIBLE
                search.visibility = View.VISIBLE
                back.visibility = View.VISIBLE

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

//   TODO Observe Pet Response

    private fun observeIntrestedPetResponse() {

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._intrestedPetData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }
                                    data.addAll(response.data.result.docs)
                                    pages = response.data.result.pages
                                    page = response.data.result.page


                                    data.forEach { pet ->
                                        pet.petId.favUser.forEach { likedUserId ->
                                            pet.petId.isLike = likedUserId == userId
                                        }
                                    }




                                    interestedPetAdapter()
                                    setDataAccordingly()

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            setDataAccordingly()
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            if (loaderFlag) {
                                binding.PetListRecycler.isVisible = false
                                binding.NotFound.isVisible = false
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

    private fun interestedPetAdapter() {
        binding.PetListRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        listadapter = InterestedPetAdapter(requireContext(), data, this, this, this,this)
        binding.PetListRecycler.adapter = listadapter
    }

    //  TODO Observe Product Response

    private fun observeIntrestedProductResponse() {

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._intrestedProductData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {


                                    loaderFlagProduct = false
                                    if (!dataLoadFlagProduct) {
                                        dataProducts.clear()
                                    }
                                    dataProducts.addAll(response.data.result.docs)
                                    pagesProduct = response.data.result.pages
                                    pageProduct = response.data.result.page


                                    dataProducts.forEach { product ->
                                        product.productId.likesUser.forEach { likedUserId ->
                                            product.productId.isLike = likedUserId == userId
                                        }
                                    }




                                    setProductListAdapter()


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

    private fun setProductListAdapter() {
        binding.ProductListRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        productListAdapter =
            InterestedProductAdapter(requireContext(), dataProducts, this, this, this,this)
        binding.ProductListRecycler.adapter = productListAdapter
    }


    //  TODO Observe Service Response


    private fun observeIntrestedServiceResponse() {

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._intrestedServiceData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.responseCode == 200) {
                                try {


                                    loaderFlagService = false
                                    if (!dataLoadFlagService) {
                                        dataService.clear()
                                    }
                                    dataService.addAll(response.data.result.docs)
                                    pagesService = response.data.result.pages
                                    pageService = response.data.result.page



                                    dataService.forEach { service ->
                                        service.serviceId.likesUser.forEach { likedUserId ->
                                            service.serviceId.isLike = likedUserId == userId
                                        }
                                    }



                                    setServiceListAdapter()


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

    private fun setServiceListAdapter() {
        binding.ServiceListRecycler.layoutManager = LinearLayoutManager(requireContext())
        servicesAdapter = InterestedServicesAdapter(requireContext(), dataService, this, this, this,this)
        binding.ServiceListRecycler.adapter = servicesAdapter
    }


//    TODO Like Unlike Api CAll

    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlike(_id: String, position: Int, heart: ImageView, heartfill: ImageView) {
        if (heart.isVisible) {
            dataProducts[position].productId.isLike = true
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, dataProducts.size)
            productListAdapter.notifyDataSetChanged()

        } else {
            dataProducts[position].productId.isLike = false
            productListAdapter.notifyItemChanged(position)
            productListAdapter.notifyItemRangeChanged(position, dataProducts.size)
            productListAdapter.notifyDataSetChanged()

        }
        viewModel.addLikeProductApi(token, _id)
    }

    private fun observeLikeProductResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._likeData.collect { response ->

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


    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlikePet(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) {

        if (heart.isVisible) {
            data[position].petId.isLike = true
            listadapter.notifyItemChanged(position)
            listadapter.notifyItemRangeChanged(position, data.size)
            listadapter.notifyDataSetChanged()

        } else {
            data[position].petId.isLike = false
            listadapter.notifyItemChanged(position)
            listadapter.notifyItemRangeChanged(position, data.size)
            listadapter.notifyDataSetChanged()

        }

        viewModel.likeUnlikePetApi(token, _id)
    }

    private fun observeLikePetResponse() {

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
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


    @SuppressLint("NotifyDataSetChanged")
    override fun addLikeUnlikeService(
        _id: String,
        position: Int,
        heart: ImageView,
        heartfill: ImageView
    ) {

        if (heart.isVisible) {
            dataService[position].serviceId.isLike = true
            servicesAdapter.notifyItemChanged(position)
            servicesAdapter.notifyItemRangeChanged(position, dataService.size)
            servicesAdapter.notifyDataSetChanged()

        } else {
            dataService[position].serviceId.isLike = false
            servicesAdapter.notifyItemChanged(position)
            servicesAdapter.notifyItemRangeChanged(position, dataService.size)
            servicesAdapter.notifyDataSetChanged()

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


//    TODO Tab Manage


    private fun productsDataManage() {
        binding.PetListRecycler.isVisible = false
        binding.ServiceListRecycler.isVisible = false

        if (dataProducts.size > 0) {
            binding.NotFound.isVisible = false
            binding.ProductListRecycler.isVisible = true
        } else {
            binding.NotFound.isVisible = true
            binding.NotFound.text= "No Products Found."
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

        if (data.size > 0) {
            binding.NotFound.isVisible = false
            binding.PetListRecycler.isVisible = true
        } else {
            binding.NotFound.isVisible = true
            binding.NotFound.text= "No Pets Found."
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


        if (dataService.size > 0) {
            binding.NotFound.isVisible = false
            binding.ServiceListRecycler.isVisible = true
        } else {
            binding.NotFound.isVisible = true
            binding.NotFound.text= "No Services Found."
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

    override fun petUnIntrested(id:String,position:Int) {
        data.removeAt(position)
        listadapter.notifyItemRemoved(position)
        listadapter.notifyItemRangeChanged(position, data.size)

        viewModel.addToInterestedApi(token, type = "PET", petId = id, productId = "", serviceId = "")
    }

    override fun productUnIntrested(id:String,position:Int) {
        dataProducts.removeAt(position)
        productListAdapter.notifyItemRemoved(position)
        productListAdapter.notifyItemRangeChanged(position, dataProducts.size)


        viewModel.addToInterestedApi(token, type = "PRODUCT", petId = "", productId = id, serviceId = "")
    }

    override fun serviceUnIntrested(id:String,position:Int) {

        dataService.removeAt(position)
        servicesAdapter.notifyItemRemoved(position)
        servicesAdapter.notifyItemRangeChanged(position, dataService.size)

        viewModel.addToInterestedApi(token, type = "SERVICE", petId = "", productId = id, serviceId = id)
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




}