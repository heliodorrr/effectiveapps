package com.effectiveapps.app.ui.smartphonedetails

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import com.effectiveapps.app.R
import com.effectiveapps.app.databinding.FragmentSmartphoneDetailsBinding
import com.effectiveapps.app.ui.cart.CartViewModel
import com.effectiveapps.app.ui.navigation.Routes
import com.effectiveapps.app.ui.smartphonedetails.SmartphoneDetailsFragmentsState.Companion.FirstOptionIndex
import com.effectiveapps.app.ui.smartphonedetails.SmartphoneDetailsFragmentsState.Companion.SecondOptionIndex
import com.effectiveapps.app.ui.utils.compatDrawable
import com.effectiveapps.app.ui.utils.decoratePrice
import com.effectiveapps.app.ui.utils.pixelSize
import com.effectiveapps.app.ui.utils.setVisible
import com.effectiveapps.di.viewModelInjector
import com.effectiveapps.domain.RequestResult
import dagger.android.support.AndroidSupportInjection


import javax.inject.Inject
import javax.inject.Provider

class SmartphoneDetailsFragment : Fragment() {

    private lateinit var smartphoneDetailsViewModel: SmartphoneDetailsViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var binding: FragmentSmartphoneDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    @Inject
    fun injectViewModel(
        sdProvider: Provider<SmartphoneDetailsViewModel>,
        cartProvider: Provider<CartViewModel>

    ) {
        smartphoneDetailsViewModel = viewModelInjector(
            provider = sdProvider,
            owner = requireActivity()
        )
        cartViewModel = viewModelInjector(
            provider = cartProvider,
            owner = requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmartphoneDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachNotifiers()

        subscribeToData()

        setupNavigation()

    }

    private fun setupNavigation() {
        binding.detailsBuyButton.setOnClickListener {
            val currentStateResult = smartphoneDetailsViewModel.currentStateResult
            if (currentStateResult is RequestResult.Success) {
                cartViewModel.putItem(currentStateResult.data)
            }
        }
        binding.detailsToTheBagButton.setOnClickListener {
            findNavController().navigate(Routes.Cart)
        }
        binding.detailsBackButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setRating(rating: Double) = with(binding.ratingContainer){
        doOnPreDraw {
            val starOffset = requireContext().pixelSize(R.dimen.details_star_offset)
            val starWidth =  binding.ratingContainer.width/5f - starOffset
            val cbr = Rect(0, 0, width, height)
            clipBounds = cbr.apply {
                val starSpan = starWidth + starOffset
                val intRating = rating.toInt()
                val fractionPart = rating - intRating
                right = (intRating * starSpan + fractionPart * starWidth).toInt()
            }
        }
    }


    private fun attachNotifiers() {
        binding.colorOptionCard1.setOnClickListener {
            smartphoneDetailsViewModel.notifyColorOptionChanged(FirstOptionIndex)
        }
        binding.colorOptionCard2.setOnClickListener {
            smartphoneDetailsViewModel.notifyColorOptionChanged(SecondOptionIndex)
        }


        binding.detailsStorageOption1.setOnClickListener {
            smartphoneDetailsViewModel.notifyStorageOptionChanged(FirstOptionIndex)
        }

        binding.detailsStorageOption2.setOnClickListener {
            smartphoneDetailsViewModel.notifyStorageOptionChanged(SecondOptionIndex)
        }

        binding.detailsFavoriteButton.setOnClickListener { smartphoneDetailsViewModel.notifyFavoriteStatusChanged() }
    }

    private fun subscribeToData() {

        val viewPagerBinder = attachDetailsImageAdapterToViewPager(binding.detailsImageViewPager)

        val capacityChosenBackground = requireContext().compatDrawable(R.drawable.orange_rect)

        val capacityTextColorChosen = requireContext()
            .getColor(R.color.details_storage_option_chosen)

        val capacityTextColorUnchosen = requireContext()
            .getColor(R.color.details_storage_option_unchosen)

        val isFavoriteDrawable = requireContext().compatDrawable(R.drawable.icon_heart_fill_white)

        val notFavoriteDrawable = requireContext().compatDrawable(R.drawable.icon_heart_hollow_white)

        smartphoneDetailsViewModel.subscribeFragment(this) { state->

            viewPagerBinder(state.images)
            setRating(state.rating)

            binding.detailsFavoriteIcon
                .setImageDrawable(if (state.isFavorite) isFavoriteDrawable else notFavoriteDrawable)

            binding.detailsTitle.text = state.title

            binding.detailsCpuText.text = state.cpu

            binding.detailsPrice.text = decoratePrice(state.price)

            binding.detailsCameraText.text = state.camera

            binding.detailsRamText.text = state.ram

            binding.detailsStorage.text =
                state.capacityOptionForIndex(SecondOptionIndex)

            binding.colorOptionCard1
                .setCardBackgroundColor(state.colorOptionForIndex(FirstOptionIndex))

            binding.colorOptionCard2
                .setCardBackgroundColor(state.colorOptionForIndex(SecondOptionIndex))

            binding.colorOptionSign1.setVisible(state.colorOptionIndex == FirstOptionIndex)
            binding.colorOptionSign2.setVisible(state.colorOptionIndex == SecondOptionIndex)


            fun bindDetailsOption(tv: TextView, index: Int) = tv.run {
                text = state.capacityOptionForIndex(index)
                if (state.storageOptionIndex == index) {
                    background = capacityChosenBackground
                    setTextColor(capacityTextColorChosen)
                } else {
                    setTextColor(capacityTextColorUnchosen)
                    background = null
                }
            }

            bindDetailsOption(binding.detailsStorageOption1, FirstOptionIndex)
            bindDetailsOption(binding.detailsStorageOption2, SecondOptionIndex)

        }

    }

    companion object {

    }
}