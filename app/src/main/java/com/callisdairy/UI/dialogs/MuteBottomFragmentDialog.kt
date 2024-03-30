package com.callisdairy.UI.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import com.callisdairy.R

import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class MuteBottomFragmentDialog(context: Context) : BottomSheetDialogFragment() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.mute_dialog_design, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))

    }

}