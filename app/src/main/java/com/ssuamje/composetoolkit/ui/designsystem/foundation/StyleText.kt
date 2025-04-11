package com.ssuamje.composetoolkit.ui.designsystem.foundation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

fun AnnotatedString.styleText(block: StyleTagScope.() -> Unit): AnnotatedString {
    val scope = StyleTagScope().apply(block)
    return this.applyStyles(scope.styles)
}

fun String.isStyled(): Boolean {
    return isTagValid(this)
}

fun String.styleText(block: StyleTagScope.() -> Unit): AnnotatedString {
    val scope = StyleTagScope().apply(block)
    return if (this.isStyled()) this.applyMarkup().applyStyles(scope.styles)
    else this.applyStyles(scope.styles)
}

class StyleTagScope {
    internal val styles = mutableListOf<TextStyle>()

    operator fun TextAlign.unaryPlus() {
        styles.add(TextStyle(textAlign = this))
    }

    operator fun TextStyle.unaryPlus() {
        styles.add(this)
    }

    operator fun Color.unaryPlus() {
        styles.add(TextStyle(color = this))
    }
}

private fun String.applyStyles(styles: List<TextStyle>): AnnotatedString {
    val combinedStyle = styles.fold(TextStyle()) { acc, style ->
        acc.merge(style)
    }

    return AnnotatedString.Builder().apply {
        pushStyle(combinedStyle.toSpanStyle())
        pushStyle(combinedStyle.toParagraphStyle())
        append(this@applyStyles)
        pop()
        pop()
    }.toAnnotatedString()
}

private fun AnnotatedString.applyStyles(styles: List<TextStyle>): AnnotatedString {
    val combinedStyle = styles.fold(TextStyle()) { acc, style ->
        acc.merge(style)
    }
    val spanStyle = combinedStyle.toSpanStyle()

    return AnnotatedString.Builder().apply {
        pushStyle(spanStyle)
        append(this@applyStyles)
        pop()
    }.toAnnotatedString()
}