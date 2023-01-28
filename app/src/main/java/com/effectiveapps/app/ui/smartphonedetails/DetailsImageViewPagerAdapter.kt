package com.effectiveapps.app.ui.smartphonedetails

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.effectiveapps.app.R
import com.effectiveapps.app.databinding.DetailsImageViewholderBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

import com.effectiveapps.app.ui.utils.pixelSize
import com.effectiveapps.app.ui.utils.shimmerGlideRequestBuilder

import kotlin.math.absoluteValue


fun attachDetailsImageAdapterToViewPager(
    viewPager2: ViewPager2,
): (List<String>)->Unit {


    val adapter =  ListDelegationAdapter(
        detailsImageAdapterDelegate()
    )

    viewPager2.run {
        this.adapter = adapter
        offscreenPageLimit = 1
        addItemDecoration(decoration(context))
        setPageTransformer(transformer(context))
    }

    return {
        if (adapter.items!=it) {
            adapter.items = it
            adapter.notifyDataSetChanged()
        }
    }

}

private fun detailsImageAdapterDelegate()
= adapterDelegateViewBinding<String, String, DetailsImageViewholderBinding>(
    viewBinding = { i, c -> DetailsImageViewholderBinding.inflate(i, c, false) }
) {
    bind {
        shimmerGlideRequestBuilder(binding.detailsImageShimmer)
            .load(item)
            .into(binding.detailsImage)
    }
}

private fun decoration(context: Context): RecyclerView.ItemDecoration {
    val offset = context.pixelSize(R.dimen.details_image_h_margin)

    return object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = offset
            outRect.right = offset
        }
    }

}
private fun transformer(context: Context): ViewPager2.PageTransformer {
    val margin = context.pixelSize(R.dimen.details_image_h_margin)
    val offset  = context.pixelSize(R.dimen.details_image_decoration_offset)

    val t = margin + offset

    return ViewPager2.PageTransformer { page, position ->

        val sf = 1 -  (.25f * position.absoluteValue)
        page.scaleY = sf
        page.scaleX = sf

        page.translationX = -t * position
    }
}