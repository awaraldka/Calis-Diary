package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.SearchListAdapter
import com.callisdairy.Interface.HomeUserProfileView
import com.callisdairy.Interface.SuggestionListClick
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.GlobalSearchUsers

import com.callisdairy.databinding.ActivitySearchBinding
import com.callisdairy.viewModel.GlobalSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), SuggestionListClick, HomeUserProfileView {

    private lateinit var  binding : ActivitySearchBinding

    lateinit var searchListAdapter : SearchListAdapter
    lateinit var data: ArrayList<GlobalSearchUsers>
    var token = ""

    private val viewModel: GlobalSearchViewModel by viewModels()
    lateinit var socketInstance: SocketManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        token  = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()


        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)

//        viewModel.globalSearchApi(token , "")


        binding.DFsearch.addTextChangedListener(textWatcher)






        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }

        observeSearchResponse()
        observeResponseFollowUnFollow()
    }



    private val textWatcher = object:TextWatcher{
        private var searchJob: Job? = null
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(text: Editable?) {
            searchJob?.cancel() // Cancel previous search job if it exists

            searchJob = CoroutineScope(Dispatchers.Main).launch {
                viewModel.globalSearchApi(token, text.toString())
            }

        }
    }



    private fun observeSearchResponse() {


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel._globalSearchData.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            binding.progressBar.isVisible = false

                            if(response.data?.responseCode == 200) {
                                try {

                                    if (response.data.result.users.size > 0){
                                        binding.searchRecycler.isVisible = true
                                        binding.NotFound.isVisible = false
                                        data = response.data.result.users
                                        setFollowingAdaptor()
                                    }else{
                                        binding.searchRecycler.isVisible = false
                                        binding.NotFound.isVisible = true
                                    }


                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.searchRecycler.isVisible = false
                            binding.NotFound.isVisible = true
                            binding.progressBar.isVisible = false
                            Progresss.stop()
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, this@SearchActivity)
                            }
                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            binding.progressBar.isVisible = true
                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }


        }
    }


    private fun setFollowingAdaptor() {
        binding.searchRecycler.layoutManager = LinearLayoutManager(this)
        searchListAdapter = SearchListAdapter(this,data,this,this)
        binding.searchRecycler.adapter = searchListAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun follow(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String
    ) {
        if(followButton.isVisible && privacyType.lowercase() == "public"){
            data[position].isFollow = true
            data[position].isRequested = false
            searchListAdapter.notifyItemChanged(position)
            searchListAdapter.notifyItemRangeChanged(position, data.size)
            searchListAdapter.notifyDataSetChanged()
        }else if (followButton.isVisible && privacyType.lowercase() == "private"){
            data[position].isFollow = false
            data[position].isRequested = true
            searchListAdapter.notifyItemChanged(position)
            searchListAdapter.notifyItemRangeChanged(position, data.size)
            searchListAdapter.notifyDataSetChanged()
        }

        viewModel.followUnfollowAPi(token,_id)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun unFollow(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String,
        requestedButton: LinearLayout
    ) {
        if(unFollowButton.isVisible && privacyType.lowercase() == "public"){
            data[position].isFollow = false
            searchListAdapter.notifyItemChanged(position)
            searchListAdapter.notifyItemRangeChanged(position, data.size)
            searchListAdapter.notifyDataSetChanged()
        }else if (unFollowButton.isVisible && privacyType.lowercase() == "private"){
            data[position].isFollow = false
            data[position].isRequested = false
            searchListAdapter.notifyItemChanged(position)
            searchListAdapter.notifyItemRangeChanged(position, data.size)
            searchListAdapter.notifyDataSetChanged()
        }

        viewModel.followUnfollowAPi(token,_id)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun requested(
        _id: String,
        position: Int,
        follow: Boolean,
        requested: Boolean,
        followButton: LinearLayout,
        unFollowButton: LinearLayout,
        privacyType: String,
        requestedButton: LinearLayout
    ) {
        if (requestedButton.isVisible && privacyType.lowercase() == "private"){
            data[position].isFollow = false
            data[position].isRequested = false
            searchListAdapter.notifyItemChanged(position)
            searchListAdapter.notifyItemRangeChanged(position, data.size)
            searchListAdapter.notifyDataSetChanged()
        }
        viewModel.followUnfollowAPi(token,_id)

    }


    private fun observeResponseFollowUnFollow() {


        lifecycleScope.launch {
            viewModel._followUnfollowData.collectLatest{ response ->

                when (response) {

                    is Resource.Success -> {
                        if(response.data?.responseCode == 200) {
                            try {
                            }catch (e:Exception){
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

    override fun viewProfile(_id: String, userName: String) {
        val intent = Intent(this, CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "OtherUsers")
        intent.putExtra("userName", userName)
        intent.putExtra("id", _id)
        startActivity(intent)
    }


}