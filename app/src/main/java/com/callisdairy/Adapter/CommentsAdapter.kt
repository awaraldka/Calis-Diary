package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.HomeComments
import com.callisdairy.Interface.ViewReply
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.CommentListDocs
import com.callisdairy.api.response.RepliesListDocs
import com.callisdairy.databinding.CommentsModellayoutBinding
import com.callisdairy.extension.setSafeOnClickListener


class CommentsAdapter(
    var context: Context,
    var data: ArrayList<CommentListDocs>,
    var commentReplyData: ArrayList<RepliesListDocs>,
    var click: HomeComments,
    var viewComment: ViewReply

) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    lateinit var commentAdapter: CommentsReplyAdapter
    var selectItem:Int = 0
    var flag = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommentsModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listData = data[position]


        Glide.with(context).load(listData.userId.petImage).placeholder(R.drawable.placeholder_pet).into(holder.binding.petImage)
        Glide.with(context).load(listData.userId.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userImage)

        holder.binding.UserName.text = listData.userId.userName
        holder.binding.Comment.text = listData.comment
        holder.binding.commentCount.text = listData.repliesCount.toString()
        holder.binding.LikesCount.text = listData.likeCount.toString()
        holder.binding.Time.text = DateFormat.covertTimeOtherFormat(listData.createdAt)


        holder.binding.commentCount.isVisible = listData.repliesCount  > 0
        holder.binding.LikesCount.isVisible = listData.likeCount > 0

        if (listData.isLiked){
            holder.binding.likePost.isVisible = true
            holder.binding.UnlikePost.isVisible = false
        }else{
            holder.binding.UnlikePost.isVisible = true
            holder.binding.likePost.isVisible = false
        }



        if(flag){
            if (selectItem == position) {
                holder.binding.commentReplied.visibility =  View.VISIBLE
                holder.binding.viewReply.visibility =  View.GONE
            } else {
                holder.binding.commentReplied.visibility =  View.GONE
                holder.binding.viewReply.isVisible = listData.repliesCount > 0
            }
        }else{
            holder.binding.viewReply.isVisible = listData.repliesCount > 0
        }


        holder.binding.Reply.setOnClickListener {
            click.commentSection(holder.binding.UserName.text.toString(), listData._id)
        }

        val userId =  SavedPrefManager.getStringPreferences(context,SavedPrefManager.userId).toString()
        holder.binding.editCommentClick.isVisible = userId == listData.userId._id
        holder.binding.deleteCommentClick.isVisible = userId == listData.userId._id



        holder.binding.deleteCommentClick.setOnClickListener {
            click.ownCommentDeleteClick(holder.binding.UserName.text.toString(), data[position].comment, listData._id,position)

        }


        holder.binding.editCommentClick.setOnClickListener {
            click.ownCommentEditClick(holder.binding.UserName.text.toString(), data[position].comment, listData._id,position)

        }


        holder.binding.viewReply.setSafeOnClickListener{
            viewComment.showAllComments(listData._id, position)
            flag = true
            selectItem=position
        }

        holder.binding.llCount.setOnClickListener {
            viewComment.likeComment(listData._id,listData.isLiked, holder.binding.UnlikePost, holder.binding.likePost,position,listData._id)
        }







        holder.binding.commentReplied.layoutManager = LinearLayoutManager(context)
        commentAdapter = CommentsReplyAdapter(context, commentReplyData,click)
        holder.binding.commentReplied.adapter = commentAdapter


    }

    override fun getItemCount(): Int {
        return data.size

    }

    inner class ViewHolder(val binding: CommentsModellayoutBinding) : RecyclerView.ViewHolder(binding.root)






}