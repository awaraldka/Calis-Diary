package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.ViewPetFromProfile
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddPetActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.MyPetListDocs
import com.callisdairy.databinding.MyProfileItemListBinding

class MyProfileItemListAdaptor(
    var context: Context,
    var data: ArrayList<MyPetListDocs>,
    val click: ViewPetFromProfile,
    val isYouFlag: Boolean
) :
    RecyclerView.Adapter<MyProfileItemListAdaptor.ViewHolder>() {

    inner class ViewHolder(val binding: MyProfileItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MyProfileItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]

            holder.binding.PetName.text = listData.petName
            holder.binding.Time.text = DateFormat.covertTimeOtherFormat(listData.createdAt!!)

            if(listData.mediaUrls.size > 0){
                Glide.with(context).load(listData.mediaUrls[0].media.mediaUrlMobile).placeholder(R.drawable.placeholder_pet).into(holder.binding.itemImage)
            }else{
                Glide.with(context).load(R.drawable.placeholder_pet).into(holder.binding.itemImage)
            }

            val userId = SavedPrefManager.getStringPreferences(context,SavedPrefManager.userId).toString()


            if (isYouFlag){
                if (listData.isMarketPlace) {
                    holder.binding.marketPlaceButton.visibility = View.VISIBLE
                } else {
                    holder.binding.marketPlaceButton.visibility = View.GONE
                }
            }


            holder.binding.edit.isVisible = userId == listData.userId._id





            holder.binding.viewPet.setOnClickListener {
                click.viewPetDetails(listData._id!!)
            }

            holder.binding.mainButtonTv.visibility = View.VISIBLE
            holder.binding.heartOutline.visibility = View.GONE
//            holder.binding.edit.visibility = View.VISIBLE




            holder.binding.edit.setOnClickListener {
                val intent = Intent(context, AddPetActivity::class.java)
                intent.putExtra("flag", "Edit Pet")
                intent.putExtra("id", listData._id)
                context.startActivity(intent)
            }


        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

}