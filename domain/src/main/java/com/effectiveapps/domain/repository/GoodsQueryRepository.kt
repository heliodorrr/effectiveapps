package com.effectiveapps.domain.repository

import com.effectiveapps.domain.model.GoodsQuery

interface GoodsQueryRepository {
    suspend fun getGoodsQuery(): GoodsQuery
}