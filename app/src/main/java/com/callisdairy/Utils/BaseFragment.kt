package com.callisdairy.Utils

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.callisdairy.R

abstract class BaseFragment : Fragment() {

    private var loadingPopup: PopupWindow? = null
    private var mActivity: Activity? = null
    var TAG = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = javaClass.name
        mActivity = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(setLayout(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mActivity?.window?.decorView?.post {
//            initLoadingPopup()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    /**
     * TODO: contentview
     * @date 2020/7/27 19:53
     */
    protected abstract fun setLayout(): Int

    /**
     * TODO: 初始化控件
     * @date 2020/7/27 19:54
     */
    protected abstract fun initView()

    /**
     * TODO:填充数据
     * @date 2020/7/27 20:37
     */
    protected abstract fun initData()


    /**
     * 初始化加载dialog
     */
//    open fun initLoadingPopup() {
//        val loadingView = LayoutInflater.from(mActivity).inflate(R.layout.pop_loading, null)
//        loadingPopup = PopupWindow(
//            loadingView,
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//
//        loadingPopup?.let {
//            it.isFocusable = true
//            it.isClippingEnabled = false
//            it.setBackgroundDrawable(ColorDrawable())
//        }
//        val pb = loadingView.findViewById<ImageView>(R.id.pb)
//        activity?.let { Glide.with(it).asGif().centerCrop().load(R.drawable.refresh).into(pb) }
//    }

    /**
     * 显示加载框
     */
    open fun showLoadingPopup() {
        loadingPopup?.showAtLocation(activity?.window?.decorView, Gravity.CENTER, 0, 0)
    }

    /**
     * 隐藏加载框
     */
    open fun hideLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup?.dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        hideLoadingPopup()
    }

    open fun refreshData(loading: Boolean = true) {}
}