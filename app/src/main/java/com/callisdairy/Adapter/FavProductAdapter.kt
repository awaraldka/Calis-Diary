package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.InterestedClick
import com.callisdairy.Interface.LikeUnlikeProduct
import com.callisdairy.Interface.productView
import com.callisdairy.Interface.viewChat
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.FavProductDocs
import com.callisdairy.databinding.InterestedProductModellayoutBinding

class FavProductAdapter (val context: Context, val data:ArrayList<FavProductDocs>, val click: productView, var chat : viewChat,
var likeClick: LikeUnlikeProduct,val interestedClick: InterestedClick
):
    RecyclerView.Adapter<FavProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InterestedProductModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]
            holder.binding.unInterest.isVisible = false
            holder.binding.hideOptionForInterestOrBuy.isVisible = false


            holder.binding.productName.text =  listData.productName
            holder.binding.Price.text = "${listData.currency}${listData.price}"
            holder.binding.productQuantity.text = "${listData.weight}"

            holder.binding.heart.visibility = View.GONE
            holder.binding.heartfill.visibility = View.VISIBLE

            if (listData.productImage.size != 0){
                Glide.with(context).load(listData.productImage[0]).placeholder(R.drawable.notfound_image).into(holder.binding.itemImage)
            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.itemImage)
            }




            holder.binding.CardClick.setOnClickListener {
                click.view(listData._id, listData.isInterested, position)
            }



            holder.binding.BuyNow.setOnClickListener {

                val sendImage = listData.productImage[0].ifEmpty {
                    ""
                }

                val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId", listData.userId._id)
                intent.putExtra("petImage", listData.userId.petPic)
                intent.putExtra("userImage", listData.userId.profilePic)
                intent.putExtra("userName", listData.userId.name)
                intent.putExtra("sendImage", sendImage)
                intent.putExtra("from", "chat")
                context.startActivity(intent)
            }

            holder.binding.IntrestedClick.setOnClickListener {
                interestedClick.productInterest(listData._id, position)
            }

            if(listData.isInterested){
                holder.binding.BuyNow.isVisible = true
                holder.binding.IntrestedClick.isVisible = false
            }else{
                holder.binding.BuyNow.isVisible = false
                holder.binding.IntrestedClick.isVisible = true
            }


            holder.binding.likeUnLike.setOnClickListener {
                likeClick.addLikeUnlike(
                    listData._id,
                    position,
                    holder.binding.heart,
                    holder.binding.heartfill
                )
            }





        }catch (e:Exception){
            e.printStackTrace()
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: InterestedProductModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}