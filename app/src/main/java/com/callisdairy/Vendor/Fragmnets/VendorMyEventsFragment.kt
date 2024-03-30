package com.callisdairy.Vendor.Fragmnets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.MyEventPostAdapter
import com.callisdairy.Interface.DeleteClick
import com.callisdairy.Interface.DeleteMyEvent
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddEventActivity
import com.callisdairy.UI.Activities.AddPetActivity
import com.callisdairy.UI.Activities.subscription.SubscribePlansUserActivity
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.ListEventDocs
import com.callisdairy.databinding.FragmentVendorMyEventsBinding
import com.callisdairy.viewModel.EventListViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorMyEventsFragment : Fragment(), DeleteMyEvent, DeleteClick {

    private var _binding: FragmentVendorMyEventsBinding? =  null
    private val binding get() = _binding!!


    var data = ArrayList<ListEventDocs>()
    var adapterPosition = 0
    var eventId = ""
    var pages = 0
    var page = 1
    var limit = 20
    var dataLoadFlag = false
    var loaderFlag = true
    var token = ""
    lateinit var eventPostAdapter: MyEventPostAdapter


    private val viewModel: EventListViewModel by viewModels()

    var totalEvent = 0
    var remainingEvent = 0
    var isClickedToAdd =  false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentVendorMyEventsBinding.inflate(layoutInflater, container, false)


        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()



        binding.dfSearch.addTextChangedListener(textWatcher)



        binding.scrollViewEvent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.myListEventApi(token,"",page, limit)
                }

            }


        })








        binding.swipeRefresh.setOnRefreshListener {
            pages = 0
            page = 1
            limit = 20
            data.clear()
            dataLoadFlag = false
            loaderFlag = true

            binding.dfSearch.setText("")

            viewModel.checkPlanApi(token = token)
            viewModel.myListEventApi(token,"",page, limit)
            binding.swipeRefresh.isRefreshing =  false
        }



        binding.addEventClick.setOnClickListener {
            isClickedToAdd = true
            viewModel.checkPlanApi(token = token)
        }


        return binding.root
    }


    override fun onStart() {
        super.onStart()
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()
        dataLoadFlag = false
        viewModel.checkPlanApi(token = token)
        viewModel.myListEventApi(token,"",1, 10)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseProductList()
        observeResponseDeleteEvent()
        observeResponseCheckLimitOfPostingContent()
    }


    private fun observeResponseProductList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._myListEventData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }

                                    if (response.data.result.docs.size > 0){
                                        binding.NotFound.isVisible = false
                                        binding.postRecycler.isVisible = true

                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        postAdapter()

                                    }else{
                                        binding.NotFound.isVisible = true
                                        binding.postRecycler.isVisible = false
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.postRecycler.isVisible = false

                            response.message?.let { message ->
                                if (!message.contains("Event not found")){
                                    androidExtension.alertBox(message, requireContext())

                                }
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false

                            if (loaderFlag) {
                                binding.postRecycler.isVisible = false
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


    private fun postAdapter() {
        binding.postRecycler.layoutManager = LinearLayoutManager(requireContext())
        eventPostAdapter = MyEventPostAdapter(requireContext(), data,this)
        binding.postRecycler.adapter = eventPostAdapter
    }

    override fun delete(id: String, position: Int) {
        adapterPosition = position
        eventId = id
        androidExtension.alertBoxDelete(getString(R.string.delete_events),requireContext(),this)

    }

    override fun editEvent(id: String, position: Int) {
        val intent =  Intent(requireContext(), AddEventActivity::class.java)
        intent.putExtra("flag", "EditEvent")
        intent.putExtra("eventId", id)
        startActivity(intent)
    }

    override fun share(imageUrl:String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, imageUrl)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseDeleteEvent() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._myListDeleteData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    if (data.size == 1){
                                        binding.NotFound.isVisible = true
                                        binding.postRecycler.isVisible = false
                                    }

                                    data.removeAt(adapterPosition)
                                    eventPostAdapter.notifyItemRemoved(adapterPosition)
                                    eventPostAdapter.notifyItemRangeChanged(adapterPosition, data.size)

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
                            Progresss.start(requireContext())

                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }

        }


    }

    override fun deleteItem() {
        viewModel.deleteEventApi(token,eventId)
    }




    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            page = 1
            limit = 20
            data.clear()
            dataLoadFlag = false
            loaderFlag = false
            viewModel.myListEventApi(token,s.toString(),page, limit)

        }

    }

    private fun observeResponseCheckLimitOfPostingContent() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._checkPlanData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {


                                    totalEvent = response.data.result.totalEvent.toInt()
                                    remainingEvent = response.data.result.remainingEvent.toInt()
                                    binding.count.text =  "$remainingEvent"

                                    if (isClickedToAdd){
                                        if (response.data.result.remainingEvent.toInt() > 0){
                                            val intent = Intent(requireContext(), AddEventActivity::class.java)
                                            startActivity(intent)
                                        }

                                        if (response.data.result.remainingEvent.toInt() == 0){
                                            val intent =Intent(requireContext(),
                                                SubscribePlansUserActivity::class.java)
                                            intent.putExtra("Screen","Event Vendor")
                                            startActivity(intent)
                                        }

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