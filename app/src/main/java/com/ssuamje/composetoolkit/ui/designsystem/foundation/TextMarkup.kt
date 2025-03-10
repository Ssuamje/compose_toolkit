package com.ssuamje.composetoolkit.ui.designsystem.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssuamje.composetoolkit.previews.Previewer

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun MarkupTagPreview() {
    var text by remember { mutableStateOf("hello world!") }

    Previewer.Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DSColors.Background)
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text.styleText {
                +DSFonts.Body.L
                +DSColors.White
            })
            Text(
                text = text.applyMarkup(),
                modifier = Modifier
                    .background(DSColors.Black)
                    .padding(20.dp)
            )
            Spacer(Modifier.weight(1f))
            Button(onClick = { text = "hello world!" }) { Text("Reset") }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.Center,
                maxItemsInEachRow = 3,
            ) {
                MarkupTag.entries.forEach {
                    Button(onClick = {
                        text = text.markup(it)
                    }) {
                        Text(it.tag)
                    }
                }
            }
        }
    }
}


enum class MarkupTag(val tag: String, val style: TextStyle) {

    // Colors
    BLACK("cblack", TextStyle(color = Color.Black)),
    WHITE("cwhite", TextStyle(color = Color.White)),
    RED("cred", TextStyle(color = Color.Red)),
    YELLOW("cyellow100", TextStyle(color = Color.Yellow)),
    GREEN("cgreen", TextStyle(color = Color.Green)),
    BLUE("cblue", TextStyle(color = Color.Blue)),
    GRAY("cgray", TextStyle(color = Color.Gray)),
    LIGHT_GRAY("clightgray", TextStyle(color = Color.LightGray)),
    DARK_GRAY("cdarkgray", TextStyle(color = Color.DarkGray)),
    MAGENTA("cmagenta", TextStyle(color = Color.Magenta)),
    TRANSPARENT("ctransparent", TextStyle(color = Color.Transparent)),

    // Alignment
    LEFT("aleft", TextStyle(textAlign = TextAlign.Left)),
    CENTER("acenter", TextStyle(textAlign = TextAlign.Center)),
    RIGHT("aright", TextStyle(textAlign = TextAlign.Right)),
    JUSTIFY("ajustify", TextStyle(textAlign = TextAlign.Justify)),

    // Font
    REGULAR("fregular", TextStyle(fontWeight = FontWeight.Normal)),
    MEDIUM("fmedium", TextStyle(fontWeight = FontWeight.Medium)),
    SEMI_BOLD("fsemibold", TextStyle(fontWeight = FontWeight.SemiBold)),
    BOLD("fbold", TextStyle(fontWeight = FontWeight.Bold)),
    EXTRA_BOLD("fextrabold", TextStyle(fontWeight = FontWeight.ExtraBold)),
    ITALIC("fitalic", TextStyle(fontStyle = FontStyle.Italic)),
    UNDERLINE("funderline", TextStyle(textDecoration = TextDecoration.Underline)),

    TITLE_L("ftitlel", DSFonts.Title.L),
    TITLE_M("ftitlem", DSFonts.Title.M),
    SUBTITLE_L("fsubtitlel", DSFonts.Subtitle.L),
    SUBTITLE_M("fsubtitlem", DSFonts.Subtitle.M),
    SUBTITLE_S("fsubtitles", DSFonts.Subtitle.S),
    SUBTITLE_XS("fsubtitlexs", DSFonts.Subtitle.XS),
    BODY_XL("fbodyxl", DSFonts.Body.XL),
    BODY_L("fbodyl", DSFonts.Body.L),
    BODY_M("fbodym", DSFonts.Body.M),
    BODY_S("fbodys", DSFonts.Body.S),
    ;

    val start = "<$tag>"
    val end = "</$tag>"

