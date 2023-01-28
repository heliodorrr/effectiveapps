package com.effectiveapps.app.ui.explorer.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.effectiveapps.app.databinding.PriceFilterVieholderBinding
import com.effectiveapps.app.ui.utils.decoratePrice
import com.effectiveapps.app.ui.utils.inRange
import com.effectiveapps.app.ui.utils.textToInt
import com.effectiveapps.app.ui.utils.unsafeEditableWatcher
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerInterface
import com.skydoves.powerspinner.PowerSpinnerView
import com.effectiveapps.domain.model.BestSellerGood
import com.effectiveapps.domain.model.Good



internal fun attachPriceFilter(
    spinnerView: PowerSpinnerView,
    attachFilter: ((Good)->Boolean)->Unit,
    minPrice: Int,
    maxPrice: Int,
    lo: LifecycleOwner
) {
    spinnerView.lifecycleOwner = lo
    val adapter: PowerSpinnerInterface<Nothing> =
        filterAdapter(spinnerView, attachFilter, minPrice, maxPrice)

    spinnerView.text = obtainHeader(minPrice, maxPrice)
    spinnerView.setSpinnerAdapter(adapter)
}

private fun filterAdapter(
    spinnerView: PowerSpinnerView,
    filterApplier: ((Good)->Boolean)->Unit,
    minPrice: Int,
    maxPrice: Int
) = object : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PowerSpinnerInterface<Nothing> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PriceFilterVieholderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val from = binding.filterPriceFrom
        val to = binding.filterPriceTo
        val toMsg = binding.filterErrorMsgTo

        from.run {
            convenienceWatcher()
            setText(minPrice.toString())
        }
        to.run {
            convenienceWatcher()
            setText(maxPrice.toString())
        }

        val onSuccess: ()->Unit = {
            val priceFrom = from.textToInt()
            val priceTo = to.textToInt()
            spinnerView.text = obtainHeader(priceFrom, priceTo)
        }

        val priceFilterPredicate: (Good)->Boolean = {
            when(it) {
                is BestSellerGood -> it.discountPrice.inRange(from.textToInt(), to.textToInt())
                else -> true
            }
        }

        val sign = "Cant be greater than $maxPrice"
        val commonFilter: ()->Unit = {
            onSuccess()
            if(to.textToInt() > maxPrice) {
                toMsg.text = sign
            } else {
                toMsg.text = ""
            }
        }

        from.priceWatcher(filterApplier, priceFilterPredicate, commonFilter)
        to.priceWatcher(filterApplier, priceFilterPredicate, commonFilter)

        return object : RecyclerView.ViewHolder(binding.root) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun getItemCount() = 1
    override fun notifyItemSelected(index: Int) {}
    override fun setItems(itemList: List<Nothing>) {}

    override var index: Int
        get() = 0
        set(value) {}
    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<Nothing>?
        get() = null
        set(value) {}
    override val spinnerView: PowerSpinnerView
        get() = spinnerView
}

private inline fun EditText.priceWatcher(
    crossinline priceFilterApplier: ((Good) -> Boolean)->Unit,
    crossinline filterToApply: (Good)->Boolean,
    crossinline outerFilter: ()->Unit
) {
    doOnTextChanged { _, _, _, _ ->
        outerFilter()
        priceFilterApplier { filterToApply(it) }
    }
}

private fun obtainHeader(min: Int, max: Int) = "${decoratePrice(min)} - ${decoratePrice(max)}"

fun EditText.convenienceWatcher() {
    unsafeEditableWatcher f@{ editable->
        if (editable.isEmpty()) editable.replace(0,0, "0")
        if (editable.length ==1) return@f

        var startZeroes = 0

        for (c in editable) {
            if (c =='0') {
                startZeroes++
            }
            else break
        }
        if (startZeroes!=0) {
            editable.replace(0, startZeroes, "")
        }
    }
}
















