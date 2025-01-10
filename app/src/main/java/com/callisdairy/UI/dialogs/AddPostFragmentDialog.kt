package com.callisdairy.UI.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.callisdairy.Interface.AddPostListener
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddPostFragmentDialog(val addPostListener : AddPostListener) : BottomSheetDialogFragment() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.add_post_dialog_design, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))
        val post = contentView.findViewById<LinearLayout>(R.id.post)
        val story = contentView.findViewById<LinearLayout>(R.id.story)
        val pet = contentView.findViewById<LinearLayout>(R.id.pet)
        val live = contentView.findViewById<LinearLayout>(R.id.live)

        post.setSafeOnClickListener {
            dismiss()
            addPostListener.post()
        }

        story.setSafeOnClickListener {
            dismiss()
            addPostListener.story()
        }

        pet.setSafeOnClickListener {
            dismiss()
            addPostListener.pet()
        }

        live.setSafeOnClickListener {
            dismiss()
            addPostListener.live()
        }


    }

}