package com.callisdairy.UI.Activities.subscription

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.callisdairy.Interface.BuyPlan
import com.callisdairy.R
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.ActivitySubscribePlansUserBinding
import com.callisdairy.viewModel.CheckContentLimitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscribePlansUserActivity : AppCompatActivity(), BuyPlan {

    private lateinit var binding: ActivitySubscribePlansUserBinding

    var token = ""
    var screen = ""
    lateinit var adapter : PlanListAdapter
    private val viewModel :  CheckContentLimitViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  =ActivitySubscribePlansUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        token =  SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()


        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }


        intent?.getStringExtra("Screen")?.let { screen = it }


        if (screen =="Event"){
            binding.title.text = "Events Plans"
            Glide.with(this).load(R.drawable.events_ico).into(binding.img)
            viewModel.planListApi(type = "PLAN", userType = "USER",moduleName= "EVENT")
        }

        if (screen =="Missing"){
            viewModel.planListApi(type = "PLAN", userType = "USER",moduleName= "MISSINGPET")
        }

        if (screen =="Product"){
            binding.title.text = "Product Plans"
            binding.img.isVisible = false
            viewModel.planListApi(type = "PLAN", userType = "VENDOR",moduleName= "PRODUCT")
        }

        if (screen =="Service"){
            binding.title.text = "Services Plans"
            binding.img.isVisible = false
            viewModel.planListApi(type = "PLAN", userType = "VENDOR",moduleName= "SERVICE")
        }

        if (screen =="Pet"){
            binding.title.text = "Pets Plans"
            binding.img.isVisible = false
            viewModel.planListApi(type = "PLAN", userType = "VENDOR",moduleName= "PET")
        }

        if (screen =="Event Vendor"){
            binding.title.text = "Events Plans"
            binding.img.isVisible = false
            viewModel.planListApi(type = "PLAN", userType = "VENDOR",moduleName= "EVENT")
        }


        observeResponse()


    }



    private fun observeResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._planData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    binding.planRecycler.layoutManager =  LinearLayoutManager(this@SubscribePlansUserActivity)
                                    adapter = PlanListAdapter(this@SubscribePlansUserActivity,response.data.result.docs,click= this@SubscribePlansUserActivity)
                                    binding.planRecycler.adapter =  adapter
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

    override fun buy(id: String) {
        val intent = Intent(this,CardDetailsActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("screen",screen)
        startActivity(intent)
        finishAfterTransition()
    }


}