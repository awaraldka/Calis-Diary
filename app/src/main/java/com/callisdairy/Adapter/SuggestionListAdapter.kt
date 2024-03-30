package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.SuggestionListClick
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.api.response.suggestionListDocs
import com.callisdairy.databinding.SuggestionListLayoutBinding
import com.callisdairy.extension.setSafeOnClickListener

class SuggestionListAdapter(
    var context: Context,
    val data: ArrayList<suggestionListDocs>,
    val click: SuggestionListClick
):RecyclerView.Adapter<SuggestionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SuggestionListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            val listData = data[position]


            holder.binding.userName.text = listData.userName
            Glide.with(context).load(listData.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petImage)
            Glide.with(context).load(listData.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userImage)




            holder.binding.viewProfile.setSafeOnClickListener {
                val intent = Intent(context, CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "OtherUsers")
                intent.putExtra("userName", listData.userName)
                intent.putExtra("id", listData._id)
                context.startActivity(intent)
            }

            holder.binding.ViewAccount.setSafeOnClickListener {
                val intent = Intent(context, CommonActivityForViewActivity::class.java)
                intent.putExtra("flag", "OtherUsers")
                intent.putExtra("userName", listData.userName)
                intent.putExtra("id", listData._id)
                context.startActivity(intent)
            }






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



        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: SuggestionListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}