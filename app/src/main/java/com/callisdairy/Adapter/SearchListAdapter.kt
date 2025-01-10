package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.HomeUserProfileView
import com.callisdairy.Interface.SuggestionListClick
import com.callisdairy.R
import com.callisdairy.api.response.GlobalSearchUsers
import com.callisdairy.databinding.SearchListModellayoutBinding

class SearchListAdapter(
    var context: Context,
    var users: ArrayList<GlobalSearchUsers>,
    val click: SuggestionListClick,
    val profileView: HomeUserProfileView
) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchListModellayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = users[position]

            Glide.with(context).load(listData.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petImage)
            Glide.with(context).load(listData.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userImage)
            holder.binding.Name.text =  listData.name
            holder.binding.txtName.text =  listData.userName

            if (listData.privacyType.lowercase() =="public"){
                if(listData.isFollow){
                    holder.binding.FollowButton.isVisible  = false
                    holder.binding.RequestedButton.isVisible  = false
                    holder.binding.UnFollowButton.isVisible  = true
                } else {
                    holder.binding.FollowButton.isVisible  = true
                    holder.binding.UnFollowButton.isVisible  = false
                    holder.binding.RequestedButton.isVisible  = false

                }
            }else if (listData.privacyType.lowercase() == "private"){

                if(listData.isFollow){
                    holder.binding.FollowButton.isVisible  = false
                    holder.binding.RequestedButton.isVisible  = false
                    holder.binding.UnFollowButton.isVisible  = true
                } else if(!listData.isFollow && listData.isRequested){
                    holder.binding.FollowButton.isVisible  = false
                    holder.binding.UnFollowButton.isVisible  = false
                    holder.binding.RequestedButton.isVisible  = true
                } else {
                    holder.binding.FollowButton.isVisible  = true
                    holder.binding.UnFollowButton.isVisible  = false
                    holder.binding.RequestedButton.isVisible  = false

                }
            }


            holder.binding.FollowButton.setOnClickListener {
                click.follow(listData._id,position,listData.isFollow,listData.isRequested,holder.binding.FollowButton,holder.binding.UnFollowButton,listData.privacyType)
            }
            holder.binding.UnFollowButton.setOnClickListener {
                click.unFollow(listData._id,position,listData.isFollow,listData.isRequested,holder.binding.FollowButton,holder.binding.UnFollowButton,listData.privacyType,holder.binding.RequestedButton)
            }
            holder.binding.RequestedButton.setOnClickListener {
                click.requested(listData._id,position,listData.isFollow,listData.isRequested,holder.binding.FollowButton,holder.binding.UnFollowButton,listData.privacyType,holder.binding.RequestedButton)
            }

            holder.binding.viewProfile.setOnClickListener {
                profileView.viewProfile(listData._id,listData.userName)
            }




        } catch (_: Exception) {

        }
    }

    override fun getItemCount(): Int {
        return users.size
    }


    inner class ViewHolder(val binding: SearchListModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}