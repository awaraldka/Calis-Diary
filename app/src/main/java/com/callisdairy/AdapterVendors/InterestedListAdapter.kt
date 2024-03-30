package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.ViewUserList
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.InterestedUserDocs
import com.callisdairy.databinding.IntrestedUserListBinding

class InterestedListAdapter(
    var context: Context, var data: ArrayList<InterestedUserDocs>,val from: String,val click: ViewUserList
):
    RecyclerView.Adapter<InterestedListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = IntrestedUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            data[position].apply {


                holder.binding.viewUsers.setOnClickListener {
                    click.viewUserList(_id,from)
                }


                if (from == "HomeProduct"){
                    holder.binding.productName.text =  productId?.productName
                    holder.binding.location.text =  productId?.productGenerateId
                    if (productId!!.productImage!!.isNotEmpty()){
                        Glide.with(context).load(productId.productImage?.get(0)).into(holder.binding.imageItem)
                    }




                }

                if (from== "HomeService"){
                    holder.binding.productName.text =  serviceId?.serviceName
                    holder.binding.location.text =  serviceId?.serviceGenerateId
                    holder.binding.firstName.text = context.getString(R.string.service_name)
                    holder.binding.locationName.text = context.getString(R.string.service_id)
                    if (serviceId!!.serviceImage!!.isNotEmpty()){
                        Glide.with(context).load(serviceId.serviceImage?.get(0)).into(holder.binding.imageItem)
                    }
                }

                if (from == "HomePet"){
                    holder.binding.firstName.text = context.getString(R.string.pet_name)
                    holder.binding.locationName.text = context.getString(R.string.pet_breed)
                    holder.binding.productName.text =  petId?.petName
                    holder.binding.location.text =  petId?.breed

                    if (petId!!.mediaUrls!!.isNotEmpty() && petId.mediaUrls?.get(0)?.type!!.lowercase() == "image"){
                        Glide.with(context).load(petId.mediaUrls[0].media.mediaUrlMobile).into(holder.binding.imageItem)
                    }
                }


                holder.binding.date.text = DateFormat.dateFormatEvent(createdAt)?.replace("am", "AM")?.replace("pm", "PM")






            }


        }catch (_:Exception){

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: IntrestedUserListBinding) :
        RecyclerView.ViewHolder(binding.root)


}