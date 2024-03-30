package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.InterestedClick
import com.callisdairy.Interface.LikeUnlikeService
import com.callisdairy.Interface.serviceView
import com.callisdairy.Interface.viewChat
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.FavServiceDocs
import com.callisdairy.databinding.InterestedServicesModellayoutBinding

class FavServicesAdapter (var context: Context,
                          var data:ArrayList<FavServiceDocs>,
                          val click: serviceView, var chat : viewChat,
                          val likeClick: LikeUnlikeService,val interestedClick: InterestedClick
                                 ):
    RecyclerView.Adapter<FavServicesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InterestedServicesModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]
            holder.binding.unInterest.isVisible = false
            holder.binding.hideOptionForInterestOrBuy.isVisible = false


            holder.binding.ServiceName.text = listData.serviceName
            holder.binding.locationName.text = "${listData.userId.country} ${listData.userId.address} ${listData.userId.zipCode}"
            holder.binding.nameUser.text = listData.userId.name


            val experience = listData.experience
            val experienceMonth = listData.experience_month

            val experienceText = when {
                experience.toString().isEmpty() -> "$experienceMonth months"
                else -> "$experience years $experienceMonth months"
            }

            holder.binding.experience.text = experienceText


            if (listData.isLike) {
                holder.binding.heart.isVisible = false
                holder.binding.heartfill.isVisible = true
            }else{
                holder.binding.heart.isVisible = true
                holder.binding.heartfill.isVisible = false
            }
            Glide.with(context).load(listData.serviceImage.getOrNull(0)).placeholder(R.drawable.notfound_image).into(holder.binding.serviceImage)



            holder.binding.ServiceView.setOnClickListener {
                click.viewDes(listData._id, listData.isInterested)
            }


            holder.binding.BuyNow.setOnClickListener {
                val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId", listData.userId._id)
                intent.putExtra("petImage", listData.userId.petPic)
                intent.putExtra("userImage", listData.userId.profilePic)
                intent.putExtra("userName", listData.userId.name)
                intent.putExtra("sendImage",listData.serviceImage[0])
                intent.putExtra("from", "chat")
                context.startActivity(intent)
            }

            holder.binding.IntrestedClick.setOnClickListener {
                interestedClick.serviceInterest(listData._id, position)
            }

            if(listData.isInterested){
                holder.binding.BuyNow.isVisible = true
                holder.binding.IntrestedClick.isVisible = false
            }else{
                holder.binding.BuyNow.isVisible = false
                holder.binding.IntrestedClick.isVisible = true
            }




            holder.binding.likeClick.setOnClickListener {
                likeClick.addLikeUnlikeService(
                    listData._id,
                    position,
                    holder.binding.heart,
                    holder.binding.heartfill
                )
            }


        }catch (_:Exception){

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: InterestedServicesModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}