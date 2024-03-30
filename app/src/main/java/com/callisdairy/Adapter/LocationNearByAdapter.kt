package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.LocationClick
import com.callisdairy.Interface.LocationClickNearBy
import com.callisdairy.api.OtherApi.NearByLocationResults
import com.callisdairy.databinding.LocationLayoutBinding

class LocationNearByAdapter(var context: Context, val predictions: ArrayList<NearByLocationResults>, val click: LocationClickNearBy):RecyclerView.Adapter<LocationNearByAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LocationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData =  predictions[position]
            holder.binding.txtName.text = listData.name

            holder.binding.cardClickView.setOnClickListener {
                click.getLocationNearBy(holder.binding.txtName.text.toString())
            }


        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return predictions.size
    }

    inner class ViewHolder(val binding: LocationLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}