package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.api.response.TagPeopleDocs

class TagPeopleAdapter(
    var context: Context,
    var data: List<TagPeopleDocs>,
) : RecyclerView.Adapter<TagPeopleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.tag_people, parent, false)


        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val tagData = data[position]

            holder.tagPeopleCK.isChecked = tagData.isSelected

            holder.Heading.text = tagData.name





            Glide.with(context).load(tagData.profilePic).placeholder(R.drawable.placeholder).into(holder.userPic)
            Glide.with(context).load(tagData.petPic).placeholder(R.drawable.placeholder_pet).into(holder.petPic)


            holder.tagPeopleCK.setOnClickListener {
                tagData.isSelected = !tagData.isSelected
                notifyDataSetChanged()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(filteredList: List<TagPeopleDocs>) {
        data = filteredList
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var userPic: ImageView = view.findViewById(R.id.img2)
        var petPic: ImageView = view.findViewById(R.id.img1)
        var cardClickView = view.findViewById<LinearLayout>(R.id.cardClickView)
        var Heading = view.findViewById<TextView>(R.id.txtName)
        var Count = view.findViewById<ImageView>(R.id.txtCount)
        var Cancle = view.findViewById<TextView>(R.id.txtCancle)
        var llCount = view.findViewById<LinearLayout>(R.id.llCount)
        var tagPeopleCK = view.findViewById<CheckBox>(R.id.tagPeopleCK)


    }
}