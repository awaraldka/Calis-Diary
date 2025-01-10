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
import com.callisdairy.Interface.LikeUnlikeService
import com.callisdairy.Interface.serviceView
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.api.response.ServiceListDocs
import com.callisdairy.databinding.ServiceListBinding
import com.callisdairy.extension.setSafeOnClickListener

class serviceListAdapter(val context: Context, val docs: ArrayList<ServiceListDocs>,val click: serviceView,
                         val likeClick: LikeUnlikeService,val interestedClick: InterestedClick
) :
    RecyclerView.Adapter<serviceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ServiceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            val listData = docs[position]

            holder.binding.ServiceName.text =  listData.serviceName
            holder.binding.locationName.text =  listData.userId.address
            holder.binding.nameUser.text =  listData.userId.name


            val experience = listData.experience
            val experienceMonth = listData.experience_month

            val experienceText = when {
                experience.toString().isEmpty() -> "$experienceMonth months"
                else -> "$experience years $experienceMonth months"
            }

            holder.binding.experience.text = experienceText

            if (listData.serviceImage.size !=0){
                Glide.with(context).load(listData.serviceImage[0]).placeholder(R.drawable.notfound_image).into(holder.binding.serviceImage)
            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.serviceImage)
            }




            holder.binding.ServiceView.setOnClickListener {
                click.viewDes(listData._id, listData.isInterested)
            }

            holder.binding.likeClick.setOnClickListener {
                likeClick.addLikeUnlikeService(listData._id,position,holder.binding.heart,holder.binding.heartfill)
            }

            holder.binding.BuyNow.setSafeOnClickListener{
                val intent = Intent(context, OneToOneChatActivity::class.java)
                intent.putExtra("receiverId", listData.userId._id)
                intent.putExtra("petImage", listData.userId.petPic)
                intent.putExtra("userImage", listData.userId.profilePic)
                intent.putExtra("userName", listData.userId.name)
                intent.putExtra("sendImage", listData.serviceImage[0])
                intent.putExtra("from", "chat")
                context.startActivity(intent)
            }

            holder.binding.IntrestedClick.setOnClickListener {
                interestedClick.serviceInterest(listData._id, position)
            }

//            val userId  = SavedPrefManager.getStringPreferences(context,SavedPrefManager.userId).toString()


                if (listData.isLike){
                    holder.binding.heart.visibility = View.GONE
                    holder.binding.heartfill.visibility = View.VISIBLE

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
        return docs.size
    }

    inner class ViewHolder(val binding: ServiceListBinding) :
        RecyclerView.ViewHolder(binding.root)

}