package com.effectiveapps.app.ui.cart

data class CartItem(
    val title: String,
    val price: Int,
    val image: String?,
    val amount: Int,
) {
    val cost get() = price * amount

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is CartItem) return false
        if (
            other.title == title &&
            other.price == price &&
            other.image == image
        ) return true
        return false
    }
    override fun hashCode() = title.hashCode()

    fun merge(other: CartItem): CartItem {
        return if (other==this) {
            CartItem(
                title,
                price,
                image,
                amount + other.amount
            )
        } else this
    }

}
