package com.ssuamje.composetoolkit.composables

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlin.time.Duration

// Dependencies: com.airbnb.android:lottie-compose
@Composable
fun Animation(
    @RawRes resInt: Int,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    pauseAfterIterationDuration: Duration = Duration.ZERO,
    desiredIterationDuration: Duration? = null,
    speedMagnification: Float = 1f,
    progress: Float? = null,
    contentScale: ContentScale = ContentScale.Fit,
    onAnimationEnd: () -> Unit = {},
) {
    val compositionResult = rememberLottieComposition(LottieCompositionSpec.RawRes(resInt))
    val composition = compositionResult.value
    var isPaused by remember { mutableStateOf(false) }

    val speed = if (desiredIterationDuration != null && composition != null) {
        val compositionDurationMillis = composition.duration
        if (compositionDurationMillis > 0) {
            compositionDurationMillis / desiredIterationDuration.inWholeMilliseconds.toFloat()
        } else 1f
    } else 1f

    val animationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed * speedMagnification,
    )

    if (animationState.isAtEnd && !animationState.isPlaying && iterations != LottieConstants.IterateForever) {
        LaunchedEffect(animationState.progress) {
            isPaused = true
            if (pauseAfterIterationDuration != Duration.ZERO) delay(pauseAfterIterationDuration)
            isPaused = false
            if (animationState.progress == 1f) onAnimationEnd()
        }
    }

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = { progress ?: animationState.progress },
            contentScale = contentScale,
            modifier = modifier
        )
    }
}