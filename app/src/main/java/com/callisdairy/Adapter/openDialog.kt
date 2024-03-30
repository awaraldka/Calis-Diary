package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.PopupItemClickListener
import com.callisdairy.ModalClass.DialogData
import com.callisdairy.R
import com.callisdairy.UI.Activities.Signup
import com.callisdairy.api.response.CountryList


class openDialog(
    var context: Context,
    var data: ArrayList<CountryList>,
    var flag: String,
    var click: PopupItemClickListener
) :
    RecyclerView.Adapter<openDialog.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]

        when (flag) {
            "State" -> {
                holder.Names.text = Data.name
                holder.Names.setOnClickListener {
                    Data.name.let { it1 -> click.getData(it1,flag,Data.isoCode) }
                }

            }
            "City" -> {
                holder.Names.text = Data.name
                holder.Names.setOnClickListener {
                    Data.name.let { it1 -> click.getData(it1,flag,Data.isoCode) }
                }

            }
            "Country" -> {
                holder.Names.text = Data.name
                holder.Names.setOnClickListener {
                    Data.name.let { it1 -> click.getData(it1,flag,Data.isoCode) }
                }

            }
            "Region" -> {
                holder.Names.text = Data.name
                holder.Names.setOnClickListener {
                    Data.name.let { it1 -> click.getData(it1,flag,"") }
                }

            }
            "Pet Type" -> {
                holder.Names.text = Data.petCategoryName
                holder.Names.setOnClickListener {
                    Data.petCategoryName.let { it1 -> click.getData(it1,flag,Data._id) }
                }

            }
            "Pet Breed" -> {
                holder.Names.text = Data.petBreedName
                holder.Names.setOnClickListener {
                    Data.petBreedName.let { it1 -> click.getData(it1,flag,Data._id) }
                }

            }
            else -> {

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
    fun filterList(filteredList: ArrayList<CountryList>) {
        data = filteredList
        notifyDataSetChanged()

    }


}