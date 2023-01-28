package com.effectiveapps.app.ui.cart

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.effectiveapps.app.ui.smartphonedetails.SmartphoneDetailsFragmentsState
import javax.inject.Inject

sealed class CartItemEvent(val data: List<CartItem>) {
    class Source(data: List<CartItem>): CartItemEvent(data)
    class Changed(data: List<CartItem>, val index: Int): CartItemEvent(data)
    class Inserted(data: List<CartItem>, val index: Int): CartItemEvent(data)
    class Deleted(data: List<CartItem>, val index: Int): CartItemEvent(data)
}

class CartViewModel @Inject constructor() : ViewModel() {

    private val cartItemsLiveData = MutableLiveData<CartItemEvent>(CartItemEvent.Source(emptyList()))

    fun putItem(item: SmartphoneDetailsFragmentsState) {
        val items = cartItemsLiveData.value!!.data
        val newItem = CartItem(
            title = item.title,
            price = item.price,
            image = item.images[0],
            amount = 1
        )
        val contains = items.indexOf(newItem)
        if (contains < 0) {
            val newItems = buildList {
                addAll(items)
                add(newItem)
            }
            cartItemsLiveData.value = CartItemEvent.Inserted(newItems, newItems.size)
        } else { notifyItemChanged(newItem.amount, contains) }
    }

    private fun notifyItemChanged(delta: Int, index: Int) {
        val currentItems = cartItemsLiveData.value!!.data
        val newItems = buildList {
            addAll(currentItems)
            val currentItem = currentItems[index]
            val currentAmount = currentItem.amount
            val newItem = currentItem
                .copy(amount = (currentAmount + delta).coerceIn(1, Int.MAX_VALUE))
            set(index, newItem)
        }
        cartItemsLiveData.value = CartItemEvent.Changed(newItems, index)
    }

    val notifyItemIncreased: (Int)->Unit = { notifyItemChanged(1, it) }
    val notifyItemDecreased: (Int)->Unit = { notifyItemChanged(-1, it) }

    val notifyItemDeleted: (Int)->Unit = { deletedAt->
        val newList = cartItemsLiveData.value!!.data.filterIndexed { i, _ -> i!=deletedAt }
        cartItemsLiveData.value = CartItemEvent.Deleted(newList, deletedAt)
    }


    fun subscriberToCartEvents(
        lo: LifecycleOwner,
        eventHandler: (CartItemEvent)->Unit
    ) {
        cartItemsLiveData.observe(lo, eventHandler)
    }

}