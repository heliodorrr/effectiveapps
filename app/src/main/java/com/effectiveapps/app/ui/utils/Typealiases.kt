package com.effectiveapps.app.ui.utils

typealias DataSubscriber<T> = ((T)->Unit)->Unit
typealias ListSubscriber<T> = DataSubscriber<List<T>>