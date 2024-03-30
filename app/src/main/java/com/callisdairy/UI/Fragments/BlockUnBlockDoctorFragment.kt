package com.callisdairy.UI.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.BlockDoctorAdapter
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.Interface.UnBlockUser
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.AppointmentListUserId
import com.callisdairy.databinding.FragmentBlockUnblockDoctorBinding
import com.callisdairy.viewModel.DoctorAppointmentViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlockUnBlockDoctorFragment : Fragment(), UnBlockUser, CommonDialogInterface {

    private var _binding: FragmentBlockUnblockDoctorBinding? =  null
    private val binding get() = _binding!!

    var dataBlocked: List<AppointmentListUserId> = listOf()
    lateinit var back: ImageView

    private val viewModel : DoctorAppointmentViewModel by viewModels()
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true

    var dataPosition  = 0
    var userId  = ""
    var token  = ""
    lateinit var adapter:BlockDoctorAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentBlockUnblockDoctorBinding.inflate(layoutInflater, container, false)
        back = activity?.findViewById(R.id.backTitle)!!


        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()


        back.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }
        binding.pullToRefresh.setOnRefreshListener {
            binding.pullToRefresh.isRefreshing = false
            binding.DFsearch.setText("")
        }

        callApis("")

        binding.DFsearch.addTextChangedListener(textWatcher)

        binding.scrollViewPage.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.getAllBlockedUserApi(token,"",page, limit)
                }

            }


        })



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGetAllBlockedUserResponse()
        observeBlockUserResponse()


        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }

        })

    }


    private fun observeGetAllBlockedUserResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._getAllBlockedUserData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if(response.data?.responseCode == 200) {
                                try {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        dataBlocked = emptyList()
                                    }

                                    dataBlocked = dataBlocked + response.data.result.docs


                                    if (dataBlocked.isNotEmpty()){

                                        binding.NotFound.isVisible = false
                                        binding.blockUnBlockDoctor.isVisible = true

                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setAdapterClinic()
                                    }else{
                                        binding.NotFound.isVisible = true
                                        binding.blockUnBlockDoctor.isVisible = false
                                    }



                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.blockUnBlockDoctor.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message,requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            if (loaderFlag){
                                binding.NotFound.isVisible = false
                                binding.blockUnBlockDoctor.isVisible = false
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



    private fun setAdapterClinic() {
        binding.blockUnBlockDoctor.layoutManager = LinearLayoutManager(requireContext())
         adapter = BlockDoctorAdapter(requireContext(), dataBlocked,this)
        binding.blockUnBlockDoctor.adapter = adapter
    }

    override fun unBlockUser(position: Int, id: String) {
        dataPosition = position
        userId =id
        androidExtension.alertBoxCommon("Are you sure you want to unblock this doctor?",requireContext(),this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun commonWork() {

        dataBlocked = dataBlocked.toMutableList().apply { removeAt(dataPosition) }
        adapter.notifyItemRemoved(dataPosition)
        adapter.notifyItemRangeChanged(dataPosition, dataBlocked.size)
        adapter.notifyDataSetChanged()




        viewModel.blockUserApi(token,userId)
    }


    private fun observeBlockUserResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._blockUserData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    if(dataBlocked.isEmpty()){
                                        dataBlocked = emptyList()
                                        binding.NotFound.isVisible = true
                                        binding.blockUnBlockDoctor.isVisible = false
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



    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isEmpty()){
                callApis("")
            }else{
                callApis(s.toString())
            }
        }

    }


    private fun callApis(search:String){
        page = 1
        limit = 10
        dataBlocked = emptyList()
        dataLoadFlag = false
        loaderFlag = false
        viewModel.getAllBlockedUserApi(token,search,page, limit)
    }


}