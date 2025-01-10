package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.media3.common.Player
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.MorOptionsClick
import com.callisdairy.R
import com.callisdairy.UI.Activities.WatchActivity
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerStateCallback
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.loadVideo
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.databinding.HomeLauyoutBinding
import com.callisdairy.extension.setSafeOnClickListener

class ImageHomeAdapter(
    var context: Context,
    var data: ArrayList<MediaUrls>,
    val moreClick: MorOptionsClick
) :
    RecyclerView.Adapter<ImageHomeAdapter.MyViewHolder>() , PlayerStateCallback {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val binding = HomeLauyoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val listData = data[position]


            holder.binding.itemVideoExoplayer.loadVideo(listData.media.mediaUrlMobile,this,holder.binding.progressBar,holder.binding.thumbnail,position,false)

            if (listData.type.lowercase() == "video") {
                holder.binding.itemVideoExoplayer.keepScreenOn = true
                holder.binding.video.isVisible = true
                holder.binding.itemVideoExoplayer.isVisible = true
                holder.binding.progressBar.isVisible = true
                holder.binding.imgBanner.isVisible = false
                Glide.with(context).load(listData.media.thumbnail).placeholder(R.drawable.notfound_image).into(holder.binding.thumbnail)

            } else {

                holder.binding.imgBanner.isVisible = true
                holder.binding.video.isVisible = false
                holder.binding.progressBar.isVisible = false



                Glide.with(context).load(listData.media.mediaUrlMobile).placeholder(R.drawable.notfound_image).into(holder.binding.imgBanner)

            }


            holder.binding.imgBanner.setSafeOnClickListener {
                moreClick.viewImages(data)
            }

            holder.binding.video.setSafeOnClickListener {
                val intent =  Intent(context, WatchActivity::class.java)
                intent.putExtra("url",listData.media.mediaUrlMobile)
                context.startActivity(intent)
            }




        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    override fun onViewRecycled(holder: MyViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.adapterPosition
        PlayerViewAdapter.releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }


    override fun getItemCount(): Int {
        return data.size
    }


    inner class MyViewHolder(val binding: HomeLauyoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {

    }

    override fun onVideoBuffering(player: Player) {

    }

    override fun onStartedPlaying(player: Player) {

    }

    override fun onFinishedPlaying(player: Player) {

    }


}