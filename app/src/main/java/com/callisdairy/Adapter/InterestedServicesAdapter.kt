package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.LikeUnlikeService
import com.callisdairy.Interface.UnIntrestedPetProductService
import com.callisdairy.Interface.serviceView
import com.callisdairy.Interface.viewChat
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.IntrestedPetDocs
import com.callisdairy.databinding.InterestedServicesModellayoutBinding

class
 InterestedServicesAdapter (var context: Context,
                                 var data:ArrayList<IntrestedPetDocs>,
                                 val click: serviceView, var chat : viewChat,
                                 val likeClick: LikeUnlikeService,
                                 val clickUnInterest: UnIntrestedPetProductService
                                 ):
    RecyclerView.Adapter<InterestedServicesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InterestedServicesModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]


            if (listData.serviceId.isLike){
                holder.binding.heart.visibility = View.GONE
                holder.binding.heartfill.visibility = View.VISIBLE
            }else{
                holder.binding.heart.visibility = View.VISIBLE
                holder.binding.heartfill.visibility = View.GONE
            }


            holder.binding.ServiceName.text = listData.serviceId.serviceName.takeIf { it.isNotBlank() }
            holder.binding.locationName.text = "${listData.userId.country} ${listData.userId.address} ${listData.userId.zipCode}"
            holder.binding.nameUser.text = listData.userId.name.takeIf { it.isNotBlank() }


            if (listData.serviceId.serviceImage.size != 0){
                Glide.with(context).load(listData.serviceId.serviceImage[0]).placeholder(R.drawable.notfound_image).into(holder.binding.serviceImage)
            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.serviceImage)
            }

            val experience = listData.serviceId.experience?.toString()?.takeIf { it.isNotBlank() } ?: "NA"
            val experienceMonth = listData.serviceId.experience_month?.takeIf { it.isNotBlank() } ?: "NA"

            holder.binding.experience.text = when {
                experience == "NA" -> {
                    if (experienceMonth == "NA") {
                        "NA"
                    } else {
                        "$experienceMonth months"
                    }
                }
                else -> {
                    if (experienceMonth == "NA") {
                        "$experience years"
                    } else {
                        "$experience years $experienceMonth months"
                    }
                }
            }









            holder.binding.ServiceView.setOnClickListener {
                click.viewDes(listData.serviceId._id, false)
            }


            holder.binding.unInterest.setOnClickListener {
                clickUnInterest.serviceUnIntrested(listData.serviceId._id, position)
            }

            holder.binding.BuyNow.setOnClickListener {
                val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId", listData.userId._id)
                intent.putExtra("petImage", listData.userId.petPic)
                intent.putExtra("userImage", listData.userId.profilePic)
                intent.putExtra("userName", listData.userId.name)
                intent.putExtra("sendImage",listData.serviceId.serviceImage[0])
                intent.putExtra("from", "chat")
                context.startActivity(intent)
            }





            holder.binding.likeClick.setOnClickListener {
                likeClick.addLikeUnlikeService(
                    listData.serviceId._id,
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