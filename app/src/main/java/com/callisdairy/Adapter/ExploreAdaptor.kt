package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.ViewPetFromProfile
import com.callisdairy.R
import com.callisdairy.UI.Activities.WatchActivity
import com.callisdairy.api.response.ExploreListDocs
import com.callisdairy.databinding.ExploreMoreBinding
import com.callisdairy.databinding.InterestedPetModellayoutBinding

class ExploreAdaptor(var context: Context, var data: ArrayList<ExploreListDocs>) :
    RecyclerView.Adapter<ExploreAdaptor.ViewHolder>() {

    inner class ViewHolder(val binding: ExploreMoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExploreMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            data[position].apply {
                if (media.mediaType.lowercase() == "video") {
                    Glide.with(context).load(media.thumbnail).into(holder.binding.videoViewSplash)
                }

                holder.binding.title.text = title
                holder.binding.description.text = description
                holder.binding.videoViewSplash.setOnClickListener {
                    val intent = Intent(context, WatchActivity::class.java)
                    intent.putExtra("url", media.mediaUrlMobile)
                    context.startActivity(intent)
                }

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

}