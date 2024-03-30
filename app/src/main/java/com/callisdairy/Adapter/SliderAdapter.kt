package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.api.response.PetImage
import com.callisdairy.databinding.ImageSlideModellayoutBinding

class SliderAdapter(var context: Context, var data: ArrayList<PetImage>) : RecyclerView.Adapter<SliderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val binding = ImageSlideModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val listData = data[position]
            Glide.with(context).load(listData.url).placeholder(R.drawable.placeholder_pet).into(holder.binding.imgBanner)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(val binding: ImageSlideModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}