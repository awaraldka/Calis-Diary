package com.callisdairy.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.FAQsAdapter
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.FaqResult
import com.callisdairy.databinding.ActivityFaqactivityBinding
import com.callisdairy.viewModel.FaqViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FAQActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqactivityBinding

    lateinit var FAQsAdapter: FAQsAdapter
    lateinit var result: ArrayList<FaqResult>
    lateinit var socketInstance: SocketManager

    var loaderFlag = true

    private val viewModel: FaqViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)

        viewModel.faqListApi("")

        binding.DfSearch.addTextChangedListener(textWatcher)


        faqListObserver()

        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }

    }


    private fun faqListObserver(){


        lifecycleScope.launchWhenStarted {
            viewModel._faqListData.collect { response ->

                when (response) {

                    is Resource.Success -> {
                        Progresss.stop()
                        if(response.data?.responseCode == 200) {
                            try {
                                loaderFlag = false
                                result =response.data.result
                                faqRecycler()
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progresss.stop()
                        response.message?.let { message ->
                            if (!message.lowercase().contains("data not found")){
                                androidExtension.alertBox(message, this@FAQActivity)
                            }

                        }
                    }

                    is Resource.Loading -> {
                        if (loaderFlag){
                            Progresss.start(this@FAQActivity)
                        }

                    }

                    is Resource.Empty -> {
                        
                    }

                }

            }
        }
    }




    private fun faqRecycler() {
        binding.FAQsRecycler.layoutManager = LinearLayoutManager(this)
        FAQsAdapter = FAQsAdapter(this,result)
        binding.FAQsRecycler.adapter = FAQsAdapter
    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            result.clear()
            if (s.toString().isEmpty()){
                viewModel.faqListApi(s.toString())
            }else{

                viewModel.faqListApi(s.toString())
            }

        }

    }



}