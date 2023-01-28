package com.effectiveapps.app.ui.utils

import android.text.Editable
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

inline fun EditText.unsafeEditableWatcher(
    crossinline lambda: (Editable)->Unit
) {
    addTextChangedListener {
        it?.let { lambda(it) }
    }
}

inline fun EditText.safeEditableWatcher(
    crossinline lambda: (Editable)->Unit
) {
    var emitterIsMe = false
    addTextChangedListener {
        if (emitterIsMe) return@addTextChangedListener
        emitterIsMe = true
        it?.let { lambda(it) }
        emitterIsMe = false
    }
}
