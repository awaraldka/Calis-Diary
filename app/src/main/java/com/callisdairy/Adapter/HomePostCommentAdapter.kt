package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.ModalClass.descriptionImage
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommentsActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.TopComments
import com.callisdairy.databinding.PostCommentModellayoutBinding

class HomePostCommentAdapter(
    var context: Context, var topComments: ArrayList<TopComments>) :
    RecyclerView.Adapter<HomePostCommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PostCommentModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = topComments[position]


            holder.binding.commentUserName.text = listData.userId.userName
            Glide.with(context).load(listData.userId.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userImage)
            holder.binding.timeOfComment.text= DateFormat.covertTimeOtherFormat(listData.createdAt)


            if(listData.isLiked){
                holder.binding.LikedComment.isVisible = true
                holder.binding.unLikedComment.isVisible = false
            }else{
                holder.binding.LikedComment.isVisible = false
                holder.binding.unLikedComment.isVisible = true
            }



            if (listData.comment.length > 30){
                holder.binding.Comment.text = "${listData.comment.substring(0,35)}..."
            }else{
                holder.binding.Comment.text = listData.comment
            }


            holder.binding.CommentClick.setOnClickListener {
                val intent = Intent(context, CommentsActivity::class.java)
                context.startActivity(intent)
            }




        } catch (_: Exception) {

        }
    }

    override fun getItemCount(): Int {
        return topComments.size

    }


    inner class ViewHolder(val binding: PostCommentModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}