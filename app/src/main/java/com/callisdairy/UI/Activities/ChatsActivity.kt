package com.callisdairy.UI.Activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.ChatsAdapter
import com.callisdairy.R.*
import com.callisdairy.Socket.*
import com.callisdairy.api.response.SearchDocs
import com.callisdairy.databinding.ActivityChatsBinding
import com.callisdairy.viewModel.peopleSearchViewModel
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.extension.setSafeOnClickListener
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsBinding

    lateinit var chatsAdapter: ChatsAdapter

    var token = ""
    lateinit var docs: ArrayList<SearchDocs>


    var userId = ""

    lateinit var socketInstance: SocketManager
    private val datalist: ArrayList<chatDataResult> = ArrayList<chatDataResult>()



    private val viewModel: peopleSearchViewModel by viewModels()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = style.Fade
        socketInstance = SocketManager.getInstance(this)

        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.Token)!!
        userId = SavedPrefManager.getStringPreferences(this,SavedPrefManager.userId).toString()


        binding.DFsearch.addTextChangedListener(textWatcher)

//       observeSearchPeopleResponse()


        SocketInitalize()

        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }




    }


    private fun SocketInitalize() {
        onAddListeners()
        if (!socketInstance.isConnected) {
            socketInstance.connect()
        } else {
            //   onlineStatus()
        }


        socketInstance.chatDataList(userId)

    }



    private fun onAddListeners() {
        socketInstance.initialize(object : SocketManager.SocketListener {
            override fun onConnected() {
                Log.e("socket", "omd " + "onConnected")
                socketInstance.onlineUser(userId)
            }
            override fun onDisConnected() {
                socketInstance.connect()
            }

            override fun chatHistroy(listdat: ArrayList<ChatHistoryResult>) {

            }



            override fun chatListData(listdat: ArrayList<chatDataResult>) {
                datalist.clear()
                datalist.addAll(listdat)

                binding.llSearch.isVisible = datalist.size > 0
                binding.NotFound.isVisible = datalist.isEmpty()

                setChatAdapter()
            }

            override fun viewchat(listdat: ArrayList<MessagesChat>) {

            }

            override fun oneToOneChat(listdat: ChatHistoryResult) {

            }

            override fun onlineUser(listdat: ArrayList<OnlineUserResult>) {
            }

            override fun offlineUser(listdat: CheckOnlineUserResult) {

            }

            override fun checkOnlineUser(listdat: CheckOnlineUserResult) {


            }

            override fun typing(listdat: JsonObject) {

            }

            override fun typingUser(listdat: UserTypingResult) {

            }


        })}



    private fun setChatAdapter() {
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        chatsAdapter = ChatsAdapter(this, datalist)
        binding.chatRecycler.adapter = chatsAdapter
    }




    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterData(s.toString())

        }
    }


    private fun filterData(searchText: String) {
        val filteredList: ArrayList<chatDataResult> = ArrayList()

        try {
            for (item in datalist) {
                try {
                    if (item.senderId.name.lowercase().contains(searchText.lowercase()) ||
                        item.receiverId.name.lowercase().contains(searchText.lowercase())) {
                        filteredList.add(item)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            try {
                chatsAdapter.filterList(filteredList)

            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }



    }



}