package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.SuggestionListAdapter
import com.callisdairy.Interface.SuggestionListClick
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.suggestionListDocs
import com.callisdairy.databinding.ActivitySuggestionListBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.viewModel.SuggestionListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SuggestionListActivity : AppCompatActivity(), SuggestionListClick {


    private lateinit var binding: ActivitySuggestionListBinding

    var pages = 0
    var page = 1
    var limit = 20
    var dataLoadFlag =  false
    var loaderFlag = true

    var token = ""
    var from = ""
    var data = ArrayList<suggestionListDocs>()
    lateinit var suggestionListAdapter:SuggestionListAdapter
    private val viewModel : SuggestionListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuggestionListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getStringExtra("from")?.let {
            from = it
        }



        if (from == "Home"){
            binding.backButton.isVisible = true
            binding.skip.isVisible = false
            binding.Username.text = getString(R.string.people_you_may_know)
        }else{
            binding.backButton.isVisible = false
            binding.skip.isVisible = true
        }


        token  = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token).toString()

        viewModel.suggestionListAPi(token,"",page, limit)

        binding.suggestionScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.isVisible = true
                if (page > pages) {
                    binding.ProgressBarScroll.isVisible = false
                } else {
                    viewModel.suggestionListAPi(token,"", page, limit)
                }
            }
        })

        observeResponseHomeList()
        observeResponseFollowUnFollow()


        binding.skip.setOnClickListener {
            SavedPrefManager.savePreferenceBoolean(this, SavedPrefManager.isSuggestion,true)
            viewModel.updateStatusAPi(token)

        }


        binding.SearchUsers.addTextChangedListener(textWatcher)

        observeUpdateStatus()

    }






    private fun observeResponseHomeList() {


        lifecycleScope.launch {
            viewModel._suggestionListData.collectLatest{ response ->

                when (response) {

                    is Resource.Success -> {

                        Progresss.stop()
                        if(response.data?.responseCode == 200) {
                            try {
                                loaderFlag = false
                                if (!dataLoadFlag) {
                                    data.clear()
                                }

                                binding.notFound.isVisible = response.data.result.docs.isEmpty()
                                binding.suggestionUserList.isVisible = response.data.result.docs.isNotEmpty()

                                if (from == "Home"){
                                    data = response.data.result.docs.filter { !it.isFollow  && !it.isRequested} as ArrayList<suggestionListDocs>
                                }else{
                                    data.addAll(response.data.result.docs)
                                }

                                pages = response.data.result.totalPages
                                page = response.data.result.page
                                setSuggestionListAdapter()

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        binding.notFound.isVisible = true
                        response.message?.let { message ->
                            androidExtension.alertBox(message, this@SuggestionListActivity)
                        }

                    }

                    is Resource.Loading -> {
                        binding.notFound.isVisible = false
                        if (loaderFlag){
                            Progresss.start(this@SuggestionListActivity)
                        }

                    }

                    is Resource.Empty -> {
                    }

                }

            }

        }



    }



    private fun setSuggestionListAdapter() {
        binding.suggestionUserList.layoutManager = LinearLayoutManager(this)
        suggestionListAdapter = SuggestionListAdapter(this,data,this)
        binding.suggestionUserList.adapter = suggestionListAdapter
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
            suggestionListAdapter.notifyItemChanged(position)
            suggestionListAdapter.notifyItemRangeChanged(position, data.size)
            suggestionListAdapter.notifyDataSetChanged()
        }else if (followButton.isVisible && privacyType.lowercase() == "private"){
            data[position].isFollow = false
            data[position].isRequested = true
            suggestionListAdapter.notifyItemChanged(position)
            suggestionListAdapter.notifyItemRangeChanged(position, data.size)
            suggestionListAdapter.notifyDataSetChanged()
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
            suggestionListAdapter.notifyItemChanged(position)
            suggestionListAdapter.notifyItemRangeChanged(position, data.size)
            suggestionListAdapter.notifyDataSetChanged()
        }else if (unFollowButton.isVisible && privacyType.lowercase() == "private"){
            data[position].isFollow = false
            data[position].isRequested = false
            suggestionListAdapter.notifyItemChanged(position)
            suggestionListAdapter.notifyItemRangeChanged(position, data.size)
            suggestionListAdapter.notifyDataSetChanged()
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
            suggestionListAdapter.notifyItemChanged(position)
            suggestionListAdapter.notifyItemRangeChanged(position, data.size)
            suggestionListAdapter.notifyDataSetChanged()
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



//    Search User

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
            binding.ProgressBarScroll.isVisible = false
            viewModel.suggestionListAPi(token,s.toString(),page, limit)

        }

    }


//     update Status Api

    private fun observeUpdateStatus() {
        lifecycleScope.launch {
            viewModel._updateStatusData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if(response.data!!.responseCode == 200) {
                            try {
                                SavedPrefManager.savePreferenceBoolean(this@SuggestionListActivity, SavedPrefManager.isSuggestion, true)

                                val intent = Intent(this@SuggestionListActivity,FragmentContainerActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()

                    }

                    is Resource.Loading -> {
                        Progresss.start(this@SuggestionListActivity)
                    }

                    is Resource.Empty -> {
                    }

                }

            }
        }
    }







}