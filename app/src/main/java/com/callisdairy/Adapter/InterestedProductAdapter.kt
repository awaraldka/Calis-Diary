package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.LikeUnlikeProduct
import com.callisdairy.Interface.UnIntrestedPetProductService
import com.callisdairy.Interface.productView
import com.callisdairy.Interface.viewChat
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.IntrestedPetDocs
import com.callisdairy.databinding.InterestedProductModellayoutBinding

class InterestedProductAdapter (val context: Context, val data:ArrayList<IntrestedPetDocs>, val click: productView, var chat : viewChat,
var likeClick: LikeUnlikeProduct,val clickUnInterest: UnIntrestedPetProductService
):
    RecyclerView.Adapter<InterestedProductAdapter.ViewHolder>() {


    var image  = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InterestedProductModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]

            holder.binding.productName.text =  listData.productId.productName.takeIf { it.isNotBlank() }
            holder.binding.Price.text = "${listData.productId.currency} ${listData.productId.price}".takeIf { it.isNotBlank() }
            holder.binding.productQuantity.text = "${listData.productId.weight} Kg".takeIf { it.isNotBlank() }

            if (listData.productId.isLike){
                holder.binding.heart.visibility = View.GONE
                holder.binding.heartfill.visibility = View.VISIBLE
            }else{
                holder.binding.heart.visibility = View.VISIBLE
                holder.binding.heartfill.visibility = View.GONE
            }


            if (listData.productId.productImage.size != 0){
                Glide.with(context).load(listData.productId.productImage[0]).placeholder(R.drawable.notfound_image).into(holder.binding.itemImage)

            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.itemImage)
            }



            holder.binding.CardClick.setOnClickListener {
                click.view(listData.productId._id, false, position)
            }

            holder.binding.unInterest.setOnClickListener {
                clickUnInterest.productUnIntrested(listData.productId._id, position)
            }
            holder.binding.BuyNow.setOnClickListener {
                 val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId", listData.userId._id)
                intent.putExtra("petImage", listData.userId.petPic)
                intent.putExtra("userImage", listData.userId.profilePic)
                intent.putExtra("userName", listData.userId.name)
                intent.putExtra("sendImage",listData.productId.productImage[0])
                intent.putExtra("from", "chat")
                context.startActivity(intent)
            }

            holder.binding.likeUnLike.setOnClickListener {
                likeClick.addLikeUnlike(
                    listData.productId._id,
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