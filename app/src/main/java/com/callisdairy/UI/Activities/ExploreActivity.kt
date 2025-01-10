package com.callisdairy.UI.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.ExploreAdaptor
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.ExploreListDocs
import com.callisdairy.databinding.ActivityExploreBinding
import com.callisdairy.viewModel.StaticContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding
    var data = ArrayList<ExploreListDocs>()
    private val viewModel : StaticContentViewModel by viewModels()

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }


        binding.swipeRefresh.setOnRefreshListener {
            binding.DFsearch.setText("")
            binding.ProgressBarScroll.isVisible = false
            binding.swipeRefresh.isRefreshing = false
        }


        viewModel.exploreApi("",page, limit)

        binding.scrollExplore.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.exploreApi("",page, limit)
                }

            }


        })



        binding.DFsearch.addTextChangedListener(textWatcher)





        observeResponseExplore()


    }


    private fun observeResponseExplore() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._ExploreData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.statusCode == 200) {
                                try {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data.clear()
                                    }



                                    if (response.data.result.docs.size > 0){
                                        binding.notFound.isVisible = false
                                        binding.exploreMore.isVisible = true
                                        data.addAll(response.data.result.docs)

                                        pages = response.data.result.pages
                                        page = response.data.result.page

                                        setAdapter()
                                    }else{
                                        binding.notFound.isVisible = true
                                        binding.exploreMore.isVisible = false
                                        binding.ProgressBarScroll.isVisible = false
                                    }

                                }catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.notFound.isVisible = true
                            binding.exploreMore.isVisible = false
                            response.message?.let { message ->
//                                androidExtension.alertBox(message, this@ExploreActivity)
                            }

                        }

                        is Resource.Loading -> {
                            binding.notFound.isVisible = false
                            if (loaderFlag){
                                Progresss.start(this@ExploreActivity)
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
        binding.exploreMore.layoutManager = LinearLayoutManager(this)
        val adapter = ExploreAdaptor(this,data)
        binding.exploreMore.adapter = adapter
    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            page= 1
            limit =10
            dataLoadFlag = false
            loaderFlag = false
            viewModel.exploreApi(s.toString(),page, limit)
        }

    }




}