    companion object {
        init {
            val duplicatedTags = entries
                .groupBy { it.tag }
                .filter { it.value.size > 1 }

            if (duplicatedTags.isNotEmpty()) {
                val duplicateInfo = duplicatedTags.entries.joinToString { entry ->
                    "${entry.key}: ${entry.value.joinToString { it.name }}"
                }
                throw IllegalArgumentException("Duplicated tags found: $duplicateInfo")
            }
        }

        fun combine(vararg tags: MarkupTag): TextStyle {
            return tags.fold(TextStyle()) { acc, tag ->
                acc.merge(tag.style)
            }
        }

        fun fromStartTag(tag: String): MarkupTag? = entries.find { it.start == tag }
        fun fromEndTag(tag: String): MarkupTag? = entries.find { it.end == tag }
    }
}

fun String.markup(vararg tags: MarkupTag): String {
    return tags.fold(this) { acc, tag -> "${tag.start}$acc${tag.end}" }
}

fun String.applyMarkup(): AnnotatedString {
    if (!isTagValid(this)) return AnnotatedString(this)
    val builder = AnnotatedString.Builder()
    var index = 0
    val tagStack = mutableListOf<MarkupTag>()

    while (index < this.length) {
        if (this.startsWith("<", index)) {
            val endIndex = this.indexOf(">", index)
            if (endIndex != -1) {
                val tag = this.substring(index, endIndex + 1)
                val startTag = MarkupTag.fromStartTag(tag)
                val endTag = MarkupTag.fromEndTag(tag)
                when {
                    startTag != null -> {
                        tagStack.add(startTag)
                    }

                    endTag != null -> {
                        if (tagStack.isNotEmpty() && tagStack.last() == endTag) {
                            tagStack.removeAt(tagStack.size - 1)
                        } else {
                            throw IllegalArgumentException("Mismatched closing tag: $tag at position $index")
                        }
                    }
                }
                index = endIndex + 1
            } else {
                index++
            }
        } else {
            val currentStyle =
                tagStack.fold(TextStyle()) { acc, styleTag -> acc.merge(styleTag.style) }
            builder.pushStyle(currentStyle.toSpanStyle())

            val nextTagIndex = this@applyMarkup.indexOf("<", index)
            val substringEnd =
                if (nextTagIndex == -1) this@applyMarkup.length else nextTagIndex

            if (index <= substringEnd && substringEnd <= this@applyMarkup.length) {
                val textSegment = this@applyMarkup.substring(index, substringEnd)
                builder.append(textSegment)
                index = substringEnd
            } else {
                throw StringIndexOutOfBoundsException("Invalid substring indices: begin=$index, end=$substringEnd, length=${this@applyMarkup.length}")
            }

            builder.pop()
        }
    }

    return builder.toAnnotatedString()
}

private fun isTagValid(markedUp: String): Boolean {
    val errors = mutableListOf<String>()
    val tagStack = mutableListOf<String>()
    var tagSize = 0

    var index = 0
    while (index < markedUp.length) {
        if (markedUp.startsWith("<", index)) {
            val endIndex = markedUp.indexOf(">", index)
            if (endIndex == -1) {
                errors.add("Unclosed tag at position $index")
                break
            }

            val tag = markedUp.substring(index, endIndex + 1)
            if (tag.startsWith("</")) {
                val tagName = tag.substring(2, tag.length - 1)
                if (tagStack.isEmpty() || tagStack.last() != tagName) errors.add("Mismatched or unclosed tag: $tag at position $index")
                else tagStack.removeAt(tagStack.size - 1)
            } else {
                val tagName = tag.substring(1, tag.length - 1)
                tagStack.add(tagName)
                tagSize++
            }
            index = endIndex + 1
        } else {
            index++
        }
    }

    if (tagSize == 0) errors.add("No tags found")
    if (tagStack.isNotEmpty()) errors.add("Unclosed tag: <${tagStack.last()}>")
    if (errors.isNotEmpty()) println("Invalid StyledText: ${errors.joinToString()}")
        .also { return false }
    return true
}