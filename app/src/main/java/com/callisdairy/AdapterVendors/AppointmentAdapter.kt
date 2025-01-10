package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.api.response.Appointment
import com.callisdairy.databinding.AppointmentListBinding
import com.callisdairy.extension.setSafeOnClickListener
import java.text.SimpleDateFormat
import java.util.Locale

class AppointmentAdapter(val context: Context, var mArrayUri: List<Appointment>,val from:String):RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AppointmentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            val inputFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            mArrayUri[position].apply {
                Glide.with(context).load(userId.petPic).placeholder(R.drawable.placeholder_pet).into(binding.petImage)
                Glide.with(context).load(userId.profilePic).placeholder(R.drawable.placeholder).into(binding.userProfilePicPublic)
                binding.Name.text =  userId.name
                binding.petName.text =  petName
                binding.petType.text =  userId.petType
                val time = inputFormat.parse(slot)
                val time12Hour = time?.let {
                    outputFormat.format(it)
                }
                binding.appointmentDate.text = "${DateFormat.formatDate(appointmentDate)} $time12Hour"
                binding.status.text =  appointmentStatus.uppercase()

                if (appointmentStatus.lowercase() == "approved" ||appointmentStatus.lowercase() == "confirmed" ||appointmentStatus.lowercase() == "completed" ){
                    holder.binding.status.setTextColor(android.graphics.Color.parseColor("#00FF00"))
                }else{
                    holder.binding.status.setTextColor(android.graphics.Color.parseColor("#FF0000"))

                }


                binding.CardClick.setSafeOnClickListener {
                    val intent = Intent(context, CommonContainerActivity::class.java)
                    intent.putExtra("id",id)
                    intent.putExtra("flag", "Appointment View")
                    intent.putExtra("from", from)
                    context.startActivity(intent)
                }


            }




        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mArrayUri.size
    }

    inner class ViewHolder(val binding: AppointmentListBinding) : RecyclerView.ViewHolder(binding.root)
}