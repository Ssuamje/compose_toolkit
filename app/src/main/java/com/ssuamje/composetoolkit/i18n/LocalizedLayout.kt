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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.R
import com.ssuamje.composetoolkit.extensions.clickableNoRipple
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
                Text("Change language and click text")
            }
            LocalizedLayout<ColumnScope>(
                layoutResId = R.string.test_localized_layout,
                parent = this@Column
            ) {
                provide("1") {
                    val origin = rememberLocalizedString(R.string.test_localized_1)
                    var text by remember { mutableStateOf(origin) }
                    Text(
                        text.styleText {
                            +DSFonts.Title.M
                            +DSColors.White
                        },
                        modifier = Modifier.clickableNoRipple { text += "!" }
                    )
                }
                provide("2") {
                    val origin = rememberLocalizedString(R.string.test_localized_2)
                    var text by remember { mutableStateOf(origin) }
                    Text(
                        text.styleText {
                            +DSFonts.Title.M
                            +DSColors.White
                        },
                        modifier = Modifier.clickableNoRipple { text += "!" }
                    )
                }
                provide("3") {
                    val origin = rememberLocalizedString(R.string.test_localized_3)
                    var text by remember { mutableStateOf(origin) }
                    Text(
                        text.styleText {
                            +DSFonts.Title.M
                            +DSColors.White
                        },
                        modifier = Modifier.clickableNoRipple { text += "!" }
                    )
                }
            }
        }
    }
}

/**
 * @see R.string.layout_keys
 * @exception IllegalArgumentException : string.xml에 정의된 layout_keys와 일치하지 않는 key가 존재할 경우
 */
object LocalizedLayoutManager {
    @StringRes
    private val availableKeysResource: Int = R.string.layout_keys
    private lateinit var availableKeys: List<LayoutKey>
    private val regex = """\{(.*?)\}""".toRegex()

    @Composable
    fun rememberLayoutKeys(localizedLayoutResource: String): List<LayoutKey> {
        if (!::availableKeys.isInitialized) {
            availableKeys = extractKeys(getLocalizedString(availableKeysResource))
        }
        val extractedKeys = extractKeys(localizedLayoutResource)
        require(extractedKeys.all { it in availableKeys }) {
            "Invalid layout key found in $localizedLayoutResource. " +
                    "Available keys are: $availableKeys"
        }
        return remember(localizedLayoutResource) { extractedKeys }
    }

    private fun extractKeys(input: String): List<LayoutKey> {
        return regex.findAll(input).map { it.groupValues[1] }.toList()
    }
}


class LocalizedLayoutScope<T> {
    private val _layoutComposables: MutableMap<LayoutKey, @Composable T.() -> Unit> = mutableMapOf()
    val layoutComposables: Map<LayoutKey, @Composable T.() -> Unit>
        get() = _layoutComposables.toMap()

    fun provide(key: LayoutKey, block: @Composable T.() -> Unit) {
        _layoutComposables[key] = block
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
    noinline content: LocalizedLayoutScope<T>.() -> Unit,
) {
    val scope = remember(content) { LocalizedLayoutScope<T>().apply(content) }
    val localized = rememberLocalizedString(layoutResId)
    val keys = LocalizedLayoutManager.rememberLayoutKeys(localized)

    keys.forEach { key ->
        scope.layoutComposables[key]?.invoke(parent)
    }
}