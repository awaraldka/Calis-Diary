package com.callisdairy.UI.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.callisdairy.R

class ReportPostDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        val view = inflater.inflate(R.layout.sell_pet_design, container, false)!!
        val cancelButton = view.findViewById<LinearLayout>(R.id.cancelButton)
        val confirmButton = view.findViewById<LinearLayout>(R.id.confirmButton)


//        cancelButton.setOnClickListener {
//            dismiss()
//
//        }
//
//        confirmButton.setOnClickListener {
//            dismiss()
//
//        }
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

