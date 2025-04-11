package com.ssuamje.composetoolkit.i18n

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.R
import com.ssuamje.composetoolkit.previews.Previewer
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSColors
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSFonts
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText
import java.util.Locale

typealias LayoutKey = String

// (설치된 앱)에서 - In (Installed Apps) {title} {description} - {description} {title}
// (터닝)을 찾아주세요 - Find (Turning)

@Preview
@Composable
fun LocalizedLayoutPreview() {
    Previewer.Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DSColors.Background),
            verticalArrangement = Arrangement.spacedBy(
                12.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    Previewer.locale = if (Previewer.locale == Locale.KOREAN) {
                        Locale.ENGLISH
                    } else {
                        Locale.KOREAN
                    }
                }
            ) {
                Text("Change Language")
            }
            LocalizedLayout<ColumnScope>(
                layoutResId = R.string.test_localized_layout,
                parent = this@Column
            ) {
                provide("1") {
                    Text(
                        rememberLocalizedString(R.string.test_localized_1).styleText {
                            +DSFonts.Title.M
                            +DSColors.White
                        },
                    )
                }
                provide("2") {
                    Text(
                        rememberLocalizedString(R.string.test_localized_2).styleText {
                            +DSFonts.Title.M
                            +DSColors.White
                        },
                    )
                }
                provide("3") {
                    Text(
                        rememberLocalizedString(R.string.test_localized_3).styleText {
                            +DSFonts.Title.M
                            +DSColors.White
                        },
                    )
                }
            }
        }
    }
}


class LocalizedLayoutScope<T> {
    val replacements = mutableListOf<Pair<LayoutKey, @Composable T.() -> Unit>>()

    fun provide(key: LayoutKey, block: @Composable T.() -> Unit) {
        replacements.add(key to block)
    }

    companion object {
        val REGEX = """\{(.*?)\}""".toRegex()
    }
}


/**
 * example:
 *  layoutResource = ko -> "{title} {description}", en -> "{description} {title}"
 */
@Composable
inline fun <reified T> LocalizedLayout(
    @StringRes layoutResId: Int,
    parent: T,
    crossinline content: LocalizedLayoutScope<T>.() -> Unit,
) {
    val scope = remember { LocalizedLayoutScope<T>().apply(content) }
    val localized = rememberLocalizedString(layoutResId)
    val keys = remember(localized) {
        LocalizedLayoutScope.REGEX.findAll(localized).map { it.groupValues[1] }
    }
    val replacementMap = remember(scope) { scope.replacements.toMap() }

    keys.forEach { key ->
        replacementMap[key]?.invoke(parent)
    }
}