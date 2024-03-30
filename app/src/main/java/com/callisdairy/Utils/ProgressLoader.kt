package com.callisdairy.Utils

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.ProgressBar
import com.callisdairy.R

object Progresss {
    private var dialog: Dialog? = null

    fun start(context: Context?) {
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        dialog = Dialog(context!!)
        dialog?.setContentView(R.layout.loader)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.setCancelable(false)

        val progressBar = dialog?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = ProgressBar.VISIBLE

        dialog?.show()
    }

    fun stop() {
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        dialog = null
    }
}
