package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.R
import com.callisdairy.api.response.PetCategoryListDocs


class openDialogCatgeory(
    var context: Context,
    var data: ArrayList<PetCategoryListDocs>,
    var flag: String,
    var click: PopupItemClickListener
) :
    RecyclerView.Adapter<openDialogCatgeory.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]


        when (flag) {
            "Category" -> {
                holder.Names.text = Data.categoryName
                holder.Names.setOnClickListener {
                    Data.categoryName.let { it1 -> click.getData(it1,flag,Data._id) }
                }

            }  else -> {
            holder.Names.text = Data.subCategoryName
            holder.Names.setOnClickListener {
                Data.subCategoryName.let { it1 -> click.getData(it1,flag,Data._id) }
            }
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
    fun filterList(filteredList: ArrayList<PetCategoryListDocs>) {
        data = filteredList
        notifyDataSetChanged()

    }


}