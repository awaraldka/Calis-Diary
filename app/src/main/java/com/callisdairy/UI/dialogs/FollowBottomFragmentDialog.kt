package com.callisdairy.UI.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.callisdairy.Interface.FollowDialogListener
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView


class FollowBottomFragmentDialog(
    val followDialogListener: FollowDialogListener,
    val userName: String,
    val petPic: String,
    val profilePic: String
) : BottomSheetDialogFragment() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView: View = View.inflate(context, R.layout.follow_dialog_design, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(resources.getColor(R.color.transparent))
        val restrictClick = contentView.findViewById<LinearLayout>(R.id.restrict)
        val followClick = contentView.findViewById<LinearLayout>(R.id.follow)
        val muteClick = contentView.findViewById<LinearLayout>(R.id.mute)
        val userNameText = contentView.findViewById<TextView>(R.id.userName)
        val userImage = contentView.findViewById<CircleImageView>(R.id.userImage)
        val petImage = contentView.findViewById<CircleImageView>(R.id.petImage)

        Glide.with(requireContext()).load(profilePic).placeholder(R.drawable.placeholder).into(userImage)
        Glide.with(requireContext()).load(petPic).placeholder(R.drawable.placeholder_pet).into(petImage)
        userNameText.text = userName


        restrictClick.setSafeOnClickListener {
            dismiss()
            followDialogListener.restrictListener()
        }

        followClick.setSafeOnClickListener {
            dismiss()
            followDialogListener.followListener()
        }

        muteClick.setSafeOnClickListener {
            dismiss()
            followDialogListener.muteListener()
        }
    }

}