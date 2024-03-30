package com.callisdairy.UI.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import android.widget.LinearLayout
import com.callisdairy.Interface.AddListenerVendor
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VendorAddPosts(val addPostListener : AddListenerVendor) : BottomSheetDialogFragment() {


    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.view_vendor, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))
        val addProduct = contentView.findViewById<LinearLayout>(R.id.addProduct)
        val addPet = contentView.findViewById<LinearLayout>(R.id.addPet)
        val addService = contentView.findViewById<LinearLayout>(R.id.addService)

        addProduct.setSafeOnClickListener {
            dismiss()
            addPostListener.product()
        }

        addPet.setSafeOnClickListener {
            dismiss()
            addPostListener.pet()
        }

        addService.setSafeOnClickListener {
            dismiss()
            addPostListener.service()
        }




    }

}