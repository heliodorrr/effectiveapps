package com.effectiveapps.app.ui.explorer.goodsscroll

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.effectiveapps.app.R
import com.effectiveapps.app.ui.utils.pixelSize
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter


internal fun attachGoodsScrollDecorations(
    rv: RecyclerView,
    adapter: ListDelegationAdapter<List<GoodsScrollItem>>
) {
    attachItemDecoration(rv, adapter)
    attachLayoutManager(rv, adapter)
}

private fun attachLayoutManager(
    rv: RecyclerView,
    adapter: ListDelegationAdapter<List<GoodsScrollItem>>
) {

    val context = rv.context

    val lm = GridLayoutManager(context,2,  GridLayoutManager.VERTICAL, false)
    lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val item = adapter.items?.get(position) ?: throw IllegalStateException("Data is not set")
            return when(item) {
                is GoodsScrollItem.BestSellerGoodItem -> 1
                is GoodsScrollItem.HotSalesViewPagerItem -> 2
                is GoodsScrollItem.HeaderItem -> 2

            }
        }
    }

    rv.layoutManager = lm

}

private fun attachItemDecoration(
    rv: RecyclerView,
    adapter: ListDelegationAdapter<List<GoodsScrollItem>>
) {
    val context = rv.context

    val hMargin = context.pixelSize(R.dimen.goods_scroll_best_seller_h_margin)
    val vMargin = context.pixelSize(R.dimen.goods_scroll_best_seller_v_margin)
    val verticalEdgeMargin = context.pixelSize(R.dimen.goods_scroll_best_seller_v_edge_margin)
    val explorerSidePaddings = context.pixelSize(R.dimen.fragment_explorer_side_paddings)
    val hotSalesVMargin = context.pixelSize(R.dimen.goods_scroll_hot_sales_v_margin)

    val itemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {

            val pos = parent.getChildAdapterPosition(view)

            if (pos==1) {
                outRect.top = hotSalesVMargin
                outRect.bottom = hotSalesVMargin
            } else if (pos==2  || pos==0) {
                outRect.left = explorerSidePaddings
                outRect.right = explorerSidePaddings
            }

            val bsPos = pos - 3
            if (bsPos < 0) return

            if (bsPos % 2 == 0) {
                outRect.right = hMargin
                outRect.left = explorerSidePaddings
            } else {
                outRect.left = hMargin
                outRect.right = explorerSidePaddings
            }

            val adapterLastIndex = adapter.items?.lastIndex
                ?: throw IllegalStateException("Should be items")

            outRect.bottom = vMargin

            if(bsPos == bsPos.coerceIn(0, 1)) {
                outRect.top = verticalEdgeMargin
            }
            if(pos == pos.coerceIn(adapterLastIndex-1, adapterLastIndex)) {
                outRect.bottom = verticalEdgeMargin
            }
        }
    }

    rv.addItemDecoration(itemDecoration)
}