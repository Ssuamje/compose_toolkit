package com.ssuamje.composetoolkit.layouts.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import com.ssuamje.composetoolkit.previews.Previewer

@Preview
@Composable
fun BottomSheetOverlayPreview() {
    Previewer.Theme {

    }
}

val LocalBottomSheetScope =
    staticCompositionLocalOf<BottomSheetScope> { error("BottomSheetScope not provided") }

class BottomSheetScope : OverlayScope<BottomSheetScope.BottomSheetContent>() {
    inner class BottomSheetContent(
        override val id: OverlayId = OverlayId(),
        override val dismiss: () -> Unit = { close(id) },
        override val composable: @Composable (OverlayId) -> Unit
    ) : OverlayContent

    @Composable
    override fun Render() {

    }
}
