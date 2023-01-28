package com.effectiveapps.app.ui.utils

fun decoratePrice(price: Int) = buildString {
    val priceString = price.toString()
    append('$')
    priceString.forEachIndexed { index, c ->
        append(c)
        val isAppendable = (priceString.length - (index+1) ) % 3 == 0
        if (isAppendable && index!=priceString.lastIndex) {
            append(',')
        }
    }
}