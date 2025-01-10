package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.PetProfileClick
import com.callisdairy.ModalClass.petProfileClass
import com.callisdairy.R
import com.callisdairy.databinding.PetListProfileBinding
import com.callisdairy.extension.setSafeOnClickListener

class AddPetProfileAdapter(
    val context: Context,
    var data: ArrayList<petProfileClass>,
    val click: PetProfileClick
) : RecyclerView.Adapter<AddPetProfileAdapter.ViewHolder>() {

    var selectItem: Int = 0
    var flag = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PetListProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {

                Glide.with(context).load(userProfileImage).placeholder(R.drawable.placeholder_pet)
                    .into(holder.binding.img)

                holder.binding.type.text = name
                holder.binding.selected.isVisible = isDefaultUserProfile



                if (addNewProfile && data.size -1  == position){
                    holder.binding.viewPost.isVisible = false
                    holder.binding.addMoreProfile.isVisible = true
                    holder.binding.type.text = "Add Profile"
                }else{
                    holder.binding.viewPost.isVisible = true
                    holder.binding.addMoreProfile.isVisible = false
                }



            }

            if (flag) {
                if (selectItem == position) {
                    holder.binding.selected.visibility = View.VISIBLE
                } else {
                    holder.binding.selected.visibility = View.GONE
                }
            }


            holder.binding.viewPost.setOnClickListener {
                flag = true
                selectItem = position
                notifyDataSetChanged()
                click.petProfileUpdate(data[position]._id,holder.binding.type.text.toString())
            }



            holder.binding.addMoreProfile.setSafeOnClickListener{
                click.checkPlan()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
         return data.size

        }
    

    inner class ViewHolder(val binding: PetListProfileBinding) :
        RecyclerView.ViewHolder(binding.root)
}