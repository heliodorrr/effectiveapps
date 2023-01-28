package com.effectiveapps.domain

import android.util.Log

sealed class RequestResult<out T> {
    object Loading: RequestResult<Nothing>()
    class Error(val error: Throwable): RequestResult<Nothing>() {
        fun print(tag: String) {
            Log.d(tag, error.run { message + stackTraceToString() })
        }
    }
    class Success<T>(val data: T): RequestResult<T>()
}
