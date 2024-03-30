package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.UnBlockUser
import com.callisdairy.api.response.AppointmentListUserId
import com.callisdairy.databinding.BlockUnblockClientBinding

class BlockUserAdapter(val context: Context, var mArrayUri: List<AppointmentListUserId>,val click:UnBlockUser):RecyclerView.Adapter<BlockUserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BlockUnblockClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            mArrayUri[position].apply {
                Glide.with(context).load(petPic).into(binding.petImage)
                Glide.with(context).load(profilePic).into(binding.userProfilePicPublic)
                binding.Name.text =  name
                binding.petName.text =  "Pet Name: NA"
                binding.petType.text =  "Pet Type: $petCategoryName"

                binding.block.isVisible = true


                binding.block.setOnClickListener {
                    click.unBlockUser(position,id)
                }



            }




        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mArrayUri.size
    }

    inner class ViewHolder(val binding: BlockUnblockClientBinding) : RecyclerView.ViewHolder(binding.root)
}