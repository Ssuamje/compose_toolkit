package com.ssuamje.composetoolkit.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.ui.designsystem.foundation.DSColors
import com.ssuamje.composetoolkit.ui.designsystem.foundation.styleText
import java.util.UUID

@Preview
@Composable
fun OverlayProviderPreview() {
    val popupScope = PopupScope()
    val overlayProvider = OverlayProvider(listOf(popupScope))

    overlayProvider.Scaffolding { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DSColors.Background)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    popupScope.open(PopupScope.PopupContent {
                        Text(UUID.randomUUID().toString().substring(0..5).styleText {
                            +listOf(
                                DSColors.Gray._200,
                                DSColors.Green._100,
                                DSColors.White,
                                DSColors.Purple._400
                            ).random()
                        },
                            modifier = Modifier.clickable { popupScope.close(it) })
                    })
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


private typealias OverlayId = String

interface OverlayScope {
    val contents: List<OverlayContent>

    @Composable
    fun Skeleton()
    fun open(content: OverlayContent)
    fun close(id: OverlayId)
    fun closeLatest()
}

interface OverlayContent {
    val id: OverlayId
    val composable: @Composable (OverlayId) -> Unit
}

val LocalOverlayProvider =
    staticCompositionLocalOf<OverlayProvider> { error("No OverlayProvider provided") }

class OverlayProvider(
    private val scopes: List<OverlayScope>,
) {
    @Composable
    fun Scaffolding(
        content: @Composable (PaddingValues) -> Unit,
    ) {
        CompositionLocalProvider(
            LocalOverlayProvider provides this,
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                content = { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        content(paddingValues)
                        View(paddingValues, scopes)
                    }
                }
            )
        }
    }

    @Composable
    private fun View(
        paddingValues: PaddingValues,
        scopes: List<OverlayScope>
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            for (scope in scopes) {
                scope.Skeleton()
            }
        }
    }
}

class PopupScope : OverlayScope {
    override val contents: SnapshotStateList<OverlayContent> = mutableStateListOf()

    data class PopupContent(
        override val id: OverlayId = UUID.randomUUID().toString(),
        override val composable: @Composable (OverlayId) -> Unit
    ) : OverlayContent

    @Composable
    override fun Skeleton() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                contents.forEach { content ->
                    content.composable(content.id)
                }
            }
        }
    }

    override fun open(content: OverlayContent) {
        contents.add(content)
    }

    override fun close(id: OverlayId) {
        contents.removeAll { it.id == id }
    }

    override fun closeLatest() {
        contents.removeLastOrNull()
    }
}
