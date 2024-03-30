  package com.callisdairy.UI.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.SuggestionListAdapter
import com.callisdairy.Interface.SuggestionListClick
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.api.response.suggestionListDocs
import com.callisdairy.databinding.FragmentSuggestionHomeBinding
import com.callisdairy.viewModel.SuggestionListViewModel
import com.callisdairy.Utils.SavedPrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

  @AndroidEntryPoint
class SuggestionHomeFragment : Fragment(), SuggestionListClick {

    private var _binding: FragmentSuggestionHomeBinding?= null
    private val binding get() = _binding!!


    var pages = 0
    var page = 1
    var limit = 20
    var dataLoadFlag =  false
    var loaderFlag = true

    var data = ArrayList<suggestionListDocs>()


    private val viewModel :SuggestionListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSuggestionHomeBinding.inflate(layoutInflater, container, false)


        val token  = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()

        viewModel.suggestionListAPi(token,"",page, limit)

        binding.suggestionScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.suggestionListAPi(token,"+", page, limit)
                }
            }
        })



        return binding.root
    }

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          observeResponseHomeList()
      }




    private fun observeResponseHomeList() {


        viewLifecycleOwner.lifecycleScope.launch {
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
                                data.addAll(response.data.result.docs)
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
                        response.message?.let { message ->
//                            androidExtension.alertBox(message, requireContext())
                        }

                    }

                    is Resource.Loading -> {
//                        Progresss.start(requireContext())
                    }

                    is Resource.Empty -> {
                        Progresss.stop()
                    }

                }

            }

        }



    }



    private fun setSuggestionListAdapter() {
          binding.suggestionUserList.layoutManager = LinearLayoutManager(requireContext())
          val suggestionListAdapter = SuggestionListAdapter(requireContext(),data,this)
          binding.suggestionUserList.adapter = suggestionListAdapter
      }

      override fun follow(
          _id: String,
          position: Int,
          follow: Boolean,
          requested: Boolean,
          followButton: LinearLayout,
          unFollowButton: LinearLayout,
          privacyType: String
      ) {
          Toast.makeText(requireContext(), "follow", Toast.LENGTH_SHORT).show()
      }

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
          Toast.makeText(requireContext(), "unFollow", Toast.LENGTH_SHORT).show()

      }

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
      }

  }