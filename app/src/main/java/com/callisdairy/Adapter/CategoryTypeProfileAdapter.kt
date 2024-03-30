package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.Interface.PopupItemClickListenerProfile
import com.callisdairy.ModalClass.DialogData
import com.callisdairy.R
import com.callisdairy.UI.Activities.Signup
import com.callisdairy.api.response.CountryList


class CategoryTypeProfileAdapter(
    var context: Context,
    var data: ArrayList<String>,
    var flag: String,
    var click: PopupItemClickListenerProfile
) :
    RecyclerView.Adapter<CategoryTypeProfileAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listData = data[position]


        if (flag == "Pet Category") {
            holder.Names.text = listData
            holder.Names.setOnClickListener {
                click.getDataForProfile(holder.Names.text.toString(),flag,"")
            }
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Names: TextView


        init {
            Names = itemView.findViewById(R.id.content_textView)

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun filterListProfile(filteredList: ArrayList<String>) {
        data = filteredList
        notifyDataSetChanged()

    }


}