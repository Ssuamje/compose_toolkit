package com.ssuamje.composetoolkit.previews

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.LocaleList
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@SuppressLint("ConstantLocale")
object Previewer {
    private var locale by mutableStateOf(Locale.getDefault())

    @Composable
    fun Theme(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    ) {
        val context = LocalContext.current
        val locale = locale
        val localizedContext = remember(locale) { context.withLocale(locale) }

        CompositionLocalProvider(
            LocalContext provides localizedContext,
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                content()
            }
        }
    }
}

fun Context.withLocale(locale: Locale): ContextWrapper {
    val config = Configuration(resources.configuration)

    Locale.setDefault(locale)
    config.setLocale(locale)
    config.setLocales(LocaleList(locale))

    return ContextWrapper(createConfigurationContext(config))
}