package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.PetNameClickListener
import com.callisdairy.R
import com.callisdairy.api.response.CountryList
import com.callisdairy.api.response.MyPetListDocs


class OpenDialogForPetName(
    var context: Context,
    var data: ArrayList<MyPetListDocs>,
    var click: PetNameClickListener
) :
    RecyclerView.Adapter<OpenDialogForPetName.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            data[position].apply {
                holder.Names.text = petName
                holder.Names.setOnClickListener {
//                   click.selectedData(petName,mediaUrls,gender,petName,userId.name,userId.address,userId.email,userId.mobileNumber,petBreedId!!,_id!!)
                   click.selectedData(petName,mediaUrls,gender,petName,userId.name,userId.address,userId.email,userId.mobileNumber,petBreedId!!._id,_id!!)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
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
    fun filterList(filteredList: ArrayList<MyPetListDocs>) {
        data = filteredList
        notifyDataSetChanged()

    }


}