package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.VendorHomeClick
import com.callisdairy.ModalClass.VendorHomeModalClass
import com.callisdairy.databinding.HomeDetailsBinding
import com.callisdairy.extension.setSafeOnClickListener

class VendorHomeAdapter(var context: Context, var data: ArrayList<VendorHomeModalClass>, val click: VendorHomeClick):
    RecyclerView.Adapter<VendorHomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.binding.Description.isSelected = true
            data[position].apply {
                holder.binding.Value.text = total.toString()
                holder.binding.Description.text = name
                Glide.with(context).load(image).into(holder.binding.icons)
            }



            holder.binding.HomeCardview.setSafeOnClickListener{
                click.viewDetails(holder.binding.Description.text.toString())
            }


        }catch (_:Exception){

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: HomeDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)


}