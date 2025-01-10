package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.Socket.chatDataResult
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.ChatsModellayoutBinding

class ChatsAdapter(var context: Context, var data: ArrayList<chatDataResult>) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChatsModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            val chatListData = data[position]
            val userId = SavedPrefManager.getStringPreferences(context, SavedPrefManager.userId).toString()

            if(userId == chatListData.messages[0].receiverId){
                holder.binding.txtName.text = chatListData.senderId.name
                holder.binding.txtCount.text = chatListData.unReadCount.toString()
                holder.binding.txtCount.isVisible = chatListData.unReadCount > 0
                Glide.with(context).load(chatListData.senderId.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petImage)
                Glide.with(context).load(chatListData.senderId.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userImage)
                holder.binding.time.text = DateFormat.getTimeOnly(chatListData.lastConversation[0].createdAt)


                if (chatListData.messages[0].mediaType.lowercase() == "image") {
                    holder.binding.txtChat.text = "sent a image."
                }


                if (chatListData.messages[0].mediaType.lowercase() == "text"){
                    holder.binding.txtChat.text = chatListData.lastConversation[0].lastMessage

                }



                holder.binding.ChatClick.setOnClickListener {
                    val intent = Intent(context, OneToOneChatActivity::class.java)
                    intent.putExtra("receiverId", chatListData.senderId._id)
                    intent.putExtra("petImage", chatListData.senderId.petPic)
                    intent.putExtra("userImage", chatListData.senderId.profilePic)
                    intent.putExtra("userName", chatListData.senderId.name)
                    intent.putExtra("userTypeChat", chatListData.senderId.userType)
                    context.startActivity(intent)
                }


            }else{
                holder.binding.txtName.text = chatListData.receiverId.name
                holder.binding.txtCount.text = chatListData.unReadCount.toString()
                holder.binding.txtCount.isVisible = chatListData.unReadCount > 0
                Glide.with(context).load(chatListData.receiverId.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petImage)
                Glide.with(context).load(chatListData.receiverId.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userImage)
                holder.binding.time.text = DateFormat.getTimeOnly(chatListData.lastConversation[0].createdAt)

                if (chatListData.messages[0].mediaType.lowercase() == "image") {
                    holder.binding.txtChat.text = "sent a image."
                }


                if (chatListData.messages[0].mediaType.lowercase() == "text"){
                    holder.binding.txtChat.text = chatListData.lastConversation[0].lastMessage

                }


                holder.binding.ChatClick.setOnClickListener {
                   val intent = Intent(context, OneToOneChatActivity::class.java)
                    intent.putExtra("receiverId", chatListData.receiverId._id)
                    intent.putExtra("petImage", chatListData.receiverId.petPic)
                    intent.putExtra("userImage", chatListData.receiverId.profilePic)
                    intent.putExtra("userName", chatListData.receiverId.name)
                    intent.putExtra("userTypeChat", chatListData.receiverId.userType)
                    context.startActivity(intent)
                }


            }

        } catch (_: Exception) {

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: ChatsModellayoutBinding) : RecyclerView.ViewHolder(binding.root)


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<chatDataResult>) {
        data = filteredList
        notifyDataSetChanged()

    }




}