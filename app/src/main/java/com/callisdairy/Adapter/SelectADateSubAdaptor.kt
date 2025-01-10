package com.callisdairy.Adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.AppointmentListener
import com.callisdairy.Interface.GetTime
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.AppointmentAllSlotTimes
import com.callisdairy.databinding.SelectADateSubModalClassBinding


class SelectADateSubAdaptor(
    var context: Context,
    var data: List<AppointmentAllSlotTimes>,
    var date: String,
    var timeclick: GetTime,
    var Appointmentlistener: AppointmentListener,
    var positioncount: Int
) :

    RecyclerView.Adapter<SelectADateSubAdaptor.ViewHolder>() {
    var  select:Int=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectADateSubModalClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            with(data[position]) {
                binding.TextView1.text = DateFormat.convert24To12(time)

                if(slotStatus) {
                    binding.TextView1.setBackgroundResource(R.drawable.client_textview_bg_gray)
                } else {
                    if (data[position].flag == true) {

                        data[position].flag1 = true
                        timeclick.getTime(time, date)
                        binding.TextView1.setBackgroundResource(R.drawable.main_button_background)
                        binding.TextView1.setTextColor(Color.parseColor("#FFFFFF"))


                    } else {
                        binding.TextView1.setBackgroundResource(R.drawable.border_background)
                        binding.TextView1.setTextColor(Color.parseColor("#000000"))
                    }
                }

                binding.click.setOnClickListener {
                    if(!data[position].slotStatus) {
                        select=position
                        data[position].flag = data[position].flag != true
                        Appointmentlistener.appointmentListenerSet(positioncount,position)
                    }
                }


            }
        }catch (e:Exception){
            e.printStackTrace()
        }







    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: SelectADateSubModalClassBinding) :  RecyclerView.ViewHolder(binding.root)

}

