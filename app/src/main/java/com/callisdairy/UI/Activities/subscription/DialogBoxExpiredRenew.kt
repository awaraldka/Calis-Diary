package com.callisdairy.UI.Activities.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.callisdairy.Interface.RenewSubscription
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener

class DialogBoxExpiredRenew(val click:RenewSubscription, private val titleText:String, private val descriptionText:String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.subscription_expire_popup, container, false)
        val yesBtn = view.findViewById<TextView>(R.id.Continue_button_popup)
        val title = view.findViewById<TextView>(R.id.title)
        val description = view.findViewById<TextView>(R.id.description)



        title.text = if(titleText != ""){
            titleText
        }else{
            "Subscription Expired"
        }

        description.text = if(descriptionText != ""){
            descriptionText
        }else{
            "Your Subscription has expired.Please renew it now to enjoy all service."
        }


        yesBtn.setSafeOnClickListener {
            click.renewNow()
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