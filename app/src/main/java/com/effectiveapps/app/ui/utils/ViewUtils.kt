package com.effectiveapps.app.ui.utils

import android.app.Activity
import android.view.View
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun View.addKeyboardInsetListener() {
    val window = (context as Activity).window
    val decor = window.decorView
    val thisView = this

    ViewCompat.setOnApplyWindowInsetsListener(decor) { v, insets ->
        val returnted = ViewCompat.onApplyWindowInsets(v, insets)
        val visibilityState = if (insets.keyboardVisible()) View.GONE else View.VISIBLE
        thisView.visibility = visibilityState
        returnted
    }
}


fun WindowInsetsCompat.keyboardVisible() = isVisible(WindowInsetsCompat.Type.ime())


fun EditText.textToInt() = text.toString().toIntWithHandleErrors()

fun String.toIntWithHandleErrors() = try {
    toInt()
} catch (e: Exception) {
    0
}