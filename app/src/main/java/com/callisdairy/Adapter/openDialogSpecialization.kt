package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.SpecializationClick
import com.callisdairy.ModalClass.Specialization
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener


class openDialogSpecialization(
    var context: Context,
    var data: ArrayList<Specialization>,
    var click: SpecializationClick
) :
    RecyclerView.Adapter<openDialogSpecialization.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].apply {
            holder.Names.text = name


            holder.Names.setSafeOnClickListener {
                click.clickValue(name)
            }
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }

     class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var Names: TextView



        init {
            Names = itemView.findViewById(R.id.content_textView)

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Specialization>) {
        data = filteredList
        notifyDataSetChanged()

    }


}