package com.callisdairy.Vendor.Fragmnets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.NotificationAdapter
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.Interface.NotificationClick
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommentsActivity
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.UI.Fragments.RequestedListFragment
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.NotificationListResult
import com.callisdairy.databinding.FragmentNotificationVendorBinding
import com.callisdairy.viewModel.NotificationViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class NotificationVendorFragment : Fragment(), NotificationClick, CommonDialogInterface {

    private var _binding: FragmentNotificationVendorBinding? =  null
    private val binding get() = _binding!!

    var token = ""
    var clearAll = false
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true

    lateinit var titleVendor: TextView
    lateinit var backVendor: ImageView


    var data : ArrayList<NotificationListResult> = ArrayList()
    lateinit var Adapter : NotificationAdapter



    private val viewModel : NotificationViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentNotificationVendorBinding.inflate(layoutInflater, container, false)
        titleVendor = activity?.findViewById(R.id.titleVendor)!!
        backVendor = activity?.findViewById(R.id.backVendor)!!

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        backVendor.setOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

        titleVendor.text = getString(R.string.notification)

        viewModel.notificationApi(token,1,10)


        binding.ClearAll.setOnClickListener {
            androidExtension.alertBoxCommon(getString(R.string.delete_all_notifications),requireContext(),this)
        }

        binding.NestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.notificationApi(token,page, limit)
                }

            }


        })




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseNotification()
        observeResponseNotificationDelete()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAfterTransition()
                    parentFragmentManager.popBackStack()
                }
            })
    }





    private fun observeResponseNotification() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._notificationData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

                                    binding.ClearAll.isVisible = response.data.result.docs.size > 1

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }
                                    if (response.data.result.docs.size > 0){
                                        binding.notificationRecycler.isVisible = true
                                        binding.NotFound.isVisible = false
                                        data.addAll(response.data.result.docs)
                                        pages = response.data.result.totalPages
                                        page = response.data.result.page
                                        setAdapter()
                                    }else{
                                        binding.notificationRecycler.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }
                                    setAdapter()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.ClearAll.isVisible = false
                            binding.NotFound.isVisible = true
                            response.message?.let { message ->
                                if (!message.lowercase().contains("data not found")){
                                    androidExtension.alertBox(message, requireContext())

                                }
                            }

                        }

                        is Resource.Loading -> {
                            binding.ClearAll.isVisible = false
                            binding.NotFound.isVisible = false
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
        binding.notificationRecycler.layoutManager = LinearLayoutManager(requireContext())
        Adapter = NotificationAdapter(requireContext(),data,this)
        binding.notificationRecycler.adapter = Adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notificationDelete(id: String, position: Int) {
        data.removeAt(position)
        Adapter.notifyItemRemoved(position)
        Adapter.notifyItemRangeChanged(position, data.size)
        Adapter.notifyDataSetChanged()


        viewModel.deleteNotificationApi(token,id)

    }

    override fun viewUsersProfile(id: String, userName: String) {
        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "OtherUsers")
        intent.putExtra("userName", userName)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun readNotifications(
        notificationType: String,
        postIds: String,
        notificationIds: String,
        description: String
    ) {
        if (notificationType == "COMMENT_LIKE" || notificationType == "POST_COMMENT" || notificationType == "POST_REPLY" ){
//            Post Comment
            val intent = Intent(requireContext(), CommentsActivity::class.java)
            intent.putExtra("_id", postIds)
            startActivity(intent)
        }


        if (notificationType =="POST_LIKE" ||notificationType =="TAGGED"){
            val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
            intent.putExtra("flag", "ViewMyPost")
            intent.putExtra("id", postIds)
            startActivity(intent)
        }


        if(description.lowercase().contains("requested to following")){
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_container, RequestedListFragment())
                .addToBackStack(null).commit()
        }




    }


    private fun observeResponseNotificationDelete() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._removeData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

//                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {


                                    binding.ClearAll.isVisible = data.size >1


                                    if(data.size == 0){
                                        binding.notificationRecycler.isVisible= false
                                        binding.NotFound.isVisible = true
                                    }

                                    if (clearAll){
                                        binding.ClearAll.isVisible = false
                                        binding.notificationRecycler.isVisible= false
                                        binding.NotFound.isVisible = true

                                    }

                                    clearAll = false
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
//                            Progresss.stop()
//                            response.message?.let { message ->
//                                androidExtension.alertBox(message, requireContext())
//                            }

                        }

                        is Resource.Loading -> {
//                            Progresss.start(requireContext())

                        }

                        is Resource.Empty -> {
//                            Progresss.stop()
                        }

                    }

                }

            }

        }


    }

    override fun commonWork() {
        clearAll = true
        viewModel.deleteNotificationApi(token,"")
    }


}