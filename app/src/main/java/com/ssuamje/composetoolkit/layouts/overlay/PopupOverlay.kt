package com.ssuamje.composetoolkit.layouts.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.extensions.clickableNoRipple
import com.ssuamje.composetoolkit.extensions.condition
import com.ssuamje.composetoolkit.previews.Previewer
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSColors
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText

@Preview
@Composable
fun PopUpOverlayPreview() {
    Previewer.Theme {
        val popupScope = LocalPopupScope.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    popupScope.open {

                        fun getRandomColor(): Color {
                            return listOf(
                                Color.Black,
                                Color.Green,
                                Color.Red,
                                Color.White
                            ).random()
                        }

                        var color by remember { mutableStateOf(getRandomColor()) }


                        Text(
                            "Click here to recompose with random color"
                                .styleText { +color },
                            modifier = Modifier.clickableNoRipple {
                                color = getRandomColor()
                            }
                        )
                    }
                }
            ) {
                Text("add random item, click to delete")
            }
            Button(onClick = { popupScope.closeLatest() }) {
                Text("remove latest")
            }
        }
    }
}

val LocalPopupScope = staticCompositionLocalOf<PopupScope> { error("PopupScope not provided") }

@Composable
fun rememberPopupScope(): PopupScope {
    return remember { PopupScope() }
}

fun PopupScope.open(
    isBackgroundClickDismissable: Boolean = true,
    isBackgroundDimmed: Boolean = true,
    isFramed: Boolean = false,
    content: @Composable (OverlayId) -> Unit,
) {
    open(
        Content(
            isFramed = isFramed,
            isBackgroundClickDismissable = isBackgroundClickDismissable,
            isBackgroundDimmed = isBackgroundDimmed,
            composable = content
        )
    )
}

class PopupScope : OverlayScope<PopupScope.Content>() {

    inner class Content(
        override val id: OverlayId = OverlayId(),
        val isFramed: Boolean = false,
        val isBackgroundClickDismissable: Boolean = true,
        val isBackgroundDimmed: Boolean = true,
        override val composable: @Composable ((OverlayId) -> Unit),
    ) : OverlayContent {
        override fun dismiss() = close(id)
    }

    @Composable
    override fun Render() {
        getContents().forEach { content ->
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
                            clickableNoRipple { content.dismiss() }
                        }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickableNoRipple {/*  팝업 내부 클릭 이벤트를 소모해 외부 클릭 이벤트가 트리거되지 않도록 함 */ }
                ) {
                    if (content.isFramed) PopupSkeleton { content.composable(content.id) }
                    else content.composable(content.id)
                }
            }
        }
    }
}


@Composable
fun PopupSkeleton(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(20.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    isWidthConstrained: Boolean = true,
    isHeightConstrained: Boolean = true,
    backgroundColor: Color = Color.Black,
    shape: RoundedCornerShape = RoundedCornerShape(size = 20.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels
    val density = LocalDensity.current
    val widthWithDensity = remember { with(density) { screenWidth.toDp() } }
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
    val heightWithDensity = remember { with(density) { screenHeight.toDp() } }

    Column(
        modifier = modifier
            .condition(isWidthConstrained) { widthIn(min = widthWithDensity * 5 / 6) }
            .condition(isHeightConstrained) { heightIn(min = heightWithDensity * 1 / 3) }
            .background(backgroundColor, shape)
            .padding(padding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        content()
    }
}