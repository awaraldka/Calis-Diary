package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.ModalClass.CommentsModelClass
import com.callisdairy.R

class StoryAdapter (
    var context: Context
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.story_list, parent, false)


        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 3

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}