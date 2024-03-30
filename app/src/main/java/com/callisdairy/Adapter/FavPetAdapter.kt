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
import com.callisdairy.Interface.LikeUnlikePet
import com.callisdairy.Interface.viewChat
import com.callisdairy.Interface.viewPests
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.FavPetDocs
import com.callisdairy.databinding.InterestedPetModellayoutBinding

class FavPetAdapter(
    var context: Context, var data: ArrayList<FavPetDocs>, val click: viewPests, var chat: viewChat,
    val likeClick: LikeUnlikePet,val interestedClick: InterestedClick
):
    RecyclerView.Adapter<FavPetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InterestedPetModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]

            holder.binding.unInterest.isVisible = false
            holder.binding.hideOptionForInterestOrBuy.isVisible = false

            holder.binding.locationName.text = "${listData.userId.country} ${listData.userId.address} ${listData.userId.zipCode}"
            holder.binding.nameUser.text = listData.userId.name
            holder.binding.Price.text= "${listData.currency}${listData.price}"
            holder.binding.Title.text = listData.petName

            holder.binding.heart.visibility = View.GONE
            holder.binding.heartfill.visibility = View.VISIBLE



            holder.binding.CardClick.setOnClickListener {
                click.viewPet(listData._id, listData.isInterested)
            }
            holder.binding.BuyNow.setOnClickListener {
                val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId", listData.userId._id)
                intent.putExtra("petImage", listData.userId.petPic)
                intent.putExtra("userImage", listData.userId.profilePic)
                intent.putExtra("userName", listData.userId.name)
                intent.putExtra("sendImage", listData.mediaUrls[0].media.mediaUrlMobile)
                intent.putExtra("from", "chat")
                context.startActivity(intent)

            }

            holder.binding.IntrestedClick.setOnClickListener {
                interestedClick.petInterest(listData._id, position)
            }

            if (listData.mediaUrls.size != 0){
                Glide.with(context).load(listData.mediaUrls[0].media.mediaUrlMobile).placeholder(R.drawable.notfound_image).into(holder.binding.itemImage)
            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.itemImage)
            }



            if(listData.isInterested){
                holder.binding.BuyNow.isVisible = true
                holder.binding.IntrestedClick.isVisible = false
            }else{
                holder.binding.BuyNow.isVisible = false
                holder.binding.IntrestedClick.isVisible = true
            }

            holder.binding.likeUnLike.setOnClickListener {
                likeClick.addLikeUnlikePet(
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


    inner class ViewHolder(val binding: InterestedPetModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}