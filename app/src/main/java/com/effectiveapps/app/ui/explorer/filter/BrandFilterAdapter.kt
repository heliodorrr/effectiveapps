package com.effectiveapps.app.ui.explorer.filter

import androidx.lifecycle.LifecycleOwner
import com.effectiveapps.app.ui.utils.ListSubscriber
import com.skydoves.powerspinner.PowerSpinnerView
import com.effectiveapps.domain.model.Good

private const val BrandAll = "All"

private fun List<String>.withAll() = buildList<String> {
    add(BrandAll);
    this@withAll.forEach {
        add(it)
    }
}

internal fun attachBrandFilter(
    powerSpinner: PowerSpinnerView,
    subscriber: ListSubscriber<String>,
    attachFilter: ((Good)->Boolean)->Unit,
    lo: LifecycleOwner
) {

    powerSpinner.lifecycleOwner = lo

    powerSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
        attachFilter {
            if (newItem == BrandAll) true else it.brand == newItem
        }
    }

    subscriber {
        val items = it.withAll()
        powerSpinner.setItems(items)
        powerSpinner.selectedIndex
        powerSpinner.selectItemByIndex(0)

    }
}