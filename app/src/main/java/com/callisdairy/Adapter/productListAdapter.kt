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
import com.callisdairy.Interface.LikeUnlikeProduct
import com.callisdairy.Interface.productView
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.ProductListDocs
import com.callisdairy.databinding.ProductListBuyBinding
import com.callisdairy.extension.setSafeOnClickListener

class productListAdapter(val context:Context, val data:ArrayList<ProductListDocs>, val click: productView,
                         var likeClick: LikeUnlikeProduct,val interestedClick: InterestedClick
)
    :RecyclerView.Adapter<productListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductListBuyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       try {
           val listData = data[position]

           holder.binding.productName.text =  listData.productName
           holder.binding.Price.text = "${listData.currency} ${listData.price}"
           holder.binding.productQuantity.text = "${listData.weight} KG"
           if (listData.productImage.size != 0){
               Glide.with(context).load(listData.productImage[0]).placeholder(R.drawable.notfound_image).into(holder.binding.itemImage)
           }else{
               Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.itemImage)
           }



           holder.binding.CategoryCV.setSafeOnClickListener{
               click.view(listData._id, listData.isInterested, position)
           }

           holder.binding.BuyNow.setSafeOnClickListener{
               val intent = Intent(context, OneToOneChatActivity::class.java)
               intent.putExtra("receiverId", listData.userId._id)
               intent.putExtra("petImage", listData.userId.petPic)
               intent.putExtra("userImage", listData.userId.profilePic)
               intent.putExtra("userName", listData.userId.name)
               intent.putExtra("sendImage", listData.productImage[0])
               intent.putExtra("from", "chat")
               context.startActivity(intent)
           }

           holder.binding.likeClick.setOnClickListener {
               likeClick.addLikeUnlike(listData._id, position, holder.binding.heart,holder.binding.heartfill )
           }

           holder.binding.IntrestedClick.setOnClickListener {
               interestedClick.productInterest(listData._id, position)
           }



           if (listData.isLike){
               holder.binding.heart.isVisible = false
               holder.binding.heartfill.isVisible = true

           }else{
               holder.binding.heart.visibility = View.VISIBLE
               holder.binding.heartfill.visibility = View.GONE

           }

           if(listData.isInterested){
               holder.binding.BuyNow.isVisible = true
               holder.binding.IntrestedClick.isVisible = false
           }else{
               holder.binding.BuyNow.isVisible = false
               holder.binding.IntrestedClick.isVisible = true
           }






       }catch (e:Exception){
           e.printStackTrace()
       }



    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: ProductListBuyBinding) :
        RecyclerView.ViewHolder(binding.root)


}