package com.callisdairy.pdfreader

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.R
import com.callisdairy.databinding.ListItemPdfPageBinding
import com.rajat.pdfviewer.PdfRendererCore

internal class PdfViewAdapter(
    private val renderer: PdfRendererCore,
    private val pageSpacing: Rect,
    private val enableLoadingForPages: Boolean
) :
    RecyclerView.Adapter<PdfViewAdapter.PdfPageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        val binding = ListItemPdfPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PdfPageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return renderer.getPageCount()
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PdfPageViewHolder(private val binding: ListItemPdfPageBinding) : RecyclerView.ViewHolder(binding.root) {

        val pdfViewPageLoadingProgress:ProgressBar = binding.root.findViewById(R.id.pdf_view_page_loading_progress)

        fun bind(position: Int) {
            with(binding) {
                handleLoadingForPage(position)

                pageView.setImageBitmap(null)
                renderer.renderPage(position) { bitmap: Bitmap?, pageNo: Int ->
                    if (pageNo != position)
                        return@renderPage
                    bitmap?.let {
                        containerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            height =
                                (containerView.width.toFloat() / ((bitmap.width.toFloat() / bitmap.height.toFloat()))).toInt()
                            this.topMargin = pageSpacing.top
                            this.leftMargin = pageSpacing.left
                            this.rightMargin = pageSpacing.right
                            this.bottomMargin = pageSpacing.bottom
                        }
                        pageView.setImageBitmap(bitmap)
                        pageView.animation = AlphaAnimation(0F, 1F).apply {
                            interpolator = LinearInterpolator()
                            duration = 300
                        }

                        pdfViewPageLoadingProgress.hide()
                    }
                }
            }
        }

        private fun handleLoadingForPage(position: Int) {
            if (!enableLoadingForPages) {
                pdfViewPageLoadingProgress.hide()
                return
            }

            if (renderer.pageExistInCache(position)) {
                pdfViewPageLoadingProgress.hide()
            } else {
                pdfViewPageLoadingProgress.show()
            }
        }
    }


}
