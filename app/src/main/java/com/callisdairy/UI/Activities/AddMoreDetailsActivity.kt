package com.callisdairy.UI.Activities

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.databinding.ActivityAddMoreDetailsBinding
import com.callisdairy.databinding.ActivityAddPetBinding
import com.callisdairy.extension.setSafeOnClickListener
import java.util.*

class AddMoreDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMoreDetailsBinding
    val c = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }

        binding.previousButton.setSafeOnClickListener {
            finishAfterTransition()
        }

        binding.addButton.setSafeOnClickListener {
            finishAfterTransition()
        }





        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        c.set(year, month + 1, date)

        binding.llVaccinationCalender.setSafeOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                DatePickerDialog.OnDateSetListener
                { view, year, monthOfYear, dayOfMonth ->
                    var dateFormat = DateFormat.dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")
                    binding.txtVaccination.text = dateFormat

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }

        binding.llAppointment.setSafeOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme, { view, year, monthOfYear, dayOfMonth ->
                    var dateFormat = DateFormat.dateFormatPicker("$dayOfMonth-${monthOfYear + 1}-$year")
                    binding.txtAppointment.text = dateFormat

                },
                year,
                month,
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }

    }

}