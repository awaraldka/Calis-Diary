package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.databinding.ViewPagerLayoutBinding

class ImageSliderAdaptor(var context: Context, var data: ArrayList<String>) : RecyclerView.Adapter<ImageSliderAdaptor.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val binding = ViewPagerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {

            if(data.size > 0){
                Glide.with(context).load(data[position]).into(holder.binding.imgBanner)
            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.imgBanner)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(val binding: ViewPagerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}