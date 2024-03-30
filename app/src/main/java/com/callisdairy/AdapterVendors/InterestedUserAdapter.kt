package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.GetLocation
import com.callisdairy.R
import com.callisdairy.UI.Activities.OneToOneChatActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.InterestedUserDocs
import com.callisdairy.databinding.InterestUserListBinding
import com.callisdairy.databinding.IntrestedUserListBinding
import com.callisdairy.extension.setSafeOnClickListener

class InterestedUserAdapter(
    var context: Context, var data: ArrayList<InterestedUserDocs>,val from: String,val click: GetLocation
):
    RecyclerView.Adapter<InterestedUserAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InterestUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            data[position].apply {

                Glide.with(context).load(userId.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petPicture)
                Glide.with(context).load(userId.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userProfilePic)

                holder.binding.productName.text = userId.userName
                holder.binding.location.text =  "${userId.city} ${userId.state} ${userId.country} ${userId.zipCode}"
                holder.binding.date.text = DateFormat.dateFormatEvent(createdAt)?.replace("am", "AM")?.replace("pm", "PM")



                holder.binding.ChatClick.setSafeOnClickListener {
                    val intent = Intent(context, OneToOneChatActivity::class.java)
                    intent.putExtra("receiverId", userId._id)
                    intent.putExtra("petImage", userId.petPic)
                    intent.putExtra("userImage", userId.profilePic)
                    intent.putExtra("userName", userId.userName)
                    context.startActivity(intent)
                }


                holder.binding.locationClick.setSafeOnClickListener {
                    click.getLocation(holder.binding.location.text.toString())
                }


            }


        }catch (_:Exception){

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: InterestUserListBinding) :
        RecyclerView.ViewHolder(binding.root)


}