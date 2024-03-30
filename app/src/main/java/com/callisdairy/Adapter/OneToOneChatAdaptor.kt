package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.AudioListeners
import com.callisdairy.Interface.FinishAudioListener
import com.callisdairy.Interface.ViewImages
import com.callisdairy.R
import com.callisdairy.Socket.ChatHistoryResult
import com.callisdairy.UI.Activities.WatchActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.databinding.ChatListDesignBinding
import com.callisdairy.extension.setSafeOnClickListener

class OneToOneChatAdaptor(
    val context: Context,
    val chatData: ArrayList<ChatHistoryResult>,
    val audioListeners: AudioListeners,
    val click: ViewImages
) : RecyclerView.Adapter<OneToOneChatAdaptor.ViewHolder>(), FinishAudioListener {
    class ViewHolder(val binding: ChatListDesignBinding) :
        RecyclerView.ViewHolder(binding.root)


    var selectItem: Int = 0
    var flag = false
    var audioFlag = false


    var selectItemOther: Int = 0
    var flagOther = false
    var audioFlagOther = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ChatListDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userId =
            SavedPrefManager.getStringPreferences(context, SavedPrefManager.userId).toString()

        chatData[position].apply {


            if (userId != messages[0].receiverId) {
                holder.binding.otherUserLL.isVisible = false
                holder.binding.ownLayout.isVisible = true


                if (userId == senderId._id) {
                    Glide.with(context).load(senderId.petPic)
                        .placeholder(R.drawable.placeholder_pet).into(holder.binding.petPicOwn)
                    Glide.with(context).load(senderId.profilePic)
                        .placeholder(R.drawable.placeholder).into(holder.binding.userImageOwn)
                } else {
                    Glide.with(context).load(receiverId.petPic)
                        .placeholder(R.drawable.placeholder_pet).into(holder.binding.petPicOwn)
                    Glide.with(context).load(receiverId.profilePic)
                        .placeholder(R.drawable.placeholder).into(holder.binding.userImageOwn)
                }





                if (messages[0].mediaType.lowercase() == "text") {
                    holder.binding.OwnUserImageRL.isVisible = false
                    holder.binding.myTextMessage.isVisible = true
                    holder.binding.OwnUserAudio.isVisible = false

                    holder.binding.myTextMessage.text = messages[0].message
                }
                if (messages[0].mediaType.lowercase() == "image") {
                    holder.binding.OwnUserImageRL.isVisible = true
                    holder.binding.OwnUserImage.isVisible = true
                    holder.binding.OwnUserVideo.isVisible = false
                    holder.binding.myTextMessage.isVisible = false
                    holder.binding.OwnUserAudio.isVisible = false
                    Glide.with(context).load(messages[0].message).into(holder.binding.OwnUserImage)

                    holder.binding.OwnUserImage.setOnClickListener {
                        click.viewImage(messages[0].message)
                    }


                }



                if (messages[0].mediaType.lowercase() == "mp4") {
                    holder.binding.OwnUserImageRL.isVisible = true
                    holder.binding.OwnUserImage.isVisible = true
                    holder.binding.OwnUserVideo.isVisible = true
                    holder.binding.myTextMessage.isVisible = false
                    holder.binding.OwnUserAudio.isVisible = false
                    Glide.with(context).load(messages[0].attachment)
                        .into(holder.binding.OwnUserImage)
                }

                if (messages[0].mediaType.lowercase() == "mp3") {
                    holder.binding.OwnUserAudio.isVisible = true
                    holder.binding.myTextMessage.isVisible = false
                    holder.binding.OwnUserImageRL.isVisible = false
                    holder.binding.myTextMessage.isVisible = false
                    holder.binding.OwnUserVideo.isVisible = false

                    Glide.with(context).load(messages[0].message).into(holder.binding.OwnUserImage)
                }

                if (messages[0].mediaType.lowercase() == "attachment") {
                    holder.binding.OwnUserImage.isVisible = true
                    holder.binding.OwnUserImageRL.isVisible = true
                    holder.binding.myTextMessage.isVisible = true
                    holder.binding.OwnUserAudio.isVisible = false
                    holder.binding.OwnUserVideo.isVisible = false
                    holder.binding.OwnUserAudio.isVisible = false

                    holder.binding.OwnUserImage.setOnClickListener {
                        click.viewImage(messages[0].attachment)
                    }
                    Glide.with(context).load(messages[0].attachment).into(holder.binding.OwnUserImage)
                    holder.binding.myTextMessage.text = messages[0].message

                }

                holder.binding.ownerUserTime.text = DateFormat.getTimeOnly(messages[0].createdAt)

                holder.binding.OwnUserPlay.setOnClickListener {
                    audioListeners.playAudioClick(
                        messages[0].message,
                        holder.binding.OwnUserPlay,
                        holder.binding.OwnUserPause,
                        holder.binding.otherUserPlay,
                        holder.binding.otherUserPause,
                        "OWN",
                        position,
                        this@OneToOneChatAdaptor
                    )
                    flag = true
                    audioFlagOther = false
                    audioFlag = true
                    selectItem = position
                    notifyItemChanged(position)
                    notifyDataSetChanged()
                }

                holder.binding.OwnUserPause.setOnClickListener {
                    holder.binding.OwnUserPlay.isVisible = true
                    holder.binding.OwnUserPause.isVisible = false
                    audioListeners.pauseAudioClick()

                }

                holder.binding.OwnUserImageRL.setSafeOnClickListener {
                    if (messages[0].mediaType.lowercase() == "mp4") {
                        val intent = Intent(context, WatchActivity::class.java)
                        intent.putExtra("url", messages[0].message)
                        context.startActivity(intent)
                    }
                }


                if (audioFlag) {
                    if (flag) {
                        if (selectItem == position) {
                            holder.binding.OwnUserPause.isVisible = true
                            holder.binding.OwnUserPlay.isVisible = false
                        } else {
                            holder.binding.OwnUserPause.isVisible = false
                            holder.binding.OwnUserPlay.isVisible = true
                        }

                    }
                } else {
                    holder.binding.OwnUserPlay.isVisible = true
                    holder.binding.OwnUserPause.isVisible = false
                }

            } else {
                holder.binding.otherUserLL.isVisible = true
                holder.binding.ownLayout.isVisible = false


                if (audioFlagOther) {
                    if (flagOther) {
                        if (selectItemOther == position) {
                            holder.binding.otherUserPause.isVisible = true
                            holder.binding.otherUserPlay.isVisible = false
                        } else {
                            holder.binding.otherUserPause.isVisible = false
                            holder.binding.otherUserPlay.isVisible = true
                        }

                    }
                } else {
                    holder.binding.otherUserPause.isVisible = false
                    holder.binding.otherUserPlay.isVisible = true
                }


                if (userId != senderId._id) {
                    Glide.with(context).load(senderId.petPic)
                        .placeholder(R.drawable.placeholder_pet).into(holder.binding.petPicOther)
                    Glide.with(context).load(senderId.profilePic)
                        .placeholder(R.drawable.placeholder).into(holder.binding.userPicOther)
                } else {
                    Glide.with(context).load(receiverId.petPic)
                        .placeholder(R.drawable.placeholder_pet).into(holder.binding.petPicOther)
                    Glide.with(context).load(receiverId.profilePic)
                        .placeholder(R.drawable.placeholder).into(holder.binding.userPicOther)
                }


                if (messages[0].mediaType.lowercase() == "text") {
                    holder.binding.otherUserImageRL.isVisible = false
                    holder.binding.otherUserAudio.isVisible = false
                    holder.binding.othersMessageText.isVisible = true
                    holder.binding.othersMessageText.text = messages[0].message

                }


                if (messages[0].mediaType.lowercase() == "image") {
                    holder.binding.otherUserImageRL.isVisible = true
                    holder.binding.otherUserImage.isVisible = true
                    holder.binding.othersMessageText.isVisible = false
                    holder.binding.otherUserVideo.isVisible = false
                    holder.binding.otherUserAudio.isVisible = false
                    holder.binding.otherUserImage.setOnClickListener {
                        click.viewImage(messages[0].message)
                    }
                    Glide.with(context).load(messages[0].message)
                        .into(holder.binding.otherUserImage)
                }

                if (messages[0].mediaType.lowercase() == "mp4") {
                    holder.binding.otherUserImageRL.isVisible = true
                    holder.binding.otherUserImage.isVisible = true
                    holder.binding.otherUserVideo.isVisible = true
                    holder.binding.othersMessageText.isVisible = false
                    holder.binding.otherUserAudio.isVisible = false

                    Glide.with(context).load(messages[0].attachment)
                        .into(holder.binding.otherUserImage)
                }

                if (messages[0].mediaType.lowercase() == "mp3") {
                    holder.binding.otherUserAudio.isVisible = true
                    holder.binding.othersMessageText.isVisible = false
                    holder.binding.otherUserImageRL.isVisible = false
                    Glide.with(context).load(messages[0].message)
                        .into(holder.binding.otherUserImage)
                }


                if (messages[0].mediaType.lowercase() == "attachment") {
                    holder.binding.otherUserImage.isVisible = true
                    holder.binding.otherUserImageRL.isVisible = true
                    holder.binding.othersMessageText.isVisible = true
                    holder.binding.otherUserVideo.isVisible = false
                    holder.binding.otherUserAudio.isVisible = false
                    Glide.with(context).load(messages[0].attachment)
                        .into(holder.binding.otherUserImage)
                    holder.binding.othersMessageText.text = messages[0].message
                    holder.binding.otherUserImage.setOnClickListener {
                        click.viewImage(messages[0].attachment)
                    }

                }

                holder.binding.otherUserTime.text = DateFormat.getTimeOnly(messages[0].createdAt)



                holder.binding.otherUserImageRL.setSafeOnClickListener {
                    if (messages[0].mediaType.lowercase() == "mp4") {
                        val intent = Intent(context, WatchActivity::class.java)
                        intent.putExtra("url", messages[0].message)
                        context.startActivity(intent)
                    }
                }
            }


        }
    }

    override fun getItemCount(): Int {
        return chatData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun finishAudio(s: String) {
        if (s == "OWN") {

            audioFlag = false

            notifyDataSetChanged()
        } else {

            audioFlagOther = false

            notifyDataSetChanged()
        }
    }
}