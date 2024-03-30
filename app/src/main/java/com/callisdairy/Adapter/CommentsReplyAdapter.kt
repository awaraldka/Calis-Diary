package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.HomeComments
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.RepliesListDocs
import com.callisdairy.extension.setSafeOnClickListener

class CommentsReplyAdapter(
    var context: Context,
    var data: ArrayList<RepliesListDocs>,
    var click:HomeComments) : RecyclerView.Adapter<CommentsReplyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.comments_reply_modellayout, parent, false)


        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listData = data[position]


        Glide.with(context).load(listData.userId.petPic).into(holder.petImage)
        Glide.with(context).load(listData.userId.profilePic).into(holder.userImage)

        holder.UserName.text = listData.userId.userName
        holder.Comment.text = listData.reply
        holder.Time.text = DateFormat.covertTimeOtherFormat(listData.createdAt)
        val userId =  SavedPrefManager.getStringPreferences(context,SavedPrefManager.userId).toString()
        holder.editCommentClick.isVisible = userId == listData.userId._id
        holder.deleteCommentClick.isVisible = userId == listData.userId._id

        holder.deleteCommentClick.setSafeOnClickListener{
                click.ownCommentRepliedDeleteClick(holder.UserName.text.toString(), data[position].reply, listData._id,position,"Replied")
        }

        holder.editCommentClick.setSafeOnClickListener{
                click.ownCommentRepliedEditClick(holder.UserName.text.toString(), data[position].reply, listData._id,position,"Replied")
        }

    }

    override fun getItemCount(): Int {
        return data.size

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var petImage = view.findViewById<ImageView>(R.id.img1)
        var userImage = view.findViewById<ImageView>(R.id.img2)
        var UserName = view.findViewById<TextView>(R.id.txtUserName)
        var Comment = view.findViewById<TextView>(R.id.txtComment)
        var Time = view.findViewById<TextView>(R.id.txtTime)
        var Reply = view.findViewById<TextView>(R.id.Reply)
        var commentCount = view.findViewById<TextView>(R.id.commentCount)
        var mainClick = view.findViewById<LinearLayout>(R.id.mainClick)
        var editCommentClick = view.findViewById<ImageView>(R.id.editCommentClick)
        var deleteCommentClick = view.findViewById<ImageView>(R.id.deleteCommentClick)

    }
}