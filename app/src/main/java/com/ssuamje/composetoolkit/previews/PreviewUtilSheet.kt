package com.ssuamje.composetoolkit.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.R
import com.ssuamje.composetoolkit.i18n.rememberLocalizedString
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSColors
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSFonts
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText
import java.util.Locale

@Preview
@Composable
fun PreviewUtilSheetPreview() {
    Previewer.Theme(showUtilView = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DSColors.Background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(rememberLocalizedString(stringRes = R.string.test_localized_1).styleText {
                +DSFonts.Title.L
                +DSColors.White
            })
        }
    }
}

@Composable
fun PreviewUtilSheet(
    onLocaleChange: (Locale) -> Unit,
) {
    Column(
        modifier = Modifier
            .height(200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    onLocaleChange(Locale.ENGLISH)
                }
            ) {
                Text("ENGLISH")
            }

            Button(
                onClick = {
                    onLocaleChange(Locale.KOREAN)
                }
            ) {
                Text("한국어")
            }
        }
    }
}
