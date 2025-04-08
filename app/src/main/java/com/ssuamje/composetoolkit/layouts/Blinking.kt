package com.ssuamje.composetoolkit.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun Blinking(
    modifier: Modifier = Modifier,
    appearTransitionDuration: Duration = 0.3.seconds,
    disappearTransitionDuration: Duration = 0.3.seconds,
    appearTransition: EnterTransition = fadeIn(animationSpec = tween(durationMillis = appearTransitionDuration.inWholeMilliseconds.toInt())),
    disappearTransition: ExitTransition = fadeOut(animationSpec = tween(durationMillis = disappearTransitionDuration.inWholeMilliseconds.toInt())),
    appearDuration: Duration = 2.seconds,
    disappearDuration: Duration = 1.seconds,
    onAppear: (() -> Unit)? = null,
    onDisappear: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            isVisible = true
            onAppear?.invoke()
            delay(appearDuration + appearTransitionDuration)

            isVisible = false
            delay(disappearTransitionDuration)
            onDisappear?.invoke()
            delay(disappearDuration)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = appearTransition,
            exit = disappearTransition
        ) {
            content?.invoke()
        }
    }
}