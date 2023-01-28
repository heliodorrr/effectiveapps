package com.effectiveapps.app.ui.smartphonedetails

import android.graphics.Color
import com.effectiveapps.domain.model.SmartphoneGoodDetails

data class SmartphoneDetailsFragmentsState(
    private val smartphoneGoodDetails: SmartphoneGoodDetails,
    val colorOptionIndex: Int = 0,
    val storageOptionIndex: Int = 0,
    val isFavorite: Boolean = smartphoneGoodDetails.isFavorites
) {

    val title get() = smartphoneGoodDetails.title
    val cpu get() = smartphoneGoodDetails.cpu
    val camera get() = smartphoneGoodDetails.camera
    val price get() = smartphoneGoodDetails.price
    val rating get() = smartphoneGoodDetails.rating
    val images get() = smartphoneGoodDetails.images
    val sd get() = smartphoneGoodDetails.sd
    val ram get() = smartphoneGoodDetails.ssd

    val frontImage: String?
        get() {
            return if (images.isEmpty()) null else images[0]
        }

    fun capacityOptionForIndex(index: Int)
    = if (index < smartphoneGoodDetails.capacity.size) {
        smartphoneGoodDetails.capacity[index]
    } else {
        ""
    }

    fun colorOptionForIndex(index: Int)
    = if (index < smartphoneGoodDetails.color.size) {
        Color.parseColor(smartphoneGoodDetails.color[index])
    } else {
        Color.GRAY
    }

    companion object {
        const val FirstOptionIndex = 0
        const val SecondOptionIndex = 1
    }
}