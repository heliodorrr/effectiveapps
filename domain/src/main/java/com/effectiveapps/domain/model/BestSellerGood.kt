package com.effectiveapps.domain.model




data class BestSellerGood(
    override val id: Int,
    override val title: String,
    override val brand: String,
    val discountPrice: Int,
    val isFavorites: Boolean,
    val picture: String,
    val priceWithoutDiscount: Int,
): Good()


