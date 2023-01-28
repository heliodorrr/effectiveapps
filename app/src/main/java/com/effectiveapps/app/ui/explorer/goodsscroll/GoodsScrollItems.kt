package com.effectiveapps.app.ui.explorer.goodsscroll

import com.effectiveapps.domain.model.BestSellerGood
import com.effectiveapps.domain.model.HotSalesGood

sealed class GoodsScrollItem {
    data class HeaderItem(val headerName: String): GoodsScrollItem()
    data class BestSellerGoodItem(val data: BestSellerGood): GoodsScrollItem()
    data class HotSalesViewPagerItem(val data: List<HotSalesGood>): GoodsScrollItem()
}



