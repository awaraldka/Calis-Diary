package com.callisdairy.UI.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import com.callisdairy.Interface.MoreOptions
import com.callisdairy.R
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.extension.setSafeOnClickListener

import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class MoreOnPostDialog(
    val moreOptionsClick: MoreOptions,
    val _id: String,
    val position: Int,
    val userId:String ,
    val imageUrl:String
) : BottomSheetDialogFragment() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.more_options, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))
        val sharePost = contentView.findViewById<LinearLayout>(R.id.sharePost)
        val hidePost = contentView.findViewById<LinearLayout>(R.id.hidePost)
        val deletePost = contentView.findViewById<LinearLayout>(R.id.DeletePost)
        val ReportPost = contentView.findViewById<LinearLayout>(R.id.ReportPost)
        val crossMore = contentView.findViewById<LinearLayout>(R.id.crossMore)


        val user_Id = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.userId).toString()


        if (user_Id == userId){
            hidePost.isVisible = false
            ReportPost.isVisible = false
            deletePost.isVisible = true
        }


        sharePost.setSafeOnClickListener {
            moreOptionsClick.share(imageUrl)
            dismiss()
        }


        deletePost.setSafeOnClickListener {
            moreOptionsClick.deletePost(_id,position)
            dismiss()
        }

        hidePost.setSafeOnClickListener {
            moreOptionsClick.hidePost(_id,position)
            dismiss()
        }

        ReportPost.setSafeOnClickListener {
            moreOptionsClick.report(_id,position)
            dismiss()
        }

        crossMore.setSafeOnClickListener {

            dismiss()
        }




    }

}