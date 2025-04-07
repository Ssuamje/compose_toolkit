package com.ssuamje.composetoolkit.layouts.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.util.UUID

@JvmInline
value class OverlayId(val value: String = UUID.randomUUID().toString())

abstract class OverlayScope<T : OverlayContent> {
    private val contents: SnapshotStateList<T> = mutableStateListOf()

    open fun open(content: T) {
        contents.add(content)
    }

    open fun close(id: OverlayId) {
        contents.removeAll { it.id == id }
    }

    open fun closeLatest() {
        contents.removeLastOrNull()
    }

    open fun closeAll() {
        contents.clear()
    }

    open fun update(id: OverlayId, content: T) {
        val index = contents.indexOfFirst { it.id == id }
        if (index != -1) {
            contents[index] = content
        }
    }

    @Composable
    protected fun getContents(): SnapshotStateList<T> = contents

    @Composable
    abstract fun Render()
}

interface OverlayContent {
    val id: OverlayId
    val dismiss: () -> Unit
    val composable: @Composable (OverlayId) -> Unit
}

interface BackgroundClickDismissable {
    fun onBackgroundClick()
}

val LocalOverlayProvider =
    staticCompositionLocalOf<OverlayProvider> { error("No OverlayProvider provided") }

@Composable
fun rememberOverlayProvider(
    vararg scopes: OverlayScope<*>,
): OverlayProvider {
    return remember(scopes) {
        OverlayProvider(scopes.toList())
    }
}

class OverlayProvider(
    private val scopes: List<OverlayScope<*>>,
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
        scopes: List<OverlayScope<*>>
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            for (scope in scopes) {
                scope.Render()
            }
        }
    }
}

