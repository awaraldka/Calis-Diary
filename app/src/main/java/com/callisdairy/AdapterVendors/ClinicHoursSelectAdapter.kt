package com.callisdairy.AdapterVendors

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.ModalClass.ClinicHours
import com.callisdairy.R
import com.callisdairy.databinding.ClinicHoursBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ClinicHoursSelectAdapter(
    private val context: Context,
    private val data: List<ClinicHours>
) : RecyclerView.Adapter<ClinicHoursSelectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClinicHoursBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clinicHours = data[position]

        holder.bind(clinicHours)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: ClinicHoursBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clinicCK.setOnCheckedChangeListener { _, isChecked ->
                val clinicHours = data[adapterPosition]
                clinicHours.isSelected = isChecked
                if (!isChecked) {

                    clinicHours.openTime = ""
                    clinicHours.closeTime = ""
                }
                validateFields(clinicHours,binding)
            }

            binding.startTime.setOnClickListener {
                val clinicHours = data[adapterPosition]
                showTimePickerDialog(binding.startTime, clinicHours,binding)
            }

            binding.endTime.setOnClickListener {
                val clinicHours = data[adapterPosition]
                showTimePickerDialog(binding.endTime, clinicHours,binding)
            }
        }

        fun bind(clinicHours: ClinicHours) {
            binding.apply {
                txtName.text = clinicHours.day
                startTime.text = clinicHours.openTime
                endTime.text = clinicHours.closeTime

                clinicCK.isChecked = clinicHours.isSelected

                if (clinicHours.isSelected && (clinicHours.openTime.isEmpty() || clinicHours.closeTime.isEmpty()) || !clinicHours.isSelected && (clinicHours.openTime.isNotEmpty() || clinicHours.closeTime.isNotEmpty())) {
                    clinicCK.isChecked = false
                    clinicHours.isSelected = false
                    startTime.text = ""
                    clinicHours.openTime = ""
                    endTime.text = ""
                    clinicHours.closeTime = ""
                }

            }
        }
    }

    private fun validateFields(clinicHours: ClinicHours,binding: ClinicHoursBinding) {
        if (clinicHours.isSelected && clinicHours.openTime.isEmpty()) {
            binding.startTime.setBackgroundResource(R.drawable.errordrawable)
        } else {
            binding.startTime.setBackgroundResource(R.drawable.white_border_background)
        }

        if (clinicHours.isSelected && clinicHours.closeTime.isEmpty()) {
            binding.endTime.setBackgroundResource(R.drawable.errordrawable)
        } else {
            binding.endTime.setBackgroundResource(R.drawable.white_border_background)
        }
    }

    private fun showTimePickerDialog(
        textView: TextView,
        clinicHours: ClinicHours,
        binding: ClinicHoursBinding
    ) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val is24HourFormat = false

        val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val displayHour = if (selectedHour > 12) selectedHour - 12 else selectedHour
                val amPm = if (selectedHour >= 12) "PM" else "AM"
                val time = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, selectedMinute, amPm)
                textView.text = time

                if (textView.id == R.id.startTime) {
                    clinicHours.openTime = time
                } else if (textView.id == R.id.endTime) {
                    val openTime = parseTime(clinicHours.openTime)
                    val endTime = parseTime(time)

                    if (openTime != null && endTime != null && endTime.before(openTime)) {
                        textView.text = ""
                        Toast.makeText(context, "End time cannot be before start time", Toast.LENGTH_SHORT).show()
                        return@TimePickerDialog
                    }

                    clinicHours.closeTime = time
                }

                validateFields(clinicHours, binding)
            },
            hour,
            minute,
            is24HourFormat
        )

        timePickerDialog.show()
    }

    private fun parseTime(timeString: String): Date? {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return try {
            format.parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}
