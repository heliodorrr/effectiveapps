package com.effectiveapps.domain.utils

import kotlinx.coroutines.flow.flow
import com.effectiveapps.domain.RequestResult

inline fun <T> generateFlow(
    crossinline valueProducer: suspend ()->T
) = flow {
    emit(RequestResult.Loading)
    try {
        emit(RequestResult.Success(valueProducer()))
    } catch (e: Throwable) {
        emit(RequestResult.Error(e))
    }
}