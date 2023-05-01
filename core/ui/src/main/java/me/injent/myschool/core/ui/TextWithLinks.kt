package me.injent.myschool.core.ui

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import me.injent.myschool.core.common.util.LINK_REGEX_SCHEME

@Composable
fun TextWithLinks(
    text: String,
    onLinkClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current
) {
    val annotatedText = buildAnnotatedString {
        for (link in text.split(' ')) {
            if (link.matches(LINK_REGEX_SCHEME.toRegex())) {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    appendLink("$link ", link)
                }
            } else {
                withStyle(SpanStyle(color = color)) {
                    append("$link ")
                }
            }
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.onLinkClick(offset) { url ->
                onLinkClick(url)
            }
        },
        style = style,
        modifier = modifier
    )
}

private fun AnnotatedString.Builder.appendLink(linkText: String, linkUrl: String) {
    pushStringAnnotation(tag = linkUrl, annotation = linkUrl)
    append(linkText)
    pop()
}

private fun AnnotatedString.onLinkClick(offset: Int, onClick: (String) -> Unit) {
    getStringAnnotations(start = offset, end = offset).firstOrNull()?.let {
        onClick(it.item)
    }
}