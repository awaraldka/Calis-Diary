package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.LikeUnlikePet
import com.callisdairy.Interface.UnIntrestedPetProductService
import com.callisdairy.Interface.viewChat
import com.callisdairy.Interface.viewPests
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.IntrestedPetDocs
import com.callisdairy.databinding.InterestedPetModellayoutBinding

class InterestedPetAdapter (var context: Context, var data:ArrayList<IntrestedPetDocs>, val click: viewPests, var chat : viewChat,
 val likeClick: LikeUnlikePet,val clickUnInterest: UnIntrestedPetProductService
):
    RecyclerView.Adapter<InterestedPetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InterestedPetModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]

            holder.binding.locationName.text = "${listData.userId.country} ${listData.userId.address} ${listData.userId.zipCode}"
            holder.binding.nameUser.text = listData.userId.name

            if (listData.petId.price == null){
                holder.binding.Price.text= "$0"
            }else{
                holder.binding.Price.text= " ${listData.petId.currency}${listData.petId.price}"
            }

            holder.binding.Title.text = listData.petId.petName

            if (listData.petId.isLike){
                holder.binding.heart.visibility = View.GONE
                holder.binding.heartfill.visibility = View.VISIBLE
            }else{
                holder.binding.heart.visibility = View.VISIBLE
                holder.binding.heartfill.visibility = View.GONE
            }

            if (listData.petId.mediaUrls.size != 0){
                Glide.with(context).load(listData.petId.mediaUrls[0].media.mediaUrlMobile).placeholder(R.drawable.notfound_image).into(holder.binding.itemImage)
            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.itemImage)
            }

            holder.binding.CardClick.setOnClickListener {
                click.viewPet(listData.petId._id, false)
            }

            holder.binding.unInterest.setOnClickListener {
                clickUnInterest.petUnIntrested(listData.petId._id, position)
            }


            holder.binding.BuyNow.setOnClickListener {
                val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId", listData.userId._id)
                intent.putExtra("petImage", listData.userId.petPic)
                intent.putExtra("userImage", listData.userId.profilePic)
                intent.putExtra("userName", listData.userId.name)
                intent.putExtra("sendImage",listData.petId.mediaUrls[0].media.mediaUrlMobile)
                intent.putExtra("from", "chat")
                context.startActivity(intent)
            }




            holder.binding.likeUnLike.setOnClickListener {
                likeClick.addLikeUnlikePet(
                    listData.petId._id,
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