package com.effectiveapps.data.model

import com.google.gson.annotations.SerializedName
import com.effectiveapps.domain.model.HotSalesGood

data class HotSalesGoodDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_buy")
    val isBuy: Boolean,
    @SerializedName("is_new")
    val isNew: Boolean,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("title")
    val title: String
) {
    private fun obtainBrand() = title.substringBefore(' ')

    fun toHotSalesGood() = HotSalesGood(
        id = id,
        isBuy = isBuy,
        isNew = isNew,
        picture = picture,
        subtitle = subtitle,
        title = title,
        brand =  obtainBrand()
    )
}
