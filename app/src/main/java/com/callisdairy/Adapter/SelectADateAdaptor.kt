package com.callisdairy.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.AppointmentListener
import com.callisdairy.Interface.GetTime
import com.callisdairy.api.response.AppointmentDateResult
import com.callisdairy.databinding.SelectADateModalClassBinding


class SelectADateAdaptor(
    var context: Context,
    var data: List<AppointmentDateResult>,
    var Time: GetTime,
    var Appointmentlistener: AppointmentListener
) :
    RecyclerView.Adapter<SelectADateAdaptor.ViewHolder>(), GetTime {
 var flag :Boolean=false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectADateModalClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {

        val binding = holder.binding
        with(data[position]) {
            val dateStr = date
            binding.DaysTextView.text = "$day $date"
            binding.SelectADate2RecyclerWishlist.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            binding.SelectADate2RecyclerWishlist.layoutManager = GridLayoutManager(context,4)
            val colorAdaptor = SelectADateSubAdaptor(context,  allSlotTimes, date, this@SelectADateAdaptor,Appointmentlistener,position)
            binding.SelectADate2RecyclerWishlist.adapter = colorAdaptor
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: SelectADateModalClassBinding) :  RecyclerView.ViewHolder(binding.root)

    override fun getTime(time: String, appointment_date: String) {
        Time.getTime(time, appointment_date)
    }







}