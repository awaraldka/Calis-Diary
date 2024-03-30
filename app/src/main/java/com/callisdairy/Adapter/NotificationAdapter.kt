package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.NotificationClick
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.NotificationListResult
import com.callisdairy.databinding.NotificationModellayoutBinding

class NotificationAdapter(
    var context: Context,
    var data: ArrayList<NotificationListResult>,
    val click: NotificationClick


) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val notificationData = data[position]



            holder.binding.txtHeading.text = notificationData.description
            holder.binding.txtTime.text = DateFormat.covertTimeOtherFormat(notificationData.createdAt)
//            holder.binding.txtCount.text = Data.count.toString()


            holder.binding.deleteNotification.setOnClickListener {
                click.notificationDelete(notificationData._id,position)
            }


            Glide.with(context).load(notificationData.actionBy.petPic).into(holder.binding.petimage)
            Glide.with(context).load(notificationData.actionBy.profilePic).into(holder.binding.userImage)

            holder.binding.viewNotification.setOnClickListener {
                try {

                    val notificationType = notificationData.notificationType.ifEmpty {
                        ""
                    }
                    val postId = notificationData.postId._id.ifEmpty {
                        ""
                    }
                    val _id = notificationData._id.ifEmpty {
                        ""
                    }
                    val description = notificationData.description.ifEmpty {
                        ""
                    }


                    click.readNotifications(notificationType,postId,_id,description)

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }


            holder.binding.viewProfile.setOnClickListener {
                click.viewUsersProfile(notificationData.actionBy._id,notificationData.actionBy.userName)
            }





        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size

    }



    inner class ViewHolder(val binding:NotificationModellayoutBinding): RecyclerView.ViewHolder(binding.root)


}