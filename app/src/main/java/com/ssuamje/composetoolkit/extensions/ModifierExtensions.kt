package com.ssuamje.composetoolkit.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

fun Modifier.condition(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier {
    return clickable(
        onClick = onClick,
        indication = null,
        interactionSource = MutableInteractionSource()
    )
}