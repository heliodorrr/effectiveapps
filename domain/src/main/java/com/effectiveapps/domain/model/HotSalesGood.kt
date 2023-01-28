package com.effectiveapps.domain.model


data class HotSalesGood(
    override val id: Int,
    override val title: String,
    override val brand: String,
    val subtitle: String,
    val isBuy: Boolean,
    val isNew: Boolean,
    val picture: String,
): Good()

