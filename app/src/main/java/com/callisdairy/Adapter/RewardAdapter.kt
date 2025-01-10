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
import com.callisdairy.ModalClass.TagPeopleModelClass
import com.callisdairy.R

class RewardAdapter (
    var context: Context,
    var data: ArrayList<TagPeopleModelClass>


) : RecyclerView.Adapter<RewardAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.tag_people_modellayout, parent, false)


        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var Data = data.get(position)


        Glide.with(context).load(Data.Image).into(holder.image1)
        Glide.with(context).load(Data.Image2).into(holder.image2)

        holder.Heading.text = Data.UserName





    }

    override fun getItemCount(): Int {
        return data.size

    }




    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var image1 = view.findViewById<ImageView>(R.id.img1)
        var image2 = view.findViewById<ImageView>(R.id.img2)
        var Heading = view.findViewById<TextView>(R.id.txtName)


    }
}