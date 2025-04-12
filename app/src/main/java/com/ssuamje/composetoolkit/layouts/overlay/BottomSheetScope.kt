package com.ssuamje.composetoolkit.layouts.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.extensions.clickableNoRipple
import com.ssuamje.composetoolkit.extensions.condition
import com.ssuamje.composetoolkit.ui.designsystem.foundation.ComposeToolkitTheme
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSColors
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSFonts
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun LocalBottomSheetScopePreview() {
    ComposeToolkitTheme {
        val bottomSheetScope = LocalBottomSheetScope.current
        var text by remember { mutableStateOf("눌러서 느낌표 더하기") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DSColors.Background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    bottomSheetScope.open(
                        bottomSheetScope.Content {
                            Text(text.styleText {
                                +DSFonts.Title.M
                                +DSColors.White
                            }, modifier = Modifier.clickableNoRipple { text += "!" })
                        }
                    )
                }
            ) { Text("Click here to show bottom sheet") }
        }
    }
}

@Preview
@Composable
fun BottomSheetOverlayPreview() {
    val bottomSheetScope = BottomSheetScope()
    val overlayProvider = OverlayProvider(listOf(bottomSheetScope))

    overlayProvider.Scaffolding {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DSColors.Background)
                .padding(it),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    bottomSheetScope.open(
                        bottomSheetScope.Content {
                            Text("안녕하세요".styleText {
                                +DSFonts.Title.M
                                +DSColors.White
                            })
                        }
                    )
                }
            ) { Text("Click here to show bottom sheet") }
        }
    }
}

val LocalBottomSheetScope =
    staticCompositionLocalOf<BottomSheetScope> { error("BottomSheetScope not provided") }

@Composable
fun rememberBottomSheetScope(): BottomSheetScope {
    return remember { BottomSheetScope() }
}

fun BottomSheetScope.open(
    isBackgroundDimmed: Boolean = true,
    isDraggable: Boolean = true,
    isBackgroundClickDismissable: Boolean = true,
    content: @Composable () -> Unit
) {
    val overlayContent = Content(
        isBackgroundDimmed = isBackgroundDimmed,
        isDraggable = isDraggable,
        isBackgroundClickDismissable = isBackgroundClickDismissable,
        composable = { _ -> content() }
    )
    open(overlayContent)
}

class BottomSheetScope : OverlayScope<BottomSheetScope.Content>() {

    inner class Content(
        override val id: OverlayId = OverlayId(),
        val isBackgroundClickDismissable: Boolean = true,
        val isBackgroundDimmed: Boolean = true,
        val isDraggable: Boolean = true,
        val isHeightConstrained: Boolean = false,
        private val _isVisible: MutableState<Boolean> = mutableStateOf(true),
        val isVisible: State<Boolean> = _isVisible,
        override val dismiss: () -> Unit = {
            close(id)
        },
        override val composable: @Composable (OverlayId) -> Unit
    ) : OverlayContent {
        fun hide() {
            _isVisible.value = false
        }
    }

    @Composable
    override fun Render() {
        getContents().forEach { content ->
            var localVisible by remember { mutableStateOf(false) }
            val isVisible by content.isVisible

            LaunchedEffect(isVisible) {
                localVisible = isVisible
                if (!isVisible) {
                    delay(0.2.seconds)
                    content.dismiss()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .condition(content.isBackgroundDimmed) {
                        background(DSColors.Dimmed)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .condition(content.isBackgroundClickDismissable) {
                            clickableNoRipple { content.hide() }
                        }
                )
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = localVisible,
                    enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight }
                    ),
                    exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight }
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                    ) {
                        BottomSheetHeader(isDraggable = content.isDraggable) { content.hide() }
                        BottomSheetSkeleton(
                            isHeightConstrained = content.isHeightConstrained,
                            modifier = Modifier.clickableNoRipple {/*  시트 내부 클릭 이벤트를 소모해 외부 클릭 이벤트가 트리거되지 않도록 함 */ }
                        ) { content.composable(content.id) }
                    }
                }
            }
        }
    }

    @Composable
    fun BottomSheetHeader(
        modifier: Modifier = Modifier,
        backgroundColor: Color = DSColors.Background,
        shape: Shape = RoundedCornerShape(16.dp),
        isDraggable: Boolean = true,
        onDrag: () -> Unit,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(
                    color = backgroundColor,
                    shape = shape,
                )
                .condition(isDraggable) {
                    pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount > 0) {
                                onDrag()
                            }
                        }
                    }
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isDraggable) {
                Box(
                    modifier = Modifier
                        .size(80.dp, 4.dp)
                        .background(
                            color = DSColors.Gray._500,
                            shape = shape,
                        )
                )
            }
        }
    }

    @Composable
    fun BottomSheetSkeleton(
        modifier: Modifier = Modifier,
        padding: PaddingValues = PaddingValues(12.dp),
        isHeightConstrained: Boolean = true,
        verticalArrangement: Arrangement.Vertical = Arrangement.Top,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        backgroundColor: Color = DSColors.Background,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val density = LocalDensity.current
        val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
        val heightWithDensity = with(density) { screenHeight.toDp() }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .condition(isHeightConstrained) { heightIn(min = heightWithDensity * 5 / 6) }
                .background(backgroundColor)
                .padding(padding),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }
}
