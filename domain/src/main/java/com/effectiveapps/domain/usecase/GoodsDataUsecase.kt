package com.effectiveapps.domain.usecase

import com.effectiveapps.domain.repository.GoodsQueryRepository
import com.effectiveapps.domain.utils.generateFlow
import javax.inject.Inject

class GoodsDataUseCase @Inject constructor(
    private val goodsQueryRepository: GoodsQueryRepository
) {
    fun invoke() = generateFlow{ goodsQueryRepository.getGoodsQuery() }
}

