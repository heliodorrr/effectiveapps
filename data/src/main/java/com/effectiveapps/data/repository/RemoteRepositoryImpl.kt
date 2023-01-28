package com.effectiveapps.data.repository

import com.effectiveapps.data.api.TestApi
import com.effectiveapps.domain.model.GoodsQuery
import com.effectiveapps.domain.model.SmartphoneGoodDetails
import com.effectiveapps.domain.repository.GoodsQueryRepository
import com.effectiveapps.domain.repository.SmartphoneDetailsRepository
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val api: TestApi
): GoodsQueryRepository, SmartphoneDetailsRepository {
    override suspend fun getGoodsQuery(): GoodsQuery {
        return api.getGoodsQuery()
    }

    override suspend fun getSmartphoneDetails(): SmartphoneGoodDetails {
        return api.getSmartphoneDetails().toSmartphoneGoodDetails()
    }


}