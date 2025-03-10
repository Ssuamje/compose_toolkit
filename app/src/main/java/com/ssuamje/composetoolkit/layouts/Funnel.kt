package com.ssuamje.composetoolkit.layouts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSFonts
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText

@Preview
@Composable
fun Preview() {
    val fastTransition: AnimatedContentTransitionScope<Any>.() -> ContentTransform = {
        slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
    }
    val slowTransition: AnimatedContentTransitionScope<Any>.() -> ContentTransform = {
        fadeIn(animationSpec = tween(1000)) togetherWith fadeOut(animationSpec = tween(1000))
    }

    Funnel(initialStep = "step1") {
        step("step1") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Step 1".styleText {
                    +DSFonts.Title.L
                    +Color.White
                })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { stepTo("step2", fastTransition) }) {
                    Text("Go to Step 2 (Fast Slide)")
                }
            }
        }

        step("step2") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Step 2".styleText {
                    +DSFonts.Title.L
                    +Color.White
                })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { stepTo("step3", slowTransition) }) {
                    Text("Go to Step 3 (Slow Fade)")
                }
            }
        }

        step("step3") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Step 3".styleText {
                    +DSFonts.Title.L
                    +Color.White
                })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { stepTo("step1") }) {
                    Text("Go to Step 1 (Default)")
                }
            }
        }
    }
}

data class FunnelStep(
    val key: Any,
    val composable: @Composable FunnelScope.() -> Unit,
)

class FunnelScope(private val stepToInternal: (Any, AnimatedContentTransitionScope<Any>.() -> ContentTransform) -> Unit) {
    private val _steps = mutableListOf<FunnelStep>()
    val steps: List<FunnelStep> get() = _steps

    fun step(key: Any, content: @Composable FunnelScope.() -> Unit) {
        _steps.add(FunnelStep(key, content))
    }

    fun stepTo(
        key: Any,
        transitionSpec: (AnimatedContentTransitionScope<Any>.() -> ContentTransform)? = null,
    ) {
        stepToInternal(key, transitionSpec ?: { fadeIn() togetherWith fadeOut() })
    }
}

@Composable
fun Funnel(
    initialStep: Any,
    modifier: Modifier = Modifier,
    animationContentLabel: String = "FunnelTransition",
    defaultTransitionSpec: AnimatedContentTransitionScope<Any>.() -> ContentTransform = { fadeIn() togetherWith fadeOut() },
    onStepNotFound: @Composable () -> Unit = { throw IllegalArgumentException("Step not found") },
    content: FunnelScope.() -> Unit,
) {
    var currentStep by remember { mutableStateOf(initialStep) }
    var currentTransitionSpec by remember { mutableStateOf(defaultTransitionSpec) }

    val stepTo: (Any, (AnimatedContentTransitionScope<Any>.() -> ContentTransform)?) -> Unit =
        remember {
            { key, transitionSpec ->
                currentTransitionSpec = transitionSpec ?: defaultTransitionSpec
                currentStep = key
            }
        }

    val funnelScope = remember(content, stepTo) {
        FunnelScope(stepTo).apply { content() }
    }
    funnelScope.content()
    val steps = funnelScope.steps

    Box(
        modifier = modifier,
    ) {
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = { currentTransitionSpec(this) },
            label = animationContentLabel
        ) { stepKey ->
            val step = steps.find { it.key == stepKey }
            if (step == null) onStepNotFound()
            else step.composable.invoke(funnelScope)
        }
    }
}