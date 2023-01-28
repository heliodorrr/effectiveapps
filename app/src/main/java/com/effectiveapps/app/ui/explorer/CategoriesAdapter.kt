package com.effectiveapps.app.ui.explorer

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.effectiveapps.app.R
import com.effectiveapps.app.databinding.CategoryViewholderBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding


private fun getPlaceholderItems(context: Context) = listOf(
    CategoryItem.fromDrawableRes(context, R.drawable.category_phones, "Phones"),
    CategoryItem.fromDrawableRes(context, R.drawable.category_computer, "Computer"),
    CategoryItem.fromDrawableRes(context, R.drawable.category_health, "Health"),
    CategoryItem.fromDrawableRes(context, R.drawable.category_books, "Books"),
)


internal data class CategoryItem(
    val icon: Drawable,
    val name: String,
) {
    companion object {
        fun fromDrawableRes(
            context: Context,
            @DrawableRes res: Int,
            name: String
        ) = CategoryItem(
            ResourcesCompat.getDrawable(context.resources, res, null)!!,
            name
        )
    }
}


internal fun attachCategoriesAdapter(
    rv: RecyclerView,
    dataSubscriber: ((Int)->Unit)->Unit,
    notifier: (Int) -> Unit
) {

    var prev = 0
    val positionSupplier = { prev }

    val delegationAdapter = delegationAdapter(notifier, positionSupplier)
    val adapter = ListDelegationAdapter(delegationAdapter).apply {
        items = getPlaceholderItems(rv.context)
    }

    dataSubscriber { current ->
        val prevCache = prev
        prev = current
        if (prevCache!=current) {
            adapter.notifyItemChanged(prevCache)
            adapter.notifyItemChanged(current)
        }

    }

    attachItemOffsets(rv)

    rv.adapter = adapter

}

private fun delegationAdapter(
    notifier: (Int)->Unit,
    positionSupplier: ()->Int
) = adapterDelegateViewBinding<CategoryItem, CategoryItem, CategoryViewholderBinding>(
    viewBinding = { i, vg -> CategoryViewholderBinding.inflate(i, vg, false) }
) {
    binding.root.setOnClickListener {
        notifier(adapterPosition)
    }

    val chosenIconTintColor = context.getColor(R.color.category_icon_is_chosen)
    val chosenElevation = context
        .resources
        .getDimensionPixelSize(R.dimen.category_elevation_is_chosen).toFloat()
    val chosenCardColor = context.getColor(R.color.category_card_is_chosen)

    val notChosenIconColor = context.getColor(R.color.category_icon_not_chosen)
    val notChosenElevation = context
        .resources
        .getDimensionPixelSize(R.dimen.category_elevation_not_chosen).toFloat()
    val notChosenCardColor = context.getColor(R.color.category_card_not_chosen)
    val notChosenTextColor = context.getColor(R.color.dark_blue)

    bind {
        binding.categoryIcon.setImageDrawable(item.icon)
        val holderIsChosen = adapterPosition == positionSupplier()

        if (holderIsChosen) {
            item.icon.setTint(chosenIconTintColor)
            binding.categoryCard.elevation = chosenElevation
            binding.categoryCard.setCardBackgroundColor(chosenCardColor)
            binding.categoryName.setTextColor(chosenCardColor)
        } else {
            item.icon.setTint(notChosenIconColor)
            binding.categoryCard.elevation = notChosenElevation
            binding.categoryCard.setCardBackgroundColor(notChosenCardColor)
            binding.categoryName.setTextColor(notChosenTextColor)
        }

        binding.categoryName.text = item.name

    }
}

private fun attachItemOffsets(rv: RecyclerView) {
    rv.addItemDecoration(
        object : RecyclerView.ItemDecoration() {
            val offset = rv
                .context
                .resources
                .getDimensionPixelOffset(R.dimen.category_item_h_offset)

            val shadowOffset = rv
                .context
                .resources
                .getDimensionPixelOffset(R.dimen.category_item_shadow_offset)

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val adapterPosition = parent.getChildAdapterPosition(view)
                outRect.top = shadowOffset
                if (adapterPosition!=0) {
                    outRect.left = offset
                } else {
                    outRect.left = shadowOffset
                }
            }
        }
    )
}