package com.callisdairy.UI.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.callisdairy.Interface.FollowDialogListener
import com.callisdairy.Interface.RestrictDialogListener
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener

import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class RestrictBottomFragmentDialog(var restrictDialogListener : RestrictDialogListener) : BottomSheetDialogFragment() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.restrict_dialog_design, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))
        var back = contentView.findViewById<ImageView>(R.id.back)
        var restrictAccountButton = contentView.findViewById<LinearLayout>(R.id.restrict_account_button)

        back.setSafeOnClickListener {
            dismiss()
        }

        restrictAccountButton.setSafeOnClickListener {
            dismiss()
            restrictDialogListener.restrictAccountListener()
        }

    }

}