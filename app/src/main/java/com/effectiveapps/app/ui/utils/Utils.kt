package com.effectiveapps.app.ui.utils

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder

val AdapterDelegateViewBindingViewHolder<*, *>.glide
    get() = Glide.with(context).asBitmap()




