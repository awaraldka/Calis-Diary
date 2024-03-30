package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.FriendListClick
import com.callisdairy.Interface.HomeUserProfileView
import com.callisdairy.R
import com.callisdairy.api.response.RequestedListDocs
import com.callisdairy.databinding.FriendListBinding

class RequestedFriendsListAdapter(var context: Context,var requestedData: ArrayList<RequestedListDocs>,val click: FriendListClick,
                                  val profileView: HomeUserProfileView
) : RecyclerView.Adapter<RequestedFriendsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
             val requestDataList  = requestedData[position]
            holder.binding.Name.text =  requestDataList.requestId.name
            holder.binding.txtName.text =  requestDataList.requestId.userName

            Glide.with(context).load(requestDataList.requestId.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petImage)
            Glide.with(context).load(requestDataList.requestId.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userImage)

            holder.binding.confirmButton.setOnClickListener {
                click.acceptRequest(requestDataList._id,position)
            }

            holder.binding.rejectRequest.setOnClickListener {
                click.rejectRequest(requestDataList._id,position)
            }

            holder.binding.viewProfile.setOnClickListener {
                profileView.viewProfile(requestDataList.requestId._id,requestDataList.requestId.userName)
            }


        }catch(e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return requestedData.size
    }




    inner class ViewHolder(val binding: FriendListBinding) :
        RecyclerView.ViewHolder(binding.root)
}