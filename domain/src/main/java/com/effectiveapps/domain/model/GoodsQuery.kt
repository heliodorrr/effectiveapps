package com.effectiveapps.domain.model

data class GoodsQuery(
    val hotSalesGoods: List<HotSalesGood>,
    val bestSellerGood: List<BestSellerGood>
)