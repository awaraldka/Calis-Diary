package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.media3.common.Player
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.BannerSliderListener
import com.callisdairy.UI.Activities.WatchActivity
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerStateCallback
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter
import com.callisdairy.UI.Fragments.autoPlayVideo.PlayerViewAdapter.Companion.loadVideo
import com.callisdairy.api.response.BannerListDocs
import com.callisdairy.databinding.SliderImageBinding
import com.callisdairy.extension.setSafeOnClickListener


class MarketBannerAdapter(
    var context: Context,
    var data: ArrayList<BannerListDocs>,
    val bannerSliderListener: BannerSliderListener
) : RecyclerView.Adapter<MarketBannerAdapter.ViewHolder>(),PlayerStateCallback {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SliderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            data[position].apply {

                holder.binding.itemVideoExoplayer.loadVideo(bannerImage[0].mediaUrlMobile,this@MarketBannerAdapter,holder.binding.progressBar,holder.binding.thumbnail,position,false)
                holder.binding.itemVideoExoplayer.keepScreenOn = true
                if (bannerImage.getOrNull(0)?.type?.lowercase() == "video") {
                    holder.binding.itemVideoExoplayer.keepScreenOn = true
                    holder.binding.imgBanner.isVisible = false
                    holder.binding.itemVideoExoplayer.isVisible = true
//                    playVideo(bannerImage[0].mediaUrlMobile, holder)
//                    bannerSliderListener.slider("video", bannerImage.getOrNull(0)!!.mediaUrlMobile, bannerImage.getOrNull(0)!!.duration.toLong())

                    Glide.with(context).load(bannerImage.getOrNull(0)!!.thumbnail).into(holder.binding.thumbnail)
                } else {
                    holder.binding.imgBanner.isVisible = true
                    holder.binding.itemVideoExoplayer.isVisible = false
                    Glide.with(context).load(bannerImage.getOrNull(0)!!.mediaUrlMobile)
                        .into(holder.binding.imgBanner)
                    bannerSliderListener.slider("image","", 3)

                }


                holder.binding.video.setSafeOnClickListener {
                    val intent =  Intent(context, WatchActivity::class.java)
                    intent.putExtra("url",bannerImage.getOrNull(0)!!.mediaUrlMobile)
                    context.startActivity(intent)
                }

                holder.binding.imgBanner.setSafeOnClickListener{
                    try {
                        var url = ""
                        url = link.ifBlank {
                            "http://www.google.com"
                        }


                        val uri = Uri.parse(url) // missing 'http://' will cause crashed
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }

                }

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onViewRecycled(holder: ViewHolder) {
        val position = holder.adapterPosition
        PlayerViewAdapter.releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }



    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: SliderImageBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onVideoDurationRetrieved(duration: Long, player: Player) {

    }

    override fun onVideoBuffering(player: Player) {

    }

    override fun onStartedPlaying(player: Player) {

    }

    override fun onFinishedPlaying(player: Player) {

    }








}