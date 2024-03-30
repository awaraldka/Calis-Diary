package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.VendorProductClick
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.RewardsResponseDocs
import com.callisdairy.databinding.RewardsLayutBinding

class VendorRewardsAdapter(
    var context: Context, var data: ArrayList<RewardsResponseDocs>):
    RecyclerView.Adapter<VendorRewardsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RewardsLayutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            data[position].apply {


                holder.binding.UserName.text = userName
                holder.binding.RedeemPoints.text = points
                holder.binding.productStatus.text = transferStatus
                holder.binding.date.text = DateFormat.getDate(createdAt)
                holder.binding.time.text = DateFormat.getTime(createdAt)?.replace("am", "AM")?.replace("pm", "PM")
                holder.binding.Description.text = description


                if (transferStatus.uppercase() == "SUCCESS"){
                    holder.binding.productStatus.setTextColor(android.graphics.Color.parseColor("#49CC90"))
                }else{
                    holder.binding.productStatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))

                }





            }


        }catch (_:Exception){

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: RewardsLayutBinding) :
        RecyclerView.ViewHolder(binding.root)


}