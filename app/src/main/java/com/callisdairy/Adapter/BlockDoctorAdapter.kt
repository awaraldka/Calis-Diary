package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.RemoveImage
import com.callisdairy.Interface.UnBlockUser
import com.callisdairy.ModalClass.BlockDoctorData
import com.callisdairy.ModalClass.BlockUserData
import com.callisdairy.ModalClass.addPetRequest
import com.callisdairy.api.request.mediaUrls
import com.callisdairy.api.response.AppointmentListUserId
import com.callisdairy.databinding.BlockUnblockClientBinding
import com.callisdairy.databinding.BlockUnblockDoctorBinding
import com.callisdairy.databinding.ViewSelectedValueBinding
import java.util.ArrayList

class BlockDoctorAdapter(val context: Context, var mArrayUri: List<AppointmentListUserId>,val click: UnBlockUser):RecyclerView.Adapter<BlockDoctorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BlockUnblockDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            mArrayUri[position].apply {
                Glide.with(context).load(profilePic).into(binding.doctorImage)
                binding.doctorName.text =  name
                binding.tvSpecialization.text =  "specialization"
                binding.tvExp.text =  ""
                    binding.Unblock.isVisible = false
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

    inner class ViewHolder(val binding: BlockUnblockDoctorBinding) : RecyclerView.ViewHolder(binding.root)
}