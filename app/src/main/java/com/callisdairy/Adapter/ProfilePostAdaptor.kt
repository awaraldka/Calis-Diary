package com.callisdairy.Adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.ViewPost
import com.callisdairy.R
import com.callisdairy.api.response.MyPostDocs
import com.callisdairy.databinding.ProfilePostListBinding

class ProfilePostAdaptor(
    var context: Context,
    var data: ArrayList<MyPostDocs>,
    val click: ViewPost
) :
    RecyclerView.Adapter<ProfilePostAdaptor.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProfilePostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]


            if (listData.images.isEmpty() && listData.videos.isEmpty()){
                holder.binding.txtDescription.isVisible =  true
                holder.binding.img.isVisible =  false
            }else{

                holder.binding.txtDescription.isVisible =  false
                holder.binding.img.isVisible =  true
            }


            if (listData.title.length > 100){
                val text = "<font color=\"blue\">Read More</font>"
                holder.binding.txtDescription.text = Html.fromHtml("${listData.title.replace("\"","").substring(0,98)}...", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }else{
                holder.binding.txtDescription.text = listData.title.replace("\"","")
            }




            if (listData.mediaUrls.size > 0){
                if(listData.mediaUrls[0].type.lowercase() == "video"){
                    Glide.with(context).load(listData.mediaUrls[0].media.thumbnail).placeholder(R.drawable.notfound_image).into(holder.binding.img)

                }else{
                    Glide.with(context).load(listData.mediaUrls[0].media.mediaUrlMobile).placeholder(R.drawable.notfound_image).into(holder.binding.img)

                }
            }else{
                Glide.with(context).load(R.drawable.notfound_image).into(holder.binding.img)
            }


            holder.binding.viewPost.setOnClickListener {
                click.viewPost(listData._id)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: ProfilePostListBinding) :
        RecyclerView.ViewHolder(binding.root)


}