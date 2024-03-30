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
import com.callisdairy.databinding.SuggestedUserHomeBinding
import com.callisdairy.extension.setSafeOnClickListener

class SuggestedHomeAdapter(val context: Context, private val suggestedUserData: List<suggestionListDocs>,
                           val click: SuggestionListClick):RecyclerView.Adapter<SuggestedHomeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding:SuggestedUserHomeBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SuggestedUserHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return suggestedUserData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       try {
           suggestedUserData[position].apply {
               val binding = holder.binding


               binding.Name.text = name
               binding.userName.text = userName
               Glide.with(context).load(petPic).placeholder(R.drawable.placeholder_pet).into(binding.petPicture)
               Glide.with(context).load(profilePic).placeholder(R.drawable.placeholder).into(binding.userProfilePic)




               binding.viewProfile.setSafeOnClickListener {
                   val intent = Intent(context, CommonActivityForViewActivity::class.java)
                   intent.putExtra("flag", "OtherUsers")
                   intent.putExtra("userName", userName)
                   intent.putExtra("id", _id)
                   context.startActivity(intent)
               }


               if (privacyType.lowercase() =="public"){
                   if(isFollow){
                       binding.FollowButton.isVisible  = false
                       binding.RequestedButton.isVisible  = false
                       binding.UnFollowButton.isVisible  = true
                   } else {
                       binding.FollowButton.isVisible  = true
                       binding.UnFollowButton.isVisible  = false
                       binding.RequestedButton.isVisible  = false

                   }
               }else if (privacyType.lowercase() == "private"){

                   if(isFollow){
                       binding.FollowButton.isVisible  = false
                       binding.RequestedButton.isVisible  = false
                       binding.UnFollowButton.isVisible  = true
                   } else if(!isFollow && isRequested){
                       binding.FollowButton.isVisible  = false
                       binding.UnFollowButton.isVisible  = false
                       binding.RequestedButton.isVisible  = true
                   } else {
                       binding.FollowButton.isVisible  = true
                       binding.UnFollowButton.isVisible  = false
                       binding.RequestedButton.isVisible  = false

                   }
               }

               holder.binding.FollowButton.setOnClickListener {
                   click.follow(_id,position,isFollow,isRequested,holder.binding.FollowButton,holder.binding.UnFollowButton,privacyType)
               }
               holder.binding.UnFollowButton.setOnClickListener {
                   click.unFollow(_id,position,isFollow,isRequested,holder.binding.FollowButton,holder.binding.UnFollowButton,privacyType,holder.binding.RequestedButton)
               }
               holder.binding.RequestedButton.setOnClickListener {
                   click.requested(_id,position,isFollow,isRequested,holder.binding.FollowButton,holder.binding.UnFollowButton,privacyType,holder.binding.RequestedButton)
               }
               
           }

       }catch (e:Exception){
           e.printStackTrace()
       }
    }
}