package com.callisdairy.Utils

import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager


class DialogUtils {

    private val dialogList: MutableList<Dialog> = ArrayList()

    fun createDialog(context: Context?, dialogLayout: View?, animationType: Int): Dialog? {
        val dialog = Dialog(context!!, R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)
        dialog.setContentView(dialogLayout!!)
        val layoutParams: WindowManager.LayoutParams = dialog.window!!.attributes
        layoutParams.dimAmount = 0.7f
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setGravity(Gravity.CENTER)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (animationType == 0) {
            dialog.window?.attributes?.windowAnimations = R.style.Animation_Dialog
        } else {
//            dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.AnimationBottomPopUp
        }
        dialog.show()
        dialogList.add(dialog)
        return dialog
    }

}