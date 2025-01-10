package com.callisdairy.Vendor.Fragmnets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.AdapterVendors.InterestedListAdapter
import com.callisdairy.Interface.ViewUserList
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.api.response.InterestedUserDocs
import com.callisdairy.databinding.FragmentIntrestedClientInBinding
import com.callisdairy.viewModel.VendorCommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntrestedClientInFragment : Fragment(), ViewUserList {

    private var _binding: FragmentIntrestedClientInBinding? = null
    private val binding get() = _binding!!

    lateinit var titleVendor: TextView
    lateinit var backVendor: ImageView


    var from = ""
    var productId = ""
    var token = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true
    var data= ArrayList<InterestedUserDocs>()
    private val viewModel: VendorCommonViewModel by viewModels()
    



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentIntrestedClientInBinding.inflate(layoutInflater, container, false)
        titleVendor = activity?.findViewById(R.id.titleVendor)!!
        backVendor = activity?.findViewById(R.id.backVendor)!!

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        arguments?.getString("from")?.let { from= it }

        backVendor.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }


        when (from) {
            "HomeService" -> {
                titleVendor.text = getString(R.string.intrested_service)
                data.clear()
                pages = 0
                page = 1
                limit = 10
                viewModel.interestedProductListApi(token,"SERVICE",1,10,"", "","")
            }
            "HomeProduct" -> {
                titleVendor.text = getString(R.string.intrested_products)
                data.clear()
                pages = 0
                page = 1
                limit = 10
                viewModel.interestedProductListApi(token,"PRODUCT",1,10, "","","")
            }
            else -> {
                titleVendor.text = getString(R.string.intrested_pets)
                data.clear()
                pages = 0
                page = 1
                limit = 10
                viewModel.interestedProductListApi(token,"PET",1,10,"","", "")
            }
        }




        binding.swipeRefresh.setOnRefreshListener {
            dataLoadFlag =  false
            loaderFlag = true
            binding.ProgressBarScroll.isVisible = false
            when (from) {
                "HomeService" -> {
                    viewModel.interestedProductListApi(token,"SERVICE",1,10,"", "","")
                }
                "HomeProduct" -> {
                    viewModel.interestedProductListApi(token,"PRODUCT",1,10, "","","")
                }
                else -> {
                    viewModel.interestedProductListApi(token,"PET",1,10,"","", "")
                }
            }

            binding.swipeRefresh.isRefreshing = false
        }


        binding.scrollPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    if (from  == "HomeService"){
                        viewModel.interestedProductListApi(token,"SERVICE",page, limit,"", "","")
                    }else if(from == "HomeProduct"){
                        viewModel.interestedProductListApi(token,"PRODUCT",page, limit, "","","")
                    }else{
                        viewModel.interestedProductListApi(token,"PET",page, limit,"","", "")
                    }

                }
            }
        })




        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })

        observeResponseList()
       
    }


    private fun observeResponseList() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._intrestedUserListData.collectLatest{ response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if(response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }
                                    if (response.data.result!!.docs!!.size > 0){
                                        binding.InterestedRecycler.isVisible = true
                                        binding.NotFound.isVisible = false
                                        data.addAll(response.data.result.docs!!)
                                        pages = response.data.result.pages!!
                                        page = response.data.result.page!!
                                        setAdapter()
                                    }else{
                                        binding.InterestedRecycler.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.InterestedRecycler.isVisible = false
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
        binding.InterestedRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = InterestedListAdapter(requireContext(),data, from,this)
        binding.InterestedRecycler.adapter = adapter
    }

    override fun viewUserList(_id: String?, from: String) {
        val intent = Intent(requireContext(), CommonContainerActivity::class.java)
        intent.putExtra("flag","viewInterestedUser")
        intent.putExtra("from",from)
        intent.putExtra("id",productId)
        startActivity(intent)
    }


}