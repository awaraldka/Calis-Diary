package com.callisdairy.Utils.story

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

fun AppCompatImageView.loadImage(imageUrl: String?, imageLoadingListener: ImageLoadingListener) {
    try {
        Glide.with(this.context)
            .load(imageUrl)
            .apply(provideRequestOptions(this.context))
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(context, "onLoadFailed", Toast.LENGTH_SHORT).show()
                    imageLoadingListener.onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(context, "onResourceReady", Toast.LENGTH_SHORT).show()
                    if (resource != null) {
                        imageLoadingListener.onResourceReady(resource.toBitmap(), resource)
                    }
                    return false
                }
            })
            .into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun loadThumbnailImage(
    context: Context,
    imageUrl: String?,
    imageLoadingListener: ImageLoadingListener
) {
    Glide.with(context)
        .asBitmap()
        .apply(provideFixedSizeRequestOptions(context))
        .sizeMultiplier(0.5f)
        .load(imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageLoadingListener.onResourceReady(resource, null)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                imageLoadingListener.onLoadCleared()
            }
        })
}

private fun createPlaceholder(context: Context) = CircularProgressDrawable(context).apply {
    strokeWidth = 5f
    centerRadius = 40f
    setColorSchemeColors(Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.WHITE)
    start()
}

private fun provideFixedSizeRequestOptions(context: Context): RequestOptions {
    return RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(ContextCompat.getDrawable(context, android.R.drawable.stat_notify_error))
        .override(300, 300)
        .placeholder(createPlaceholder(context))
        .circleCrop()
}

fun provideRequestOptions(context: Context): RequestOptions {
    val circularProgressDrawable = createPlaceholder(context)

    return RequestOptions()
        .error(circularProgressDrawable)
        .placeholder(circularProgressDrawable)
        .fitCenter()
        .format(DecodeFormat.PREFER_ARGB_8888)
}

interface ImageLoadingListener {
    fun onLoadFailed() {}
    fun onResourceReady(bitmap: Bitmap, drawable: Drawable?)
    fun onLoadCleared() {}
}