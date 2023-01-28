package com.effectiveapps.domain.usecase

import com.effectiveapps.domain.repository.SmartphoneDetailsRepository
import com.effectiveapps.domain.utils.generateFlow
import javax.inject.Inject

class SmartphoneDetailsUseCase @Inject constructor(
    private val repository: SmartphoneDetailsRepository
) {
    fun invoke() = generateFlow { repository.getSmartphoneDetails() }
}