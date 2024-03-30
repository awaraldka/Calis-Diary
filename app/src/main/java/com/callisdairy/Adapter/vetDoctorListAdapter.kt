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
import com.callisdairy.api.response.VetOrDoctorDocs
import com.callisdairy.databinding.DoctorListBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class vetDoctorListAdapter(
    private val context: Context,
    private val docs: List<VetOrDoctorDocs>
) : RecyclerView.Adapter<vetDoctorListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DoctorListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            docs[position].doctorVetProfile.apply {
                val binding = holder.binding
                Glide.with(context).load(userProfileImage).placeholder(R.drawable.vet_doctor).into(binding.doctorImage)
                binding.tvSpecialization.text = specialization.takeIf { it!!.isNotBlank() }?:"NA"
                binding.doctorName.text = "$firstName $middleName $lastName".takeIf { it.isNotBlank() }?:"NA"
                binding.tvExp.text = "Exp: $experience years".takeIf { it!!.isNotBlank() }?:"NA"

                val currentDay = LocalDateTime.now().dayOfWeek.toString().lowercase()
                var isAvailable = false

                clinicHours.find { clinicHour ->
                    val clinicDay = clinicHour.day.lowercase()
                    currentDay.contains(clinicDay) && clinicHour.openTime.isNotEmpty() && clinicHour.closeTime.isNotEmpty()
                }?.let { availableClinicHour ->
                    with(binding.availableAt) {
                        text = "${availableClinicHour.openTime} To ${availableClinicHour.closeTime}"
                    }
                    isAvailable = true
                }


                if (!isAvailable) {
                    binding.availableAt.text = "Not Available"
                }



                holder.binding.ServiceView.setOnClickListener {
                    val intent = Intent(context, CommonActivityForViewActivity::class.java)
                    intent.putExtra("id",userId)
                    intent.putExtra("flag", "viewDoctors")
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
