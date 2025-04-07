package com.ssuamje.composetoolkit.extensions

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object BorderColorManager {
    private val colors =
        listOf(Color.Blue, Color.Red, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
    private var index = 0

    val nextColor: Color
        get() {
            val color = colors[index]
            index = (index + 1) % colors.size
            return color
        }
}

fun Modifier.debugBorder(color: Color? = null, width: Dp = 1.dp): Modifier {
    return this.border(width, color ?: BorderColorManager.nextColor)
}

fun Modifier.condition(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

infix fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier {
    return this.clickable(
        onClick = onClick,
        indication = null,
        interactionSource = MutableInteractionSource()
    )
}