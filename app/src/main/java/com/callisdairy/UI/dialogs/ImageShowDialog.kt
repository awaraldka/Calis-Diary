package com.callisdairy.UI.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.callisdairy.Adapter.ImageSliderAdaptorZoom
import com.callisdairy.R
import com.callisdairy.api.response.MediaUrls
import com.callisdairy.extension.setSafeOnClickListener
import me.relex.circleindicator.CircleIndicator3

class ImageShowDialog(var imageList: ArrayList<MediaUrls>) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.image_view_layout, container, false)
        val multi_image = view.findViewById<ViewPager2>(R.id.multi_image)
        val indicator3 = view.findViewById<CircleIndicator3>(R.id.indicator)
        val cross = view.findViewById<ImageView>(R.id.cross)
        val imageAdaptor = ImageSliderAdaptorZoom(imageList, requireContext())
        multi_image.adapter = imageAdaptor
        if (imageList.size > 1) {
            indicator3.setViewPager(multi_image)
        }

        cross.setSafeOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val layoutParams: WindowManager.LayoutParams = dialog!!.getWindow()!!.getAttributes()
        layoutParams.dimAmount = 0.7f
        dialog!!.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog!!.window?.setGravity(Gravity.CENTER)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window?.attributes = layoutParams
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog!!.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog

    }


}