package com.callisdairy.UI.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.callisdairy.Interface.VendorFilter
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener
import java.util.*

class FilterVendorDialog(val click : VendorFilter,val from : String) : DialogFragment() {

    lateinit var datePicker : DatePickerDialog
    val c = Calendar.getInstance()
    var yearset=0
    var monthset=0
    var day=0
    var type = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.vendor_filter, container, false)!!
        val cancelButton = view.findViewById<LinearLayout>(R.id.cancelButton)
        val confirmButton = view.findViewById<LinearLayout>(R.id.confirmButton)
        val startDate = view.findViewById<TextView>(R.id.startDate)
        val endDate = view.findViewById<TextView>(R.id.endDate)
        val selectEndDate = view.findViewById<RelativeLayout>(R.id.selectEndDate)
        val selectStartDate = view.findViewById<RelativeLayout>(R.id.selectStartDate)
        val llGender = view.findViewById<LinearLayout>(R.id.llGender)
        val filter = view.findViewById<Spinner>(R.id.filter)
        val typeView = view.findViewById<TextView>(R.id.type)
        val tvStartDate = view.findViewById<TextView>(R.id.tvStartDate)
        val tvEndDate = view.findViewById<TextView>(R.id.tvEndDate)
        val tvType = view.findViewById<TextView>(R.id.tvType)

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)


        if (from == "Pet"){
            typeView.isVisible = false
            llGender.isVisible = false
        }else{
            typeView.isVisible = true
            llGender.isVisible = true
        }


        selectStartDate.setSafeOnClickListener {

            datePicker = DatePickerDialog(
                requireContext(),
                R.style.DatePickerTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    c.set(year, monthOfYear, dayOfMonth)
                    startDate.text = ""
                    startDate.text = "$year-${monthOfYear + 1}-$dayOfMonth"

                    yearset = year
                    monthset = monthOfYear
                    day = dayOfMonth
                }, year, month, date
            )
            datePicker.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePicker.show()

        }
        c.set(yearset, monthset, day)




        selectEndDate.setSafeOnClickListener {

            datePicker = DatePickerDialog(
                requireContext(),
                R.style.DatePickerTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    endDate.text = "$year-${monthOfYear + 1}-$dayOfMonth"

                },
                year,
                month,
                date
            )
            datePicker.datePicker.minDate = c.timeInMillis


            datePicker.show()

        }


        cancelButton.setSafeOnClickListener {
            dismiss()

        }

        confirmButton.setSafeOnClickListener {
            tvStartDate.isVisible = false
            tvEndDate.isVisible = false
            tvType.isVisible = false
            selectStartDate.setBackgroundResource(R.drawable.white_border_background)
            selectEndDate.setBackgroundResource(R.drawable.white_border_background)
            if (filter.selectedItem.toString() != "Select product type"){
                type = filter.selectedItem.toString()
            }


            if (startDate.text.isEmpty() && type.isEmpty()){
                if(startDate.text.isEmpty()){
                    tvStartDate.visibility = View.VISIBLE
                    tvStartDate.text = requireContext().getString(R.string.date_from)
                    selectStartDate.setBackgroundResource(R.drawable.errordrawable)
                }else if(endDate.text.isEmpty()){
                    tvEndDate.visibility = View.VISIBLE
                    tvEndDate.text = requireContext().getString(R.string.date_end)
                    selectEndDate.setBackgroundResource(R.drawable.errordrawable)
                }else{

                    click.vendorFilter(startDate.text.toString(),endDate.text.toString(),type)
                    dismiss()
                }
            }else{
                click.vendorFilter(startDate.text.toString().ifEmpty { "" },endDate.text.toString().ifEmpty { "" },type)
                dismiss()
            }








        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
    }
}