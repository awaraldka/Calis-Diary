package com.callisdairy.UI.Fragments

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.RequestedFriendsListAdapter
import com.callisdairy.Interface.FriendListClick
import com.callisdairy.Interface.HomeUserProfileView
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.RequestedListDocs
import com.callisdairy.databinding.FragmentRequestedListBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.RequestedListViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RequestedListFragment : Fragment(), FriendListClick, HomeUserProfileView {


    private lateinit var binding: FragmentRequestedListBinding
    var token  = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag =  false
    var loaderFlag = true

    var adapterPosition = 0


    var from= ""

    var requestedData = ArrayList<RequestedListDocs>()
    lateinit var Adapter : RequestedFriendsListAdapter

    private val viewModel : RequestedListViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRequestedListBinding.inflate(layoutInflater, container, false)

        arguments?.getString("from")?.let { from = it }

        if (from == "user"){
            allIds()
        }else{
            val backButton = activity?.findViewById<ImageView>(R.id.backVendor)!!

            backButton.setSafeOnClickListener {
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }
        }


        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()


        viewModel.requestedListApi(token,page,limit)




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResponseRequestList()
        observeResponseAcceptOrReject()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (from == "vendor"){
                    activity?.finishAfterTransition()
                }
                parentFragmentManager.popBackStack()
            }

        })


    }


    private fun observeResponseRequestList() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._RequestedListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if(response.data?.responseCode == 200) {
                                try {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        requestedData.clear()
                                    }

                                    requestedData.addAll(response.data.result.docs)


                                    if (requestedData.size > 0){

                                        binding.NotFound.isVisible = false
                                        binding.FriendsList.isVisible = true

                                        pages = response.data.result.totalPages
                                        page = response.data.result.page
                                        requestedListAdapter()
                                    }else{
                                        binding.NotFound.isVisible = true
                                        binding.FriendsList.isVisible = false
                                    }



                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.FriendsList.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message,requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            if (loaderFlag){
                                binding.NotFound.isVisible = false
                                binding.FriendsList.isVisible = false
                                Progresss.start(requireContext())
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
    private fun requestedListAdapter(){
        binding.FriendsList.layoutManager = LinearLayoutManager(requireContext())
        Adapter = RequestedFriendsListAdapter(requireContext(),requestedData,this,this)
        binding.FriendsList.adapter = Adapter
    }

    override fun acceptRequest(_id: String, position: Int) {
        adapterPosition = position
        viewModel.acceptRejectApi(token,"ACCEPT",_id)
    }

    override fun rejectRequest(_id: String, position: Int) {
        adapterPosition = position
        viewModel.acceptRejectApi(token,"REJECT",_id)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponseAcceptOrReject() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel._acceptOrRejectData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if(response.data?.responseCode == 200) {
                                try {

                                    if(requestedData.size == 1){
                                        binding.NotFound.isVisible = true
                                        binding.FriendsList.isVisible = false
                                    }


                                    requestedData.removeAt(adapterPosition)
                                    Adapter.notifyItemRemoved(adapterPosition)
                                    Adapter.notifyItemRangeChanged(adapterPosition, requestedData.size)
                                    Adapter.notifyDataSetChanged()

                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message,requireContext())
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
        val intent = Intent(requireContext(), CommonActivityForViewActivity::class.java)
        intent.putExtra("flag", "OtherUsers")
        intent.putExtra("userName", userName)
        intent.putExtra("id", _id)
        startActivity(intent)
    }


    private fun allIds() {



        val chat = activity?.findViewById<ImageView>(R.id.ChantClick)!!
        val mainTitle = activity?.findViewById<TextView>(R.id.MainTitle)!!
        val back = activity?.findViewById<ImageView>(R.id.back)!!
        val search = activity?.findViewById<ImageView>(R.id.SearchClick)!!
        val Username = activity?.findViewById<TextView>(R.id.Username)!!
        val addPost = activity?.findViewById<ImageView>(R.id.addPost)!!


        mainTitle.visibility = View.GONE
        addPost.visibility = View.GONE
        chat.visibility = View.VISIBLE
        back.visibility = View.VISIBLE
        search.visibility = View.GONE


        Username.text = getString(R.string.friends)

        back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }



}