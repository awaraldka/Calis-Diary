package com.callisdairy.UI.Activities.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.callisdairy.Interface.PaymentDoneListener
import com.callisdairy.R
import com.callisdairy.extension.androidExtension.initLoader
import com.callisdairy.extension.setSafeOnClickListener

class DialogBoxPayment(var paymentDoneListener : PaymentDoneListener) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.payment_popup, container, false)
        val yesBtn = view.findViewById<Button>(R.id.Continue_button_popup)
        val paymentLottie = view.findViewById<LottieAnimationView>(R.id.lottie_payment)
        paymentLottie.initLoader(true)
        yesBtn.setSafeOnClickListener {
            paymentDoneListener.paymentDone()
            dismiss()


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