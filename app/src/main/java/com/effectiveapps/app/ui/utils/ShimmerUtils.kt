package com.effectiveapps.app.ui.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder

private val DefaultShimmer = Shimmer.ColorHighlightBuilder()
    .setHighlightAlpha(1f)
    .setBaseAlpha(0.1f)
    .setBaseColor(Color.LTGRAY)
    .setWidthRatio(2f)
    .setDuration(600L)
    .build()

private val Bg = ColorDrawable(Color.GRAY)

fun AdapterDelegateViewBindingViewHolder<*, *>.shimmerGlideRequestBuilder(
    sfl: ShimmerFrameLayout,
    shimmer: Shimmer = DefaultShimmer,
    minShimmerDelay: Long = 600L
): RequestBuilder<Bitmap> {

    sfl.background = Bg
    sfl.setShimmer(shimmer)
    sfl.showShimmer(true)

    val startTime = AnimationUtils.currentAnimationTimeMillis()

    return glide.addListener(
        object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                sfl.hideShimmer()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                val elapsedTime = AnimationUtils.currentAnimationTimeMillis() - startTime
                if (elapsedTime < minShimmerDelay) {
                    postDelayed(minShimmerDelay - elapsedTime) {
                        sfl.hideShimmer()
                        sfl.background = null
                    }
                } else {
                    sfl.hideShimmer()
                    sfl.background = null
                }
                return false
            }

        }
    )
}