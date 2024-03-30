package com.callisdairy.UI.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.openDialog
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.ModalClass.DialogData
import com.callisdairy.R
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.CountryList
import com.callisdairy.databinding.FragmentFilterBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.SignUpViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterFragment : Fragment(),PopupItemClickListener {

    private lateinit var binding: FragmentFilterBinding

    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    var flag = ""
    lateinit var adapter: openDialog

    var data=  ArrayList<DialogData>()
    var filterData:ArrayList<CountryList> =  ArrayList()

    private val viewModel: SignUpViewModel by viewModels()
    var countryCode = ""
    var stateCode = ""
    var cityCode = ""

//   Tool Bar

    lateinit var back: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for requireContext() fragment
        binding = FragmentFilterBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        allIds()
        toolBarWithBottomTab()


        binding.llSelectRegion.setSafeOnClickListener {
            flag = "Region"
//            openPopUp(flag)
        }

        binding.llSelectCountry.setSafeOnClickListener {
            flag = "Country"
            openPopUp(flag)

        }

        binding.llSelectState.setSafeOnClickListener {
            flag = "State"
            openPopUp(flag)
        }

        binding.llSelectCity.setSafeOnClickListener {
            flag = "City"
            openPopUp(flag)
        }

        back.setSafeOnClickListener {
            fragmentManager?.popBackStack()
        }


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCountryListResponse()
        observeCityListResponse()
        observeStateListResponse()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp(flag: String) {

        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(requireContext(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            dialougbackButton.setSafeOnClickListener { dialog.dismiss() }


            var SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            when (flag) {
                "State" -> {
                    dialougTitle.text = "State"
                    viewModel.getStateListApi(countryCode)

                }
                "City" -> {
                    dialougTitle.text = flag
                    viewModel.getCityListApi(countryCode,stateCode)

                }
                "Country" -> {
                    dialougTitle.text = flag

                    viewModel.getCountryApi()

                }
            }


            SearchEditText.addTextChangedListener(textWatchers)



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }



    override fun getData(data: String, flag: String, code:String) {
        when (flag) {
            "City" -> {
                binding.etCity.text = data
                cityCode = data
                dialog.dismiss()

            }
            "State" -> {
                binding.etState.text = data
                stateCode = code
                binding.etCity.text = ""
                dialog.dismiss()
            }
            "Country" -> {
                binding.etCountry.text = data
                countryCode = code
                binding.etState.text = ""
                binding.etCity.text = ""
                dialog.dismiss()
            }
        }
    }



//    Country List Observer

    private fun observeCountryListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._countryStateFlow.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.statusCode == 200) {
                                try {
                                    filterData = response.data.result
                                    setAdapter(response.data.result)
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

//    State List Observer

    private fun observeStateListResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._stateData.collect { response ->

                    when (response) {

                        is Resource.Success -> {

                            if (response.data?.statusCode == 200) {
                                Progresss.stop()
                                try {
                                    filterData = response.data.result
                                    setAdapter(response.data.result)
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

//    State List Observer

    private fun observeCityListResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._citydata.collect { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.statusCode == 200) {
                                try {
                                    filterData = response.data.result
                                    setAdapter(response.data.result)
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





    private fun setAdapter(result: ArrayList<CountryList>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = openDialog(requireContext(), result, flag,this)
        recyclerView.adapter = adapter
    }


    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterData(s.toString())


        }
    }

    private fun filterData(searchText: String) {
        val filteredList: ArrayList<CountryList> = ArrayList()


        for (item in filterData) {
            try {
                if (item.name.lowercase().contains(searchText.lowercase())) {
                    filteredList.add(item)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        try {
            adapter.filterList(filteredList)

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }






    private fun allIds(){



        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!

    }
    private fun toolBarWithBottomTab(){

        mainTitle.text = ""
        back.visibility = View.GONE
        chat.visibility = View.GONE
        search.visibility = View.GONE
    }

}