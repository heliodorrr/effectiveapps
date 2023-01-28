package com.effectiveapps.app.ui.explorer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.effectiveapps.app.databinding.FilterBottomSheetBinding
import com.effectiveapps.app.databinding.FragmentExplorerBinding
import com.effectiveapps.app.ui.explorer.filter.attachBrandFilter
import com.effectiveapps.app.ui.explorer.filter.attachPriceFilter
import com.effectiveapps.app.ui.explorer.goodsscroll.attachGoodsScrollAdapterToRecycler
import com.effectiveapps.app.ui.navigation.Routes
import com.effectiveapps.app.ui.utils.addKeyboardInsetListener
import com.effectiveapps.di.viewModelInjector
import com.effectiveapps.domain.model.Good
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.AndroidSupportInjection



import javax.inject.Inject
import javax.inject.Provider

private const val PriceFilterKey = "pr"
private const val BrandFilterKey = "br"
private const val SearchbarFilterKey = "sbfk"

class ExplorerFragment : Fragment() {


    private lateinit var binding: FragmentExplorerBinding
    private lateinit var explorerViewModel: ExplorerViewModel
    private lateinit var bottomSheetBinding: FilterBottomSheetBinding

    private val filterBox = FilterBox<Good> { explorerViewModel.filterStateFlow.value = it }

    @Inject
    fun injectViewModel(provider: Provider<ExplorerViewModel>) {
        explorerViewModel = viewModelInjector(provider)
    }

    override fun onAttach(context: Context) { super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExplorerBinding.inflate(inflater, container, false)
        bottomSheetBinding = FilterBottomSheetBinding
            .inflate(layoutInflater, binding.root, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom navigation when keyboard shows up
        binding.bottomNavigationContainer.addKeyboardInsetListener()

        setupCategories()

        setupSearchBarFilter()

        setupGoodsScroll()

        setupFilterBottomSheet()

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.bottomNavigationContainer.getFragment<com.effectiveapps.app.ui.bottomnav.BottomNavigationFragment>()
            .addCallback(com.effectiveapps.app.ui.bottomnav.BottomNavigationFragment.Bag) {
                findNavController().navigate(Routes.Cart)
            }
    }

    private fun navigateToDetails() {
        findNavController().navigate(Routes.Details)
    }


    private fun setupGoodsScroll() {
        attachGoodsScrollAdapterToRecycler(
            rv = binding.goodsScroll,
            dataSubscriber = {

                explorerViewModel.subscribeGoodsScrollData(viewLifecycleOwner, it)
            },
            notifyFavoriteChanged = explorerViewModel.favoriteChanged,
            notifierSubscriber = { explorerViewModel.setOnFavoriteChanged(it) },
            onClick = { navigateToDetails() }
        )
    }

    private fun setupFilterBottomSheet() {
        val behavior = BottomSheetBehavior.from(bottomSheetBinding.root)
        behavior.isHideable = true

        val doReverse: (View)->Unit = {
            behavior.state = if (behavior.state==BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.STATE_HIDDEN
            } else {
                BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.filterIcon.setOnClickListener(doReverse)
        bottomSheetBinding.filterDoneButton.setOnClickListener(doReverse)
        bottomSheetBinding.filterCloseButton.setOnClickListener(doReverse)

        attachBrandFilter(
            bottomSheetBinding.brandSpinner,
            subscriber = { explorerViewModel.subscriberToBrands(this, it)  },
            attachFilter = { filterBox[BrandFilterKey] = it },
            viewLifecycleOwner
        )

        attachPriceFilter(
            bottomSheetBinding.priceSpinner,
            attachFilter = { filterBox[PriceFilterKey] = it },
            minPrice = 0,
            maxPrice = 10_000,
            lo = viewLifecycleOwner
        )


        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }




    private fun setupSearchBarFilter() {
        binding.searchbarTextfield.doOnTextChanged { text, start, before, count ->
            filterBox[SearchbarFilterKey] = {
                text?.let { nonNullText->
                    it.title.contains(nonNullText,true)
                } ?: true
            }
        }
    }

    private fun setupCategories() {
        attachCategoriesAdapter(
            rv = binding.categoryRecycler,
            dataSubscriber = {
                explorerViewModel.subscribeToCategoryIndex(viewLifecycleOwner, it)
            },
            notifier = explorerViewModel.categoryNotifier
        )
    }

    companion object {

    }
}