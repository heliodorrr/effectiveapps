package com.effectiveapps.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import javax.inject.Provider

inline fun <reified VM : ViewModel> Fragment.viewModelInjector(
    provider: Provider<out VM>,
    owner: ViewModelStoreOwner = this
): VM {
    return ViewModelProvider(owner, factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return provider.get() as T
        }
    })[VM::class.java]
}