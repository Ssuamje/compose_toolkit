package com.ssuamje.composetoolkit.i18n

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ssuamje.composetoolkit.R
import com.ssuamje.composetoolkit.previews.withLocale
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSColors
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSFonts
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText
import java.util.Locale

@Preview
@Composable
fun LocalizedStringPreview() {
    var param1 by remember { mutableStateOf("one") }
    var param2 by remember { mutableStateOf("two") }
    val localized = rememberLocalizedString(
        R.string.test_with_two_strings,
        param1,
        param2,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DSColors.Background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    param1 += "!"
                }
            ) { Text("param1 + !") }
            Button(
                onClick = {
                    param2 += "!"
                }
            ) { Text("param2 + !") }
        }
        Text(localized.styleText {
            +DSFonts.Title.L
            +DSColors.White
        })
    }
}

@Composable
fun rememberLocalizedString(
    @StringRes stringRes: Int,
    vararg formatArgs: Any,
    locale: Locale? = null,
): String {
    val context = LocalContext.current
    return remember(stringRes, *formatArgs) {
        getLocalizedString(context, stringRes, *formatArgs, locale = locale)
    }
}

@Composable
fun getLocalizedString(
    @StringRes stringRes: Int,
    vararg formatArgs: Any,
    locale: Locale? = null,
): String {
    val context = LocalContext.current
    return getLocalizedString(context, stringRes, *formatArgs, locale = locale)
}

fun getLocalizedString(
    context: Context,
    @StringRes stringRes: Int,
    vararg formatArgs: Any,
    locale: Locale? = null,
): String {
    val resources = if (locale != null) {
        context.withLocale(locale).resources
    } else {
        context.resources
    }

    return resources.getString(stringRes, *formatArgs)
}
