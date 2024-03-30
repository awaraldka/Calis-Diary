package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.ModalClass.DoctorVetData
import com.callisdairy.R
import com.callisdairy.UI.Activities.CommonActivityForViewActivity
import com.callisdairy.api.response.Appointment
import com.callisdairy.api.response.VetOrDoctorDocs
import com.callisdairy.databinding.DoctorListBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AppointmentHistoryListAdapter(
    private val context: Context,
    private val docs: List<Appointment>
) : RecyclerView.Adapter<AppointmentHistoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DoctorListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            val data = docs.filterNotNull()

            data[position].docId.docIdForDoctor.apply {
                val binding = holder.binding
                binding.tvSpecialization.text = specialization
                binding.doctorName.text = firstName + middleName + lastName
                binding.tvExp.text = "Exp: $experience years"

               binding.Status.text = context.getString(R.string.appointment_status)
                binding.availableAt.text = "  ${docs[position].appointmentStatus.uppercase()}"

                if (docs[position].appointmentStatus.lowercase() == "approved" ||docs[position].appointmentStatus.lowercase() == "confirmed" ||docs[position].appointmentStatus.lowercase() == "completed" ){
                    holder.binding.availableAt.setTextColor(android.graphics.Color.parseColor("#00FF00"))
                }else{
                    holder.binding.availableAt.setTextColor(android.graphics.Color.parseColor("#FF0000"))

                }

                Glide.with(context).load(userProfileImage).placeholder(R.drawable.vet_doctor).into(binding.doctorImage)
                holder.binding.ServiceView.setOnClickListener {
                    val intent = Intent(context, CommonActivityForViewActivity::class.java)
                    intent.putExtra("id",docs[position].id)
                    intent.putExtra("flag", "View User AppointmentHistory Fragment")
                    context.startActivity(intent)
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return docs.size
    }

    inner class ViewHolder(val binding: DoctorListBinding) :
        RecyclerView.ViewHolder(binding.root)

}
