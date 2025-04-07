package com.ssuamje.composetoolkit.ui.designsystem.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.ssuamje.composetoolkit.layouts.overlay.LocalBottomSheetScope
import com.ssuamje.composetoolkit.layouts.overlay.LocalOverlayProvider
import com.ssuamje.composetoolkit.layouts.overlay.LocalPopupScope
import com.ssuamje.composetoolkit.layouts.overlay.rememberBottomSheetScope
import com.ssuamje.composetoolkit.layouts.overlay.rememberOverlayProvider
import com.ssuamje.composetoolkit.layouts.overlay.rememberPopupScope

@Composable
fun ComposeToolkitTheme(
    content: @Composable () -> Unit
) {
    val popupScope = rememberPopupScope()
    val bottomSheetScope = rememberBottomSheetScope()
    val overlayProvider = rememberOverlayProvider(popupScope, bottomSheetScope)
    CompositionLocalProvider(
        LocalOverlayProvider provides overlayProvider,
        LocalPopupScope provides popupScope,
        LocalBottomSheetScope provides bottomSheetScope,
    ) {
        overlayProvider.Scaffolding {
            content()
        }
    }
}