package com.effectiveapps.data.model

import com.google.gson.annotations.SerializedName
import com.effectiveapps.domain.model.BestSellerGood

data class BestSellerGoodDto(
    @SerializedName("discount_price")
    val discountPrice: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_favorites")
    val isFavorites: Boolean,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("price_without_discount")
    val priceWithoutDiscount: Int,
    @SerializedName("title")
    val title: String
) {

    private fun obtainBrand() = title.substringBefore(' ')

    fun toBestSellerGood() = BestSellerGood(
        id = id,
        discountPrice = discountPrice,
        isFavorites = isFavorites,
        picture = picture,
        priceWithoutDiscount = priceWithoutDiscount,
        title = title,
        brand = obtainBrand()
    )
}
