package me.injent.myschool.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import me.injent.myschool.R
import me.injent.myschool.updates.versioncontrol.Update
import me.injent.myschool.core.designsystem.component.HtmlText
import me.injent.myschool.core.designsystem.component.MsTextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateDialog(
    update: Update,
    onUpdateRequest: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!update.required)
                onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(.85f),
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${if (update.required) {
                        stringResource(R.string.important_update)
                    } else {
                        stringResource(R.string.update_available)
                    }} (v${update.versionName})",
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )

                val scrollState = rememberScrollState()
                HtmlText(
                    html = update.content,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(384.dp)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                ) {
                    if (!update.required) {
                        MsTextButton(
                            text = stringResource(R.string.later),
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = onDismiss
                        )
                    } else {
                        Spacer(Modifier)
                    }
                    MsTextButton(
                        text = stringResource(R.string.move_to_download),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = onUpdateRequest
                    )
                }
            }
        }
    }
}