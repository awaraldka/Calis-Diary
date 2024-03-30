package com.callisdairy.UI.Activities.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.callisdairy.Interface.RenewSubscription
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener

class DialogBoxAlertRenew(val click:RenewSubscription, val text:String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.subscription_expire_popup, container, false)
        val llNoButton = view.findViewById<LinearLayout>(R.id.llNoButton)
        val llYesButton = view.findViewById<LinearLayout>(R.id.llYesButton)
        val description = view.findViewById<TextView>(R.id.description)


        description.text = "Your Subscription expiring in $text. Would you like to renew now?."

        llYesButton.setSafeOnClickListener {
            click.renewNow()
            dismiss()
        }
        llNoButton.setSafeOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
//        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
    }



}