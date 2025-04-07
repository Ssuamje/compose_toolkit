package com.ssuamje.composetoolkit.extensions

import androidx.compose.ui.graphics.Color

fun Color.opacity(level: Float): Color {
    return copy(alpha = level)
}