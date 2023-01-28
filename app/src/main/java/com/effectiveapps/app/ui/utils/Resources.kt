package com.effectiveapps.app.ui.utils

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

fun Context.pixelSize(@DimenRes dimenRes: Int) = this
.resources
.getDimensionPixelSize(dimenRes)

fun Context.compatDrawable(@DrawableRes drawableRes: Int) = ResourcesCompat
    .getDrawable(resources, drawableRes, null)