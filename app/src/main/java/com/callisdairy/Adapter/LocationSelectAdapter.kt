package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.LocationClick
import com.callisdairy.databinding.LocationLayoutBinding
import com.kanabix.api.LocationPrediction

class LocationSelectAdapter(var context: Context,val predictions: ArrayList<LocationPrediction>,val click: LocationClick):RecyclerView.Adapter<LocationSelectAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LocationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData =  predictions[position]
            holder.binding.txtName.text = listData.description

            holder.binding.cardClickView.setOnClickListener {
                click.getLocation(holder.binding.txtName.text.toString())
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