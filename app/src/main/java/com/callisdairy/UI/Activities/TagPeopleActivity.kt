package com.callisdairy.UI.Activities

import dagger.hilt.android.AndroidEntryPoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.callisdairy.Adapter.TagPeopleAdapter
import com.callisdairy.Interface.TagPeople
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.TagPeopleDocs
import com.callisdairy.databinding.ActivityTagPeopleBinding
import com.callisdairy.viewModel.TagPeopleViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.extension.androidExtension
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class TagPeopleActivity : AppCompatActivity(),TagPeople {

    private lateinit var binding: ActivityTagPeopleBinding

    lateinit var Adapter : TagPeopleAdapter
    var token = ""
    lateinit var docs: ArrayList<TagPeopleDocs>


    private val viewModel: TagPeopleViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTagPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)



        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token)!!




        binding.etSearch.addTextChangedListener(textWatcher)

        observeTagPeopleResponse()

        binding.back.setSafeOnClickListener{
            finishAfterTransition()
        }

        binding.forward.setSafeOnClickListener {
            finishAfterTransition()
        }
    }


    val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){
            viewModel.tagPeopleApi(token, s.toString())
        }

        override fun afterTextChanged(s: Editable?) {




        }

    }




    private fun observeTagPeopleResponse() {


        lifecycleScope.launchWhenCreated {
            viewModel._tagPeopleData.collectLatest { response ->

                when (response) {

                    is Resource.Success -> {

//                        hideProgressBar()

                        if(response.data?.statusCode == 200) {
                            try {
                                docs = response.data.result.docs
                                setAdapter()
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        docs.clear()
//                        hideProgressBar()
                        response.message?.let { message ->
//                            if (!message.lowercase().contains("user not found"))
                            androidExtension.alertBox(message, this@TagPeopleActivity)
                        }

                    }

                    is Resource.Loading -> {
//                        showProgressBar()
                    }

                    is Resource.Empty -> {
//                        hideProgressBar()
                    }

                }

            }
        }
    }



    private fun setAdapter() {
//        binding.tagPeopleRecycler.layoutManager = LinearLayoutManager(this)
//        Adapter = TagPeopleAdapter(this,docs)
//        binding.tagPeopleRecycler.adapter = Adapter
    }

    override fun selectedPeople(id: String, name: String) {
    }


}