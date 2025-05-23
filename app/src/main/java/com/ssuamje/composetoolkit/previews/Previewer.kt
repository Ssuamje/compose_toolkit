package com.ssuamje.composetoolkit.previews

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.LocaleList
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import com.ssuamje.composetoolkit.extensions.clickableNoRipple
import com.ssuamje.composetoolkit.layouts.overlay.LocalBottomSheetScope
import com.ssuamje.composetoolkit.layouts.overlay.LocalOverlayProvider
import com.ssuamje.composetoolkit.layouts.overlay.LocalPopupScope
import com.ssuamje.composetoolkit.layouts.overlay.open
import com.ssuamje.composetoolkit.layouts.overlay.rememberBottomSheetScope
import com.ssuamje.composetoolkit.layouts.overlay.rememberOverlayProvider
import com.ssuamje.composetoolkit.layouts.overlay.rememberPopupScope
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSFonts
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("ConstantLocale")
object Previewer {
    var locale by mutableStateOf(Locale.getDefault())

    @Composable
    fun Theme(
        modifier: Modifier = Modifier,
        showUtilView: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        val context = LocalContext.current
        val locale = locale
        val localizedContext = remember(locale) { context.withLocale(locale) }

        val popupScope = rememberPopupScope()
        val bottomSheetScope = rememberBottomSheetScope()
        val overlayProvider = rememberOverlayProvider(popupScope, bottomSheetScope)

        CompositionLocalProvider(
            LocalContext provides localizedContext,
            LocalOverlayProvider provides overlayProvider,
            LocalPopupScope provides popupScope,
            LocalBottomSheetScope provides bottomSheetScope,
        ) {
            overlayProvider.Scaffolding {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    key(locale) {
                        content()
                    }
                    if (showUtilView) PreviewUtilSheetButton(modifier = Modifier.align(Alignment.TopEnd))
                }
            }
        }
    }
}

@Composable
fun PreviewUtilSheetButton(
    modifier: Modifier = Modifier,
) {
    val bottomSheetScope = LocalBottomSheetScope.current
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    scope.launch {
                        change.consume()
                        offsetX.snapTo(offsetX.value + dragAmount.x)
                        offsetY.snapTo(offsetY.value + dragAmount.y)
                    }
                }
            }
            .clickableNoRipple {
                bottomSheetScope.open {
                    PreviewUtilSheet(
                        onLocaleChange = {
                            Previewer.locale = it
                        }
                    )
                }
            }
    ) {
        Text("🔍".styleText {
            +DSFonts.Body.L
        })
    }
}

fun Context.withLocale(locale: Locale): ContextWrapper {
    val config = Configuration(resources.configuration)

    Locale.setDefault(locale)
    config.setLocale(locale)
    config.setLocales(LocaleList(locale))

    return ContextWrapper(createConfigurationContext(config))
}