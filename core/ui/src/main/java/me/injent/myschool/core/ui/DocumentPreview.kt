package me.injent.myschool.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.injent.myschool.core.designsystem.component.WebView

const val GOOGLE_DOCS_VIEWER = "https://docs.google.com/gview?embedded=true&url="

@Composable
fun DocumentPreview(
    url: String,
    modifier: Modifier = Modifier,
    allowToPreview: Boolean,
) {
    if (allowToPreview) {
        WebView(
            url = GOOGLE_DOCS_VIEWER + url,
            modifier = modifier
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.material_hasnt_preview),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}