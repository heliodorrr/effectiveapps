package com.effectiveapps.app.ui.cart

import androidx.recyclerview.widget.RecyclerView
import com.effectiveapps.app.databinding.CartViewholderBinding
import com.effectiveapps.app.ui.utils.decoratePrice
import com.effectiveapps.app.ui.utils.shimmerGlideRequestBuilder
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding


internal fun attachCartItems(
    rv: RecyclerView,
    notifyAmountIncreased: (Int)->Unit,
    notifyAmountDecreased: (Int)->Unit,
    notifyItemDeleted: (Int)->Unit,
    registerEventHandler: ((CartItemEvent)->Unit)->Unit
) {

    val adapter = ListDelegationAdapter(
        cartItemDelegate(
            notifyAmountIncreased = notifyAmountIncreased,
            notifyAmountDecreased = notifyAmountDecreased,
            notifyItemDeleted = notifyItemDeleted
        )
    )

    registerEventHandler {
        adapter.items = it.data
        when(it) {
            is CartItemEvent.Source -> adapter.notifyDataSetChanged()
            is CartItemEvent.Deleted -> adapter.notifyItemRemoved(it.index)
            is CartItemEvent.Changed -> adapter.notifyItemChanged(it.index)
            is CartItemEvent.Inserted -> adapter.notifyItemInserted(it.index)
        }
    }

    rv.adapter = adapter
}



fun cartItemDelegate(
    notifyAmountIncreased: (Int)->Unit,
    notifyAmountDecreased: (Int)->Unit,
    notifyItemDeleted: (Int)->Unit
) = adapterDelegateViewBinding<CartItem, CartItem, CartViewholderBinding>(
    viewBinding = { i, c -> CartViewholderBinding.inflate(i, c, false) }
) {
    binding.cartItemDelete.setOnClickListener {
        notifyItemDeleted(adapterPosition)
    }

    binding.cartItemPlusButton.setOnClickListener { notifyAmountIncreased(adapterPosition) }
    binding.cardItemMinusButton.setOnClickListener { notifyAmountDecreased(adapterPosition) }

    bind {
        val data = item
        shimmerGlideRequestBuilder(binding.cartImageShimmer, minShimmerDelay = 0)
            .load(data.image)
            .into(binding.cartImage)

        binding.cartItemPrice.text = decoratePrice(data.cost)
        binding.cartItemAmount.text = data.amount.toString()
        binding.cartItemTitle.text = data.title

    }
}
