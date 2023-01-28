package com.effectiveapps.domain.repository

import com.effectiveapps.domain.model.SmartphoneGoodDetails

interface SmartphoneDetailsRepository {
    suspend fun getSmartphoneDetails(): SmartphoneGoodDetails
}