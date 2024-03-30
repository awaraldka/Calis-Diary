package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.RequestedFriendsListAdapter
import com.callisdairy.Interface.FriendListClick
import com.callisdairy.Interface.HomeUserProfileView
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.RequestedListDocs
import com.callisdairy.databinding.ActivityRequestedListBinding
import com.callisdairy.viewModel.RequestedListViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RequestedListActivity : AppCompatActivity(), FriendListClick, HomeUserProfileView {

    private lateinit var binding: ActivityRequestedListBinding

    var token = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true

    var adapterPosition = 0


    var requestedData = ArrayList<RequestedListDocs>()
    lateinit var Adapter: RequestedFriendsListAdapter

    private val viewModel: RequestedListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()


        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }


        viewModel.requestedListApi(token, page, limit)




        observeResponseRequestList()
        observeResponseAcceptOrReject()
    }


    private fun observeResponseRequestList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._RequestedListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        requestedData.clear()
                                    }

                                    requestedData.addAll(response.data.result.docs)


                                    if (requestedData.size > 0) {

                                        binding.NotFound.isVisible = false
                                        binding.FriendsList.isVisible = true

                                        pages = response.data.result.totalPages
                                        page = response.data.result.page
                                        requestedListAdapter()
                                    } else {
                                        binding.NotFound.isVisible = true
                                        binding.FriendsList.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.FriendsList.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@RequestedListActivity)
                            }

                        }

                        is Resource.Loading -> {
                            if (loaderFlag) {
                                binding.NotFound.isVisible = false
                                binding.FriendsList.isVisible = false
                                Progresss.start(this@RequestedListActivity)
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

    private fun requestedListAdapter() {
        binding.FriendsList.layoutManager = LinearLayoutManager(this)
        Adapter = RequestedFriendsListAdapter(this, requestedData, this, this)
        binding.FriendsList.adapter = Adapter
    }

    override fun acceptRequest(_id: String, position: Int) {
        adapterPosition = position
        viewModel.acceptRejectApi(token, "ACCEPT", _id)
    }

    override fun rejectRequest(_id: String, position: Int) {
        adapterPosition = position
        viewModel.acceptRejectApi(token, "REJECT", _id)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseAcceptOrReject() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._acceptOrRejectData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

                                    if (requestedData.size == 1) {
                                        binding.NotFound.isVisible = true
                                        binding.FriendsList.isVisible = false
                                    }


                                    requestedData.removeAt(adapterPosition)
                                    Adapter.notifyItemRemoved(adapterPosition)
                                    Adapter.notifyItemRangeChanged(
                                        adapterPosition,
                                        requestedData.size
                                    )
                                    Adapter.notifyDataSetChanged()

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@RequestedListActivity)
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

    override fun viewProfile(_id: String, userName: String) {
        val intent = Intent(this, CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "OtherUsers")
        intent.putExtra("userName", userName)
        intent.putExtra("id", _id)
        startActivity(intent)
    }


}