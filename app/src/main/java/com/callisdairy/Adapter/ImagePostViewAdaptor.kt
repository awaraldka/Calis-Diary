package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.databinding.ViewPagerLayoutBinding

class ImagePostViewAdaptor(var context: Context, var data: ArrayList<MediaUrls>) : RecyclerView.Adapter<ImagePostViewAdaptor.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val binding = ViewPagerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val listData = data[position]
            Glide.with(context).load(listData.media.mediaUrlMobile).placeholder(R.drawable.notfound_image).into(holder.binding.imgBanner)
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