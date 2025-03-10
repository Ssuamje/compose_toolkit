package com.ssuamje.composetoolkit.ui.designsystem.foundation

import androidx.compose.ui.graphics.Color

object DSColors {
    val Background = hexColor("#121315")
    val White = hexColor("#FAFAFA")
    val Black = hexColor("#1F2023")

    object Gray {
        val _50 = hexColor("#E4E4E7")
        val _100 = hexColor("#BCBDC3")
        val _200 = hexColor("#8A8C97")
        val _300 = hexColor("#6C6E79")
        val _400 = hexColor("#5E6069")
        val _500 = hexColor("#4A4C54")
        val _600 = hexColor("#37393F")
        val _700 = hexColor("#26272B")
    }

    object Green {
        val _100 = hexColor("#D1F6E7")
        val _200 = hexColor("#98E2C4")
        val _300 = hexColor("#5DCA9E")
        val _400 = hexColor("#14AF71")
        val _500 = hexColor("#0E7B4F")
        val _600 = hexColor("#08462D")
        val _700 = hexColor("#042317")
    }

    object Purple {
        val _100 = hexColor("#DCCCF4")
        val _200 = hexColor("#BEA2EC")
        val _300 = hexColor("#A178E3")
        val _400 = hexColor("#844EDA")
        val _500 = hexColor("#6237A6")
        val _600 = hexColor("#4A2783")
        val _700 = hexColor("#331D57")
    }

    object Yellow {
        val _100 = hexColor("#FCF1D4")
        val _200 = hexColor("#F9E1A4")
        val _300 = hexColor("#F5D175")
        val _400 = hexColor("#F2C247")
        val _500 = hexColor("#C99D2C")
        val _600 = hexColor("#96741D")
        val _700 = hexColor("#614A0E")
    }
}

fun hexColor(hex: String): Color {
    if (hex.length != 6 && hex.length != 7) {
        throw IllegalArgumentException("Hex color must be 6 or 7 characters long")
    }
    return when (hex.first()) {
        '#' -> {
            Color(android.graphics.Color.parseColor(hex))
        }

        else -> {
            Color(android.graphics.Color.parseColor("#$hex"))
        }

    }
}
