package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.DeleteMyEvent
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.ListEventDocs
import com.callisdairy.databinding.EventPostBinding

class MyEventPostAdapter(var context: Context, val dataList: ArrayList<ListEventDocs>, val clickDelete: DeleteMyEvent):
    RecyclerView.Adapter<MyEventPostAdapter.ViewHolder>() {

    lateinit var imageAdaptor: ImageSliderAdaptor


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = dataList[position]


            holder.binding.deleteIcon.isVisible = true
            holder.binding.editPost.isVisible = true


            holder.binding.name.text = listData.userId.userName.ifEmpty {
                listData.userId.name
            }
            holder.binding.time.text  = DateFormat.covertTimeOtherFormat(listData.createdAt)


            holder.binding.eventName.text =  listData.eventName
            holder.binding.timeEvent.text = DateFormat.eventDateFormat(listData.date)

            holder.binding.address.text = "${listData.address} ${listData.city} ${listData.state} ${listData.country} ${listData.userId.zipCode}"


            Glide.with(context).load(listData.userId.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petPicture)


            if (listData.description.length > 120){
                holder.binding.txtDescription.text = Html.fromHtml("${listData.description.substring(0, 110)}...${context.getText(R.string.read_more)}")
                holder.binding.textAllDescription.setOnClickListener {
                    holder.binding.textAllDescription.isVisible = false
                    holder.binding.txtDescription.isVisible = true
                }

                holder.binding.txtDescription.setOnClickListener {
                    holder.binding.textAllDescription.isVisible = true
                    holder.binding.txtDescription.isVisible = false
                }

            }else{
                holder.binding.txtDescription.text = listData.description

            }

            holder.binding.textAllDescription.text = "${listData.description}...Read Less"





            holder.binding.indicator.isVisible = listData.image.size >1


            imageAdaptor = ImageSliderAdaptor(context,listData.image)
            holder.binding.storeViewpager.adapter = imageAdaptor
            holder.binding.indicator.setViewPager(holder.binding.storeViewpager)


            holder.binding.deleteIcon.setOnClickListener {
                clickDelete.delete(listData._id,position)
            }

            holder.binding.editPost.setOnClickListener {
                clickDelete.editEvent(listData._id,position)
            }

            holder.binding.viewAddress.setOnClickListener {
                val latitude = listData.lat // Destination latitude
                val longitude = listData.long // Destination longitude
                val label = "${listData.address} ${listData.city} ${listData.state} ${listData.country} ${listData.userId.zipCode}" // Destination label

                val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps") // Use Google Maps app
                context.startActivity(intent)
            }


            holder.binding.shareButton.setOnClickListener {
                val image = listData.image[0].ifEmpty {
                    ""
                }
                clickDelete.share(image)
            }

        } catch (_: Exception) {

        }
    }




    override fun getItemCount(): Int {
        return dataList.size
    }


    inner class ViewHolder(val binding: EventPostBinding) :
        RecyclerView.ViewHolder(binding.root)


}